package com.attendance.servlet;

import com.attendance.service.SysLogService;
import com.attendance.util.Result;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * 系统日志Servlet
 * @author 汪轩增
 */
@WebServlet(name = "SysLogServlet", urlPatterns = {"/log/*"})
public class SysLogServlet extends HttpServlet {
    
    private SysLogService logService = new SysLogService();
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        String pathInfo = request.getPathInfo();
        if (pathInfo == null) pathInfo = "/";
        
        response.setContentType("application/json;charset=UTF-8");
        PrintWriter out = response.getWriter();
        
        try {
            if ("/list".equals(pathInfo) || "/".equals(pathInfo) || "".equals(pathInfo)) {
                doList(request, out);
            } else {
                out.print(Result.error(404, "接口不存在").toJson());
            }
        } catch (Exception e) {
            e.printStackTrace();
            out.print(Result.error("服务器错误").toJson());
        }
    }
    
    private void doList(HttpServletRequest request, PrintWriter out) {
        int page = 1;
        int pageSize = 20;
        String pageStr = request.getParameter("page");
        String sizeStr = request.getParameter("size");
        
        if (pageStr != null) page = Integer.parseInt(pageStr);
        if (sizeStr != null) pageSize = Integer.parseInt(sizeStr);
        
        int total = logService.count();
        out.print(Result.success(logService.listByPage(page, pageSize), (long) total).toJson());
    }
}
