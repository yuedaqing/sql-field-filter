package com.yue.sqlfilter.service;

import com.yue.sqlfilter.common.BaseResponse;
import com.yue.sqlfilter.model.SqlField;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

/**
 * @author 可乐
 */
public interface SqlFilterService {
    /**
     * 替换SQL语句属性文件
     * @param file sql文件
     * @return 替换后的sql文件
     */
    Map<String,String> insertFilter(MultipartFile file, SqlField sqlField) throws IOException;
}
