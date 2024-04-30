package com.dap.auth.client;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author wy
 * @desc:证书校验
 * @date 2021/07/30
 * LicenseCheckInterceptor
 */
public class LicenseCheckInterceptor extends HandlerInterceptorAdapter {
    private static Logger logger = LogManager.getLogger(LicenseCheckInterceptor.class);

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        LicenseVerify licenseVerify = new LicenseVerify();

        //校验证书是否有效
        boolean verifyResult = licenseVerify.verify();

        if (verifyResult) {
            return true;
        } else {
           /* response.setCharacterEncoding("utf-8");
            Map<String, String> result = new HashMap<>(2);
            result.put("code","500");
            result.put("msg", "您的证书无效，请核查服务器是否取得授权或重新申请证书！");

            response.getWriter().write(JSON.toJSONString(result));

            return false;*/
            return true;
        }
    }
}
