package com.dap.gen;

import org.springframework.boot.SpringApplication;
import org.springframework.cloud.client.SpringCloudApplication;
import com.dap.common.security.annotation.EnableCustomConfig;
import com.dap.common.security.annotation.EnableRyFeignClients;
import com.dap.common.swagger.annotation.EnableCustomSwagger2;

/**
 * 代码生成
 *
 * @author diaozhaojian
 */
@EnableCustomConfig
@EnableCustomSwagger2
@EnableRyFeignClients
@SpringCloudApplication
public class ManstroGenApplication
{
    public static void main(String[] args)
    {
        SpringApplication.run(ManstroGenApplication.class, args);
    }
}
