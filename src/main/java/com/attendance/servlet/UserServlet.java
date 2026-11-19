package com.attendance.servlet;

import com.attendance.model.User;
import com.attendance.service.UserService;
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

/**
 * 用户管理Servlet
 * @author 汪轩增
 */
public class UserServlet extends HttpServlet {
    
    private UserService userService = new UserService();
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        String pathInfo = request.getPathInfo();
        if (pathInfo == null) pathInfo = "/";
        
        response.setContentType("application/json;charset=UTF-8");
        PrintWriter out = response.getWriter();
        
        try {
            switch (pathInfo) {
                case "/login":
                    doLogin(request, out);
                    break;
                case "/logout":
                    doLogout(request, out);
                    break;
                case "/add":
                    doAdd(request, out);
                    break;
                case "/resetPwd":
                    doResetPwd(request, out);
                    break;
                case "/changePwd":
                    doChangePwd(request, out);
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
    protected void doPut(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        String pathInfo = request.getPathInfo();
        if (pathInfo == null) pathInfo = "/";
        
        response.setContentType("application/json;charset=UTF-8");
        PrintWriter out = response.getWriter();
        
        try {
            switch (pathInfo) {
                case "/update":
                    doUpdate(request, out);
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
                case "/current":
                    doGetCurrent(request, out);
                    break;
                case "/list":
                    doList(request, out);
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
    
    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        String pathInfo = request.getPathInfo();
        if (pathInfo == null) pathInfo = "/";
        
        response.setContentType("application/json;charset=UTF-8");
        PrintWriter out = response.getWriter();
        
        try {
            if (pathInfo.matches("/\\d+")) {
                doDelete(request, out, pathInfo.substring(1));
            } else {
                out.print(Result.error(404, "接口不存在").toJson());
            }
        } catch (Exception e) {
            e.printStackTrace();
            out.print(Result.error("服务器错误").toJson());
        }
    }
    
    private void doDelete(HttpServletRequest request, PrintWriter out, String id) {
        if (userService.deleteUser(Integer.parseInt(id))) {
            out.print(Result.success("删除成功").toJson());
        } else {
            out.print(Result.error("删除失败").toJson());
        }
    }
    
    private void doLogin(HttpServletRequest request, PrintWriter out) throws IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        
        User user = userService.login(username, password);
        if (user == null) {
            out.print(Result.error("用户名或密码错误").toJson());
            return;
        }
        
        HttpSession session = request.getSession();
        session.setAttribute("currentUser", user);
        session.setMaxInactiveInterval(30 * 60);
        
        // 清除密码
        user.setPassword(null);
        out.print(Result.success("登录成功", user).toJson());
    }
    
    private void doLogout(HttpServletRequest request, PrintWriter out) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        out.print(Result.success("登出成功").toJson());
    }
    
    private void doGetCurrent(HttpServletRequest request, PrintWriter out) {
        HttpSession session = request.getSession(false);
        if (session == null) {
            out.print(Result.unauthorized().toJson());
            return;
        }
        User user = (User) session.getAttribute("currentUser");
        if (user == null) {
            out.print(Result.unauthorized().toJson());
            return;
        }
        user.setPassword(null);
        out.print(Result.success(user).toJson());
    }
    
    private void doList(HttpServletRequest request, PrintWriter out) throws IOException {
        String deptIdStr = request.getParameter("deptId");
        if (deptIdStr != null && !deptIdStr.isEmpty()) {
            Integer deptId = Integer.parseInt(deptIdStr);
            out.print(Result.success(userService.listByDeptId(deptId)).toJson());
        } else {
            out.print(Result.success(userService.listAll()).toJson());
        }
    }
    
    private void doGetById(HttpServletRequest request, PrintWriter out, String id) {
        User user = userService.getById(Integer.parseInt(id));
        if (user == null) {
            out.print(Result.error("用户不存在").toJson());
        } else {
            user.setPassword(null);
            out.print(Result.success(user).toJson());
        }
    }
    
    private void doAdd(HttpServletRequest request, PrintWriter out) throws IOException {
        // 读取JSON请求体
        StringBuilder sb = new StringBuilder();
        BufferedReader reader = request.getReader();
        String line;
        while ((line = reader.readLine()) != null) {
            sb.append(line);
        }
        
        JSONObject json = new JSONObject(sb.toString());
        User user = new User();
        user.setUsername(json.getString("username"));
        user.setPassword(json.getString("password"));
        user.setRealName(json.getString("realName"));
        if (json.has("gender")) user.setGender(json.getInt("gender"));
        if (json.has("phone")) user.setPhone(json.getString("phone"));
        if (json.has("email")) user.setEmail(json.getString("email"));
        if (json.has("deptId")) user.setDeptId(json.getInt("deptId"));
        if (json.has("position")) user.setPosition(json.getString("position"));
        if (json.has("role")) user.setRole(json.getInt("role"));
        if (json.has("entryDate") && !json.getString("entryDate").isEmpty()) {
            try {
                user.setEntryDate(new java.text.SimpleDateFormat("yyyy-MM-dd").parse(json.getString("entryDate")));
            } catch (Exception e) {
                // 忽略日期解析错误
            }
        }
        
        if (userService.addUser(user)) {
            out.print(Result.success("新增用户成功").toJson());
        } else {
            out.print(Result.error("用户名已存在").toJson());
        }
    }
    
    private void doUpdate(HttpServletRequest request, PrintWriter out) throws IOException {
        // 读取JSON请求体
        StringBuilder sb = new StringBuilder();
        BufferedReader reader = request.getReader();
        String line;
        while ((line = reader.readLine()) != null) {
            sb.append(line);
        }
        
        JSONObject json = new JSONObject(sb.toString());
        User user = new User();
        if (json.has("id")) user.setId(json.getInt("id"));
        if (json.has("username")) user.setUsername(json.getString("username"));
        if (json.has("realName")) user.setRealName(json.getString("realName"));
        if (json.has("gender")) user.setGender(json.getInt("gender"));
        if (json.has("phone")) user.setPhone(json.getString("phone"));
        if (json.has("email")) user.setEmail(json.getString("email"));
        if (json.has("deptId")) user.setDeptId(json.getInt("deptId"));
        if (json.has("position")) user.setPosition(json.getString("position"));
        if (json.has("role")) user.setRole(json.getInt("role"));
        
        if (userService.updateUser(user)) {
            out.print(Result.success("修改成功").toJson());
        } else {
            out.print(Result.error("修改失败").toJson());
        }
    }
    
    private void doResetPwd(HttpServletRequest request, PrintWriter out) {
        String userIdStr = request.getParameter("userId");
        String newPassword = request.getParameter("newPassword");
        if (userIdStr == null || newPassword == null) {
            out.print(Result.error("参数错误").toJson());
            return;
        }
        if (userService.resetPassword(Integer.parseInt(userIdStr), newPassword)) {
            out.print(Result.success("密码重置成功").toJson());
        } else {
            out.print(Result.error("密码重置失败").toJson());
        }
    }
    
    private void doChangePwd(HttpServletRequest request, PrintWriter out) {
        String userIdStr = request.getParameter("userId");
        String oldPassword = request.getParameter("oldPassword");
        String newPassword = request.getParameter("newPassword");
        if (userIdStr == null || oldPassword == null || newPassword == null) {
            out.print(Result.error("参数错误").toJson());
            return;
        }
        if (userService.changePassword(Integer.parseInt(userIdStr), oldPassword, newPassword)) {
            out.print(Result.success("密码修改成功").toJson());
        } else {
            out.print(Result.error("原密码错误").toJson());
        }
    }
    
    private User parseUser(HttpServletRequest request) {
        User user = new User();
        user.setUsername(request.getParameter("username"));
        user.setPassword(request.getParameter("password"));
        user.setRealName(request.getParameter("realName"));
        String gender = request.getParameter("gender");
        if (gender != null) user.setGender(Integer.parseInt(gender));
        user.setPhone(request.getParameter("phone"));
        user.setEmail(request.getParameter("email"));
        String deptId = request.getParameter("deptId");
        if (deptId != null) user.setDeptId(Integer.parseInt(deptId));
        user.setPosition(request.getParameter("position"));
        String role = request.getParameter("role");
        if (role != null) user.setRole(Integer.parseInt(role));
        return user;
    }
}
