package com.yue.sqlfilter.utils;

import cn.hutool.db.sql.SqlExecutor;
import cn.hutool.db.sql.SqlFormatter;
import cn.hutool.db.sql.SqlUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author 可乐
 */
public class SqlFieldUtil {
    public static void main(String[] args) {
//        fieldDifference();
//        List<Integer> integers = elementIndex();
    }

    /**
     * 以源数组为准，找出新数组与源数组不同元素的索引位置
     *
     * @param array1 原数组
     * @param array2 新数组
     * @return 不同元素的索引列表
     */
    public static List<Integer> elementIndex(String[] array1, String[] array2) {
//        String[] array1 = {"A", "B", "C", "D", "E"};
//        String[] array2 = {"B", "C", "E", "F", "G","123"};
        // 用于存储不同元素在第二个数组中的下标
        List<Integer> indexList = new ArrayList<>();
        // 用于存储第二个数组与第一个数组不同的元素
        List<String> resultList = new ArrayList<>();

        for (int i = 0; i < array2.length; i++) {
            boolean isEqual = false;
            for (int j = 0; j < array1.length; j++) {
                if (array2[i].equals(array1[j])) {
                    isEqual = true;
                    break;
                }
            }
            if (!isEqual) {
                indexList.add(i + 1);
                resultList.add(array2[i]);
            }
        }
        System.out.println("resultList = " + resultList);
        return indexList;
    }

    /**
     * insert SQL语句规范性检验
     *
     * @param sql insertSQL语句校验
     * @return true：不规范， false规范
     */
    public static boolean checkInsertSql(String sql) {
        // 使用正则表达式匹配insert语句，以下仅匹配了一部分常用的语法
        String regex = "^\\s*INSERT\\s+INTO\\s+\\w+\\s*\\(.*\\)\\s*VALUES\\s*\\(.*\\)\\s*;\\s*$";
        boolean flag = false;
        for (int i = 0; i < sql.split(";").length; i++) {
            if (!sql.matches(regex)) {
                flag = true;
                break;
            }
        }
        return flag;

    }
}
