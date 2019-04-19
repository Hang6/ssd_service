package com.hang.ssd.service;

import com.hang.ssd.domain.param.OrderSubmitParam;
import com.hang.ssd.domain.vo.result.OrderResult;
import com.hang.ssd.domain.vo.result.OrderServiceResVo;

import java.util.List;

/**
 * 订单业务
 * @author yinhang
 */
public interface OrderService {
    /**
     * 用户提单
     * @param orderSubmitParam 提单参数
     * @return
     */
    OrderServiceResVo createOrder(OrderSubmitParam orderSubmitParam);

    /**
     * 骑手接单
     * @param orderId 订单id
     * @param riderId 骑手id
     * @param deliveryDistance 配送距离
     * @return
     */
    OrderServiceResVo riderConfirm(long orderId, String riderId, double deliveryDistance);

    /**
     * 骑手配送中
     * @param orderId
     * @return
     */
    OrderServiceResVo riderDelivery(long orderId);

    /**
     * 骑手送达
     * @param orderId
     * @return
     */
    OrderServiceResVo goodsArrived(long orderId);

    /**
     * 用户确认收货
     * @param orderId
     * @return
     */
    OrderServiceResVo orderFinish(long orderId);

    /**
     * 取消订单,只允许订单处于提单和接单状态
     * @param orderId
     * @return
     */
    OrderServiceResVo cancelOrder(long orderId);

    /**
     * 获取订单信息
     * @param orderId
     * @return
     */
    OrderResult getOrderByOrderId(long orderId);

    /**
     * 批量获取订单信息
     * @param wxId
     * @return
     */
    List<OrderResult> getOrdersByWxId(String wxId);

    /**
     * 批量获取未接单的订单
     * @return
     */
    List<OrderResult> getSubmitOrders();
}
