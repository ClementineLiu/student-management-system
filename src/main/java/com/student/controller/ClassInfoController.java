package com.student.controller;

import com.student.entity.ClassInfoEntity;
import com.student.service.ClassInfoService;
import com.student.utils.PageUtils;
import com.student.utils.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Map;

/**
 * 班级管理控制器
 */
@RestController
@RequestMapping("classinfo")
public class ClassInfoController {

    @Autowired
    private ClassInfoService classInfoService;

    /** 分页列表 */
    @RequestMapping("/page")
    public R page(@RequestParam Map<String, Object> params) {
        PageUtils page = classInfoService.queryPage(params);
        return R.ok().put("data", page);
    }

    /** 列表（全部，用于下拉选择） */
    @RequestMapping("/list")
    public R list() {
        return R.ok().put("data", classInfoService.list());
    }

    /** 详情 */
    @RequestMapping("/info/{id}")
    public R info(@PathVariable Long id) {
        return R.ok().put("data", classInfoService.getById(id));
    }

    /** 新增 */
    @PostMapping("/save")
    public R save(@RequestBody ClassInfoEntity entity) {
        classInfoService.save(entity);
        return R.ok();
    }

    /** 修改 */
    @RequestMapping("/update")
    public R update(@RequestBody ClassInfoEntity entity) {
        classInfoService.updateById(entity);
        return R.ok();
    }

    /** 批量删除 */
    @RequestMapping("/delete")
    public R delete(@RequestBody Long[] ids) {
        classInfoService.removeByIds(Arrays.asList(ids));
        return R.ok();
    }
}
