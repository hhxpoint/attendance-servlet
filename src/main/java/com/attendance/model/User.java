package com.attendance.model;

import java.util.Date;

/**
 * 用户实体类
 * @author 刘鹏志
 */
public class User {
    
    private Integer id;
    private String username;
    private String password;
    private String realName;
    private Integer gender;
    private String phone;
    private String email;
    private Integer deptId;
    private String position;
    private Integer role;
    private String avatar;
    private Date entryDate;
    private Date createTime;
    private Date updateTime;
    private Integer status;
    
    // 非数据库字段
    private String deptName;
    
    public User() {}
    
    // Getters and Setters
    public Integer getId() {
        return id;
    }
    
    public void setId(Integer id) {
        this.id = id;
    }
    
    public String getUsername() {
        return username;
    }
    
    public void setUsername(String username) {
        this.username = username;
    }
    
    public String getPassword() {
        return password;
    }
    
    public void setPassword(String password) {
        this.password = password;
    }
    
    public String getRealName() {
        return realName;
    }
    
    public void setRealName(String realName) {
        this.realName = realName;
    }
    
    public Integer getGender() {
        return gender;
    }
    
    public void setGender(Integer gender) {
        this.gender = gender;
    }
    
    public String getPhone() {
        return phone;
    }
    
    public void setPhone(String phone) {
        this.phone = phone;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public Integer getDeptId() {
        return deptId;
    }
    
    public void setDeptId(Integer deptId) {
        this.deptId = deptId;
    }
    
    public String getPosition() {
        return position;
    }
    
    public void setPosition(String position) {
        this.position = position;
    }
    
    public Integer getRole() {
        return role;
    }
    
    public void setRole(Integer role) {
        this.role = role;
    }
    
    public String getAvatar() {
        return avatar;
    }
    
    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }
    
    public Date getEntryDate() {
        return entryDate;
    }
    
    public void setEntryDate(Date entryDate) {
        this.entryDate = entryDate;
    }
    
    public Date getCreateTime() {
        return createTime;
    }
    
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
    
    public Date getUpdateTime() {
        return updateTime;
    }
    
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
    
    public Integer getStatus() {
        return status;
    }
    
    public void setStatus(Integer status) {
        this.status = status;
    }
    
    public String getDeptName() {
        return deptName;
    }
    
    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }
    
    /**
     * 获取角色文本
     */
    public String getRoleText() {
        switch (role != null ? role : 1) {
            case 1: return "普通员工";
            case 2: return "部门经理";
            case 3: return "管理员";
            default: return "未知";
        }
    }
    
    /**
     * 获取性别文本
     */
    public String getGenderText() {
        if (gender == null) return "未知";
        return gender == 1 ? "男" : "女";
    }
}
