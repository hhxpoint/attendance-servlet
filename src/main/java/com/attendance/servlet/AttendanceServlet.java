package com.attendance.servlet;

import com.attendance.model.Attendance;
import com.attendance.model.User;
import com.attendance.service.AttendanceService;
import com.attendance.util.Result;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * 考勤管理Servlet
 * @author 汪轩增
 */
public class AttendanceServlet extends HttpServlet {
    
    private AttendanceService attendanceService = new AttendanceService();
    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    private SimpleDateFormat dateTimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        String pathInfo = request.getPathInfo();
        if (pathInfo == null) pathInfo = "/";
        
        response.setContentType("application/json;charset=UTF-8");
        PrintWriter out = response.getWriter();
        
        try {
            switch (pathInfo) {
                case "/clockIn":
                    doClockIn(request, out);
                    break;
                case "/clockOut":
                    doClockOut(request, out);
                    break;
                default:
                    out.print(Result.error(404, "接口不存在").toJson());
            }
        } catch (Exception e) {
            e.printStackTrace();
            out.print(Result.error("服务器错误").toJson());
        }
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        String pathInfo = request.getPathInfo();
        if (pathInfo == null) pathInfo = "/";
        
        response.setContentType("application/json;charset=UTF-8");
        PrintWriter out = response.getWriter();
        
        try {
            switch (pathInfo) {
                case "/today":
                    doGetToday(request, out);
                    break;
                case "/list":
                    doList(request, out);
                    break;
                case "/deptStats":
                    doDeptStats(request, out);
                    break;
                default:
                    out.print(Result.success("考勤系统API可用").toJson());
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
            out.print(Result.error("服务器错误").toJson());
        }
    }
    
    private User getCurrentUser(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null) return null;
        return (User) session.getAttribute("currentUser");
    }
    
    private void doClockIn(HttpServletRequest request, PrintWriter out) throws ParseException {
        User user = getCurrentUser(request);
        Integer userId = 1; // 默认测试用户
        if (user != null) {
            userId = user.getId();
        }
        
        Attendance attendance = attendanceService.clockIn(userId);
        if (attendance.getLateMinutes() > 0) {
            out.print(Result.success("打卡成功（迟到" + attendance.getLateMinutes() + "分钟）", attendance).toJson());
        } else {
            out.print(Result.success("上班打卡成功", attendance).toJson());
        }
    }
    
    private void doClockOut(HttpServletRequest request, PrintWriter out) throws ParseException {
        User user = getCurrentUser(request);
        Integer userId = 1;
        if (user != null) {
            userId = user.getId();
        }
        
        Attendance attendance = attendanceService.clockOut(userId);
        if (attendance == null) {
            out.print(Result.error("请先打上班卡").toJson());
            return;
        }
        if (attendance.getEarlyMinutes() > 0) {
            out.print(Result.success("打卡成功（早退" + attendance.getEarlyMinutes() + "分钟）", attendance).toJson());
        } else {
            out.print(Result.success("下班打卡成功", attendance).toJson());
        }
    }
    
    private void doGetToday(HttpServletRequest request, PrintWriter out) throws ParseException {
        User user = getCurrentUser(request);
        Integer userId = 1;
        if (user != null) {
            userId = user.getId();
        }
        
        Attendance attendance = attendanceService.getToday(userId);
        out.print(Result.success(attendance).toJson());
    }
    
    private void doList(HttpServletRequest request, PrintWriter out) throws ParseException {
        String startDateStr = request.getParameter("startDate");
        String endDateStr = request.getParameter("endDate");
        
        List<Attendance> list;
        if (startDateStr != null && endDateStr != null) {
            Date startDate = dateFormat.parse(startDateStr);
            Date endDate = dateFormat.parse(endDateStr);
            list = attendanceService.listByDateRange(startDate, endDate);
        } else {
            list = attendanceService.listAll();
        }
        out.print(Result.success(list, (long) list.size()).toJson());
    }
    
    private void doDeptStats(HttpServletRequest request, PrintWriter out) throws ParseException {
        String startDateStr = request.getParameter("startDate");
        String endDateStr = request.getParameter("endDate");
        
        if (startDateStr == null || endDateStr == null) {
            out.print(Result.error("请提供开始日期和结束日期").toJson());
            return;
        }
        
        Date startDate = dateFormat.parse(startDateStr);
        Date endDate = dateFormat.parse(endDateStr);
        List<java.util.Map<String, Object>> stats = attendanceService.getDeptStats(startDate, endDate);
        out.print(Result.success(stats, (long) stats.size()).toJson());
    }
}
