package com.dap.modules.monitor;

import de.codecentric.boot.admin.server.config.EnableAdminServer;
import org.springframework.boot.SpringApplication;
import org.springframework.cloud.client.SpringCloudApplication;

/**
 * 监控中心
 *
 * @author diaozhaojian
 */
@EnableAdminServer
@SpringCloudApplication
public class ManstroMonitorApplication
{
    public static void main(String[] args)
    {
        SpringApplication.run(ManstroMonitorApplication.class, args);
    }
}
