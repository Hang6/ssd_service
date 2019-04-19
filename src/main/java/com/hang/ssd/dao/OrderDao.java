package com.hang.ssd.dao;

import com.hang.ssd.domain.entity.OrderInfo;
import com.hang.ssd.domain.vo.result.OrderResult;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author yinhang
 */

@Mapper
public interface OrderDao {
    /**
     * 创建订单
     * @param orderInfo
     * @return
     */
    int createOrder(OrderInfo orderInfo);

    /**
     * 更新订单状态-骑手接单
     * @param orderId
     * @param preStatus
     * @param deliveryDistance
     * @param riderWxId
     * @param riderName
     * @param riderPhone
     * @return
     */
    int updateStatusForConfirm(@Param("orderId") long orderId,@Param("deliveryDistance") double deliveryDistance,
                               @Param("preStatus") int preStatus, @Param("riderWxId") String riderWxId,
                               @Param("riderName") String riderName, @Param("riderPhone") String riderPhone);

    /**
     * 更新订单状态-配送中
     * @param orderId
     * @param preStatus
     * @return
     */
    int updateStatusForDelivery(@Param("orderId") long orderId, @Param("preStatus") int preStatus);

    /**
     * 更新订单状态-已送达
     * @param orderId
     * @param preStatus
     * @return
     */
    int updateStatusForArrived(@Param("orderId") long orderId, @Param("preStatus") int preStatus);

    /**
     * 更新订单状态-完成订单
     * @param orderId
     * @param preStatus
     * @return
     */
    int updateStatusForFinished(@Param("orderId") long orderId, @Param("preStatus") int preStatus);

    /**
     * 取消订单
     * @param orderId
     * @return
     */
    int updateStatusForCanceled(@Param("orderId") long orderId);

    /**
     * 根据订单id获取订单信息
     * @param orderId
     * @return
     */
    OrderResult getOrderByOrderId(@Param("orderId") long orderId);

    /**
     * 根据wxId获取所有订单
     * @param wxId
     * @return
     */
    List<OrderResult> getAllOrdersByWxId(@Param("wxId") String wxId);

    /**
     * 获取未接单的订单(最多50单)
     * @return
     */
    List<OrderResult> getSubmitOrders();
}
