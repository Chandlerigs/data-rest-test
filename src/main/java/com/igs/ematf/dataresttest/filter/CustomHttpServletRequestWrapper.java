package com.igs.ematf.dataresttest.filter;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;

import java.util.HashMap;
import java.util.Map;

public class CustomHttpServletRequestWrapper extends HttpServletRequestWrapper {

    private final Map<String, String[]> additionalParams;

    public CustomHttpServletRequestWrapper(HttpServletRequest request, Map<String, String[]> additionalParams) {
        super(request);
        this.additionalParams = new HashMap<>(additionalParams);
        // 将原始参数和新增参数合并
        this.additionalParams.putAll(request.getParameterMap());
    }

    @Override
    public String getParameter(String name) {
        String[] params = additionalParams.get(name);
        if (params != null && params.length > 0) {
            return params[0];
        }
        return super.getParameter(name);
    }

    @Override
    public Map<String, String[]> getParameterMap() {
        return additionalParams;
    }

    @Override
    public String[] getParameterValues(String name) {
        return additionalParams.get(name);
    }
}