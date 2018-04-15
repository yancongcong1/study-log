package lesson1;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionReader;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.xml.XmlBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

public class Test {
    private BeanFactory beanFactory;
    private XmlBeanFactory xmlBeanFactory;
    private ApplicationContext applicationContext;

    private Resource resource;
    private ResourceLoader resourceLoader;
    private BeanDefinition beanDefinition;
    private BeanDefinitionReader beanDefinitionReader;
    private BeanDefinitionRegistry beanDefinitionRegistry;

    void test() {
        FileSystemXmlApplicationContext fileSystemXmlApplicationContext;
    }
}
