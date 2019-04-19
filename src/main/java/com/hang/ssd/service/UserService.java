package com.hang.ssd.service;

import com.hang.ssd.domain.entity.UserInfo;
import com.hang.ssd.domain.vo.result.UserResult;

import java.util.List;

/**
 * 用户业务
 * @author yinhang
 */
public interface UserService {
    /**
     * 用户注册
     * @param userInfo 用户信息
     * @return
     */
    boolean userRegistered(UserInfo userInfo);

    /**
     * 用户查询
     */
    UserResult getUserByWxId(String wxId);

    List<UserResult> getUsersByPhone(String phoneNum);

    /**
     * 更新绑定手机号
     */
    boolean updateInfo(String wxId, String userName, String newBindPhone);
}
