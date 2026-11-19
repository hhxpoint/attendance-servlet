package com.attendance.servlet;

import com.attendance.util.DBUtil;
import com.attendance.util.Result;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;

/**
 * 健康检查Servlet - 用于诊断系统状态
 * @author 汪轩增
 */
public class HealthServlet extends HttpServlet {
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        response.setContentType("application/json;charset=UTF-8");
        PrintWriter out = response.getWriter();
        
        try {
            // 检查数据库连接
            boolean dbConnected = false;
            String dbError = null;
            try {
                Connection conn = DBUtil.getConnection();
                dbConnected = conn != null && !conn.isClosed();
                if (conn != null) conn.close();
            } catch (Exception e) {
                dbError = e.getMessage();
            }
            
            // 检查Session
            HttpSession session = request.getSession(false);
            boolean hasSession = session != null;
            Object user = hasSession ? session.getAttribute("currentUser") : null;
            
            java.util.Map<String, Object> status = new java.util.HashMap<>();
            status.put("system", "OK");
            status.put("databaseConnected", dbConnected);
            status.put("databaseError", dbError);
            status.put("hasSession", hasSession);
            status.put("hasUser", user != null);
            status.put("timestamp", new java.util.Date().toString());
            
            out.print(Result.success(status).toJson());
        } catch (Exception e) {
            out.print(Result.error("健康检查失败: " + e.getMessage()).toJson());
        }
    }
}
