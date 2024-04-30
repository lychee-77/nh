package com.dap.job;

import org.springframework.boot.SpringApplication;
import org.springframework.cloud.client.SpringCloudApplication;
import com.dap.common.security.annotation.EnableCustomConfig;
import com.dap.common.security.annotation.EnableRyFeignClients;
import com.dap.common.swagger.annotation.EnableCustomSwagger2;

/**
 * 定时任务
 *
 * @author diaozhaojian
 */
@EnableCustomConfig
@EnableCustomSwagger2
@EnableRyFeignClients
@SpringCloudApplication
public class ManstroJobApplication
{
    public static void main(String[] args)
    {
        SpringApplication.run(ManstroJobApplication.class, args);
    }
}
