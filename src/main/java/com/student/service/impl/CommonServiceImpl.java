package com.student.service.impl;

import com.student.dao.CommonDao;
import com.student.service.CommonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service("commonService")
public class CommonServiceImpl implements CommonService {

    @Autowired
    private CommonDao commonDao;

    @Override
    public Map<String, Object> selectCal(Map<String, Object> params) {
        String table = (String) params.get("table");
        String column = (String) params.get("column");
        return commonDao.selectCal(table, column);
    }

    @Override
    public List<Map<String, Object>> selectGroup(Map<String, Object> params) {
        String table = (String) params.get("table");
        String column = (String) params.get("column");
        return commonDao.selectGroup(table, column);
    }

    @Override
    public List<Map<String, Object>> selectValue(Map<String, Object> params) {
        String table = (String) params.get("table");
        String xColumn = (String) params.get("xColumn");
        String yColumn = (String) params.get("yColumn");
        return commonDao.selectValue(table, xColumn, yColumn);
    }
}
