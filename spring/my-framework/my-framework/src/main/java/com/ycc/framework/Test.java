package com.ycc.framework;

import com.ycc.framework.annotation.Controller;
import com.ycc.framework.annotation.Mapping;
import com.ycc.framework.loader.ClassHelper;
import com.ycc.framework.response.Data;

/**
 * created by ycc at 2018\4\24 0024
 */
@Controller
public class Test {

    @Mapping(method = "get", url = "/data")
    public Data getData(String username) {
        Data data = new Data();
        data.setModel("test for data");
        return data;
    }

    public static void main(String[] args) {
//        System.out.println(ConfigHelper.getBasePackage());
        System.out.println(ClassHelper.getClassSet());

    }
}
