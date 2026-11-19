package com.attendance.dao;

import com.attendance.model.LeaveApply;
import com.attendance.util.DBUtil;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 请假申请数据访问类
 * @author 汪轩增
 */
public class LeaveApplyDAO {
    
    /**
     * 查询所有请假记录
     */
    public List<LeaveApply> listAll() {
        List<LeaveApply> list = new ArrayList<>();
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            conn = DBUtil.getConnection();
            String sql = "SELECT l.*, u.real_name, d.dept_name, a.real_name as approver_name " +
                        "FROM leave_apply l " +
                        "LEFT JOIN user u ON l.user_id = u.id " +
                        "LEFT JOIN department d ON u.dept_id = d.id " +
                        "LEFT JOIN user a ON l.approver_id = a.id " +
                        "ORDER BY l.create_time DESC";
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();
            
            while (rs.next()) {
                list.add(extractLeave(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(conn, pstmt, rs);
        }
        return list;
    }
    
    /**
     * 查询待审批列表
     */
    public List<LeaveApply> listPending() {
        List<LeaveApply> list = new ArrayList<>();
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            conn = DBUtil.getConnection();
            String sql = "SELECT l.*, u.real_name, d.dept_name, a.real_name as approver_name " +
                        "FROM leave_apply l " +
                        "LEFT JOIN user u ON l.user_id = u.id " +
                        "LEFT JOIN department d ON u.dept_id = d.id " +
                        "LEFT JOIN user a ON l.approver_id = a.id " +
                        "WHERE l.status = 0 ORDER BY l.create_time DESC";
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();
            
            while (rs.next()) {
                list.add(extractLeave(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(conn, pstmt, rs);
        }
        return list;
    }
    
    /**
     * 根据用户ID查询
     */
    public List<LeaveApply> listByUserId(Integer userId) {
        List<LeaveApply> list = new ArrayList<>();
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            conn = DBUtil.getConnection();
            String sql = "SELECT l.*, u.real_name, d.dept_name, a.real_name as approver_name " +
                        "FROM leave_apply l " +
                        "LEFT JOIN user u ON l.user_id = u.id " +
                        "LEFT JOIN department d ON u.dept_id = d.id " +
                        "LEFT JOIN user a ON l.approver_id = a.id " +
                        "WHERE l.user_id = ? ORDER BY l.create_time DESC";
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, userId);
            rs = pstmt.executeQuery();
            
            while (rs.next()) {
                list.add(extractLeave(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(conn, pstmt, rs);
        }
        return list;
    }
    
    /**
     * 根据ID查询
     */
    public LeaveApply getById(Integer id) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            conn = DBUtil.getConnection();
            String sql = "SELECT l.*, u.real_name, d.dept_name, a.real_name as approver_name " +
                        "FROM leave_apply l " +
                        "LEFT JOIN user u ON l.user_id = u.id " +
                        "LEFT JOIN department d ON u.dept_id = d.id " +
                        "LEFT JOIN user a ON l.approver_id = a.id " +
                        "WHERE l.id = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, id);
            rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return extractLeave(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(conn, pstmt, rs);
        }
        return null;
    }
    
    /**
     * 新增请假申请
     */
    public boolean insert(LeaveApply leave) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        try {
            conn = DBUtil.getConnection();
            String sql = "INSERT INTO leave_apply (user_id, leave_type, start_time, end_time, days, reason, status, create_time) " +
                        "VALUES (?, ?, ?, ?, ?, ?, 0, NOW())";
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, leave.getUserId());
            pstmt.setInt(2, leave.getLeaveType());
            pstmt.setTimestamp(3, new Timestamp(leave.getStartTime().getTime()));
            pstmt.setTimestamp(4, new Timestamp(leave.getEndTime().getTime()));
            pstmt.setBigDecimal(5, leave.getDays());
            pstmt.setString(6, leave.getReason());
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            DBUtil.close(conn, pstmt);
        }
    }
    
    /**
     * 审批请假
     */
    public boolean approve(Integer id, Integer approverId, Integer status, String remark) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        try {
            conn = DBUtil.getConnection();
            String sql = "UPDATE leave_apply SET status=?, approver_id=?, approve_time=NOW(), approve_remark=?, update_time=NOW() WHERE id=?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, status);
            pstmt.setInt(2, approverId);
            pstmt.setString(3, remark);
            pstmt.setInt(4, id);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            DBUtil.close(conn, pstmt);
        }
    }
    
    /**
     * 撤销请假
     */
    public boolean cancel(Integer id, Integer userId) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        try {
            conn = DBUtil.getConnection();
            String sql = "UPDATE leave_apply SET status=3, update_time=NOW() WHERE id=? AND user_id=? AND status=0";
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, id);
            pstmt.setInt(2, userId);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            DBUtil.close(conn, pstmt);
        }
    }
    
    private LeaveApply extractLeave(ResultSet rs) throws SQLException {
        LeaveApply l = new LeaveApply();
        l.setId(rs.getInt("id"));
        l.setUserId(rs.getInt("user_id"));
        l.setLeaveType(rs.getObject("leave_type", Integer.class));
        l.setStartTime(rs.getTimestamp("start_time"));
        l.setEndTime(rs.getTimestamp("end_time"));
        l.setDays(rs.getBigDecimal("days"));
        l.setReason(rs.getString("reason"));
        l.setStatus(rs.getObject("status", Integer.class));
        l.setApproverId(rs.getObject("approver_id", Integer.class));
        l.setApproveTime(rs.getTimestamp("approve_time"));
        l.setApproveRemark(rs.getString("approve_remark"));
        l.setCreateTime(rs.getTimestamp("create_time"));
        l.setUpdateTime(rs.getTimestamp("update_time"));
        l.setRealName(rs.getString("real_name"));
        l.setDeptName(rs.getString("dept_name"));
        l.setApproverName(rs.getString("approver_name"));
        return l;
    }
}
