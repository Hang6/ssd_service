package com.hang.ssd.domain.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

/**
 * @author yinhang
 */

@Getter
@Setter
public class UserRequest extends BaseRequest{
    @NotNull(message = "姓名不能为空")
    private String userName;
    @NotNull(message = "电话不能为空")
    private String phone;
}
