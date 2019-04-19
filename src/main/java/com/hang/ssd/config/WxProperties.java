package com.hang.ssd.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * 小程序配置
 * @author yinhang
 */
@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "wx")
public class WxProperties {
    private String appId;
    private String secret;
}
