package com.attendance.listener;

import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

/**
 * Session监听器
 * @author 周童
 */
@WebListener
public class SessionListener implements HttpSessionListener {
    
    @Override
    public void sessionCreated(HttpSessionEvent se) {
        System.out.println("Session创建: " + se.getSession().getId());
    }
    
    @Override
    public void sessionDestroyed(HttpSessionEvent se) {
        System.out.println("Session销毁: " + se.getSession().getId());
    }
}
