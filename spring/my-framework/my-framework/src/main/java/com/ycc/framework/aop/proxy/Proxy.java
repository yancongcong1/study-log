package com.ycc.framework.aop.proxy;

/**
 * Created on 2018\9\4 0004 by yancongcong
 */
public interface Proxy {

    Object doProxy(ProxyChain proxyChain) throws Throwable;

}
