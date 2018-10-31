package com.ycc.framework.aop.annotation;

import java.lang.annotation.*;

/**
 * Created on 2018\9\4 0004 by yancongcong
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Aspect {
    Class<? extends Annotation> value();
}
