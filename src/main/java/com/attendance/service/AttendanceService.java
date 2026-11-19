package com.attendance.service;

import com.attendance.dao.AttendanceDAO;
import com.attendance.model.Attendance;
import com.attendance.util.DateUtil;
import java.util.Date;
import java.util.List;

/**
 * 考勤业务类
 * @author 梁宏博
 */
public class AttendanceService {
    
    private AttendanceDAO attendanceDAO = new AttendanceDAO();
    
    /**
     * 上班打卡
     */
    public Attendance clockIn(Integer userId) {
        Date now = new Date();
        Date today = DateUtil.parseDate(DateUtil.formatDate(now));
        
        // 检查今日是否已打卡
        Attendance exist = attendanceDAO.getTodayAttendance(userId, today);
        if (exist != null && exist.getClockInTime() != null) {
            return exist; // 已打过上班卡
        }
        
        int lateMinutes = DateUtil.getLateMinutes(now);
        int status = lateMinutes > 0 ? 2 : 1;
        
        Attendance attendance = new Attendance();
        attendance.setUserId(userId);
        attendance.setAttendDate(today);
        attendance.setClockInTime(now);
        attendance.setStatus(status);
        attendance.setLateMinutes(lateMinutes);
        
        if (exist != null) {
            attendance.setId(exist.getId());
            attendanceDAO.update(attendance);
        } else {
            attendanceDAO.clockIn(attendance);
        }
        
        return attendance;
    }
    
    /**
     * 下班打卡
     */
    public Attendance clockOut(Integer userId) {
        Date now = new Date();
        Date today = DateUtil.parseDate(DateUtil.formatDate(now));
        
        Attendance attendance = attendanceDAO.getTodayAttendance(userId, today);
        if (attendance == null || attendance.getClockInTime() == null) {
            return null; // 未打上班卡
        }
        
        int earlyMinutes = DateUtil.getEarlyMinutes(now);
        attendance.setClockOutTime(now);
        attendance.setEarlyMinutes(earlyMinutes);
        
        // 更新状态
        if (attendance.getStatus() == 2 && earlyMinutes > 0) {
            attendance.setStatus(4); // 迟到且早退
        } else if (earlyMinutes > 0) {
            attendance.setStatus(3); // 早退
        } else if (attendance.getStatus() != 2) {
            attendance.setStatus(1); // 正常
        }
        
        attendanceDAO.update(attendance);
        return attendance;
    }
    
    /**
     * 获取今日考勤
     */
    public Attendance getToday(Integer userId) {
        Date today = DateUtil.parseDate(DateUtil.formatDate(new Date()));
        return attendanceDAO.getTodayAttendance(userId, today);
    }
    
    /**
     * 查询所有
     */
    public List<Attendance> listAll() {
        return attendanceDAO.listAll();
    }
    
    /**
     * 按日期范围查询
     */
    public List<Attendance> listByDateRange(Date startDate, Date endDate) {
        return attendanceDAO.listByDateRange(startDate, endDate);
    }
    
    /**
     * 部门考勤统计
     */
    public List<java.util.Map<String, Object>> getDeptStats(Date startDate, Date endDate) {
        return attendanceDAO.getDeptStats(startDate, endDate);
    }
}
