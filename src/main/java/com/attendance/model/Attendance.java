package com.attendance.model;

import java.util.Date;

/**
 * 考勤记录实体类
 * @author 刘鹏志
 */
public class Attendance {
    
    private Integer id;
    private Integer userId;
    private Date attendDate;
    private Date clockInTime;
    private Date clockOutTime;
    private Integer status;
    private Integer lateMinutes;
    private Integer earlyMinutes;
    private String remark;
    private Date createTime;
    private Date updateTime;
    
    // 非数据库字段
    private String realName;
    private String deptName;
    
    public Attendance() {}
    
    // Getters and Setters
    public Integer getId() {
        return id;
    }
    
    public void setId(Integer id) {
        this.id = id;
    }
    
    public Integer getUserId() {
        return userId;
    }
    
    public void setUserId(Integer userId) {
        this.userId = userId;
    }
    
    public Date getAttendDate() {
        return attendDate;
    }
    
    public void setAttendDate(Date attendDate) {
        this.attendDate = attendDate;
    }
    
    public Date getClockInTime() {
        return clockInTime;
    }
    
    public void setClockInTime(Date clockInTime) {
        this.clockInTime = clockInTime;
    }
    
    public Date getClockOutTime() {
        return clockOutTime;
    }
    
    public void setClockOutTime(Date clockOutTime) {
        this.clockOutTime = clockOutTime;
    }
    
    public Integer getStatus() {
        return status;
    }
    
    public void setStatus(Integer status) {
        this.status = status;
    }
    
    public Integer getLateMinutes() {
        return lateMinutes;
    }
    
    public void setLateMinutes(Integer lateMinutes) {
        this.lateMinutes = lateMinutes;
    }
    
    public Integer getEarlyMinutes() {
        return earlyMinutes;
    }
    
    public void setEarlyMinutes(Integer earlyMinutes) {
        this.earlyMinutes = earlyMinutes;
    }
    
    public String getRemark() {
        return remark;
    }
    
    public void setRemark(String remark) {
        this.remark = remark;
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
    
    public String getRealName() {
        return realName;
    }
    
    public void setRealName(String realName) {
        this.realName = realName;
    }
    
    public String getDeptName() {
        return deptName;
    }
    
    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }
    
    /**
     * 获取状态文本
     */
    public String getStatusText() {
        switch (status != null ? status : 0) {
            case 0: return "未打卡";
            case 1: return "正常";
            case 2: return "迟到";
            case 3: return "早退";
            case 4: return "迟到且早退";
            case 5: return "缺勤";
            default: return "未知";
        }
    }
}
