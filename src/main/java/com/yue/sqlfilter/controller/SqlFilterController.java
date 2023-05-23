package com.yue.sqlfilter.controller;

import com.yue.sqlfilter.common.BaseResponse;
import com.yue.sqlfilter.common.ResultUtils;
import com.yue.sqlfilter.service.SqlFilter;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

/**
 * @author 可乐
 */
@RestController
@RequestMapping("/sql/filter")
public class SqlFilterController {
    @Resource
    private SqlFilter sqlFilter;

    @PostMapping("/insert")
    public BaseResponse<?> getNewInsertSql(@RequestPart MultipartFile file, HttpServletResponse response){
        return ResultUtils.success("");
    }
}
