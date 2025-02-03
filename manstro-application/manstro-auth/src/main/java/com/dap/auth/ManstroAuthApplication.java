package com.dap.auth;

import org.springframework.boot.SpringApplication;
import org.springframework.cloud.client.SpringCloudApplication;
import com.dap.common.security.annotation.EnableRyFeignClients;

/**
 * 认证授权中心
 *
 * @author Lychee
 */
@EnableRyFeignClients
@SpringCloudApplication
public class ManstroAuthApplication
{
    public static void main(String[] args)
    {
        SpringApplication.run(ManstroAuthApplication.class, args);
    }
}
