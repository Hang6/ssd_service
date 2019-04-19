package com.hang.ssd.utils;

import com.hang.ssd.domain.constant.IdentityType;
import com.hang.ssd.domain.response.OrderResponse;
import com.hang.ssd.domain.vo.result.OrderResult;

/**
 * 订单信息转换工具
 * @author yinhang
 */
public class OrderConvertUtils {
    public static OrderResponse result2response(OrderResult orderResult, String searchWxId){
        OrderResponse orderResponse = new OrderResponse();

        orderResponse.setOrderId(orderResult.getOrderId());
        orderResponse.setOrderStatus(orderResult.getOrderStatus());
        orderResponse.setUserName(orderResult.getUserName());
        orderResponse.setUserPhone(orderResult.getUserPhone());
        orderResponse.setCreateTime(TimeUtils.timeStamp2Date(String.valueOf(orderResult.getCreateTime())));

        orderResponse.setGoodsName(orderResult.getGoodsName());
        orderResponse.setGoodsQuantity(orderResult.getGoodsQuantity());

        orderResponse.setReceiverName(orderResult.getReceiverName());
        orderResponse.setReceiverPhone(orderResult.getReceiverPhone());
        orderResponse.setReceiverAddress(orderResult.getReceiverAddress());
        orderResponse.setReceiverLongitude(orderResult.getReceiverLongitude());
        orderResponse.setReceiverLatitude(orderResult.getReceiverLatitude());
        orderResponse.setRiderName(orderResult.getRiderName());
        orderResponse.setRiderPhone(orderResult.getRiderPhone());
        orderResponse.setDeliveryPrice(orderResult.getDeliveryPrice());
        orderResponse.setDeliveryDistance(orderResult.getDeliveryDistance());

        orderResponse.setRemarks(orderResult.getRemarks());
        if (searchWxId.equals(orderResult.getWxId())){
            orderResponse.setUserIdentity(IdentityType.COMSUMER.getTypeCode());
        }else if (searchWxId.equals(orderResult.getRiderWxId())){
            orderResponse.setUserIdentity(IdentityType.RIDER.getTypeCode());
        }

        return orderResponse;
    }
}
