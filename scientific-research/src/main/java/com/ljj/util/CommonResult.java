package com.ljj.util;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 自定义返回类
 * 可以返回一些需要的json
 * @param <T>
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommonResult<T> {

    /**
     * 状态码 例如404,200
     */
    private Integer code;

    /**
     * 返回结果
     * true  或者  false
     */
    private Boolean result;

    /**
     * 信息
     */
    private String message;

    /**
     * 结果集 信息体对象
     */
    private T data;

    public CommonResult(Integer code, Boolean result, String message) {
        this.code = code;
        this.result = result;
        this.message = message;
    }
}
