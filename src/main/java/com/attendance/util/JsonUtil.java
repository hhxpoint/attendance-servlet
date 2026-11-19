package com.attendance.util;

import org.json.JSONObject;
import org.json.JSONArray;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;

/**
 * JSON工具类
 * 用于将Java对象转换为JSON格式
 * @author 汪轩增
 */
public class JsonUtil {
    
    /**
     * 将对象转换为Map，便于org.json处理
     */
    public static Map<String, Object> toMap(Object obj) {
        if (obj == null) {
            return null;
        }
        
        Map<String, Object> map = new HashMap<>();
        
        // 处理List集合
        if (obj instanceof List) {
            List<?> list = (List<?>) obj;
            List<Object> result = new ArrayList<>();
            for (Object item : list) {
                result.add(toMap(item));
            }
            // 对于List，包装成Map返回
            map.put("list", result);
            return map;
        }
        
        // 获取所有字段
        Class<?> clazz = obj.getClass();
        Field[] fields = clazz.getDeclaredFields();
        
        for (Field field : fields) {
            String fieldName = field.getName();
            // 跳过serialVersionUID
            if ("serialVersionUID".equals(fieldName)) {
                continue;
            }
            
            // 构造getter方法名
            String getterName = "get" + Character.toUpperCase(fieldName.charAt(0)) + fieldName.substring(1);
            
            try {
                Method getter = clazz.getMethod(getterName);
                Object value = getter.invoke(obj);
                
                // 处理Date类型
                if (value instanceof Date) {
                    map.put(fieldName, value);
                } 
                // 处理基本类型和String
                else if (value == null || value instanceof String || value instanceof Number || value instanceof Boolean) {
                    map.put(fieldName, value);
                }
                // 其他类型转为String
                else {
                    map.put(fieldName, value != null ? value.toString() : null);
                }
            } catch (Exception e) {
                // 如果getter方法不存在，跳过该字段
            }
        }
        
        return map;
    }
    
    /**
     * 将对象转换为JSON字符串
     */
    public static String toJson(Object obj) {
        if (obj == null) {
            return "null";
        }
        
        // 处理List
        if (obj instanceof List) {
            List<?> list = (List<?>) obj;
            JSONArray array = new JSONArray();
            for (Object item : list) {
                array.put(toMap(item));
            }
            return array.toString();
        }
        
        // 处理Map
        if (obj instanceof Map) {
            return new JSONObject(obj).toString();
        }
        
        // 处理其他对象
        return new JSONObject(toMap(obj)).toString();
    }
}
