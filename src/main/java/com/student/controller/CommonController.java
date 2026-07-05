package com.student.controller;

import com.student.annotation.IgnoreAuth;
import com.student.dao.ScoreDao;
import com.student.service.CommonService;
import com.student.utils.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 通用统计控制器
 */
@RestController
@RequestMapping
public class CommonController {

    @Autowired
    private CommonService commonService;

    @Autowired
    private ScoreDao scoreDao;

    /** 统计聚合：count/sum/avg/max/min */
    @IgnoreAuth
    @RequestMapping("/cal/{tableName}/{columnName}")
    public R cal(@PathVariable String tableName, @PathVariable String columnName) {
        Map<String, Object> params = new HashMap<>();
        params.put("table", tableName);
        params.put("column", columnName);
        Map<String, Object> result = commonService.selectCal(params);
        return R.ok().put("data", result);
    }

    /** 分组统计 */
    @IgnoreAuth
    @RequestMapping("/group/{tableName}/{columnName}")
    public R group(@PathVariable String tableName, @PathVariable String columnName) {
        Map<String, Object> params = new HashMap<>();
        params.put("table", tableName);
        params.put("column", columnName);
        List<Map<String, Object>> result = commonService.selectGroup(params);
        Map<String, Object> data = new HashMap<>();
        for (Map<String, Object> row : result) {
            Object name = row.get("name");
            Object value = row.get("value");
            if (name != null) data.put(name.toString(), value);
        }
        return R.ok().put("data", data);
    }

    /** 按值统计（用于图表） */
    @IgnoreAuth
    @RequestMapping("/value/{tableName}/{xColumnName}/{yColumnName}")
    public R value(@PathVariable String tableName,
                   @PathVariable String xColumnName,
                   @PathVariable String yColumnName) {
        Map<String, Object> params = new HashMap<>();
        params.put("table", tableName);
        params.put("xColumn", xColumnName);
        params.put("yColumn", yColumnName);
        List<Map<String, Object>> result = commonService.selectValue(params);
        Map<String, Object> data = new HashMap<>();
        for (Map<String, Object> row : result) {
            Object name = row.get("name");
            Object value = row.get("value");
            if (name != null) data.put(name.toString(), value);
        }
        return R.ok().put("data", data);
    }

    /** 成绩区间分布 */
    @IgnoreAuth
    @RequestMapping("/scoreDistribution")
    public R scoreDistribution() {
        Map<String, Object> dist = scoreDao.selectScoreDistribution();
        if (dist == null) dist = new HashMap<>();
        Map<String, Object> data = new HashMap<>();
        data.put("range_0_59", dist.getOrDefault("range_0_59", 0));
        data.put("range_60_69", dist.getOrDefault("range_60_69", 0));
        data.put("range_70_79", dist.getOrDefault("range_70_79", 0));
        data.put("range_80_89", dist.getOrDefault("range_80_89", 0));
        data.put("range_90_100", dist.getOrDefault("range_90_100", 0));
        return R.ok().put("data", data);
    }
}
