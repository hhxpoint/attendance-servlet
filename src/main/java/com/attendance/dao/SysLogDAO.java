package com.attendance.dao;

import com.attendance.model.SysLog;
import com.attendance.util.DBUtil;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 系统日志数据访问类
 * @author 汪轩增
 */
public class SysLogDAO {
    
    /**
     * 分页查询日志
     */
    public List<SysLog> listByPage(int page, int pageSize) {
        List<SysLog> list = new ArrayList<>();
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            conn = DBUtil.getConnection();
            String sql = "SELECT * FROM sys_log ORDER BY create_time DESC LIMIT ?, ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, (page - 1) * pageSize);
            pstmt.setInt(2, pageSize);
            rs = pstmt.executeQuery();
            
            while (rs.next()) {
                SysLog log = new SysLog();
                log.setId(rs.getInt("id"));
                log.setUserId(rs.getObject("user_id", Integer.class));
                log.setUsername(rs.getString("username"));
                log.setOperation(rs.getString("operation"));
                log.setMethod(rs.getString("method"));
                log.setParams(rs.getString("params"));
                log.setIp(rs.getString("ip"));
                log.setTime(rs.getObject("time", Long.class));
                log.setCreateTime(rs.getTimestamp("create_time"));
                list.add(log);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(conn, pstmt, rs);
        }
        return list;
    }
    
    /**
     * 查询总数
     */
    public int count() {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            conn = DBUtil.getConnection();
            String sql = "SELECT COUNT(*) FROM sys_log";
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(conn, pstmt, rs);
        }
        return 0;
    }
    
    /**
     * 新增日志
     */
    public boolean insert(SysLog log) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        try {
            conn = DBUtil.getConnection();
            String sql = "INSERT INTO sys_log (user_id, username, operation, method, params, ip, time, create_time) " +
                        "VALUES (?, ?, ?, ?, ?, ?, ?, NOW())";
            pstmt = conn.prepareStatement(sql);
            pstmt.setObject(1, log.getUserId());
            pstmt.setString(2, log.getUsername());
            pstmt.setString(3, log.getOperation());
            pstmt.setString(4, log.getMethod());
            pstmt.setString(5, log.getParams());
            pstmt.setString(6, log.getIp());
            pstmt.setObject(7, log.getTime());
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            DBUtil.close(conn, pstmt);
        }
    }
}
