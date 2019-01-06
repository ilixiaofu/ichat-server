package com.lxf.ichat.interceptor;

import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class EncodingInterceptor implements HandlerInterceptor {

    protected static Logger logger = LoggerFactory.getLogger(EncodingInterceptor.class);

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        // POST请求编码过滤
        request.setCharacterEncoding("UTF-8");

        // GET请求编码过滤
        EncodingRequest encodingRequest = new EncodingRequest(request);
        request = encodingRequest;

        // 响应编码
        response.setCharacterEncoding("UTF-8");
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
                           ModelAndView modelAndView) throws Exception {
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
            throws Exception {
        if (ex != null) {
            logger.debug("", ex.getMessage());
            JSONObject exception = new JSONObject(true);
            exception.put("code", "500");
            exception.put("error", "未知错误");
            response.getWriter().write(exception.toJSONString());
        }
    }

}
