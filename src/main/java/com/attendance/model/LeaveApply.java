package com.attendance.model;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 请假申请实体类
 * @author 刘鹏志
 */
public class LeaveApply {
    
    private Integer id;
    private Integer userId;
    private Integer leaveType;
    private Date startTime;
    private Date endTime;
    private BigDecimal days;
    private String reason;
    private Integer status;
    private Integer approverId;
    private Date approveTime;
    private String approveRemark;
    private Date createTime;
    private Date updateTime;
    
    // 非数据库字段
    private String realName;
    private String deptName;
    private String approverName;
    
    public LeaveApply() {}
    
    // Getters and Setters
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    
    public Integer getUserId() { return userId; }
    public void setUserId(Integer userId) { this.userId = userId; }
    
    public Integer getLeaveType() { return leaveType; }
    public void setLeaveType(Integer leaveType) { this.leaveType = leaveType; }
    
    public Date getStartTime() { return startTime; }
    public void setStartTime(Date startTime) { this.startTime = startTime; }
    
    public Date getEndTime() { return endTime; }
    public void setEndTime(Date endTime) { this.endTime = endTime; }
    
    public BigDecimal getDays() { return days; }
    public void setDays(BigDecimal days) { this.days = days; }
    
    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }
    
    public Integer getStatus() { return status; }
    public void setStatus(Integer status) { this.status = status; }
    
    public Integer getApproverId() { return approverId; }
    public void setApproverId(Integer approverId) { this.approverId = approverId; }
    
    public Date getApproveTime() { return approveTime; }
    public void setApproveTime(Date approveTime) { this.approveTime = approveTime; }
    
    public String getApproveRemark() { return approveRemark; }
    public void setApproveRemark(String approveRemark) { this.approveRemark = approveRemark; }
    
    public Date getCreateTime() { return createTime; }
    public void setCreateTime(Date createTime) { this.createTime = createTime; }
    
    public Date getUpdateTime() { return updateTime; }
    public void setUpdateTime(Date updateTime) { this.updateTime = updateTime; }
    
    public String getRealName() { return realName; }
    public void setRealName(String realName) { this.realName = realName; }
    
    public String getDeptName() { return deptName; }
    public void setDeptName(String deptName) { this.deptName = deptName; }
    
    public String getApproverName() { return approverName; }
    public void setApproverName(String approverName) { this.approverName = approverName; }
    
    /**
     * 获取请假类型文本
     */
    public String getLeaveTypeText() {
        switch (leaveType != null ? leaveType : 1) {
            case 1: return "事假";
            case 2: return "病假";
            case 3: return "年假";
            case 4: return "婚假";
            case 5: return "产假";
            case 6: return "其他";
            default: return "未知";
        }
    }
    
    /**
     * 获取状态文本
     */
    public String getStatusText() {
        switch (status != null ? status : 0) {
            case 0: return "待审批";
            case 1: return "已通过";
            case 2: return "已驳回";
            case 3: return "已撤销";
            default: return "未知";
        }
    }
}
