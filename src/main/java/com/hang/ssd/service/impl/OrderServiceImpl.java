package com.hang.ssd.service.impl;

import com.hang.ssd.domain.constant.OrderStatusConst;
import com.hang.ssd.domain.entity.OrderInfo;
import com.hang.ssd.domain.param.OrderSubmitParam;
import com.hang.ssd.domain.vo.code.OrderServiceVoCode;
import com.hang.ssd.domain.vo.result.OrderResult;
import com.hang.ssd.domain.vo.result.OrderServiceResVo;
import com.hang.ssd.domain.vo.result.UserResult;
import com.hang.ssd.dao.OrderDao;
import com.hang.ssd.dao.UserDao;
import com.hang.ssd.service.OrderService;
import com.hang.ssd.utils.OrderIdUtils;
import com.hang.ssd.utils.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 订单服务
 * @author yinhang
 */
@Slf4j
@Service
public class OrderServiceImpl implements OrderService {
    @Autowired
    private UserDao userDao;
    @Autowired
    private OrderDao orderDao;

    /**
     * 用户提单
     * @param orderSubmitParam 提单参数
     * @return
     */
    @Override
    public OrderServiceResVo createOrder(OrderSubmitParam orderSubmitParam) {
        OrderServiceResVo resVo = new OrderServiceResVo();
        resVo.setOrderServiceVoCode(OrderServiceVoCode.FAILED);

        UserResult userResult;
        try {
            userResult = userDao.getUserByWxId(orderSubmitParam.getWxId());
            if (userResult == null){
                resVo.setErrorMsg("查询下单人员信息为空!请先完善个人信息");
                return resVo;
            }
        }catch (Exception e){
            log.error("#userDao.getUserByWxId# error! wxId:[{}]", orderSubmitParam.getWxId(), e);
            resVo.setErrorMsg("数据库查询error!请稍后重试!");
            return resVo;
        }

        //构建订单
        OrderInfo orderInfo = constructOrder(orderSubmitParam, userResult);
        if (orderInfo == null){
            resVo.setErrorMsg("订单参数不完整，请完善后重新下单");
            return resVo;
        }

        //订单入库
        try {
            int res = orderDao.createOrder(orderInfo);
            if (res <= 0){
                log.error("订单入库失败！orderId:[{}]", orderInfo.getOrderId());
                resVo.setErrorMsg("创建订单失败!");
                return resVo;
            }
        }catch (Exception e){
            log.error("#orderDao.createOrder# error! orderId:[{}]", orderInfo.getOrderId(), e);
            resVo.setErrorMsg("数据库error! 创建订单失败!");
            return resVo;
        }

        resVo.setOrderServiceVoCode(OrderServiceVoCode.SUCCESS);
        return resVo;
    }

    /**
     * 骑手接单
     *
     * @param orderId 订单id
     * @param riderId 骑手id
     * @return
     */
    @Override
    public OrderServiceResVo riderConfirm(long orderId, String riderId, double deliveryDistance) {
        OrderServiceResVo resVo = new OrderServiceResVo();
        resVo.setOrderServiceVoCode(OrderServiceVoCode.FAILED);

        if (orderId <= 0 || StringUtils.isEmpty(riderId)){
            log.error("#OrderServiceImpl.riderConfirm# 参数非法！");
            resVo.setErrorMsg("参数非法！请稍后重试");
        }

        OrderResult orderResult;
        try {
            orderResult = orderDao.getOrderByOrderId(orderId);
            if (orderResult == null){
                log.warn("#OrderServiceImpl.riderConfirm# 查询对应订单为空！");
                resVo.setErrorMsg("对应订单为空!");
                return resVo;
            }
        }catch (Exception e){
            log.error("orderDao.riderConfirm# error! orderId:[{}]", orderId, e);
            resVo.setErrorMsg("数据库error! 查询该订单失败");
            return resVo;
        }

        //判断该订单是否已经被接单
        if (orderResult.getOrderStatus() != OrderStatusConst.SUBMIT.getValue()){
            resVo.setErrorMsg("该订单已接单!");
            return resVo;
        }

        UserResult riderInfo;
        try {
            riderInfo = userDao.getUserByWxId(riderId);
            if (riderInfo == null){
                resVo.setErrorMsg("骑手信息为空！");
                return resVo;
            }
        }catch (Exception e){
            log.error("#userDao.getUserByWxId# error! riderId:[{}]", riderId, e);
            resVo.setErrorMsg("查询骑手出错!");
            return resVo;
        }

        //骑手接单
        try {
            int res = orderDao.updateStatusForConfirm(orderId, deliveryDistance, OrderStatusConst.SUBMIT.getValue(),
                    riderInfo.getWxId(), riderInfo.getUserName(), riderInfo.getBindPhone());
            if (res <= 0){
                resVo.setErrorMsg("接单失败！");
                return resVo;
            }
        }catch (Exception e){
            log.error("#orderDao.updateStatusForConfirm# error! orderId:[{}]", orderId, e);
            resVo.setErrorMsg("数据库错误！接单失败！");
            return resVo;
        }

        resVo.setOrderServiceVoCode(OrderServiceVoCode.SUCCESS);
        return resVo;
    }

    /**
     * 骑手配送中
     *
     * @param orderId
     * @return
     */
    @Override
    public OrderServiceResVo riderDelivery(long orderId) {
        OrderServiceResVo resVo = new OrderServiceResVo();
        resVo.setOrderServiceVoCode(OrderServiceVoCode.FAILED);

        if (orderId <= 0){
            log.error("#OrderServiceImpl.riderDelivery# 参数非法！");
            resVo.setErrorMsg("参数非法");
        }

        OrderResult orderResult;
        try {
            orderResult = orderDao.getOrderByOrderId(orderId);
            if (orderResult == null){
                log.warn("#OrderServiceImpl.riderDelivery# 查询对应订单为空！");
                resVo.setErrorMsg("查询订单为空!");
                return resVo;
            }
        }catch (Exception e){
            log.error("orderDao.getOrderByOrderId# error! orderId:[{}]", orderId, e);
            resVo.setErrorMsg("error! 查询订单失败");
            return resVo;
        }

        if (orderResult.getOrderStatus() != OrderStatusConst.CONFIRM.getValue()){
            resVo.setErrorMsg("失败！状态非法");
            return resVo;
        }

        try {
            int res = orderDao.updateStatusForDelivery(orderId, OrderStatusConst.CONFIRM.getValue());
            if (res <= 0){
                resVo.setErrorMsg("失败！状态非法");
                return resVo;
            }
        }catch (Exception e){
            log.error("#orderDao.updateStatusForDelivery# error! orderId:[{}]", orderId, e);
            resVo.setErrorMsg("失败！请稍后重试");
            return resVo;
        }

        resVo.setOrderServiceVoCode(OrderServiceVoCode.SUCCESS);
        return resVo;
    }

    /**
     * 骑手送达
     *
     * @param orderId
     * @return
     */
    @Override
    public OrderServiceResVo goodsArrived(long orderId) {
        OrderServiceResVo resVo = new OrderServiceResVo();
        resVo.setOrderServiceVoCode(OrderServiceVoCode.FAILED);

        if (orderId <= 0){
            log.error("#OrderServiceImpl.goodsArrived# 参数非法！");
            resVo.setErrorMsg("参数非法");
        }

        OrderResult orderResult;
        try {
            orderResult = orderDao.getOrderByOrderId(orderId);
            if (orderResult == null){
                log.warn("#OrderServiceImpl.goodsArrived# 查询对应订单为空！");
                resVo.setErrorMsg("查询订单为空!");
                return resVo;
            }
        }catch (Exception e){
            log.error("orderDao.getOrderByOrderId# error! orderId:[{}]", orderId, e);
            resVo.setErrorMsg("error! 查询订单失败");
            return resVo;
        }

        if (orderResult.getOrderStatus() != OrderStatusConst.DELIVERY.getValue()){
            resVo.setErrorMsg("失败！状态非法");
            return resVo;
        }

        try {
            int res = orderDao.updateStatusForArrived(orderId, OrderStatusConst.DELIVERY.getValue());
            if (res <= 0){
                resVo.setErrorMsg("失败！状态非法");
                return resVo;
            }
        }catch (Exception e){
            log.error("#orderDao.updateStatusForArrived# error! orderId:[{}]", orderId, e);
            resVo.setErrorMsg("失败！请稍后重试");
            return resVo;
        }

        resVo.setOrderServiceVoCode(OrderServiceVoCode.SUCCESS);
        return resVo;
    }

    /**
     * 用户确认收货
     *
     * @param orderId
     * @return
     */
    @Override
    public OrderServiceResVo orderFinish(long orderId) {
        OrderServiceResVo resVo = new OrderServiceResVo();
        resVo.setOrderServiceVoCode(OrderServiceVoCode.FAILED);

        if (orderId <= 0){
            log.error("#OrderServiceImpl.orderFinish# 参数非法！");
            resVo.setErrorMsg("参数非法");
        }

        OrderResult orderResult;
        try {
            orderResult = orderDao.getOrderByOrderId(orderId);
            if (orderResult == null){
                log.warn("#OrderServiceImpl.orderFinish# 查询对应订单为空！");
                resVo.setErrorMsg("查询订单为空!");
                return resVo;
            }
        }catch (Exception e){
            log.error("orderDao.getOrderByOrderId# error! orderId:[{}]", orderId, e);
            resVo.setErrorMsg("数据库error! 查询订单失败");
            return resVo;
        }

        if (orderResult.getOrderStatus() != OrderStatusConst.ARRIVED.getValue()){
            resVo.setErrorMsg("失败！状态非法");
            return resVo;
        }

        try {
            int res = orderDao.updateStatusForFinished(orderId, OrderStatusConst.ARRIVED.getValue());
            if (res <= 0){
                resVo.setErrorMsg("失败！状态非法");
                return resVo;
            }
        }catch (Exception e){
            log.error("#orderDao.updateStatusForFinished# error! orderId:[{}]", orderId, e);
            resVo.setErrorMsg("失败！请稍后重试");
            return resVo;
        }

        resVo.setOrderServiceVoCode(OrderServiceVoCode.SUCCESS);
        return resVo;
    }

    /**
     * 取消订单
     * 只允许订单处于提单状态
     * @param orderId
     * @return
     */
    @Override
    public OrderServiceResVo cancelOrder(long orderId) {
        OrderServiceResVo resVo = new OrderServiceResVo();
        resVo.setOrderServiceVoCode(OrderServiceVoCode.FAILED);

        if (orderId <= 0){
            log.error("#OrderServiceImpl.cancelOrder# 参数非法！");
            resVo.setErrorMsg("参数非法");
        }

        OrderResult orderResult;
        try {
            orderResult = orderDao.getOrderByOrderId(orderId);
            if (orderResult == null){
                log.warn("#OrderServiceImpl.cancelOrder# 查询对应订单为空！");
                resVo.setErrorMsg("查询订单为空!");
                return resVo;
            }
        }catch (Exception e){
            log.error("orderDao.riderConfirm# error! orderId:[{}]", orderId, e);
            resVo.setErrorMsg("error! 查询订单失败");
            return resVo;
        }
        //判断订单状态是否符合取消场景
        if (orderResult.getOrderStatus() != OrderStatusConst.SUBMIT.getValue()){
            resVo.setErrorMsg("已接单，无法取消");
            return resVo;
        }

        //取消订单
        try {
            int res = orderDao.updateStatusForCanceled(orderId);
            if (res <= 0){
                resVo.setErrorMsg("失败！骑手已接单");
                return resVo;
            }
        }catch (Exception e){
            log.error("#orderDao.updateStatusForCanceled# error! orderId:[{}]", orderId, e);
            resVo.setErrorMsg("错误！请稍后重试！");
            return resVo;
        }

        resVo.setOrderServiceVoCode(OrderServiceVoCode.SUCCESS);
        return resVo;
    }

    /**
     * 获取订单信息
     *
     * @param orderId
     * @return
     */
    @Override
    public OrderResult getOrderByOrderId(long orderId) {
        if (orderId <= 0){
            log.error("#OrderServiceImpl.getOrderByOrderId# 参数非法!");
            return null;
        }

        OrderResult orderResult;
        try {
            orderResult = orderDao.getOrderByOrderId(orderId);
        }catch (Exception e){
            log.error("#orderDao.getOrderByOrderId# error! orderId:[{}]", orderId, e);
            return null;
        }

        return orderResult;
    }

    /**
     * 批量获取订单信息
     *
     * @param wxId
     * @return
     */
    @Override
    public List<OrderResult> getOrdersByWxId(String wxId) {
        List<OrderResult> orderResults = new ArrayList<>();
        if (StringUtils.isEmpty(wxId)){
            log.error("#OrderServiceImpl.getOrdersByWxId# 参数为空！");
            return orderResults;
        }

        try {
            orderResults = orderDao.getAllOrdersByWxId(wxId);
        }catch (Exception e){
            log.error("#orderDao.getAllOrdersByWxId# error! wxId:[{}]", wxId, e);
        }
        return orderResults;
    }

    /**
     * 批量获取未接单的订单
     *
     * @return
     */
    @Override
    public List<OrderResult> getSubmitOrders(String wxId) {
        List<OrderResult> orderResults = new ArrayList<>();

        try {
            orderResults = orderDao.getSubmitOrders(wxId);
        }catch (Exception e){
            log.error("#orderDao.getSubmitOrders# error! wxId:[{}]", e);
        }
        return orderResults;
    }


    private OrderInfo constructOrder(OrderSubmitParam orderSubmitParam, UserResult userResult) {
        OrderInfo orderInfo = new OrderInfo();

        orderInfo.setOrderId(OrderIdUtils.constructId());
        orderInfo.setOrderStatus(OrderStatusConst.SUBMIT.getValue());
        orderInfo.setRemarks(orderSubmitParam.getRemarks());

        //下单人员信息
        orderInfo.setWxId(userResult.getWxId());
        orderInfo.setUserName(userResult.getUserName());
        orderInfo.setUserPhone(userResult.getBindPhone());

        //货物信息
        if (orderSubmitParam.getGoodsParam() != null){
            orderInfo.setGoodsName(orderSubmitParam.getGoodsParam().getGoodsName());
            orderInfo.setGoodsQuantity(orderSubmitParam.getGoodsParam().getGoodsQuantity());
            orderInfo.setGoodsPrice(orderSubmitParam.getGoodsParam().getGoodsPrice());
        } else {
            log.error("货物参数为空！");
            return null;
        }

        //收货人信息
        if (orderSubmitParam.getReceiverParam() != null){
            orderInfo.setReceiverName(orderSubmitParam.getReceiverParam().getReceiverName());
            orderInfo.setReceiverPhone(orderSubmitParam.getReceiverParam().getReceiverPhone());
            orderInfo.setReceiverAddress(orderSubmitParam.getReceiverParam().getReceiverAddress());
            orderInfo.setReceiverLongitude(orderSubmitParam.getReceiverParam().getReceiverLongitude());
            orderInfo.setReceiverLatitude(orderSubmitParam.getReceiverParam().getReceiverLatitude());
        }else {
            log.error("收货人参数为空！");
            return null;
        }

        //配送信息
        if (orderSubmitParam.getDeliveryParam() != null){
            orderInfo.setDeliveryPrice(orderSubmitParam.getDeliveryParam().getDeliveryPrice());
        }else {
            log.error("配送信息参数为空!");
            return null;
        }

        int nowTime = (int) (System.currentTimeMillis()/1000);
        orderInfo.setCreateTime(nowTime);
        orderInfo.setUpdateTime(nowTime);
        orderInfo.setExt("");

        return orderInfo;
    }
}
