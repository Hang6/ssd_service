package com.hang.ssd.domain.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

/**
 * 提单参数
 * @author yinhang
 */
@Setter
@Getter
public class SubmitRequest {
    @NotNull(message = "token不能为空")
    private String token;
    @NotNull(message = "货品名称不能为空")
    private String goodsName;
    @NotNull(message = "货品数量不能为空")
    private String goodsQuantity;
    private double goodsPrice;
    @NotNull(message = "收货人姓名不能为空")
    private String receiverName;
    @NotNull(message = "收货人电话不能为空")
    private String receiverPhone;
    @NotNull(message = "收货人地址不能为空")
    private String receiverAddress;
    @NotNull(message = "收货人地址经度不能为空")
    private double receiverLongitude;
    @NotNull(message = "收货人地址纬度不能为空")
    private double receiverLatitude;
    @NotNull(message = "配送价格不能为空")
    private double deliveryPrice;
    private String remarks;
}
