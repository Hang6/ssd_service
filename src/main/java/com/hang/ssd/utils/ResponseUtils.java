package com.hang.ssd.utils;

import com.alibaba.fastjson.JSONObject;
import com.hang.ssd.domain.constant.HttpResponseCode;

/**
 * 后台运行结果模板
 * @author yinhang
 */
public class ResponseUtils {
    private static JSONObject constructResponse(int result, Object data){
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("status", result);
        jsonObject.put("data", data);
        return jsonObject;
    }

    public static JSONObject constructSucResponse(){
        return constructResponse(HttpResponseCode.SUCCESS.getValue(), null);
    }

    public static JSONObject constructSucResponse(Object data){
        return constructResponse(HttpResponseCode.SUCCESS.getValue(), data);
    }

    public static JSONObject constructFailedResponse(){
        return constructResponse(HttpResponseCode.FAILED.getValue(), null);
    }

    public static JSONObject constructFailedResponse(Object data){
        return constructResponse(HttpResponseCode.FAILED.getValue(), data);
    }
}
