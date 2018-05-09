package com.ycc.framework;

import com.ycc.framework.annotation.Controller;
import com.ycc.framework.annotation.Mapping;
import com.ycc.framework.annotation.Param;
import com.ycc.framework.configure.ConfigHelper;
import com.ycc.framework.response.Data;
import com.ycc.framework.response.View;

/**
 * This controller isn't the member of the framework,it was built for test.
 *
 * created by ycc at 2018\4\24 0024
 */
@Controller
public class TestController {

    @Mapping(method = "get", url = "/data")
    public Data getData(@Param("username") String username, @Param("extra") String extra) {
        Data data = new Data();
        data.setModel("welcome to my frame work, " + username + ", " + extra);
        return data;
    }

    @Mapping(method = "get", url = "/view")
    public View getView() {
        View view = new View(ConfigHelper.getJspPath() + "a");
        return view;
    }
}
