package com.hang.ssd.domain.vo.result;

import lombok.Getter;
import lombok.Setter;

/**
 * 订单信息
 * @author yinhang
 */

@Setter
@Getter
public class OrderResult {
    private int id;
    private long orderId;
    private int orderStatus;
    private String remarks;

    /**
     * 下单人员信息
     */
    private String wxId;
    private String userName;
    private String userPhone;

    /**
     * 货物信息
     */
    private String goodsName;
    private String goodsQuantity;
    private double goodsPrice;

    /**
     * 收货人员信息
     */
    private String receiverName;
    private String receiverPhone;
    private String receiverAddress;
    private double receiverLongitude;
    private double receiverLatitude;

    /**
     * 配送人员信息
     */
    private String riderWxId;
    private String riderName;
    private String riderPhone;

    /**
     * 配送信息
     */
    private double deliveryPrice;
    private double deliveryDistance;

    private int createTime;
    private int updateTime;

    /**
     * 额外信息
     */
    private String ext;
}
