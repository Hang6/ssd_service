package com.hang.ssd.domain.param;

import lombok.Data;

/**
 * 收货人员信息
 * @author yinhang
 */

@Data
public class ReceiverParam {
    private String receiverName;
    private String receiverPhone;
    private String receiverAddress;
    private double receiverLongitude;
    private double receiverLatitude;
}
