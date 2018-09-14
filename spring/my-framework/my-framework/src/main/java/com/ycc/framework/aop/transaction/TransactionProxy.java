package com.ycc.framework.aop.transaction;

import com.ycc.framework.aop.annotation.Aspect;
import com.ycc.framework.aop.aop.AspectProxy;
import com.ycc.framework.aop.transaction.database.DatabaseHelper;
import com.ycc.framework.aop.transaction.database.TempDatabaseHelper;
import com.ycc.framework.mvc.annotation.Service;

import java.lang.reflect.Method;

/**
 * Created on 2018\9\14 0014 by yancongcong
 */
@Aspect(Service.class)
public class TransactionProxy extends AspectProxy {

    private static final ThreadLocal<Boolean> FLAG_HOLDER = new ThreadLocal<Boolean>() {
        @Override
        protected Boolean initialValue() {
            return false;
        }
    };

    private static final DatabaseHelper databaseHelper = new TempDatabaseHelper();

    private boolean getFlag() {
        return FLAG_HOLDER.get();
    }

    private void setFlag(boolean flag) {
        FLAG_HOLDER.set(flag);
    }

    private void removeFlag() {
        FLAG_HOLDER.remove();
    }

    @Override
    public void begin() {
        super.begin();
    }

    @Override
    public void exception() {
        databaseHelper.rollbackTransaction();
    }

    @Override
    public void end() {
        removeFlag();
    }

    @Override
    public boolean intercept(Class<?> targetClass, Method targetMethod, Object[] methodParams) throws Throwable {
        return !getFlag() && targetMethod.isAnnotationPresent(Transaction.class);
    }

    @Override
    public void before(Class<?> targetClass, Method targetMethod, Object[] methodParams) throws Throwable {
        FLAG_HOLDER.set(true);
        databaseHelper.beginTransaction();
    }

    @Override
    public void after(Class<?> targetClass, Method targetMethod, Object[] methodParams, Object result) throws Throwable {
        databaseHelper.commitTransaction();
    }
}
