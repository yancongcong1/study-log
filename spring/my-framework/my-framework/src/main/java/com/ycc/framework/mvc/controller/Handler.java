package com.ycc.framework.mvc.controller;

import java.lang.reflect.Method;

/**
 * Put the controller and the method to deal with the request.
 *
 * created by ycc at 2018\4\24 0024
 */
public class Handler {
    private Class<?> controller;
    private Method actionMethod;

    public Handler(Class<?> controller, Method actionMethod) {
        this.controller = controller;
        this.actionMethod = actionMethod;
    }

    public Class<?> getController() {
        return controller;
    }

    public void setController(Class<?> controller) {
        this.controller = controller;
    }

    public Method getActionMethod() {
        return actionMethod;
    }

    public void setActionMethod(Method actionMethod) {
        this.actionMethod = actionMethod;
    }
}
