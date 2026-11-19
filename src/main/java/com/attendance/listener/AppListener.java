package com.attendance.listener;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

/**
 * 应用启动监听器
 * @author 周童
 */
@WebListener
public class AppListener implements ServletContextListener {
    
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        ServletContext context = sce.getServletContext();
        System.out.println("========================================");
        System.out.println("   职工考勤管理系统 启动成功！");
        System.out.println("   访问地址: http://localhost:8083/attendance/");
        System.out.println("========================================");
        
        // 将系统启动时间存入应用上下文
        context.setAttribute("startTime", System.currentTimeMillis());
    }
    
    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        System.out.println("========================================");
        System.out.println("   职工考勤管理系统 已关闭");
        System.out.println("========================================");
    }
}
