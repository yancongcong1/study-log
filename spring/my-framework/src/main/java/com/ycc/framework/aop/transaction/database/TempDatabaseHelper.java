package com.ycc.framework.aop.transaction.database;

/**
 * Created on 2018\9\14 0014 by yancongcong
 */
public class TempDatabaseHelper extends DatabaseHelper {
    @Override
    public void beginTransaction() {
        System.out.println("Begin Transaction1......");
    }

    @Override
    public void commitTransaction() {
        System.out.println("Commit Transaction1......");
    }

    @Override
    public void rollbackTransaction() {
        System.out.println("Rollback Transaction1......");
    }
}
