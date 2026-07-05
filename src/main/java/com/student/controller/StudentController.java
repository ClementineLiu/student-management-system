package com.student.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.student.entity.StudentEntity;
import com.student.service.StudentService;
import com.student.utils.PageUtils;
import com.student.utils.R;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Map;

/**
 * 学生管理控制器
 */
@RestController
@RequestMapping("student")
public class StudentController {

    @Autowired
    private StudentService studentService;

    /** 分页列表（含搜索） */
    @RequestMapping("/page")
    public R page(@RequestParam Map<String, Object> params) {
        QueryWrapper<StudentEntity> qw = new QueryWrapper<>();

        // 学号搜索
        String studentId = (String) params.get("studentId");
        if (StringUtils.isNotBlank(studentId)) {
            qw.like("s.student_id", studentId);
        }
        // 姓名搜索
        String name = (String) params.get("name");
        if (StringUtils.isNotBlank(name)) {
            qw.like("s.name", name);
        }
        // 班级筛选
        String classId = (String) params.get("classId");
        if (StringUtils.isNotBlank(classId)) {
            qw.eq("s.class_id", classId);
        }
        // 性别筛选
        String gender = (String) params.get("gender");
        if (StringUtils.isNotBlank(gender)) {
            qw.eq("s.gender", gender);
        }

        qw.orderByDesc("s.addtime");

        PageUtils page = studentService.queryPage(params, qw);
        return R.ok().put("data", page);
    }

    /** 详情 */
    @RequestMapping("/info/{id}")
    public R info(@PathVariable Long id) {
        return R.ok().put("data", studentService.getById(id));
    }

    /** 新增 */
    @PostMapping("/save")
    public R save(@RequestBody StudentEntity entity) {
        // 学号唯一性校验
        QueryWrapper<StudentEntity> qw = new QueryWrapper<>();
        qw.eq("student_id", entity.getStudentId());
        if (studentService.count(qw) > 0) {
            return R.error("学号已存在");
        }
        studentService.save(entity);
        return R.ok();
    }

    /** 修改 */
    @RequestMapping("/update")
    public R update(@RequestBody StudentEntity student) {
        // 学号唯一性校验（排除自身）
        QueryWrapper<StudentEntity> qw = new QueryWrapper<>();
        qw.eq("student_id", student.getStudentId()).ne("id", student.getId());
        if (studentService.count(qw) > 0) {
            return R.error("学号已被其他学生使用");
        }
        studentService.updateById(student);
        return R.ok();
    }

    /** 批量删除 */
    @RequestMapping("/delete")
    public R delete(@RequestBody Long[] ids) {
        studentService.removeByIds(Arrays.asList(ids));
        return R.ok();
    }
}
