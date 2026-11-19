package com.attendance.service;

import com.attendance.dao.AnnouncementDAO;
import com.attendance.model.Announcement;
import java.util.List;

/**
 * 公告业务类
 * @author 梁宏博
 */
public class AnnouncementService {
    
    private AnnouncementDAO annDAO = new AnnouncementDAO();
    
    public List<Announcement> listPublished() {
        return annDAO.listPublished();
    }
    
    public Announcement getById(Integer id) {
        return annDAO.getById(id);
    }
    
    public boolean publish(Announcement ann) {
        return annDAO.insert(ann);
    }
    
    public boolean update(Announcement ann) {
        return annDAO.update(ann);
    }
    
    public boolean delete(Integer id) {
        return annDAO.delete(id);
    }
}
