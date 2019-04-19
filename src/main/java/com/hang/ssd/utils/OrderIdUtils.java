package com.hang.ssd.utils;

/**
 * 订单id生成工具
 * @author yinhang
 */
public class OrderIdUtils {
    public static long constructId(){
        long orderId = System.currentTimeMillis();
        return orderId;
    }
}
