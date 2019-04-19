package com.hang.ssd.domain.vo.code;

/**
 * 订单服务返回常量
 * @author yinhang
 */
public enum  OrderServiceVoCode {
    SUCCESS(0),

    FAILED(1);

    private int value;

    OrderServiceVoCode(int value){
        this.value = value;
    }

    public int getValue(){
        return value;
    }

    public static OrderServiceVoCode findByValue(int value){
        switch (value){
            case 0:
                return SUCCESS;
            case 1:
                return FAILED;
            default:
                return null;
        }
    }
}
