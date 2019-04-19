package com.hang.ssd.domain.response;

import lombok.Data;

/**
 * 查询订单返回类型
 * @author yinhang
 */
@Data
public class OrderResponse {
    /**
     * 订单信息
     */
    private long orderId;
    private int orderStatus;
    private String userName;
    private String userPhone;
    private String createTime;

    /**
     * 货物信息
     */
    private String goodsName;
    private String goodsQuantity;

    /**
     * 配送信息
     */
    private String receiverName;
    private String receiverPhone;
    private String receiverAddress;
    private double receiverLongitude;
    private double receiverLatitude;
    private String riderName;
    private String riderPhone;
    private double deliveryPrice;
    private double deliveryDistance;

    /**
     * 补充信息
     */
    private int userIdentity;
    private String remarks;
}
