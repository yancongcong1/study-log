package com.ycc.framework.request;

import java.util.*;

/**
 * Put the param of the request to the map struct.
 *
 * created by ycc at 2018\4\24 0024
 */
public class Param {
    private Map<String, Object> params = new HashMap<>();

    public Param() {
    }

    public Param(Map<String, Object> params) {
        this.params = params;
    }

    public Param addParam(String name, Object value) {
        this.params.put(name, value);
        return this;
    }

    public Map<String, Object> getParams() {
        return params;
    }

    public Object[] getAllValues() {
        List<Object> lists = new ArrayList<>();
        Set<Map.Entry<String, Object>> entries = this.params.entrySet();
        for (Map.Entry<String, Object> entry : entries) {
            lists.add(entry.getValue());
        }
        return lists.toArray();
    }
}
