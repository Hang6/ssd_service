package com.hang.ssd.domain.vo.result;

import lombok.Getter;
import lombok.Setter;

/**
 * 用户信息
 * @author yinhang
 */

@Setter
@Getter
public class UserResult {
    private int id;
    private String wxId;
    private String userName;
    private String bindPhone;
}
