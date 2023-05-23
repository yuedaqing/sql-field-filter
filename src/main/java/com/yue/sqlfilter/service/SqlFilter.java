package com.yue.sqlfilter.service;

import com.yue.sqlfilter.common.BaseResponse;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author 可乐
 */
public interface SqlFilter {
    /**
     * 替换SQL语句属性文件
     * @param file sql文件
     * @return 替换后的sql文件
     */
    BaseResponse<?> insertFilter(MultipartFile file,String fields1,String fields2);
}
