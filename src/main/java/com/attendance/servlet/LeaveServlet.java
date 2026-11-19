package com.attendance.servlet;

import com.attendance.model.LeaveApply;
import com.attendance.model.User;
import com.attendance.service.LeaveApplyService;
import com.attendance.util.Result;
import org.json.JSONObject;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * 请假管理Servlet
 * @author 汪轩增
 */
public class LeaveServlet extends HttpServlet {
    
    private LeaveApplyService leaveService = new LeaveApplyService();
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        String pathInfo = request.getPathInfo();
        if (pathInfo == null) pathInfo = "/";
        
        response.setContentType("application/json;charset=UTF-8");
        PrintWriter out = response.getWriter();
        
        try {
            switch (pathInfo) {
                case "/apply":
                    doApply(request, out);
                    break;
                case "/approve":
                    doApprove(request, out);
                    break;
                case "/cancel":
                    doCancel(request, out);
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
                case "/list":
                    doList(request, out);
                    break;
                case "/pending":
                    doPending(request, out);
                    break;
                case "/myList":
                    doMyList(request, out);
                    break;
                default:
                    if (pathInfo.matches("/\\d+")) {
                        doGetById(request, out, pathInfo.substring(1));
                    } else {
                        out.print(Result.error(404, "接口不存在").toJson());
                    }
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
    
    private void doApply(HttpServletRequest request, PrintWriter out) throws ParseException, IOException {
        User user = getCurrentUser(request);
        if (user == null) {
            out.print(Result.unauthorized().toJson());
            return;
        }
        
        // 读取JSON请求体
        StringBuilder sb = new StringBuilder();
        BufferedReader reader = request.getReader();
        String line;
        while ((line = reader.readLine()) != null) {
            sb.append(line);
        }
        
        JSONObject json = new JSONObject(sb.toString());
        LeaveApply leave = new LeaveApply();
        leave.setUserId(user.getId());
        leave.setLeaveType(json.getInt("leaveType"));
        // 处理datetime-local格式：将T替换为空格，并补充秒
        String startTimeStr = json.getString("startTime").replace("T", " ");
        String endTimeStr = json.getString("endTime").replace("T", " ");
        // 如果只有 yyyy-MM-dd HH:mm 格式，补充 :00
        if (startTimeStr.length() == 16) startTimeStr += ":00";
        if (endTimeStr.length() == 16) endTimeStr += ":00";
        leave.setStartTime(sdf.parse(startTimeStr));
        leave.setEndTime(sdf.parse(endTimeStr));
        leave.setDays(new java.math.BigDecimal(json.getDouble("days")));
        leave.setReason(json.getString("reason"));
        
        if (leaveService.submitApply(leave)) {
            out.print(Result.success("请假申请已提交").toJson());
        } else {
            out.print(Result.error("提交失败").toJson());
        }
    }
    
    private void doApprove(HttpServletRequest request, PrintWriter out) {
        User user = getCurrentUser(request);
        if (user == null) {
            out.print(Result.unauthorized().toJson());
            return;
        }
        
        Integer id = Integer.parseInt(request.getParameter("id"));
        Integer status = Integer.parseInt(request.getParameter("status"));
        String remark = request.getParameter("remark");
        
        if (leaveService.approve(id, user.getId(), status, remark)) {
            out.print(Result.success(status == 1 ? "审批通过" : "已驳回").toJson());
        } else {
            out.print(Result.error("审批失败").toJson());
        }
    }
    
    private void doCancel(HttpServletRequest request, PrintWriter out) {
        User user = getCurrentUser(request);
        if (user == null) {
            out.print(Result.unauthorized().toJson());
            return;
        }
        
        Integer id = Integer.parseInt(request.getParameter("id"));
        if (leaveService.cancel(id, user.getId())) {
            out.print(Result.success("请假已撤销").toJson());
        } else {
            out.print(Result.error("撤销失败").toJson());
        }
    }
    
    private void doList(HttpServletRequest request, PrintWriter out) {
        List<LeaveApply> list = leaveService.listAll();
        out.print(Result.success(list, (long) list.size()).toJson());
    }
    
    private void doPending(HttpServletRequest request, PrintWriter out) {
        List<LeaveApply> list = leaveService.listPending();
        out.print(Result.success(list, (long) list.size()).toJson());
    }
    
    private void doMyList(HttpServletRequest request, PrintWriter out) {
        User user = getCurrentUser(request);
        if (user == null) {
            out.print(Result.unauthorized().toJson());
            return;
        }
        List<LeaveApply> list = leaveService.listByUserId(user.getId());
        out.print(Result.success(list, (long) list.size()).toJson());
    }
    
    private void doGetById(HttpServletRequest request, PrintWriter out, String id) {
        LeaveApply leave = leaveService.getById(Integer.parseInt(id));
        if (leave == null) {
            out.print(Result.error("记录不存在").toJson());
        } else {
            out.print(Result.success(leave).toJson());
        }
    }
}
