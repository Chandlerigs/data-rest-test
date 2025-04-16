package com.igs.ematf.dataresttest.filter;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class CustomFilter implements Filter {
    public static final String DEFAULT_PARAMETER_VALUE = "PER_DEF_VAL";
    public static final Long DEFAULT_PARAMETER_VALUE_Long = 990909L;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;

        // 打印请求路径
        System.out.println("Intercepted URL: " + httpRequest.getRequestURI());

        if ("GET".equalsIgnoreCase(httpRequest.getMethod()) ||
                "POST".equalsIgnoreCase(httpRequest.getMethod())) {

            Map<String, String[]> parameterMap = httpRequest.getParameterMap();
            System.out.println("Parameter Map: " + parameterMap);
            // 定义要添加的默认参数
            Map<String, String[]> defaultParams = new HashMap<>();
//            defaultParams.put("id", new String[]{DEFAULT_PARAMETER_VALUE_Long + ""});
//            defaultParams.put("description", new String[]{DEFAULT_PARAMETER_VALUE_Long + ""});
//            defaultParams.put("username", new String[]{DEFAULT_PARAMETER_VALUE});
//            defaultParams.put("ruleName", new String[]{DEFAULT_PARAMETER_VALUE});

            // 包装原始请求
            CustomHttpServletRequestWrapper wrappedRequest =
                    new CustomHttpServletRequestWrapper(httpRequest, defaultParams);
            // 替换原始请求对象
            request.setAttribute("wrappedRequest", wrappedRequest);
            // 继续处理请求
            chain.doFilter(wrappedRequest, response);
        } else {
            chain.doFilter(request, response);
        }
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void destroy() {
    }
}