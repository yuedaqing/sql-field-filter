package com.yue.sqlfilter.service.imp;

import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;
import com.yue.sqlfilter.common.ErrorCode;
import com.yue.sqlfilter.common.ResultUtils;
import com.yue.sqlfilter.model.SqlField;
import com.yue.sqlfilter.service.SqlFilterService;
import com.yue.sqlfilter.utils.SqlUtil;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

/**
 * @author 可乐
 */
@Service
public class SqlFilterImpl implements SqlFilterService {
    private List<String> idList = new ArrayList<>();

    @Override
    public Map<String, String> insertFilter(MultipartFile file, SqlField sqlField) throws IOException {
        String field1 = sqlField.getField1();
        String field2 = sqlField.getField2();
        String idField = sqlField.getIdField();

        Map<String, String> map = new HashMap<>();
        byte[] bytes = file.getBytes();
        String content = new String(bytes);
        if (StrUtil.isBlank(content)) {
            return map;
        }
        String[] arr1 = field1.toLowerCase().replaceAll("\\s", "").split(",");
        String[] arr2 = field2.toLowerCase().replaceAll("\\s", "").split(",");
        List<Integer> excludeList = SqlUtil.elementIndex(arr1, arr2);
//        FileReader fileReader = new FileReader("D:\\code\\yue-project\\sql-filter\\basic-sql.text");
//        String string = fileReader.readString();
        String[] split = content.split(";");
        String oldTableName = "`hljqlk-center`.`up_task_public_basic`";
        String newTableName = "`hz_material_file`.`dn_task_public_basic`";
        StringBuilder stringBuilder = new StringBuilder();

        for (int i = 0; i < split.length; i++) {
            try {
                String newSqlContent = this.sqlFilter(split[i], excludeList, oldTableName, newTableName, idField);
                stringBuilder.append(newSqlContent);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        String join = String.join(",", this.idList);
        //写入主键
//        FileWriter idWriter = new FileWriter("D:\\code\\yue-project\\sql-filter\\id.text");
//        idWriter.write("sql总条目数" + this.idList.size());
//        idWriter.append(join);
        map.put("ids", join);
        map.put("idNum", this.idList.size() + "");
        map.put("SQLContent", stringBuilder.toString());
        return map;
    }

    /**
     * @param sql          insertSQL语句
     * @param integerList  需要删除的索引列表
     * @param oldTableName 表名
     * @param newTableName 新表明
     * @param primaryKey   主键filed 如："id"
     * @throws IOException
     */
    public String sqlFilter(String sql, List<Integer> integerList, String oldTableName, String newTableName, String primaryKey) throws IOException {
        if (StrUtil.isBlank(sql)) {
            return null;
        }
        StringBuilder stringBuilder = new StringBuilder();
        boolean isFormat = true;
        String splitWord = "values";
        String splitSymbol = ",";
        String newSql = sql.replace(oldTableName, newTableName).toLowerCase();
        String[] split = newSql.split(splitWord);
        if (ArrayUtil.isEmpty(split)) {
            return null;
        }
        Integer primaryKeyIndex = null;
        for (int i = 0; i < split.length; i++) {
            //1. 去除values前后的()
            String removeBracket = StrUtil.subBetween(split[i], "(", ")");
            String beforeBracket = split[i].substring(0, split[i].indexOf("("));
            //2. 分割字符串
            String[] arr = removeBracket.split(splitSymbol);
            if (ArrayUtil.isEmpty(arr)) {
                return null;
            }
            int[] indexArr = new int[integerList.size()];
            for (int i1 = 0; i1 < integerList.size(); i1++) {
                indexArr[i1] = integerList.get(i1);
            }
            String[] newSqlArr = removeValuesByIndices(arr, indexArr);
            if (i == 0) {

                primaryKeyIndex = findIndexIgnoreCaseAndSpace(newSqlArr, primaryKey);
                newSqlArr[0] = beforeBracket + "(" + newSqlArr[0];
                newSqlArr[newSqlArr.length - 1] = newSqlArr[newSqlArr.length - 1] + ")values";
                String join = String.join(",", newSqlArr);
                System.out.println("values前 = " + join.toLowerCase());
                stringBuilder.append(join.toLowerCase());
            } else {
                this.idList.add(newSqlArr[primaryKeyIndex]);
                newSqlArr[0] = "(" + newSqlArr[0];
                newSqlArr[newSqlArr.length - 1] = newSqlArr[newSqlArr.length - 1] + ");";
                String join = String.join(",", newSqlArr);
                System.out.println("values后 = " + join);
                stringBuilder.append(join);
            }
        }
        System.out.println(stringBuilder);
        //写入过滤后的sql语句
//        FileWriter fileWriter = new FileWriter("D:\\code\\yue-project\\sql-filter\\sql.text");
//        fileWriter.append("\n" + stringBuilder);
        return stringBuilder.toString();

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
}
