package com.hang.ssd.domain.constant;

/**
 * 订单状态常量
 * @author yinhang
 */

public enum  OrderStatusConst {
    /**
     * 用户提单
     */
    SUBMIT(1),
    /**
     * 骑手接单
     */
    CONFIRM(10),
    /**
     * 骑手配送中
     */
    DELIVERY(20),
    /**
     * 骑手已送达
     */
    ARRIVED(30),
    /**
     * 订单已完成
     */
    FINISHED(40),
    /**
     * 订单取消
     */
    CANCELED(50),
    /**
     * 非法状态
     */
    ILLEGAL(-1);


    OrderStatusConst(int value){
        this.value = value;
    }

    private int value;

    public int getValue(){
        return this.value;
    }

    public static OrderStatusConst findByValue(int value){
        switch (value){
            case 1:
                return SUBMIT;
            case 10:
                return CONFIRM;
            case 20:
                return DELIVERY;
            case 30:
                return ARRIVED;
            case 40:
                return FINISHED;
            case 50:
                return CANCELED;
            case -1:
                return ILLEGAL;
            default:
                return null;
        }
    }
}
