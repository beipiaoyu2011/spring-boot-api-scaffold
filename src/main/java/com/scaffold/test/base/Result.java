package com.scaffold.test.base;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;

/**
 * 统一API响应结果封装
 */

@Data
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class Result {

    private int code;

    private String message = "success";

    private Object data;

    public Result setCode(ResultCode resultCode){
        this.code = resultCode.code;
        return this;
    }

    public Result setMessage(String message){
        this.message = message;
        return this;
    }

    public Result setData(Object data){
        this.data = data;
        return this;
    }

}
