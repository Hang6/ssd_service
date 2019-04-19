package com.hang.ssd.domain.vo.result;

import com.hang.ssd.domain.vo.code.OrderServiceVoCode;
import lombok.Data;

/**
 * 订单服务执行结果返回描述
 * @author yinhang
 */
@Data
public class OrderServiceResVo {
    private OrderServiceVoCode orderServiceVoCode;
    private String errorMsg;
}
