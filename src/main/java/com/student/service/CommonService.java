package com.student.service;

import java.util.List;
import java.util.Map;

public interface CommonService {

    /** 统计聚合 */
    Map<String, Object> selectCal(Map<String, Object> params);

    /** 分组统计 */
    List<Map<String, Object>> selectGroup(Map<String, Object> params);

    /** 按值统计 */
    List<Map<String, Object>> selectValue(Map<String, Object> params);
}
