package com.yxj.ssoclient.context.strategy;

import com.yxj.ssoclient.context.SsoClientContext;

public interface SsoContextHolderStrategy {

    void clearContext();

    SsoClientContext getContext();

    void setContext(SsoClientContext context);

    SsoClientContext createEmptyContext();
}
