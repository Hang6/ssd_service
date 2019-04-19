package com.hang.ssd.dao;

import com.hang.ssd.domain.entity.UserInfo;
import com.hang.ssd.domain.vo.result.UserResult;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author yinhang
 */

@Mapper
public interface UserDao {
    /**
     * 用户注册
     * @param userInfo
     * @return
     */
    int createUser(UserInfo userInfo);

    /**
     * 根据id查询用户
     * @param wxId
     * @return
     */
    UserResult getUserByWxId(@Param("wxId") String wxId);

    /**
     * 根据绑定电话查询用户
     * @param phone
     * @return
     */
    List<UserResult> getUsersByPhone(@Param("phone") String phone);

    /**
     * 修改绑定手机号
     * @param wxId
     * @param userName
     * @param newPhone
     * @return
     */
    int updateInfo(@Param("wxId") String wxId, @Param("userName") String userName, @Param("newPhone") String newPhone);
}
