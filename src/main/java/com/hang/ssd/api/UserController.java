package com.hang.ssd.api;

import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.bean.WxMaJscode2SessionResult;
import com.alibaba.fastjson.JSONObject;
import com.hang.ssd.domain.entity.UserInfo;
import com.hang.ssd.domain.request.UserRequest;
import com.hang.ssd.domain.vo.result.UserResult;
import com.hang.ssd.service.CacheService;
import com.hang.ssd.service.UserService;
import com.hang.ssd.utils.StringUtils;
import com.hang.ssd.utils.ResponseUtils;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.error.WxErrorException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 用户服务api
 * @author yinhang
 */
@Slf4j
@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private WxMaService wxMaService;
    @Autowired
    private CacheService cacheService;

    /**
     * 用户完善信息
     * @return
     */
    @PostMapping("/fillinfo")
    public Object registered(@RequestBody @Validated UserRequest userRequest){
        if (StringUtils.isEmpty(userRequest.getUserName()) || StringUtils.isEmpty(userRequest.getPhone())){
            log.warn("#UserController.registered# 参数不合法");
            return ResponseUtils.constructFailedResponse("请确保姓名或电话填写正确");
        }

        String wxId = String.valueOf(cacheService.getUserOpenId(userRequest.getToken()));
        UserResult userResult = userService.getUserByWxId(wxId);
        if (userResult != null){
            ResponseUtils.constructFailedResponse("用户已存在");
        }

        UserInfo userInfo = constructUser(wxId, userRequest.getUserName(), userRequest.getPhone());

        boolean res = userService.userRegistered(userInfo);

        if (!res){
            ResponseUtils.constructFailedResponse("失败！数据库error!");
        }

        return ResponseUtils.constructSucResponse("信息已完善");
    }

    /**
     * 登录获取token
     * @param code
     * @return
     */
    @GetMapping("/login")
    public Object login(@RequestParam("code") String code){
        WxMaJscode2SessionResult result = null;
        try {
            result = wxMaService.getUserService().getSessionInfo(code);
        } catch (WxErrorException e) {
            log.error("login failed! wx error e{}", e.getMessage());
        }

        if (result == null){
            log.warn("session result is null! code{}", code);
            return ResponseUtils.constructFailedResponse("session result is null!");
        }

        String wxId = result.getOpenid();
        String token = cacheService.addTokenCache(wxId, result.getSessionKey());
        if (token == null){
            return ResponseUtils.constructFailedResponse("暂时无法登录");
        }
        UserResult userResult = userService.getUserByWxId(wxId);

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("token", token);
        jsonObject.put("userInfo", userResult);

        return ResponseUtils.constructSucResponse(jsonObject);
    }

    /**
     * 更新用户信息
     * @return
     */
    @PostMapping("/updateuserinfo")
    public Object updateUserInfo(@RequestBody @Validated UserRequest request){

        String wxId = String.valueOf(cacheService.getUserOpenId(request.getToken()));
        String phone = request.getPhone();
        String userName = request.getUserName();
        UserResult userResult = userService.getUserByWxId(wxId);
        boolean res;
        if (userResult.getBindPhone() == null){
            UserInfo userInfo = constructUser(wxId, userName, phone);
            res = userService.userRegistered(userInfo);
            if (!res){
                return ResponseUtils.constructFailedResponse("更新失败");
            }
            return ResponseUtils.constructSucResponse("成功");
        }

        if (phone.equals(userResult.getBindPhone()) && userName.equals(userResult.getUserName())){
            return ResponseUtils.constructFailedResponse("无需修改");
        }else if (phone.equals(userResult.getBindPhone()) && !userName.equals(userResult.getUserName())){
            res = userService.updateInfo(wxId, userName, null);
        }else if (!phone.equals(userResult.getBindPhone()) && userName.equals(userResult.getUserName())){
            res = userService.updateInfo(wxId, null, phone);
        }else {
            res = userService.updateInfo(wxId, userName, phone);
        }

        if (!res){
            return ResponseUtils.constructFailedResponse("更新失败");
        }

        return ResponseUtils.constructSucResponse("成功");
    }

    /**
     * 获取用户信息
     * @param token
     * @return
     */
    @GetMapping("getuser")
    public Object getUserInfoByToken(@RequestParam("token") String token){
        if (StringUtils.isEmpty(token)){
            return ResponseUtils.constructFailedResponse("token为空");
        }

        String wxId = String.valueOf(cacheService.getUserOpenId(token));
        UserResult userResult = userService.getUserByWxId(wxId);
        if (userResult== null){
            return ResponseUtils.constructFailedResponse("请完善信息");
        }

        return ResponseUtils.constructSucResponse(userResult);
    }


    private UserInfo constructUser(String wxId, String userName, String phone) {
        UserInfo userInfo = new UserInfo();
        userInfo.setWxId(wxId);
        userInfo.setUserName(userName);
        userInfo.setBindPhone(phone);
        return userInfo;
    }

}
