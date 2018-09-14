package com.ycc.framework.aop.transaction;

import java.lang.annotation.*;

/**
 * Created on 2018\9\14 0014 by yancongcong
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Transaction {
}
