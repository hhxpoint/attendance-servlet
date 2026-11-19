package com.attendance.service;

import com.attendance.dao.UserDAO;
import com.attendance.model.User;
import java.util.List;

/**
 * 用户业务类
 * @author 梁宏博
 */
public class UserService {
    
    private UserDAO userDAO = new UserDAO();
    
    /**
     * 用户登录
     */
    public User login(String username, String password) {
        if (username == null || password == null || username.trim().isEmpty() || password.trim().isEmpty()) {
            return null;
        }
        // MD5加密密码
        String md5Pwd = com.attendance.util.MD5Util.md5(password);
        return userDAO.login(username, md5Pwd);
    }
    
    /**
     * 查询所有用户
     */
    public List<User> listAll() {
        return userDAO.listAll();
    }
    
    /**
     * 根据ID查询
     */
    public User getById(Integer id) {
        return userDAO.getById(id);
    }
    
    /**
     * 根据部门查询
     */
    public List<User> listByDeptId(Integer deptId) {
        return userDAO.listByDeptId(deptId);
    }
    
    /**
     * 新增用户
     */
    public boolean addUser(User user) {
        // 检查用户名是否存在
        if (userDAO.existsByUsername(user.getUsername())) {
            return false;
        }
        // 密码MD5加密
        user.setPassword(com.attendance.util.MD5Util.md5(user.getPassword()));
        user.setRole(1); // 默认普通员工
        user.setStatus(1); // 启用状态
        return userDAO.insert(user);
    }
    
    /**
     * 修改用户
     */
    public boolean updateUser(User user) {
        return userDAO.update(user);
    }
    
    /**
     * 删除用户
     */
    public boolean deleteUser(Integer id) {
        return userDAO.delete(id);
    }
    
    /**
     * 重置密码
     */
    public boolean resetPassword(Integer userId, String newPassword) {
        String md5Pwd = com.attendance.util.MD5Util.md5(newPassword);
        return userDAO.resetPassword(userId, md5Pwd);
    }
    
    /**
     * 修改密码
     */
    public boolean changePassword(Integer userId, String oldPassword, String newPassword) {
        User user = userDAO.getById(userId);
        if (user == null) return false;
        
        String oldMd5 = com.attendance.util.MD5Util.md5(oldPassword);
        if (!oldMd5.equals(user.getPassword())) return false;
        
        String newMd5 = com.attendance.util.MD5Util.md5(newPassword);
        return userDAO.resetPassword(userId, newMd5);
    }
}
