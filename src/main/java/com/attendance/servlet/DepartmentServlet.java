package com.attendance.servlet;

import com.attendance.model.Department;
import com.attendance.service.DepartmentService;
import com.attendance.util.Result;
import org.json.JSONObject;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * 部门管理Servlet
 * @author 汪轩增
 */
public class DepartmentServlet extends HttpServlet {
    
    private DepartmentService deptService = new DepartmentService();
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        String pathInfo = request.getPathInfo();
        if (pathInfo == null) pathInfo = "/";
        
        response.setContentType("application/json;charset=UTF-8");
        PrintWriter out = response.getWriter();
        
        try {
            switch (pathInfo) {
                case "/add":
                    doAdd(request, out);
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
                case "/list":
                    doList(request, out);
                    break;
                case "/listAll":
                    doListAll(request, out);
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
        if (deptService.delete(Integer.parseInt(id))) {
            out.print(Result.success("删除部门成功").toJson());
        } else {
            out.print(Result.error("删除部门失败").toJson());
        }
    }
    
    private void doList(HttpServletRequest request, PrintWriter out) {
        out.print(Result.success(deptService.listActive()).toJson());
    }
    
    private void doListAll(HttpServletRequest request, PrintWriter out) {
        out.print(Result.success(deptService.listAll()).toJson());
    }
    
    private void doGetById(HttpServletRequest request, PrintWriter out, String id) {
        Department dept = deptService.getById(Integer.parseInt(id));
        if (dept == null) {
            out.print(Result.error("部门不存在").toJson());
        } else {
            out.print(Result.success(dept).toJson());
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
        Department dept = new Department();
        dept.setDeptName(json.getString("deptName"));
        dept.setDeptCode(json.getString("deptCode"));
        if (json.has("manager")) dept.setManager(json.getString("manager"));
        if (json.has("phone")) dept.setPhone(json.getString("phone"));
        if (json.has("description")) dept.setDescription(json.getString("description"));
        
        if (deptService.add(dept)) {
            out.print(Result.success("新增部门成功").toJson());
        } else {
            out.print(Result.error("新增部门失败").toJson());
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
        Department dept = new Department();
        if (json.has("id")) dept.setId(json.getInt("id"));
        if (json.has("deptName")) dept.setDeptName(json.getString("deptName"));
        if (json.has("deptCode")) dept.setDeptCode(json.getString("deptCode"));
        if (json.has("manager")) dept.setManager(json.getString("manager"));
        if (json.has("phone")) dept.setPhone(json.getString("phone"));
        if (json.has("description")) dept.setDescription(json.getString("description"));
        
        if (deptService.update(dept)) {
            out.print(Result.success("修改部门成功").toJson());
        } else {
            out.print(Result.error("修改部门失败").toJson());
        }
    }
    
    private void doDelete(HttpServletRequest request, PrintWriter out) {
        String idStr = request.getParameter("id");
        if (idStr == null) {
            out.print(Result.error("参数错误").toJson());
            return;
        }
        if (deptService.delete(Integer.parseInt(idStr))) {
            out.print(Result.success("删除部门成功").toJson());
        } else {
            out.print(Result.error("删除部门失败").toJson());
        }
    }
    
    private Department parseDept(HttpServletRequest request) {
        Department dept = new Department();
        dept.setDeptName(request.getParameter("deptName"));
        dept.setDeptCode(request.getParameter("deptCode"));
        dept.setManager(request.getParameter("manager"));
        dept.setPhone(request.getParameter("phone"));
        dept.setDescription(request.getParameter("description"));
        return dept;
    }
}
