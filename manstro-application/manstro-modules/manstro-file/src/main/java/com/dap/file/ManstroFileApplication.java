package com.dap.file;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import com.dap.common.swagger.annotation.EnableCustomSwagger2;

/**
 * 文件服务
 *
 * @author Lychee
 */
@EnableCustomSwagger2
@EnableDiscoveryClient
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class })
public class ManstroFileApplication
{
    public static void main(String[] args)
    {
        SpringApplication.run(ManstroFileApplication.class, args);
    }
}
