package com.lxf.ichat.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.UnsupportedEncodingException;

// get请求编码问题
public class EncodingRequest extends HttpServletRequestWrapper {
    private HttpServletRequest request;

    public EncodingRequest(HttpServletRequest request) {
        super(request);
        this.request = request;
    }

    public String getParameter(String arg0) {
        String value = request.getParameter(arg0);

        try {
            value = new String(value.getBytes("ISO-8859-1"), "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return value;
    }
}
