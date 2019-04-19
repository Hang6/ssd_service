package com.hang.ssd.domain.constant;

/**
 * http数据请求返回结果枚举
 * @author yinhang
 */
public enum HttpResponseCode {
    //成功
    SUCCESS(0),
    //失败
    FAILED(1);

    HttpResponseCode(int value){
        this.value = value;
    }

    private int value;

    public int getValue() {
        return value;
    }
}
