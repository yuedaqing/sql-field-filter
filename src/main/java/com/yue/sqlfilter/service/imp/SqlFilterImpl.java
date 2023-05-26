package com.yue.sqlfilter.service.imp;

import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;
import com.yue.sqlfilter.common.ErrorCode;
import com.yue.sqlfilter.exception.BusinessException;
import com.yue.sqlfilter.model.SqlField;
import com.yue.sqlfilter.service.SqlFilterService;
import com.yue.sqlfilter.utils.SqlFieldUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

/**
 * @author 可乐
 */
@Service
@Slf4j
public class SqlFilterImpl implements SqlFilterService {

    @Override
    public Map<String, String> insertFilter(MultipartFile file, SqlField sqlField) {
        String field1 = sqlField.getField1();
        String field2 = sqlField.getField2();
        String idField = sqlField.getIdField();
        String oldTableName = sqlField.getOldTableName();
        String newTableName = sqlField.getNewTableName();
        StringBuilder stringBuilder = new StringBuilder();

        Map<String, String> map = new HashMap<>();
        try {
            byte[] bytes = file.getBytes();
            String content = new String(bytes);
            if (StrUtil.isBlank(content)) {
                return map;
            }
            String[] split = content.trim().split(";");

            String[] arr1 = field1.toLowerCase().replaceAll("\\s", "").split(",");
            String[] arr2 = field2.toLowerCase().replaceAll("\\s", "").split(",");
            List<Integer> excludeList = SqlFieldUtil.elementIndex(arr2, arr1);
            List<String> idList = new ArrayList<>();
            if (ArrayUtil.isEmpty(split)) {
                return map;
            }
            for (int i = 0; i < split.length; i++) {
                String sql = split[i].trim();
//                ThrowUtils.throwIf(SqlFieldUtil.checkInsertSql(sql), ErrorCode.PARAMS_ERROR, "SQL语句不规范");
                String newSqlContent = this.filterSQL(sql, excludeList, oldTableName, newTableName, idField, sqlField.getConvertType(), idList);
                stringBuilder.append(newSqlContent);
            }
            String join = String.join(",", idList);
            map.put("ids", join);
            map.put("idNum", idList.size() + "");
            map.put("SQLContent", stringBuilder.toString());
        } catch (IOException e) {
            log.info("文件异常:{}", e.getMessage());
        }
        return map;
    }

    @Override
    public boolean validationField(SqlField sqlField) {
        boolean field1 = StrUtil.isBlank(sqlField.getField1());
        boolean field2 = StrUtil.isBlank(sqlField.getField2());
        boolean idField = StrUtil.isBlank(sqlField.getIdField());
        boolean oldTableName = StrUtil.isBlank(sqlField.getOldTableName());
        boolean newTableName = StrUtil.isBlank(sqlField.getNewTableName());
        if (field1 || field2 || idField || oldTableName || newTableName) {
            return true;
        }
        int covertTypeMaxValue = 2;
        return sqlField.getConvertType() == null || sqlField.getConvertType() > covertTypeMaxValue;
    }

    /**
     * @param sql          insertSQL语句
     * @param integerList  需要删除的索引列表
     * @param oldTableName 表名
     * @param newTableName 新表明
     * @param primaryKey   主键filed 如："id"
     * @throws IOException
     */
    public String filterSQL(String sql, List<Integer> integerList, String oldTableName, String newTableName, String primaryKey, Integer convertType, List<String> idList) {
        StringBuilder resultBuilder = new StringBuilder();
        String splitWord = "values";
        String splitSymbol = ",";
        String replaceValuesAfterSQL = StrUtil.replaceIgnoreCase(sql, "values", "values");
        String newSql = replaceValuesAfterSQL.replace(oldTableName, newTableName);
        String[] split = newSql.split(splitWord);
        // 只要VALUES分割后的长度大于2就说明SQL语句不规范
        if (split.length > 2) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "SQL语句不规范");
        }
        Integer primaryKeyIndex = null;
        split[split.length-1] = split[split.length-1].trim();
        // 处理两个values部分
        for (int i = 0; i < split.length; i++) {
            // 去除values前后的括号
            String removeBracket = StrUtil.subBetween(split[i], "(", ")");
            String beforeBracket = split[i].substring(0, split[i].indexOf("("));
            // 分割values括号内的内容为数组
            String[] arr = removeBracket.split(splitSymbol);
            if (ArrayUtil.isEmpty(arr)) {
                return null;
            }
            // 将指定下标的value数组移除，并根据需要进行类型转换
            int[] indexArr = new int[integerList.size()];
            for (int i1 = 0; i1 < integerList.size(); i1++) {
                indexArr[i1] = integerList.get(i1);
            }
            String[] newSqlArr = removeValuesByIndices(arr, indexArr);
            if (i == 0) {
                primaryKeyIndex = findIndexIgnoreCaseAndSpace(newSqlArr, primaryKey);
                newSqlArr[0] = beforeBracket + "(" + newSqlArr[0];
                newSqlArr[newSqlArr.length - 1] = newSqlArr[newSqlArr.length - 1] + ")values";
                // 数组转为字符串，并添加到结果中
                String join = String.join(",", newSqlArr);
                log.info("values前 = " + join.toLowerCase());
                resultBuilder.append(convertField(join, convertType));
            } else {
                idList.add(newSqlArr[primaryKeyIndex]);
                newSqlArr[0] = "(" + newSqlArr[0];
                newSqlArr[newSqlArr.length - 1] = newSqlArr[newSqlArr.length - 1] + ");\n";
                // 数组转为字符串，并添加到结果中
                String join = String.join(",", newSqlArr);
                log.info("values后 = " + join);
                resultBuilder.append(join);
            }
        }
        System.out.println(resultBuilder);
        return resultBuilder.toString();

    }

    /**
     * @param array           数组
     * @param indicesToRemove 需要删除数组的索引列表
     * @return 新数组
     */
    public static String[] removeValuesByIndices(String[] array, int[] indicesToRemove) {
        // 创建一个新的数组，长度为原数组的长度减去需要删除的索引的数量
        String[] result = new String[array.length - indicesToRemove.length];
        int index = 0;
        Arrays.sort(indicesToRemove);
        // 循环遍历原数组中的每个元素
        for (int i = 1; i <= array.length; i++) {
            // 如果当前元素的索引不在需要删除的列表中，则将其添加到新数组中
            if (Arrays.binarySearch(indicesToRemove, i) < 0) {
                result[index++] = array[i - 1];
            }
        }
        return result;
    }

    /**
     * 可以根据忽略大小写和任意空格的条件，返回字符串数组中指定字符串的索引位置。需要注意的是，空格不仅包括普通空格，还包括制表符和换行符等空白字符。
     *
     * @param arr 数组
     * @param str 目标字符串
     * @return 索引位置
     */
    public static int findIndexIgnoreCaseAndSpace(String[] arr, String str) {
        if (arr == null || str == null) {
            // 数组或字符串为空，返回-1
            return -1;
        }
        for (int i = 0; i < arr.length; i++) {
            String item = arr[i];
            if (item != null) {
                // 将字符串中的空白字符去掉，并忽略大小写比较
                if (item.trim().equalsIgnoreCase(str.trim())) {
                    // 找到了，返回索引位置
                    return i;
                }
            }
        }
        // 没有找到，返回-1
        return -1;
    }

    /**
     * 新增SQL语句 字段命名风格 0：下划线，1：小驼峰，2：全小写
     *
     * @param insertSql   insertSQL语句
     * @param convertType 转换类型
     * @return sql语句
     */
    private static String convertField(String insertSql, Integer convertType) {
        String sql = null;
        switch (convertType) {
            case 1:
                sql = StrUtil.toCamelCase(insertSql);
                break;
            case 2:
                sql = insertSql.toLowerCase();
                break;
            default:
                //小驼峰转下划线
                sql = insertSql.replaceAll("(?<=[a-z])([A-Z])", "_$1").toLowerCase();
                ;
                break;
        }
        return sql;
    }
}
