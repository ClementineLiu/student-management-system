package com.student.controller;

import com.student.entity.CourseEntity;
import com.student.service.CourseService;
import com.student.utils.PageUtils;
import com.student.utils.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Map;

/**
 * 课程管理控制器
 */
@RestController
@RequestMapping("course")
public class CourseController {

    @Autowired
    private CourseService courseService;

    /** 分页列表 */
    @RequestMapping("/page")
    public R page(@RequestParam Map<String, Object> params) {
        PageUtils page = courseService.queryPage(params);
        return R.ok().put("data", page);
    }

    /** 列表（全部，用于下拉选择） */
    @RequestMapping("/list")
    public R list() {
        return R.ok().put("data", courseService.list());
    }

    /** 详情 */
    @RequestMapping("/info/{id}")
    public R info(@PathVariable Long id) {
        return R.ok().put("data", courseService.getById(id));
    }

    /** 新增 */
    @PostMapping("/save")
    public R save(@RequestBody CourseEntity entity) {
        courseService.save(entity);
        return R.ok();
    }

    /** 修改 */
    @RequestMapping("/update")
    public R update(@RequestBody CourseEntity entity) {
        courseService.updateById(entity);
        return R.ok();
    }

    /** 批量删除 */
    @RequestMapping("/delete")
    public R delete(@RequestBody Long[] ids) {
        courseService.removeByIds(Arrays.asList(ids));
        return R.ok();
    }
}
