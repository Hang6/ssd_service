package com.hang.ssd.domain.param;

import lombok.Data;

/**
 * 货物信息
 * @author yinhang
 */

@Data
public class GoodsParam {
    private String goodsName;
    private String goodsQuantity;
    private double goodsPrice;
}
