package com.ycc.framework.response;

import java.util.HashMap;
import java.util.Map;

/**
 * Build the response of the view.
 *
 * created by ycc at 2018\4\24 0024
 */
public class View {
    private String path;
    private Map<String, Object> data;

    public View() {
    }

    public View(String path) {
        this.path = path;
        this.data = new HashMap<String, Object>();
    }

    public View(String path, Map<String, Object> data) {
        this.path = path;
        this.data = data;
    }

    public View addModel(String name, Object value) {
        this.data.put(name, value);
        return this;
    }

    public String getPath() {
        return path;
    }

    public Map<String, Object> getData() {
        return data;
    }
}
