package com.hang.ssd.service.impl;

import com.hang.ssd.domain.entity.UserInfo;
import com.hang.ssd.domain.vo.result.UserResult;
import com.hang.ssd.dao.UserDao;
import com.hang.ssd.service.UserService;
import com.hang.ssd.utils.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 用户服务
 * @author yinhang
 */
@Slf4j
@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserDao userDao;

    /**
     * 用户注册
     *
     * @param userInfo 用户信息
     * @return
     */
    @Override
    public boolean userRegistered(UserInfo userInfo) {
        try {
            int res = userDao.createUser(userInfo);
            if (res <= 0){
                log.error("用户注册失败，userId = [{}]", userInfo.getWxId());
                return false;
            }
        }catch (Exception e){
            log.error("注册失败，数据库error！", e);
            return false;
        }
        return true;
    }

    /**
     * 用户查询
     *
     * @param wxId
     */
    @Override
    public UserResult getUserByWxId(String wxId) {
        if (StringUtils.isEmpty(wxId)){
            log.error("#UserServiceImpl.getUserByWxId#  参数不合法，wxId为空!");
            return null;
        }

        UserResult userResult;
        try {
            userResult = userDao.getUserByWxId(wxId);
        }catch (Exception e){
            log.error("#userDao.getUserByWxId# 获取用户失败！数据库error！wxId:[{}]", wxId, e);
            return null;
        }
        return userResult;
    }

    @Override
    public List<UserResult> getUsersByPhone(String phoneNum) {
        if (StringUtils.isEmpty(phoneNum)){
            log.error("#UserServiceImpl.getUsersByPhone#  参数不合法，phoneNum为空!");
            return null;
        }

        List<UserResult> userResults = new ArrayList<>();
        try {
            userResults = userDao.getUsersByPhone(phoneNum);
        }catch (Exception e){
            log.error("#userDao.getUsersByPhone# 获取用户失败！数据库error！phoneNum:[{}]", phoneNum, e);
        }
        return userResults;
    }

    /**
     * 更新绑定手机号
     * @param wxId
     * @param newBindPhone
     */
    @Override
    public boolean updateInfo(String wxId, String userName, String newBindPhone) {
        try {
            int res = userDao.updateInfo(wxId, userName, newBindPhone);
            if (res <= 0){
                log.error("#userDao.updateBindPhone# 更新绑定手机失败, wxId:[{}], newPhone:[{}]", wxId, newBindPhone);
                return false;
            }
        }catch (Exception e){
            log.error("#userDao.updateBindPhone# 更新绑定手机失败！数据库error！wxId:[{}], newPhone:[{}]", wxId, newBindPhone, e);
            return false;
        }
        return true;
    }
}
