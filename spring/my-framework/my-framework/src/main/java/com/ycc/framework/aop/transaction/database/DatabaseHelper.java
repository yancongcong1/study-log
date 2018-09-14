package com.ycc.framework.aop.transaction.database;

/**
 * Created on 2018\9\14 0014 by yancongcong
 */
public abstract class DatabaseHelper {

    public abstract void beginTransaction();

    public abstract void commitTransaction();

    public abstract void rollbackTransaction();

}
