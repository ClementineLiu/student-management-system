package com.student.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * 班级实体
 */
@TableName("class_info")
public class ClassInfoEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    private Long id;
    private String className;
    private String department;
    private String grade;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date addtime;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getClassName() { return className; }
    public void setClassName(String className) { this.className = className; }
    public String getDepartment() { return department; }
    public void setDepartment(String department) { this.department = department; }
    public String getGrade() { return grade; }
    public void setGrade(String grade) { this.grade = grade; }
    public Date getAddtime() { return addtime; }
    public void setAddtime(Date addtime) { this.addtime = addtime; }
}
