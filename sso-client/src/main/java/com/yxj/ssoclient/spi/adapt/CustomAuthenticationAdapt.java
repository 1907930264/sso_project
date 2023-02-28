package com.yxj.ssoclient.spi.adapt;

import com.yxj.ssoclient.spi.CustomAuthenticationInterface;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.io.support.SpringFactoriesLoader;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

public class CustomAuthenticationAdapt implements ApplicationContextAware, BeanDefinitionRegistryPostProcessor {

    private final static String SPI_IMPL_BEAN_NAME = "customAuthenticationInterfaceImpl";

    private ApplicationContext applicationContext;

    public Boolean authentication(HttpServletRequest servletRequest, HttpServletResponse servletResponse){
        CustomAuthenticationInterface bean = applicationContext.getBean(CustomAuthenticationInterface.class);
        return bean != null ? bean.authentication(servletRequest, servletResponse) : false;
    }

    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry beanDefinitionRegistry) throws BeansException {
        List<CustomAuthenticationInterface> customAuthenticationInterfaces = SpringFactoriesLoader.loadFactories(CustomAuthenticationInterface.class, Thread.currentThread().getContextClassLoader());
        if (customAuthenticationInterfaces != null && !customAuthenticationInterfaces.isEmpty()){
            CustomAuthenticationInterface customAuthenticationInterface = customAuthenticationInterfaces.get(0);
            Class<? extends CustomAuthenticationInterface> interfaceClass = customAuthenticationInterface.getClass();
            if (!applicationContext.containsBean(SPI_IMPL_BEAN_NAME)){
                BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder.genericBeanDefinition(interfaceClass);
                AbstractBeanDefinition beanDefinition = beanDefinitionBuilder.getBeanDefinition();
                beanDefinition.setAutowireMode(AbstractBeanDefinition.AUTOWIRE_BY_NAME);
                beanDefinitionRegistry.registerBeanDefinition(SPI_IMPL_BEAN_NAME,beanDefinition);
            }
        }
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory configurableListableBeanFactory) throws BeansException {

    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
