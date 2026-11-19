package com.attendance.model;

import java.util.Date;

/**
 * 公告实体类
 * @author 刘鹏志
 */
public class Announcement {
    
    private Integer id;
    private String title;
    private String content;
    private Integer publisherId;
    private Integer type;
    private Integer isTop;
    private Date createTime;
    private Date updateTime;
    private Integer status;
    
    // 非数据库字段
    private String publisherName;
    
    public Announcement() {}
    
    // Getters and Setters
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
    
    public Integer getPublisherId() { return publisherId; }
    public void setPublisherId(Integer publisherId) { this.publisherId = publisherId; }
    
    public Integer getType() { return type; }
    public void setType(Integer type) { this.type = type; }
    
    public Integer getIsTop() { return isTop; }
    public void setIsTop(Integer isTop) { this.isTop = isTop; }
    
    public Date getCreateTime() { return createTime; }
    public void setCreateTime(Date createTime) { this.createTime = createTime; }
    
    public Date getUpdateTime() { return updateTime; }
    public void setUpdateTime(Date updateTime) { this.updateTime = updateTime; }
    
    public Integer getStatus() { return status; }
    public void setStatus(Integer status) { this.status = status; }
    
    public String getPublisherName() { return publisherName; }
    public void setPublisherName(String publisherName) { this.publisherName = publisherName; }
    
    /**
     * 获取类型文本
     */
    public String getTypeText() {
        switch (type != null ? type : 1) {
            case 1: return "通知";
            case 2: return "公告";
            case 3: return "制度";
            default: return "其他";
        }
    }
}
