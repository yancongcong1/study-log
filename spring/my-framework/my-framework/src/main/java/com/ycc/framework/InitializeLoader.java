package com.ycc.framework;

import com.ycc.framework.ioc.bean.BeanHelper;
import com.ycc.framework.mvc.controller.ControllerHelper;
import com.ycc.framework.ioc.ioc.IocHelper;
import com.ycc.framework.ioc.loader.ClassHelper;
import com.ycc.framework.ioc.loader.ClassUtil;

/**
 * Concentrate to load the class.(this step maybe needless)
 *
 * created by ycc at 2018\4\24 0024
 */
public class InitializeLoader {
    public static void init() {
        Class<?>[] classes = {
                ClassHelper.class,
                BeanHelper.class,
                IocHelper.class,
                ControllerHelper.class
        };

        for (Class<?> clazz : classes) {
            ClassUtil.loadClass(clazz.getName());
        }
    }
}
