package com.ycc.framework.mvc;

import com.ycc.framework.InitializeLoader;
import com.ycc.framework.ioc.bean.BeanHelper;
import com.ycc.framework.ioc.bean.ReflectionUtil;
import com.ycc.framework.ioc.configure.ConfigHelper;
import com.ycc.framework.mvc.controller.ControllerHelper;
import com.ycc.framework.mvc.controller.Handler;
import com.ycc.framework.mvc.request.Param;
import com.ycc.framework.mvc.response.Data;
import com.ycc.framework.mvc.response.View;
import com.ycc.framework.utils.CodeUtil;
import com.ycc.framework.utils.JsonUtil;
import com.ycc.framework.utils.StreamUtil;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.util.Enumeration;
import java.util.Map;
import java.util.Set;

/**
 * MVC框架的核心控制器
 *
 * created by ycc at 2018\4\24 0024
 */
@WebServlet(urlPatterns = {"/*"}, loadOnStartup = 0)
public class DispatcherServlet extends HttpServlet {

    /**
     * 初始化整个框架
     */
    @Override
    public void init(ServletConfig config) throws ServletException {
        InitializeLoader.init();
    }

    /**
     * web请求处理
     */
    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String reqMethod = req.getMethod().toLowerCase();
        String pathInfo = req.getPathInfo();
        Enumeration<String> paramNames = req.getParameterNames();
        Handler handler = ControllerHelper.getHandler(reqMethod, pathInfo);
        if (handler != null) {
            Class<?> controller = handler.getController();
            Object controllerBean = BeanHelper.getBean(controller);
            Method handleMethod = handler.getActionMethod();
            Param param = new Param();

            while (paramNames.hasMoreElements()) {
                String paramName = paramNames.nextElement();
                String parameter = req.getParameter(paramName);
                param.addParam(paramName, parameter);
            }
            String body = CodeUtil.decodeUrl(StreamUtil.getStream(req.getInputStream()));
            if (body != null && !"".equals(body)) {
                String[] bodyParams = body.split("&");
                for (String bodyParam : bodyParams) {
                    String[] paramBody = bodyParam.split("=");
                    if (paramBody != null && paramBody.length == 2) {
                        param.addParam(paramBody[0], paramBody[1]);
                    }
                }
            }

            Object result = ReflectionUtil.invokeMethod(controllerBean, handleMethod, param.getParams());

            if (result instanceof View) {
                View view = (View) result;
                String path = view.getPath();
                if (path != null) {
                    if (path.startsWith("/")) {
                        resp.sendRedirect(req.getContextPath() + path + ".jsp");
                    } else {
                        Map<String, Object> data = view.getData();
                        Set<Map.Entry<String, Object>> entries = data.entrySet();
                        for (Map.Entry<String, Object> entry: entries) {
                            req.setAttribute(entry.getKey(), entry.getValue());
                        }
                        String tempPath = ConfigHelper.getJspPath() + "/" + path + ".jsp";
                        req.getRequestDispatcher(tempPath).forward(req, resp);
                    }
                }
            } else if (result instanceof Data) {
                Data data = (Data) result;
                Object model = data.getModel();
                String rs = JsonUtil.getString(model);
                if (model != null) {
                    resp.setContentType("application/json");
                    resp.setCharacterEncoding("UTF-8");
                    PrintWriter writer = resp.getWriter();
                    writer.write(rs);
                    writer.flush();
                    writer.close();
                }
            }
        }
    }
}
