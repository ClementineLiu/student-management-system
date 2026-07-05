package com.student.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.student.entity.ScoreEntity;
import com.student.service.ScoreService;
import com.student.utils.PageUtils;
import com.student.utils.R;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Map;

/**
 * 成绩管理控制器
 */
@RestController
@RequestMapping("score")
public class ScoreController {

    @Autowired
    private ScoreService scoreService;

    /** 分页列表（关联查询学生名、课程名） */
    @RequestMapping("/page")
    public R page(@RequestParam Map<String, Object> params) {
        QueryWrapper<ScoreEntity> qw = new QueryWrapper<>();

        // 按学生ID筛选
        String studentId = (String) params.get("studentId");
        if (StringUtils.isNotBlank(studentId)) {
            qw.eq("sc.student_id", studentId);
        }
        // 按课程ID筛选
        String courseId = (String) params.get("courseId");
        if (StringUtils.isNotBlank(courseId)) {
            qw.eq("sc.course_id", courseId);
        }
        // 按学期筛选
        String semester = (String) params.get("semester");
        if (StringUtils.isNotBlank(semester)) {
            qw.eq("sc.semester", semester);
        }

        qw.orderByDesc("sc.addtime");

        PageUtils page = scoreService.queryPage(params, qw);
        return R.ok().put("data", page);
    }

    /** 详情 */
    @RequestMapping("/info/{id}")
    public R info(@PathVariable Long id) {
        return R.ok().put("data", scoreService.getById(id));
    }

    /** 新增 */
    @PostMapping("/save")
    public R save(@RequestBody ScoreEntity entity) {
        scoreService.save(entity);
        return R.ok();
    }

    /** 修改 */
    @RequestMapping("/update")
    public R update(@RequestBody ScoreEntity entity) {
        scoreService.updateById(entity);
        return R.ok();
    }

    /** 批量删除 */
    @RequestMapping("/delete")
    public R delete(@RequestBody Long[] ids) {
        scoreService.removeByIds(Arrays.asList(ids));
        return R.ok();
    }
}
