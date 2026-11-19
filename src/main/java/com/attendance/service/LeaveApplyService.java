package com.attendance.service;

import com.attendance.dao.LeaveApplyDAO;
import com.attendance.model.LeaveApply;
import java.util.List;

/**
 * 请假申请业务类
 * @author 梁宏博
 */
public class LeaveApplyService {
    
    private LeaveApplyDAO leaveDAO = new LeaveApplyDAO();
    
    public List<LeaveApply> listAll() {
        return leaveDAO.listAll();
    }
    
    public List<LeaveApply> listPending() {
        return leaveDAO.listPending();
    }
    
    public List<LeaveApply> listByUserId(Integer userId) {
        return leaveDAO.listByUserId(userId);
    }
    
    public LeaveApply getById(Integer id) {
        return leaveDAO.getById(id);
    }
    
    public boolean submitApply(LeaveApply leave) {
        return leaveDAO.insert(leave);
    }
    
    public boolean approve(Integer id, Integer approverId, Integer status, String remark) {
        return leaveDAO.approve(id, approverId, status, remark);
    }
    
    public boolean cancel(Integer id, Integer userId) {
        return leaveDAO.cancel(id, userId);
    }
}
