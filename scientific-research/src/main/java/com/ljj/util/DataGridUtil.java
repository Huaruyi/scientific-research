package com.ljj.util;

import org.springframework.data.domain.Page;

import java.util.HashMap;

/**
 * 用于格式化分页数据
 *
 *  code:Layui官方要求返回状态码为0
 *  message:消息，可以自定义一些返回消息提示
 *  number:元素的个数
 *  count:所有元素数量
 *  data:返回的数据对象，方便前端使用
 */
public class DataGridUtil {
    public static HashMap<String, Object> buildResult(Page page) {
        HashMap<String, Object> result = new HashMap<>(5);
        result.put("code", 0);
        result.put("message","");
        result.put("number", page.getNumberOfElements());
        result.put("count", page.getTotalElements());
        result.put("data", page.getContent());
        return result;
    }

    public static HashMap<String, Object> buildResult(Page page, String message) {
        HashMap<String, Object> result = new HashMap<>(5);
        result.put("code", 0);
        result.put("message",message);
        result.put("number", page.getNumberOfElements());
        result.put("count", page.getTotalElements());
        result.put("data", page.getContent());
        return result;
    }
}