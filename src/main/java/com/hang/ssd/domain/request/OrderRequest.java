package com.hang.ssd.domain.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

/**
 * 订单基础请求参数
 * @author yinhang
 */
@Getter
@Setter
public class OrderRequest extends BaseRequest{
    @NotNull(message = "订单id不能为空")
    private long orderId;
}
