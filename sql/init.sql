

DROP DATABASE IF EXISTS attendance_system;
CREATE DATABASE attendance_system DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE attendance_system;

-- ============================================
-- 1. 部门表 (department)
-- ============================================
DROP TABLE IF EXISTS `department`;
CREATE TABLE `department` (
  `id` INT NOT NULL AUTO_INCREMENT COMMENT '部门ID',
  `dept_name` VARCHAR(50) NOT NULL COMMENT '部门名称',
  `dept_code` VARCHAR(20) NOT NULL COMMENT '部门编号',
  `manager` VARCHAR(30) DEFAULT NULL COMMENT '部门负责人',
  `phone` VARCHAR(20) DEFAULT NULL COMMENT '联系电话',
  `description` VARCHAR(200) DEFAULT NULL COMMENT '部门描述',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `status` TINYINT DEFAULT 1 COMMENT '状态：1-启用 0-禁用',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_dept_code` (`dept_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='部门表';

-- ============================================
-- 2. 用户表 (user)
-- ============================================
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `id` INT NOT NULL AUTO_INCREMENT COMMENT '用户ID',
  `username` VARCHAR(50) NOT NULL COMMENT '用户名',
  `password` VARCHAR(100) NOT NULL COMMENT '密码(MD5加密)',
  `real_name` VARCHAR(30) NOT NULL COMMENT '真实姓名',
  `gender` TINYINT DEFAULT 1 COMMENT '性别：1-男 2-女',
  `phone` VARCHAR(20) DEFAULT NULL COMMENT '手机号',
  `email` VARCHAR(50) DEFAULT NULL COMMENT '邮箱',
  `dept_id` INT DEFAULT NULL COMMENT '所属部门ID',
  `position` VARCHAR(50) DEFAULT NULL COMMENT '职位',
  `role` TINYINT DEFAULT 1 COMMENT '角色：1-普通员工 2-部门经理 3-管理员',
  `avatar` VARCHAR(200) DEFAULT NULL COMMENT '头像路径',
  `entry_date` DATE DEFAULT NULL COMMENT '入职日期',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `status` TINYINT DEFAULT 1 COMMENT '状态：1-启用 0-禁用',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_username` (`username`),
  KEY `idx_dept_id` (`dept_id`),
  CONSTRAINT `fk_user_dept` FOREIGN KEY (`dept_id`) REFERENCES `department`(`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户表';

-- ============================================
-- 3. 考勤记录表 (attendance)
-- ============================================
DROP TABLE IF EXISTS `attendance`;
CREATE TABLE `attendance` (
  `id` INT NOT NULL AUTO_INCREMENT COMMENT '考勤ID',
  `user_id` INT NOT NULL COMMENT '用户ID',
  `attend_date` DATE NOT NULL COMMENT '考勤日期',
  `clock_in_time` DATETIME DEFAULT NULL COMMENT '上班打卡时间',
  `clock_out_time` DATETIME DEFAULT NULL COMMENT '下班打卡时间',
  `status` TINYINT DEFAULT 0 COMMENT '状态：0-未打卡 1-正常 2-迟到 3-早退 4-迟到且早退 5-缺勤',
  `late_minutes` INT DEFAULT 0 COMMENT '迟到分钟数',
  `early_minutes` INT DEFAULT 0 COMMENT '早退分钟数',
  `remark` VARCHAR(200) DEFAULT NULL COMMENT '备注',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_user_date` (`user_id`, `attend_date`),
  KEY `idx_attend_date` (`attend_date`),
  CONSTRAINT `fk_attend_user` FOREIGN KEY (`user_id`) REFERENCES `user`(`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='考勤记录表';

-- ============================================
-- 4. 请假申请表 (leave_apply)
-- ============================================
DROP TABLE IF EXISTS `leave_apply`;
CREATE TABLE `leave_apply` (
  `id` INT NOT NULL AUTO_INCREMENT COMMENT '请假ID',
  `user_id` INT NOT NULL COMMENT '申请人ID',
  `leave_type` TINYINT NOT NULL COMMENT '请假类型：1-事假 2-病假 3-年假 4-婚假 5-产假 6-其他',
  `start_time` DATETIME NOT NULL COMMENT '开始时间',
  `end_time` DATETIME NOT NULL COMMENT '结束时间',
  `days` DECIMAL(5,1) NOT NULL COMMENT '请假天数',
  `reason` VARCHAR(500) NOT NULL COMMENT '请假原因',
  `status` TINYINT DEFAULT 0 COMMENT '状态：0-待审批 1-已通过 2-已驳回 3-已撤销',
  `approver_id` INT DEFAULT NULL COMMENT '审批人ID',
  `approve_time` DATETIME DEFAULT NULL COMMENT '审批时间',
  `approve_remark` VARCHAR(200) DEFAULT NULL COMMENT '审批备注',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_status` (`status`),
  CONSTRAINT `fk_leave_user` FOREIGN KEY (`user_id`) REFERENCES `user`(`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='请假申请表';

-- ============================================
-- 5. 公告表 (announcement)
-- ============================================
DROP TABLE IF EXISTS `announcement`;
CREATE TABLE `announcement` (
  `id` INT NOT NULL AUTO_INCREMENT COMMENT '公告ID',
  `title` VARCHAR(100) NOT NULL COMMENT '公告标题',
  `content` TEXT NOT NULL COMMENT '公告内容',
  `publisher_id` INT NOT NULL COMMENT '发布人ID',
  `type` TINYINT DEFAULT 1 COMMENT '类型：1-通知 2-公告 3-制度',
  `is_top` TINYINT DEFAULT 0 COMMENT '是否置顶：0-否 1-是',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `status` TINYINT DEFAULT 1 COMMENT '状态：1-发布 0-草稿',
  PRIMARY KEY (`id`),
  KEY `idx_publisher` (`publisher_id`),
  CONSTRAINT `fk_announce_user` FOREIGN KEY (`publisher_id`) REFERENCES `user`(`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='公告表';

-- ============================================
-- 6. 系统日志表 (sys_log)
-- ============================================
DROP TABLE IF EXISTS `sys_log`;
CREATE TABLE `sys_log` (
  `id` INT NOT NULL AUTO_INCREMENT COMMENT '日志ID',
  `user_id` INT DEFAULT NULL COMMENT '操作用户ID',
  `username` VARCHAR(50) DEFAULT NULL COMMENT '用户名',
  `operation` VARCHAR(100) NOT NULL COMMENT '操作描述',
  `method` VARCHAR(200) DEFAULT NULL COMMENT '请求方法',
  `params` TEXT DEFAULT NULL COMMENT '请求参数',
  `ip` VARCHAR(50) DEFAULT NULL COMMENT 'IP地址',
  `time` BIGINT DEFAULT NULL COMMENT '执行时长(毫秒)',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `idx_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='系统日志表';

-- ============================================
-- 初始数据插入
-- 团队成员：汪轩增（队长）、梁宏博、贾兮雯、周童、刘鹏志
-- ============================================

-- 插入部门数据
INSERT INTO `department` (`dept_name`, `dept_code`, `manager`, `phone`, `description`) VALUES
('技术研发部', 'TECH001', '汪轩增', '13800000001', '负责公司核心产品研发与技术创新'),
('市场营销部', 'MKT001', '梁宏博', '13800000002', '负责市场推广、品牌宣传及客户拓展'),
('人力资源部', 'HR001', '贾兮雯', '13800000003', '负责员工招聘、培训、绩效考核及薪酬管理'),
('财务管理部', 'FIN001', '周童', '13800000004', '负责公司财务核算、资金管理及审计监督'),
('行政后勤部', 'ADM001', '刘鹏志', '13800000005', '负责公司行政事务、后勤保障及资产管理');

-- 插入用户数据（密码均为 123456 的MD5值：e10adc3949ba59abbe56e057f20f883e）
INSERT INTO `user` (`username`, `password`, `real_name`, `gender`, `phone`, `email`, `dept_id`, `position`, `role`, `entry_date`) VALUES
('admin', 'e10adc3949ba59abbe56e057f20f883e', '系统管理员', 1, '13900000001', 'admin@company.com', 1, '系统管理员', 3, '2020-01-01'),
('wangxz', 'e10adc3949ba59abbe56e057f20f883e', '汪轩增', 1, '13900000002', 'wangxz@company.com', 1, '技术总监/队长', 2, '2021-03-15'),
('lianghb', 'e10adc3949ba59abbe56e057f20f883e', '梁宏博', 1, '13900000003', 'lianghb@company.com', 2, '市场经理', 2, '2021-06-20'),
('jiaxw', 'e10adc3949ba59abbe56e057f20f883e', '贾兮雯', 2, '13900000004', 'jiaxw@company.com', 3, '人事主管', 2, '2021-09-01'),
('zhout', 'e10adc3949ba59abbe56e057f20f883e', '周童', 1, '13900000005', 'zhout@company.com', 4, '财务主管', 2, '2022-03-08'),
('liupz', 'e10adc3949ba59abbe56e057f20f883e', '刘鹏志', 1, '13900000006', 'liupz@company.com', 5, '行政主管', 2, '2022-05-15'),
('zhangsan', 'e10adc3949ba59abbe56e057f20f883e', '张三', 1, '13900000007', 'zhangsan@company.com', 1, '高级开发工程师', 1, '2022-08-20'),
('lisi', 'e10adc3949ba59abbe56e057f20f883e', '李四', 2, '13900000008', 'lisi@company.com', 2, '市场专员', 1, '2023-01-15');

-- 插入考勤记录示例数据
INSERT INTO `attendance` (`user_id`, `attend_date`, `clock_in_time`, `clock_out_time`, `status`, `late_minutes`, `early_minutes`) VALUES
(2, '2026-04-21', '2026-04-21 08:55:00', '2026-04-21 18:05:00', 1, 0, 0),
(2, '2026-04-22', '2026-04-22 09:10:00', '2026-04-22 17:50:00', 4, 10, 10),
(3, '2026-04-21', '2026-04-21 08:50:00', '2026-04-21 18:10:00', 1, 0, 0),
(3, '2026-04-22', '2026-04-22 08:45:00', '2026-04-22 18:00:00', 1, 0, 0),
(4, '2026-04-21', '2026-04-21 09:05:00', '2026-04-21 18:00:00', 2, 5, 0),
(4, '2026-04-22', '2026-04-22 08:30:00', '2026-04-22 17:30:00', 3, 0, 30),
(5, '2026-04-21', '2026-04-21 08:40:00', '2026-04-21 18:20:00', 1, 0, 0),
(5, '2026-04-22', '2026-04-22 08:55:00', '2026-04-22 18:05:00', 1, 0, 0),
(6, '2026-04-21', '2026-04-21 09:20:00', '2026-04-21 17:40:00', 4, 20, 20),
(6, '2026-04-22', '2026-04-22 08:50:00', '2026-04-22 18:00:00', 1, 0, 0);

-- 插入请假申请示例数据
INSERT INTO `leave_apply` (`user_id`, `leave_type`, `start_time`, `end_time`, `days`, `reason`, `status`, `approver_id`, `approve_time`) VALUES
(4, 2, '2026-04-23 09:00:00', '2026-04-23 18:00:00', 1.0, '身体不适，需要去医院检查', 1, 2, '2026-04-22 15:30:00'),
(6, 1, '2026-04-24 09:00:00', '2026-04-25 18:00:00', 2.0, '家中有事需要处理', 0, NULL, NULL),
(8, 3, '2026-04-28 09:00:00', '2026-04-30 18:00:00', 3.0, '年假休息', 1, 2, '2026-04-22 16:00:00');

-- 插入公告示例数据
INSERT INTO `announcement` (`title`, `content`, `publisher_id`, `type`, `is_top`) VALUES
('关于启用新版考勤系统的通知', '各位同事：\n\n为进一步规范公司考勤管理，提升管理效率，经公司研究决定，自2026年5月1日起正式启用新版职工考勤管理系统。现将有关事项通知如下：\n\n一、新系统将支持在线打卡、请假申请、考勤统计等功能；\n二、请各位同事及时登录系统，熟悉操作流程；\n三、如有疑问，请联系人力资源部。\n\n特此通知。', 1, 2, 1),
('五一劳动节放假安排', '根据国家法定节假日安排，结合公司实际情况，现将2026年五一劳动节放假安排通知如下：\n\n一、放假时间：5月1日至5月5日，共5天；\n二、4月26日（周日）、5月9日（周六）正常上班；\n三、请各部门提前做好工作安排。\n\n祝大家节日快乐！', 1, 1, 0),
('员工考勤管理补充规定', '为加强公司劳动纪律管理，维护正常工作秩序，特制定本补充规定：\n\n一、工作时间为上午9:00-12:00，下午13:30-18:00；\n二、迟到30分钟以内扣款20元/次，超过30分钟按旷工半天处理；\n三、早退参照迟到标准执行；\n四、每月允许3次弹性打卡（10分钟内）。\n\n本规定自发布之日起执行。', 1, 3, 0);

SELECT '数据库初始化完成！' AS result;
