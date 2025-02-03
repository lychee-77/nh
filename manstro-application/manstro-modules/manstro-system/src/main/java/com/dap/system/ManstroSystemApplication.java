package com.dap.system;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.AutoConfigurationPackage;
import org.springframework.cloud.client.SpringCloudApplication;
import com.dap.common.security.annotation.EnableCustomConfig;
import com.dap.common.security.annotation.EnableRyFeignClients;
import com.dap.common.swagger.annotation.EnableCustomSwagger2;

/**
 * 系统模块
 *
 * @author Lychee
 */
@EnableCustomConfig
@EnableCustomSwagger2
@EnableRyFeignClients
@SpringCloudApplication
public class ManstroSystemApplication
{
    public static void main(String[] args)
    {
        SpringApplication.run(ManstroSystemApplication.class, args);
    }
}
