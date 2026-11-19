package com.attendance.filter;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * 登录验证过滤器
 * @author 贾兮雯
 */
public class LoginFilter implements Filter {
    
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }
    
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) 
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        
        String uri = httpRequest.getRequestURI();
        
        // 放行登录登出接口和获取当前用户接口
        if (uri.contains("/user/login") || uri.contains("/user/logout")) {
            chain.doFilter(request, response);
            return;
        }
        
        // 放行静态资源
        if (uri.contains("/static/") || uri.endsWith(".html") || uri.endsWith(".css") || uri.endsWith(".js") || uri.endsWith(".jsp")) {
            chain.doFilter(request, response);
            return;
        }
        
        // 放行根路径和欢迎页
        if (uri.equals("/attendance/") || uri.equals("/attendance")) {
            chain.doFilter(request, response);
            return;
        }
        
        // 检查Session
        HttpSession session = httpRequest.getSession(false);
        if (session != null && session.getAttribute("currentUser") != null) {
            chain.doFilter(request, response);
        } else {
            // 如果是AJAX请求，返回JSON格式的401响应
            String ajaxHeader = httpRequest.getHeader("X-Requested-With");
            if (ajaxHeader != null && "XMLHttpRequest".equals(ajaxHeader)) {
                httpResponse.setContentType("application/json;charset=UTF-8");
                httpResponse.setStatus(401);
                httpResponse.getWriter().write("{\"code\":401,\"msg\":\"未登录或登录已过期\"}");
            } else {
                // 非AJAX请求可以重定向到登录页
                httpResponse.setContentType("application/json;charset=UTF-8");
                httpResponse.setStatus(401);
                httpResponse.getWriter().write("{\"code\":401,\"msg\":\"未登录或登录已过期\"}");
            }
        }
    }
    
    @Override
    public void destroy() {
    }
}
