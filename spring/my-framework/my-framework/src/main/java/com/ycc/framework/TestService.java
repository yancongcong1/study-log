package com.ycc.framework;

import com.ycc.framework.aop.transaction.Transaction;
import com.ycc.framework.mvc.annotation.Service;

/**
 * Created on 2018\9\14 0014 by yancongcong
 */
@Service
public class TestService {

    // Transaction只能注解在Service注解的类中，因为aop框架中已经做了限制，当然这个限制可以更改一下
    @Transaction
    public void test() {
        System.out.println("测试Transaction");
    }
}
