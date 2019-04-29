package com.hang.ssd;

import com.hang.ssd.domain.entity.UserInfo;
import com.hang.ssd.domain.param.DeliveryParam;
import com.hang.ssd.domain.param.GoodsParam;
import com.hang.ssd.domain.param.OrderSubmitParam;
import com.hang.ssd.domain.param.ReceiverParam;
import com.hang.ssd.domain.vo.result.OrderServiceResVo;
import com.hang.ssd.service.CacheService;
import com.hang.ssd.service.OrderService;
import com.hang.ssd.service.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SsdApplicationTests {
    @Autowired
    OrderService orderService;
    @Autowired
    UserService userService;
    @Autowired
    CacheService cacheService;

    @Test
    public void contextLoads() {
    }

    @Test
    public void createUser(){
        UserInfo userInfo = new UserInfo();
        userInfo.setWxId("12343232");
        userInfo.setBindPhone("12312312312");
        userInfo.setUserName("尹");
        userService.userRegistered(userInfo);
        System.out.println("end");
    }

    @Test
    public void createOrder(){
        OrderSubmitParam submitParam = new OrderSubmitParam();

        GoodsParam goodsParam = new GoodsParam();
        goodsParam.setGoodsName("笔");
        goodsParam.setGoodsQuantity("2");
        goodsParam.setGoodsPrice(3);

        ReceiverParam receiverParam = new ReceiverParam();
        receiverParam.setReceiverName("尹");
        receiverParam.setReceiverPhone("12312312312");
        receiverParam.setReceiverAddress("重庆邮电大学28栋");
        receiverParam.setReceiverLongitude(12345678);
        receiverParam.setReceiverLatitude(12345678);

        DeliveryParam deliveryParam = new DeliveryParam();
        deliveryParam.setDeliveryPrice(5.00);

        submitParam.setWxId("1234323211");
        submitParam.setGoodsParam(goodsParam);
        submitParam.setReceiverParam(receiverParam);
        submitParam.setDeliveryParam(deliveryParam);
        submitParam.setRemarks("");

        OrderServiceResVo resVo = orderService.createOrder(submitParam);
        System.out.println(resVo.toString());
        System.out.println("end");
    }

    @Test
    public void insertCache(){
        String token = cacheService.addTokenCache("123", "addsfsdgsdg");
        System.out.println(token);
    }

    @Test
    public void getCache(){
        Object map= cacheService.getToken("wphBwpftDCdEZkL4");

    }
}
