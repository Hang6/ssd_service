package com.hang.ssd.domain.param;

import lombok.Data;

/**
 * 提单参数
 * @author yinhang
 */

@Data
public class OrderSubmitParam {
    private String wxId;
    private GoodsParam goodsParam;
    private ReceiverParam receiverParam;
    private DeliveryParam deliveryParam;
    private String remarks;
}
