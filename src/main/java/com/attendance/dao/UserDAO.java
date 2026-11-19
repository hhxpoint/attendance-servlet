package com.attendance.dao;

import com.attendance.model.User;
import com.attendance.util.DBUtil;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 用户数据访问类
 * @author 汪轩增
 */
public class UserDAO {
    
    /**
     * 用户登录验证
     */
    public User login(String username, String password) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            conn = DBUtil.getConnection();
            String sql = "SELECT u.*, d.dept_name FROM user u " +
                        "LEFT JOIN department d ON u.dept_id = d.id " +
                        "WHERE u.username = ? AND u.status = 1";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, username);
            rs = pstmt.executeQuery();
            
            if (rs.next()) {
                String storedPwd = rs.getString("password");
                // 密码已在Service层MD5加密，这里直接比较
                if (storedPwd != null && storedPwd.equals(password)) {
                    return extractUser(rs);
                }
            }
            return null;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        } finally {
            DBUtil.close(conn, pstmt, rs);
        }
    }
    
    /**
     * 查询所有用户（含部门信息）
     */
    public List<User> listAll() {
        List<User> list = new ArrayList<>();
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            conn = DBUtil.getConnection();
            String sql = "SELECT u.*, d.dept_name FROM user u " +
                        "LEFT JOIN department d ON u.dept_id = d.id " +
                        "WHERE u.status = 1 ORDER BY u.create_time DESC";
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();
            
            while (rs.next()) {
                list.add(extractUser(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(conn, pstmt, rs);
        }
        return list;
    }
    
    /**
     * 根据ID查询用户
     */
    public User getById(Integer id) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            conn = DBUtil.getConnection();
            String sql = "SELECT u.*, d.dept_name FROM user u " +
                        "LEFT JOIN department d ON u.dept_id = d.id WHERE u.id = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, id);
            rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return extractUser(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(conn, pstmt, rs);
        }
        return null;
    }
    
    /**
     * 根据部门ID查询用户
     */
    public List<User> listByDeptId(Integer deptId) {
        List<User> list = new ArrayList<>();
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            conn = DBUtil.getConnection();
            String sql = "SELECT u.*, d.dept_name FROM user u " +
                        "LEFT JOIN department d ON u.dept_id = d.id " +
                        "WHERE u.dept_id = ? AND u.status = 1";
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, deptId);
            rs = pstmt.executeQuery();
            
            while (rs.next()) {
                list.add(extractUser(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(conn, pstmt, rs);
        }
        return list;
    }
    
    /**
     * 检查用户名是否存在
     */
    public boolean existsByUsername(String username) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            conn = DBUtil.getConnection();
            String sql = "SELECT COUNT(*) FROM user WHERE username = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, username);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(conn, pstmt, rs);
        }
        return false;
    }
    
    /**
     * 新增用户
     */
    public boolean insert(User user) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        try {
            conn = DBUtil.getConnection();
            String sql = "INSERT INTO user (username, password, real_name, gender, phone, email, " +
                        "dept_id, position, role, entry_date, create_time) " +
                        "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, NOW())";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, user.getUsername());
            pstmt.setString(2, user.getPassword());
            pstmt.setString(3, user.getRealName());
            pstmt.setObject(4, user.getGender());
            pstmt.setString(5, user.getPhone());
            pstmt.setString(6, user.getEmail());
            pstmt.setObject(7, user.getDeptId());
            pstmt.setString(8, user.getPosition());
            pstmt.setObject(9, user.getRole());
            pstmt.setDate(10, user.getEntryDate() != null ? new java.sql.Date(user.getEntryDate().getTime()) : null);
            
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            DBUtil.close(conn, pstmt);
        }
    }
    
    /**
     * 修改用户
     */
    public boolean update(User user) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        try {
            conn = DBUtil.getConnection();
            String sql = "UPDATE user SET real_name=?, gender=?, phone=?, email=?, " +
                        "dept_id=?, position=?, role=?, avatar=?, entry_date=?, update_time=NOW() " +
                        "WHERE id=?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, user.getRealName());
            pstmt.setObject(2, user.getGender());
            pstmt.setString(3, user.getPhone());
            pstmt.setString(4, user.getEmail());
            pstmt.setObject(5, user.getDeptId());
            pstmt.setString(6, user.getPosition());
            pstmt.setObject(7, user.getRole());
            pstmt.setString(8, user.getAvatar());
            pstmt.setDate(9, user.getEntryDate() != null ? new java.sql.Date(user.getEntryDate().getTime()) : null);
            pstmt.setInt(10, user.getId());
            
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            DBUtil.close(conn, pstmt);
        }
    }
    
    /**
     * 删除用户（逻辑删除）
     */
    public boolean delete(Integer id) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        try {
            conn = DBUtil.getConnection();
            String sql = "UPDATE user SET status=0, update_time=NOW() WHERE id=?";
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
    
    /**
     * 重置密码
     */
    public boolean resetPassword(Integer userId, String newPassword) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        try {
            conn = DBUtil.getConnection();
            String sql = "UPDATE user SET password=?, update_time=NOW() WHERE id=?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, newPassword);
            pstmt.setInt(2, userId);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            DBUtil.close(conn, pstmt);
        }
    }
    
    /**
     * 从ResultSet中提取用户对象
     */
    private User extractUser(ResultSet rs) throws SQLException {
        User user = new User();
        user.setId(rs.getInt("id"));
        user.setUsername(rs.getString("username"));
        user.setPassword(rs.getString("password"));
        user.setRealName(rs.getString("real_name"));
        user.setGender(rs.getObject("gender", Integer.class));
        user.setPhone(rs.getString("phone"));
        user.setEmail(rs.getString("email"));
        user.setDeptId(rs.getObject("dept_id", Integer.class));
        user.setPosition(rs.getString("position"));
        user.setRole(rs.getObject("role", Integer.class));
        user.setAvatar(rs.getString("avatar"));
        user.setEntryDate(rs.getDate("entry_date"));
        user.setCreateTime(rs.getTimestamp("create_time"));
        user.setUpdateTime(rs.getTimestamp("update_time"));
        user.setStatus(rs.getObject("status", Integer.class));
        user.setDeptName(rs.getString("dept_name"));
        return user;
    }
}
