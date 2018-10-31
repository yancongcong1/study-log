# spring解决前端无法获取错误码问题



## 问题描述
前后端分离的项目中有时候前端无法正确获取后端返回的错误码信息！！！



## 原因分析

当发生跨域请求时，浏览器会实际发出两次请求(并不是每次都发出两次，跟第一次请求返回的最大跨域时间有关)，第一次请求(`OPTIONS，又称为预检请求,下面以[第一次请求]统指[预检请求]`)会获取服务器关于允许跨域请求的一些信息，服务器会将这些信息放入响应头中。

正常情况下使用下面介绍前两种方法就可以解决跨域问题！！！但是在某些情况下却会出现这样的问题：发生跨域请求并且请求发生错误时无法正确返回服务端的错误码。比如在某些服务端使用token校验的情况下，如果token过期等，这时候前端无法正确拿到服务端返回的401错误码！为什么会出现这样的问题呢？？？

前两种解决方法虽然看似会在第一次跨域请求时正确返回允许跨域的信息，但是实际上在错误发生时并没有正确返回正确的响应头信息，这是因为我们使用了拦截器。如果服务端添加了token拦截器，当token校验失败时，拦截器会阻止请求的继续执行并且直接返回错误信息。前两种解决办法对响应头的实际处理在拦截器处理之后，所以第一次请求的响应头中并没有跨域的相关内容。而浏览器跨域请求时最重要的依据就是第一次请求的响应信息，此时第一次跨域请求的响应头中没有允许跨域的相关信息，由于浏览器的跨域机制接下来的真正跨域请求也不会发出，此时浏览器判定跨域请求失败(服务端不允许)，所以会报一个跨域错误码覆盖服务端返回的401错误码，从而导致前端无法获取正确的错误码(第一次请求错误信息被覆盖，真正的请求直接没有发出，胎死腹中)。

那么我们既然已经知道了问题的原因，那就很好解决问题了。思路：在token校验之前就设置response，让前端能够正确的发出跨域请求(这边的请求指的是真正的跨域请求，非OPTIONS)。所以接下来会有两种思路：一是添加web过滤器，二是使用框架的拦截器(该CORS拦截器必须在token拦截器之前被添加到拦截器链中)。具体实现为后两种解决办法。



## 解决方法(spring项目中)

1. @CrossOrigin

   可以在通过添加@CrossOrigin注解来解决跨域问题，@CrossOrigin的具体用法可以自行查阅相关资料。

2. 在WebMvcConfig中添加跨域配置

   ```
   @Configuration
   public class WebMvcConfig implements WebMvcConfigurer {
   
        /**
         * 这边省略自己的其他配置：拦截器、静态资源URL配置
         */
   
       // Global CORS configuration
       @Override
       public void addCorsMappings(CorsRegistry registry) {
           registry.addMapping("/api/**").allowedMethods("POST", "GET", "PUT", "DELETE");
       }
   
   }
   ```

3. 在WebMvcConfig添加web过滤器(Filter)

   ```
   @Configuration
   public class WebMvcConfig implements WebMvcConfigurer {
   
       /**
        * 解决前端axios跨域时token校验失败无法捕获401问题!
        * @return
        */
       @Bean
       public FilterRegistrationBean filterRegistration() {
           CorsConfiguration config = new CorsConfiguration();
           config.setAllowCredentials(true);
           config.addAllowedOrigin("*");
           config.addAllowedHeader("*");
           config.addAllowedMethod("*");
           UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
           source.registerCorsConfiguration("/**", config);
   
           FilterRegistrationBean registration = new FilterRegistrationBean();
           registration.setFilter(new CorsFilter(source));
           registration.addUrlPatterns("/*");
           return registration;
       }
   
   }
   ```

4. 在WebMvcConfig添加CORS拦截器(Interceptor)

   拦截器代码：

   ```
   @Component
   public class CORSInterceptor implements HandlerInterceptor {
       @Override
       public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
           String origin = request.getHeader(HttpHeaders.ORIGIN);
           if (!StringUtils.isEmpty(origin)) {
               response.setHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN, origin);
               response.setHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_METHODS, "POST, GET, OPTIONS, DELETE, PUT");
               response.setHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_HEADERS, "*");
               response.setHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_CREDENTIALS, "true");
               response.setHeader(HttpHeaders.ACCESS_CONTROL_MAX_AGE, "3600");
           }
           return true;
       }
   
       @Override
       public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
   
       }
   
       @Override
       public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
   
       }
   }
   ```

   添加拦截器：

   ```
   @Configuration
   public class WebMvcConfig implements WebMvcConfigurer {
   
       // @Autowired
       // private TokenInterceptor tokenInterceptor;
   
       @Autowired
       private CORSInterceptor corsInterceptor;
   
       @Override
       public void addInterceptors(InterceptorRegistry registry) {
       	// 必须在tokenInterceptor之前被添加到拦截器链
           registry.addInterceptor(corsInterceptor).addPathPatterns("/**");
           // registry.addInterceptor(tokenInterceptor).addPathPatterns("/**");
       }
   
   }
   ```