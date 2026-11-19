package com.attendance.dao;

import com.attendance.model.Attendance;
import com.attendance.util.DBUtil;
import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 考勤数据访问类
 * @author 汪轩增
 */
public class AttendanceDAO {
    
    /**
     * 上班打卡
     */
    public boolean clockIn(Attendance attendance) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        try {
            conn = DBUtil.getConnection();
            String sql = "INSERT INTO attendance (user_id, attend_date, clock_in_time, status, late_minutes, create_time) " +
                        "VALUES (?, ?, ?, ?, ?, NOW())";
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, attendance.getUserId());
            pstmt.setDate(2, new java.sql.Date(attendance.getAttendDate().getTime()));
            pstmt.setTimestamp(3, new Timestamp(attendance.getClockInTime().getTime()));
            pstmt.setInt(4, attendance.getStatus());
            pstmt.setInt(5, attendance.getLateMinutes());
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            DBUtil.close(conn, pstmt);
        }
    }
    
    /**
     * 更新考勤记录（下班打卡）
     */
    public boolean update(Attendance attendance) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        try {
            conn = DBUtil.getConnection();
            String sql = "UPDATE attendance SET clock_out_time=?, status=?, early_minutes=?, remark=?, update_time=NOW() WHERE id=?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setTimestamp(1, attendance.getClockOutTime() != null ? new Timestamp(attendance.getClockOutTime().getTime()) : null);
            pstmt.setInt(2, attendance.getStatus());
            pstmt.setInt(3, attendance.getEarlyMinutes());
            pstmt.setString(4, attendance.getRemark());
            pstmt.setInt(5, attendance.getId());
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            DBUtil.close(conn, pstmt);
        }
    }
    
    /**
     * 查询今日考勤
     */
    public Attendance getTodayAttendance(Integer userId, Date today) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            conn = DBUtil.getConnection();
            String sql = "SELECT a.*, u.real_name, d.dept_name FROM attendance a " +
                        "LEFT JOIN user u ON a.user_id = u.id " +
                        "LEFT JOIN department d ON u.dept_id = d.id " +
                        "WHERE a.user_id = ? AND a.attend_date = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, userId);
            pstmt.setDate(2, new java.sql.Date(today.getTime()));
            rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return extractAttendance(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(conn, pstmt, rs);
        }
        return null;
    }
    
    /**
     * 查询所有考勤记录
     */
    public List<Attendance> listAll() {
        List<Attendance> list = new ArrayList<>();
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            conn = DBUtil.getConnection();
            String sql = "SELECT a.*, u.real_name, d.dept_name FROM attendance a " +
                        "LEFT JOIN user u ON a.user_id = u.id " +
                        "LEFT JOIN department d ON u.dept_id = d.id " +
                        "ORDER BY a.attend_date DESC, a.create_time DESC";
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();
            
            while (rs.next()) {
                list.add(extractAttendance(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(conn, pstmt, rs);
        }
        return list;
    }
    
    /**
     * 按日期范围查询
     */
    public List<Attendance> listByDateRange(Date startDate, Date endDate) {
        List<Attendance> list = new ArrayList<>();
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            conn = DBUtil.getConnection();
            String sql = "SELECT a.*, u.real_name, d.dept_name FROM attendance a " +
                        "LEFT JOIN user u ON a.user_id = u.id " +
                        "LEFT JOIN department d ON u.dept_id = d.id " +
                        "WHERE a.attend_date BETWEEN ? AND ? ORDER BY a.attend_date DESC";
            pstmt = conn.prepareStatement(sql);
            pstmt.setDate(1, new java.sql.Date(startDate.getTime()));
            pstmt.setDate(2, new java.sql.Date(endDate.getTime()));
            rs = pstmt.executeQuery();
            
            while (rs.next()) {
                list.add(extractAttendance(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(conn, pstmt, rs);
        }
        return list;
    }
    
    /**
     * 新增或更新考勤记录
     */
    public boolean saveOrUpdate(Attendance attendance) {
        if (attendance.getId() != null) {
            return update(attendance);
        } else {
            return clockIn(attendance);
        }
    }
    
    /**
     * 部门考勤统计
     */
    public List<java.util.Map<String, Object>> getDeptStats(Date startDate, Date endDate) {
        List<java.util.Map<String, Object>> list = new ArrayList<>();
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            conn = DBUtil.getConnection();
            String sql = "SELECT d.dept_name, COUNT(*) as totalRecords, " +
                        "SUM(CASE WHEN a.status = 1 THEN 1 ELSE 0 END) as normalCount, " +
                        "SUM(CASE WHEN a.status IN (2,4) THEN 1 ELSE 0 END) as lateCount " +
                        "FROM attendance a " +
                        "LEFT JOIN user u ON a.user_id = u.id " +
                        "LEFT JOIN department d ON u.dept_id = d.id " +
                        "WHERE a.attend_date BETWEEN ? AND ? " +
                        "GROUP BY d.id, d.dept_name";
            pstmt = conn.prepareStatement(sql);
            pstmt.setDate(1, new java.sql.Date(startDate.getTime()));
            pstmt.setDate(2, new java.sql.Date(endDate.getTime()));
            rs = pstmt.executeQuery();
            
            while (rs.next()) {
                java.util.Map<String, Object> map = new java.util.HashMap<>();
                map.put("deptName", rs.getString("dept_name"));
                map.put("totalRecords", rs.getInt("totalRecords"));
                map.put("normalCount", rs.getInt("normalCount"));
                map.put("lateCount", rs.getInt("lateCount"));
                list.add(map);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(conn, pstmt, rs);
        }
        return list;
    }
    
    /**
     * 从ResultSet中提取考勤对象
     */
    private Attendance extractAttendance(ResultSet rs) throws SQLException {
        Attendance a = new Attendance();
        a.setId(rs.getInt("id"));
        a.setUserId(rs.getInt("user_id"));
        a.setAttendDate(rs.getDate("attend_date"));
        a.setClockInTime(rs.getTimestamp("clock_in_time"));
        a.setClockOutTime(rs.getTimestamp("clock_out_time"));
        a.setStatus(rs.getObject("status", Integer.class));
        a.setLateMinutes(rs.getObject("late_minutes", Integer.class));
        a.setEarlyMinutes(rs.getObject("early_minutes", Integer.class));
        a.setRemark(rs.getString("remark"));
        a.setCreateTime(rs.getTimestamp("create_time"));
        a.setUpdateTime(rs.getTimestamp("update_time"));
        a.setRealName(rs.getString("real_name"));
        a.setDeptName(rs.getString("dept_name"));
        return a;
    }
}
