package com.attendance.service;

import com.attendance.dao.SysLogDAO;
import com.attendance.model.SysLog;
import java.util.List;

/**
 * 系统日志业务类
 * @author 梁宏博
 */
public class SysLogService {
    
    private SysLogDAO logDAO = new SysLogDAO();
    
    public List<SysLog> listByPage(int page, int pageSize) {
        return logDAO.listByPage(page, pageSize);
    }
    
    public int count() {
        return logDAO.count();
    }
    
    public boolean addLog(SysLog log) {
        return logDAO.insert(log);
    }
}
