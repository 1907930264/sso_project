package com.yxj.ssoclient.context.strategy;

import com.yxj.ssoclient.context.SsoClientContext;
import org.springframework.util.Assert;

public class SsoThreadLocalContextStrategy implements SsoContextHolderStrategy{

    private static final ThreadLocal<SsoClientContext> contextHolder = new ThreadLocal<>();

    @Override
    public void clearContext() {
        contextHolder.remove();
    }

    @Override
    public SsoClientContext getContext() {
        SsoClientContext ctx = contextHolder.get();
        if (ctx == null) {
            ctx = createEmptyContext();
            contextHolder.set(ctx);
        }
        return ctx;
    }

    @Override
    public void setContext(SsoClientContext context) {
        Assert.notNull(context, "Only non-null SecurityContext instances are permitted");
        contextHolder.set(context);
    }

    @Override
    public SsoClientContext createEmptyContext() {
        return new SsoClientContext();
    }
}
