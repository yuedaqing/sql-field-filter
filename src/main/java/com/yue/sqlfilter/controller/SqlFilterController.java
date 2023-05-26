package com.yue.sqlfilter.controller;

import cn.hutool.core.io.IoUtil;
import cn.hutool.core.map.MapUtil;
import com.yue.sqlfilter.common.ErrorCode;
import com.yue.sqlfilter.exception.ThrowUtils;
import com.yue.sqlfilter.model.SqlField;
import com.yue.sqlfilter.service.SqlFilterService;
import com.yue.sqlfilter.utils.SqlFieldUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;

/**
 * @author 可乐
 */
@RestController
@RequestMapping("/sql/filter")
@Slf4j
public class SqlFilterController {
    @Resource
    private SqlFilterService sqlFilterService;

    @PostMapping("/insert")
    public void getNewInsertSql(@RequestPart("file") MultipartFile multipartFile, SqlField sqlField, HttpServletResponse response){
        ThrowUtils.throwIf(sqlFilterService.validationField(sqlField), ErrorCode.PARAMS_ERROR);
        Map<String, String> map = sqlFilterService.insertFilter(multipartFile, sqlField);
        String ids = map.get("ids");
        String idNum = map.get("idNum");
        String sqlContent = map.get("SQLContent");
        byte[] idContent = ("主键主数量："+idNum + "\n" +"主键内容：\n"+ ids+"\n").getBytes(StandardCharsets.UTF_8);
        try {
            response.setHeader("Content-Disposition","attachment;filename="+ URLEncoder.encode("sql.text", "utf-8"));
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8");
            ServletOutputStream outputStream = response.getOutputStream();
            outputStream.write(idContent);
            outputStream.write(("\ninsertSQL语句：\n"+sqlContent).getBytes(StandardCharsets.UTF_8));
            outputStream.flush();
            outputStream.close();
            IoUtil.close(outputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
