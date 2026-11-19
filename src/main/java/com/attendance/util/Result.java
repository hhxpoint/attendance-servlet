package com.attendance.util;

import org.json.JSONArray;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 统一响应结果封装类
 * @author 汪轩增
 */
public class Result {
    
    private Integer code;
    private String msg;
    private Object data;
    private Long total;
    
    public Result() {}
    
    public Result(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }
    
    public Result(Integer code, String msg, Object data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }
    
    /**
     * 成功响应（无数据）
     */
    public static Result success() {
        return new Result(200, "操作成功");
    }
    
    /**
     * 成功响应（带数据）
     */
    public static Result success(Object data) {
        return new Result(200, "操作成功", data);
    }
    
    /**
     * 成功响应（带数据和总数）
     */
    public static Result success(Object data, Long total) {
        Result result = new Result(200, "操作成功", data);
        result.setTotal(total);
        return result;
    }
    
    /**
     * 成功响应（自定义消息）
     */
    public static Result success(String msg, Object data) {
        return new Result(200, msg, data);
    }
    
    /**
     * 失败响应
     */
    public static Result error(String msg) {
        return new Result(500, msg);
    }
    
    /**
     * 失败响应（自定义状态码）
     */
    public static Result error(Integer code, String msg) {
        return new Result(code, msg);
    }
    
    /**
     * 未授权响应
     */
    public static Result unauthorized() {
        return new Result(401, "未登录或登录已过期");
    }
    
    /**
     * 禁止访问响应
     */
    public static Result forbidden() {
        return new Result(403, "没有权限访问");
    }
    
    /**
     * 转换为JSON字符串
     * 使用JsonUtil处理复杂对象
     */
    public String toJson() {
        Map<String, Object> result = new HashMap<>();
        result.put("code", code);
        result.put("msg", msg);
        
        // 处理data字段
        if (data != null) {
            if (data instanceof List) {
                // 列表类型，转换为JSONArray
                List<?> list = (List<?>) data;
                JSONArray array = new JSONArray();
                for (Object item : list) {
                    array.put(JsonUtil.toMap(item));
                }
                result.put("data", array);
            } else if (data instanceof Map) {
                // Map类型
                result.put("data", data);
            } else if (isBasicType(data)) {
                // 基本类型
                result.put("data", data);
            } else {
                // 复杂对象，转换为Map
                result.put("data", JsonUtil.toMap(data));
            }
        }
        
        if (total != null) {
            result.put("total", total);
        }
        
        return new JSONObject(result).toString();
    }
    
    /**
     * 判断是否为基本类型
     */
    private boolean isBasicType(Object obj) {
        return obj == null || obj instanceof String || obj instanceof Number || obj instanceof Boolean;
    }
    
    // Getters and Setters
    public Integer getCode() { return code; }
    public void setCode(Integer code) { this.code = code; }
    public String getMsg() { return msg; }
    public void setMsg(String msg) { this.msg = msg; }
    public Object getData() { return data; }
    public void setData(Object data) { this.data = data; }
    public Long getTotal() { return total; }
    public void setTotal(Long total) { this.total = total; }
}
