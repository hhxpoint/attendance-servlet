package com.attendance.dao;

import com.attendance.model.Announcement;
import com.attendance.util.DBUtil;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 公告数据访问类
 * @author 汪轩增
 */
public class AnnouncementDAO {
    
    /**
     * 查询已发布的公告
     */
    public List<Announcement> listPublished() {
        List<Announcement> list = new ArrayList<>();
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            conn = DBUtil.getConnection();
            String sql = "SELECT a.*, u.real_name as publisher_name FROM announcement a " +
                        "LEFT JOIN user u ON a.publisher_id = u.id " +
                        "WHERE a.status = 1 ORDER BY a.is_top DESC, a.create_time DESC";
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();
            
            while (rs.next()) {
                list.add(extractAnn(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(conn, pstmt, rs);
        }
        return list;
    }
    
    /**
     * 根据ID查询详情
     */
    public Announcement getById(Integer id) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            conn = DBUtil.getConnection();
            String sql = "SELECT a.*, u.real_name as publisher_name FROM announcement a " +
                        "LEFT JOIN user u ON a.publisher_id = u.id WHERE a.id = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, id);
            rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return extractAnn(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(conn, pstmt, rs);
        }
        return null;
    }
    
    /**
     * 新增公告
     */
    public boolean insert(Announcement ann) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        try {
            conn = DBUtil.getConnection();
            String sql = "INSERT INTO announcement (title, content, publisher_id, type, is_top, status, create_time) " +
                        "VALUES (?, ?, ?, ?, ?, 1, NOW())";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, ann.getTitle());
            pstmt.setString(2, ann.getContent());
            pstmt.setInt(3, ann.getPublisherId());
            pstmt.setInt(4, ann.getType());
            pstmt.setInt(5, ann.getIsTop());
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            DBUtil.close(conn, pstmt);
        }
    }
    
    /**
     * 更新公告
     */
    public boolean update(Announcement ann) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        try {
            conn = DBUtil.getConnection();
            String sql = "UPDATE announcement SET title=?, content=?, type=?, is_top=?, update_time=NOW() WHERE id=?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, ann.getTitle());
            pstmt.setString(2, ann.getContent());
            pstmt.setInt(3, ann.getType());
            pstmt.setInt(4, ann.getIsTop());
            pstmt.setInt(5, ann.getId());
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            DBUtil.close(conn, pstmt);
        }
    }
    
    /**
     * 删除公告
     */
    public boolean delete(Integer id) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        try {
            conn = DBUtil.getConnection();
            String sql = "UPDATE announcement SET status=0, update_time=NOW() WHERE id=?";
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
    
    private Announcement extractAnn(ResultSet rs) throws SQLException {
        Announcement a = new Announcement();
        a.setId(rs.getInt("id"));
        a.setTitle(rs.getString("title"));
        a.setContent(rs.getString("content"));
        a.setPublisherId(rs.getInt("publisher_id"));
        a.setType(rs.getObject("type", Integer.class));
        a.setIsTop(rs.getObject("is_top", Integer.class));
        a.setCreateTime(rs.getTimestamp("create_time"));
        a.setUpdateTime(rs.getTimestamp("update_time"));
        a.setStatus(rs.getObject("status", Integer.class));
        a.setPublisherName(rs.getString("publisher_name"));
        return a;
    }
}
