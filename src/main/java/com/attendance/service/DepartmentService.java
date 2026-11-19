package com.attendance.service;

import com.attendance.dao.DepartmentDAO;
import com.attendance.model.Department;
import java.util.List;

/**
 * 部门业务类
 * @author 梁宏博
 */
public class DepartmentService {
    
    private DepartmentDAO deptDAO = new DepartmentDAO();
    
    public List<Department> listAll() {
        return deptDAO.listAll();
    }
    
    public List<Department> listActive() {
        return deptDAO.listActive();
    }
    
    public Department getById(Integer id) {
        return deptDAO.getById(id);
    }
    
    public boolean add(Department dept) {
        return deptDAO.insert(dept);
    }
    
    public boolean update(Department dept) {
        return deptDAO.update(dept);
    }
    
    public boolean delete(Integer id) {
        return deptDAO.delete(id);
    }
}
