package com.hang.ssd.domain.vo.result;

import lombok.Data;

/**
 * 用户微信相关信息
 * @author yinhang
 */
@Data
public class WxResult {
    private String openid;
    private String session_key;
    private String unionid;
    private int errcode;
    private String errmsg;
}
