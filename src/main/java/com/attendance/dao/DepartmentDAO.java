package com.attendance.dao;

import com.attendance.model.Department;
import com.attendance.util.DBUtil;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 部门数据访问类
 * @author 汪轩增
 */
public class DepartmentDAO {
    
    /**
     * 查询所有部门（只查询未删除的）
     */
    public List<Department> listAll() {
        List<Department> list = new ArrayList<>();
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            conn = DBUtil.getConnection();
            String sql = "SELECT * FROM department WHERE status = 1 ORDER BY id";
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();
            
            while (rs.next()) {
                list.add(extractDept(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(conn, pstmt, rs);
        }
        return list;
    }
    
    /**
     * 查询启用状态的部门
     */
    public List<Department> listActive() {
        List<Department> list = new ArrayList<>();
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            conn = DBUtil.getConnection();
            String sql = "SELECT * FROM department WHERE status = 1 ORDER BY id";
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();
            
            while (rs.next()) {
                list.add(extractDept(rs));
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
    public Department getById(Integer id) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            conn = DBUtil.getConnection();
            String sql = "SELECT * FROM department WHERE id = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, id);
            rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return extractDept(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(conn, pstmt, rs);
        }
        return null;
    }
    
    /**
     * 新增部门
     */
    public boolean insert(Department dept) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        try {
            conn = DBUtil.getConnection();
            String sql = "INSERT INTO department (dept_name, dept_code, manager, phone, description, create_time) " +
                        "VALUES (?, ?, ?, ?, ?, NOW())";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, dept.getDeptName());
            pstmt.setString(2, dept.getDeptCode());
            pstmt.setString(3, dept.getManager());
            pstmt.setString(4, dept.getPhone());
            pstmt.setString(5, dept.getDescription());
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            DBUtil.close(conn, pstmt);
        }
    }
    
    /**
     * 更新部门
     */
    public boolean update(Department dept) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        try {
            conn = DBUtil.getConnection();
            String sql = "UPDATE department SET dept_name=?, dept_code=?, manager=?, phone=?, description=?, update_time=NOW() WHERE id=?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, dept.getDeptName());
            pstmt.setString(2, dept.getDeptCode());
            pstmt.setString(3, dept.getManager());
            pstmt.setString(4, dept.getPhone());
            pstmt.setString(5, dept.getDescription());
            pstmt.setInt(6, dept.getId());
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            DBUtil.close(conn, pstmt);
        }
    }
    
    /**
     * 删除部门（逻辑删除）
     */
    public boolean delete(Integer id) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        try {
            conn = DBUtil.getConnection();
            String sql = "UPDATE department SET status=0, update_time=NOW() WHERE id=?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, id);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            DBUtil.close(conn, pstmt);
        }
    }
    
    private Department extractDept(ResultSet rs) throws SQLException {
        Department dept = new Department();
        dept.setId(rs.getInt("id"));
        dept.setDeptName(rs.getString("dept_name"));
        dept.setDeptCode(rs.getString("dept_code"));
        dept.setManager(rs.getString("manager"));
        dept.setPhone(rs.getString("phone"));
        dept.setDescription(rs.getString("description"));
        dept.setCreateTime(rs.getTimestamp("create_time"));
        dept.setUpdateTime(rs.getTimestamp("update_time"));
        dept.setStatus(rs.getObject("status", Integer.class));
        return dept;
    }
}
