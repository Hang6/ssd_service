package com.hang.ssd.domain.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

/**
 *
 */

@Getter
@Setter
public class BaseRequest {
    /**
     * token
     */
    @NotNull(message = "token不能为空")
    private String token;
}
