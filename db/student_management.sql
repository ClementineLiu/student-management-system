-- ============================================
-- 学生管理系统 - 数据库初始化脚本
-- ============================================

CREATE DATABASE IF NOT EXISTS `student_management` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE `student_management`;

-- ============================================
-- 1. 管理员表
-- ============================================
DROP TABLE IF EXISTS `users`;
CREATE TABLE `users` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键',
    `username` VARCHAR(50) NOT NULL COMMENT '用户名',
    `password` VARCHAR(100) NOT NULL COMMENT '密码',
    `role` VARCHAR(20) DEFAULT '管理员' COMMENT '角色',
    `addtime` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_username` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='管理员表';

-- 默认管理员: admin / admin123
INSERT INTO `users` (`username`, `password`, `role`) VALUES ('admin', 'admin123', '管理员');

-- ============================================
-- 2. 令牌表
-- ============================================
DROP TABLE IF EXISTS `token`;
CREATE TABLE `token` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键',
    `userid` BIGINT NOT NULL COMMENT '用户ID',
    `username` VARCHAR(50) NOT NULL COMMENT '用户名',
    `tablename` VARCHAR(50) DEFAULT 'users' COMMENT '所属表',
    `role` VARCHAR(20) DEFAULT NULL COMMENT '角色',
    `token` VARCHAR(64) NOT NULL COMMENT '令牌',
    `expiratedtime` DATETIME DEFAULT NULL COMMENT '过期时间',
    `addtime` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_token` (`token`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='令牌表';

-- ============================================
-- 3. 班级表
-- ============================================
DROP TABLE IF EXISTS `class_info`;
CREATE TABLE `class_info` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键',
    `class_name` VARCHAR(100) NOT NULL COMMENT '班级名称',
    `department` VARCHAR(100) DEFAULT NULL COMMENT '所属专业/院系',
    `grade` VARCHAR(20) DEFAULT NULL COMMENT '年级（如2024级）',
    `addtime` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='班级表';

-- ============================================
-- 4. 学生表
-- ============================================
DROP TABLE IF EXISTS `student`;
CREATE TABLE `student` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键',
    `student_id` VARCHAR(20) NOT NULL COMMENT '学号',
    `name` VARCHAR(50) NOT NULL COMMENT '姓名',
    `gender` VARCHAR(10) DEFAULT NULL COMMENT '性别（男/女）',
    `age` INT DEFAULT NULL COMMENT '年龄',
    `phone` VARCHAR(20) DEFAULT NULL COMMENT '手机号',
    `email` VARCHAR(100) DEFAULT NULL COMMENT '邮箱',
    `class_id` BIGINT DEFAULT NULL COMMENT '所属班级ID',
    `address` VARCHAR(200) DEFAULT NULL COMMENT '家庭住址',
    `enrollment_date` DATE DEFAULT NULL COMMENT '入学日期',
    `addtime` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_student_id` (`student_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='学生表';

-- ============================================
-- 5. 课程表
-- ============================================
DROP TABLE IF EXISTS `course`;
CREATE TABLE `course` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键',
    `course_name` VARCHAR(100) NOT NULL COMMENT '课程名称',
    `credit` DECIMAL(3,1) DEFAULT 1.0 COMMENT '学分',
    `teacher` VARCHAR(50) DEFAULT NULL COMMENT '任课教师',
    `description` VARCHAR(500) DEFAULT NULL COMMENT '课程描述',
    `addtime` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='课程表';

-- ============================================
-- 6. 成绩表
-- ============================================
DROP TABLE IF EXISTS `score`;
CREATE TABLE `score` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键',
    `student_id` BIGINT NOT NULL COMMENT '学生ID（关联student.id）',
    `course_id` BIGINT NOT NULL COMMENT '课程ID（关联course.id）',
    `score` DECIMAL(5,1) DEFAULT NULL COMMENT '成绩（0-100）',
    `semester` VARCHAR(20) DEFAULT NULL COMMENT '学期（如2025-2026-1）',
    `exam_type` VARCHAR(20) DEFAULT '期末考试' COMMENT '考试类型（期末考试/期中考试/补考）',
    `addtime` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_student_course_semester_type` (`student_id`, `course_id`, `semester`, `exam_type`),
    KEY `idx_student_id` (`student_id`),
    KEY `idx_course_id` (`course_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='成绩表';
