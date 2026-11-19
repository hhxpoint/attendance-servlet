package com.attendance.servlet;

import com.attendance.model.Announcement;
import com.attendance.model.User;
import com.attendance.service.AnnouncementService;
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
import java.util.List;

/**
 * 公告管理Servlet
 * @author 汪轩增
 */
public class AnnouncementServlet extends HttpServlet {
    
    private AnnouncementService annService = new AnnouncementService();
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        String pathInfo = request.getPathInfo();
        if (pathInfo == null) pathInfo = "/";
        
        response.setContentType("application/json;charset=UTF-8");
        PrintWriter out = response.getWriter();
        
        try {
            switch (pathInfo) {
                case "/publish":
                    doPublish(request, out);
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
        if (annService.delete(Integer.parseInt(id))) {
            out.print(Result.success("删除公告成功").toJson());
        } else {
            out.print(Result.error("删除公告失败").toJson());
        }
    }
    
    private User getCurrentUser(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null) return null;
        return (User) session.getAttribute("currentUser");
    }
    
    private void doList(HttpServletRequest request, PrintWriter out) {
        List<Announcement> list = annService.listPublished();
        out.print(Result.success(list, (long) list.size()).toJson());
    }
    
    private void doGetById(HttpServletRequest request, PrintWriter out, String id) {
        Announcement ann = annService.getById(Integer.parseInt(id));
        if (ann == null) {
            out.print(Result.error("公告不存在").toJson());
        } else {
            out.print(Result.success(ann).toJson());
        }
    }
    
    private void doPublish(HttpServletRequest request, PrintWriter out) throws IOException {
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
        Announcement ann = new Announcement();
        ann.setTitle(json.getString("title"));
        ann.setContent(json.getString("content"));
        ann.setPublisherId(user.getId());
        if (json.has("type")) ann.setType(json.getInt("type"));
        if (json.has("isTop")) ann.setIsTop(json.getInt("isTop"));
        
        if (annService.publish(ann)) {
            out.print(Result.success("公告发布成功").toJson());
        } else {
            out.print(Result.error("发布公告失败").toJson());
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
        Announcement ann = new Announcement();
        if (json.has("id")) ann.setId(json.getInt("id"));
        ann.setTitle(json.getString("title"));
        ann.setContent(json.getString("content"));
        if (json.has("type")) ann.setType(json.getInt("type"));
        if (json.has("isTop")) ann.setIsTop(json.getInt("isTop"));
        
        if (annService.update(ann)) {
            out.print(Result.success("公告修改成功").toJson());
        } else {
            out.print(Result.error("修改公告失败").toJson());
        }
    }
    
    private void doDelete(HttpServletRequest request, PrintWriter out) {
        String idStr = request.getParameter("id");
        if (idStr == null) {
            out.print(Result.error("参数错误").toJson());
            return;
        }
        if (annService.delete(Integer.parseInt(idStr))) {
            out.print(Result.success("公告删除成功").toJson());
        } else {
            out.print(Result.error("删除公告失败").toJson());
        }
    }
}
