package com.hang.ssd.api;

import com.hang.ssd.domain.param.DeliveryParam;
import com.hang.ssd.domain.param.GoodsParam;
import com.hang.ssd.domain.param.OrderSubmitParam;
import com.hang.ssd.domain.param.ReceiverParam;
import com.hang.ssd.domain.request.OrderRequest;
import com.hang.ssd.domain.request.RiderConfirmRequest;
import com.hang.ssd.domain.request.SubmitRequest;
import com.hang.ssd.domain.response.OrderResponse;
import com.hang.ssd.domain.vo.code.OrderServiceVoCode;
import com.hang.ssd.domain.vo.result.OrderResult;
import com.hang.ssd.domain.vo.result.OrderServiceResVo;
import com.hang.ssd.service.CacheService;
import com.hang.ssd.service.OrderService;
import com.hang.ssd.utils.DistanceUtils;
import com.hang.ssd.utils.OrderConvertUtils;
import com.hang.ssd.utils.ResponseUtils;
import com.hang.ssd.utils.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * 订单服务api
 * @author yinhang
 */

@Slf4j
@RestController
@RequestMapping("/order")
public class OrderController {
    @Autowired
    private OrderService orderService;
    @Autowired
    private CacheService cacheService;

    /**
     * 用户提单
     * @param submitRequest
     * @return
     */
    @PostMapping("createorder")
    public Object orderSubmit(@RequestBody @Validated SubmitRequest submitRequest){
        System.out.println(submitRequest);
        //构建提单参数
        OrderSubmitParam orderSubmitParam = constructSubmitParam(submitRequest);

        OrderServiceResVo resVo = orderService.createOrder(orderSubmitParam);
        if (resVo.getOrderServiceVoCode() == OrderServiceVoCode.FAILED){
            return ResponseUtils.constructFailedResponse(resVo.getErrorMsg());
        }

        return ResponseUtils.constructSucResponse("订单已提交!");
    }

    /**
     * 骑手接单
     * @return
     */
    @PostMapping("confirm")
    public Object orderConfirm(@RequestBody @Validated RiderConfirmRequest riderConfirmRequest){
        String wxId = String.valueOf(cacheService.getUserOpenId(riderConfirmRequest.getToken()));
        OrderServiceResVo resVo = orderService.riderConfirm(riderConfirmRequest.getOrderId(),
                wxId, riderConfirmRequest.getDeliveryDistance());
        if (resVo.getOrderServiceVoCode() == OrderServiceVoCode.FAILED){
            return ResponseUtils.constructFailedResponse(resVo.getErrorMsg());
        }
        return ResponseUtils.constructSucResponse("接单成功");
    }

    /**
     * 骑手取货配送
     * @param orderRequest
     * @return
     */
    @PostMapping("delivery")
    public Object orderDelivery(@RequestBody @Validated OrderRequest orderRequest){
        OrderServiceResVo resVo = orderService.riderDelivery(orderRequest.getOrderId());
        if (resVo.getOrderServiceVoCode() == OrderServiceVoCode.FAILED){
            return ResponseUtils.constructFailedResponse(resVo.getErrorMsg());
        }
        return ResponseUtils.constructSucResponse("开始配送");
    }

    /**
     * 商品送达
     * @param orderRequest
     * @return
     */
    @PostMapping("arrived")
    public Object goodsArrived(@RequestBody @Validated OrderRequest orderRequest){
        OrderServiceResVo resVo = orderService.goodsArrived(orderRequest.getOrderId());
        if (resVo.getOrderServiceVoCode() == OrderServiceVoCode.FAILED){
            return ResponseUtils.constructFailedResponse(resVo.getErrorMsg());
        }
        return ResponseUtils.constructSucResponse("商品已送达");
    }

    /**
     * 订单完成
     * @param
     * @return
     */
    @PostMapping("finish")
    public Object orderFinish(@RequestBody @Validated OrderRequest orderRequest){

        OrderServiceResVo resVo = orderService.orderFinish(orderRequest.getOrderId());
        if (resVo.getOrderServiceVoCode() == OrderServiceVoCode.FAILED){
            return ResponseUtils.constructFailedResponse(resVo.getErrorMsg());
        }
        return ResponseUtils.constructSucResponse("订单已完成");
    }

    /**
     * 取消订单
     * @param
     * @return
     */
    @PostMapping("cancel")
    public Object orderCancel(@RequestBody @Validated OrderRequest orderRequest){

        OrderServiceResVo resVo = orderService.cancelOrder(orderRequest.getOrderId());
        if (resVo.getOrderServiceVoCode() == OrderServiceVoCode.FAILED){
            return ResponseUtils.constructFailedResponse(resVo.getErrorMsg());
        }
        return ResponseUtils.constructSucResponse("订单已取消");
    }

    /**
     * 获取未接单的订单
     * @return
     */
    @GetMapping("getsubmitorders")
    public Object getSubmitOrders(@RequestParam("token") String token,
                                  @RequestParam("longitude") double longitude,
                                  @RequestParam("latitude") double latitude){
        String wxId = String.valueOf(cacheService.getUserOpenId(token));
        List<OrderResult> results = orderService.getSubmitOrders(wxId);

        List<OrderResult> resultList = new ArrayList<>();
        if (!CollectionUtils.isEmpty(results)){
            for (OrderResult orderResult : results){
                //计算骑手与收货人直线距离
                double distance = DistanceUtils.getDistance(longitude, latitude,
                        orderResult.getReceiverLongitude(), orderResult.getReceiverLatitude());
                if (distance <= 3){
                    resultList.add(orderResult);
                }
            }
        }

        if (CollectionUtils.isEmpty(resultList)){
            return ResponseUtils.constructFailedResponse("暂时没有订单哦~");
        }

        return ResponseUtils.constructSucResponse(resultList);
    }

    /**
     * 根据wxId获取订单
     * @param token
     * @return
     */
    @GetMapping("getordersbywxid")
    public Object getOrdersByWxId(@RequestParam("token") String token){
        if (StringUtils.isEmpty(token)){
            return ResponseUtils.constructFailedResponse("参数错误");
        }

        String wxId = String.valueOf(cacheService.getUserOpenId(token));
        List<OrderResult> orderResults;
        orderResults = orderService.getOrdersByWxId(wxId);

        if (CollectionUtils.isEmpty(orderResults)){
            return ResponseUtils.constructFailedResponse("暂时没有订单哦~");
        }

        List<OrderResponse> orderResponseList = new ArrayList<>();
        for (OrderResult orderResult : orderResults){
            OrderResponse orderResponse = OrderConvertUtils.result2response(orderResult, wxId);
            orderResponseList.add(orderResponse);
        }
        return ResponseUtils.constructSucResponse(orderResponseList);
    }

    /**
     * 根据订单id获取订单详情
     * @param orderId
     * @return
     */
    @GetMapping("getorderbyorderid")
    public Object getOrderByOrderId(@RequestParam("orderId") long orderId, @RequestParam("token") String token){
        if (orderId <= 0 || StringUtils.isEmpty(token)){
            return ResponseUtils.constructFailedResponse("参数错误");
        }

        OrderResult orderResult = orderService.getOrderByOrderId(orderId);

        if (orderResult == null){
            return ResponseUtils.constructFailedResponse("未查询到订单");
        }
        String wxId = String.valueOf(cacheService.getUserOpenId(token));
        OrderResponse orderResponse = OrderConvertUtils.result2response(orderResult, wxId);

        return ResponseUtils.constructSucResponse(orderResponse);
    }

    /**
     * 构建提单参数
     * @param submitRequest
     * @return
     */
    private OrderSubmitParam constructSubmitParam(SubmitRequest submitRequest) {
        OrderSubmitParam orderSubmitParam = new OrderSubmitParam();

        GoodsParam goodsParam = new GoodsParam();
        goodsParam.setGoodsName(submitRequest.getGoodsName());
        goodsParam.setGoodsQuantity(submitRequest.getGoodsQuantity());
        goodsParam.setGoodsPrice(submitRequest.getGoodsPrice());

        ReceiverParam receiverParam = new ReceiverParam();
        receiverParam.setReceiverName(submitRequest.getReceiverName());
        receiverParam.setReceiverPhone(submitRequest.getReceiverPhone());
        receiverParam.setReceiverAddress(submitRequest.getReceiverAddress());
        receiverParam.setReceiverLongitude(submitRequest.getReceiverLongitude());
        receiverParam.setReceiverLatitude(submitRequest.getReceiverLatitude());

        DeliveryParam deliveryParam = new DeliveryParam();
        deliveryParam.setDeliveryPrice(submitRequest.getDeliveryPrice());

        String wxId = String.valueOf(cacheService.getUserOpenId(submitRequest.getToken()));
        orderSubmitParam.setWxId(wxId);
        orderSubmitParam.setGoodsParam(goodsParam);
        orderSubmitParam.setReceiverParam(receiverParam);
        orderSubmitParam.setDeliveryParam(deliveryParam);
        orderSubmitParam.setRemarks(submitRequest.getRemarks());

        return orderSubmitParam;
    }
}
