package com.hang.ssd.domain.request;

import lombok.Getter;
import lombok.Setter;

/**
 * 骑手接单请求参数
 * @author yinhang
 */
@Getter
@Setter
public class RiderConfirmRequest extends OrderRequest {
    private double deliveryDistance;
}
