package com.yue.sqlfilter.controller;

import cn.hutool.core.io.IoUtil;
import com.yue.sqlfilter.common.ErrorCode;
import com.yue.sqlfilter.exception.ThrowUtils;
import com.yue.sqlfilter.model.SqlField;
import com.yue.sqlfilter.service.SqlFilterService;
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
        Map<String, String> map = null;
        try {
            map = sqlFilterService.insertFilter(multipartFile, sqlField);
        } catch (IOException e) {
            log.info("文件异常:{}",e.getMessage());
        }
        String ids = map.get("ids");
        String idNum = map.get("idNum");
        String sqlContent = map.get("SQLContent");
        byte[] idContent = ("主键主数量："+idNum + "\n" +"主键内容：\n"+ ids+"\n").getBytes(StandardCharsets.UTF_8);
        // 创建 ResponseHeaders
//        HttpHeaders headers = new HttpHeaders();
////        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=file1.txt");
//        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=file2.txt");
//        // 将文件内容和 Headers 封装到 ResponseEntity 对象中返回
//        ResponseEntity<Object> response = ResponseEntity.ok().headers(headers).body(new LinkedMultiValueMap<String, Object>() {{
////            add("file1.txt", new ByteArrayResource(idContent));
//            add("file2.txt", new ByteArrayResource(sqlContent.getBytes(StandardCharsets.UTF_8)));
//        }});

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
