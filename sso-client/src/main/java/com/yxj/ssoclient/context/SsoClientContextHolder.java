package com.yxj.ssoclient.context;

import com.yxj.ssoclient.context.strategy.SsoContextHolderStrategy;
import com.yxj.ssoclient.context.strategy.SsoThreadLocalContextStrategy;
import org.springframework.util.Assert;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.StringUtils;

import java.lang.reflect.Constructor;

public class SsoClientContextHolder {

    public static final String MODE_THREADLOCAL = "MODE_THREADLOCAL";

    public static final String SYSTEM_PROPERTY = "sso-config.strategy";

    private static String strategyName = System.getProperty(SYSTEM_PROPERTY);

    private static SsoContextHolderStrategy strategy;

    static {
        initializeStrategy();
    }

    private static void initializeStrategy() {

        if (!StringUtils.hasText(strategyName)) {
            // Set default
            strategyName = MODE_THREADLOCAL;
        }
        if (strategyName.equals(MODE_THREADLOCAL)) {
            strategy = new SsoThreadLocalContextStrategy();
            return;
        }

        // Try to load a custom strategy
        try {
            Class<?> clazz = Class.forName(strategyName);
            Constructor<?> customStrategy = clazz.getConstructor();
            strategy = (SsoContextHolderStrategy) customStrategy.newInstance();
        }
        catch (Exception ex) {
            ReflectionUtils.handleReflectionException(ex);
        }
    }

    public static void clearContext() {
        strategy.clearContext();
    }


    public static SsoClientContext getContext() {
        return strategy.getContext();
    }



    public static void setContext(SsoClientContext context) {
        strategy.setContext(context);
    }


    public static void setStrategyName(String strategyName) {
        SsoClientContextHolder.strategyName = strategyName;
        initializeStrategy();
    }

    public static void setContextHolderStrategy(SsoContextHolderStrategy strategy) {
        Assert.notNull(strategy, "SsoContextHolderStrategy cannot be null");
        SsoClientContextHolder.strategy = strategy;
    }


    public static SsoContextHolderStrategy getContextHolderStrategy() {
        return strategy;
    }


    public static SsoClientContext createEmptyContext() {
        return strategy.createEmptyContext();
    }
}
