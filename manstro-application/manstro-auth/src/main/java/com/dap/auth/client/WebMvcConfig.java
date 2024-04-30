package com.dap.auth.client;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author wy
 * @desc 添加拦截器，将登陆方法进行拦截
 * @date 2021/07/30
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
    /**
     * 添加拦截器
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //如果改成/**  会导致无法通过接口获取服务器信息及生成证书
        // 可以将过滤路径改为**后,将获取接口单独过滤
        registry.addInterceptor(new LicenseCheckInterceptor()).addPathPatterns("/login");
    }
}
