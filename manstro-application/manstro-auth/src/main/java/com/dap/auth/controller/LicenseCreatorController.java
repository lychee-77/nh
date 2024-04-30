package com.dap.auth.controller;

import com.dap.auth.client.LicenseManagerHolder;
import com.dap.auth.domain.*;
import de.schlichtherle.license.LicenseContent;
import de.schlichtherle.license.LicenseManager;
import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

/**
 * @author wy
 * @decs 用于生成证书文件
 * @date 2021/07/30
 */
@RestController
@RequestMapping("/license")
public class LicenseCreatorController {

    private static Logger logger = LogManager.getLogger(LicenseCreatorController.class);

    /**
     * 证书生成路径
     */
    @Value("${license.licensePath}")
    private String licensePath;

    /**
     * 获取服务器硬件信息
     */
    @RequestMapping(value = "/getServerInfos", produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public LicenseCheckModel getServerInfos(@RequestParam(value = "osName", required = false) String osName) {
        //操作系统类型
        if (StringUtils.isBlank(osName)) {
            osName = System.getProperty("os.name");
        }
        osName = osName.toLowerCase();

        AbstractServerInfos abstractServerInfos = null;

        //根据不同操作系统类型选择不同的数据获取方法
        if (osName.startsWith("windows")) {
            abstractServerInfos = new WindowsServerInfos();
        } else if (osName.startsWith("linux")) {
            abstractServerInfos = new LinuxServerInfos();
        } else {//其他服务器类型
            abstractServerInfos = new LinuxServerInfos();
        }

        return abstractServerInfos.getServerInfos();
    }

    /**
     * 生成证书
     */
    @RequestMapping(value = "/generateLicense", produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public Map<String, Object> generateLicense(@RequestBody(required = true) LicenseCreatorParam param) {
        Map<String, Object> resultMap = new HashMap<>(2);

        if (StringUtils.isBlank(param.getLicensePath())) {
            param.setLicensePath(licensePath);
        }

        LicenseCreator licenseCreator = new LicenseCreator(param);
        boolean result = licenseCreator.generateLicense();

        if (result) {
            resultMap.put("result", "ok");
            resultMap.put("msg", param);
        } else {
            resultMap.put("result", "error");
            resultMap.put("msg", "证书文件生成失败！");
        }

        return resultMap;
    }

    /***
     * @Author: wy
     * @Date: 2021/11/1
     * @Description: 获取证书生效日期
     * @return: java.util.Map<java.lang.String,java.lang.Object>
     **/
    @PostMapping("/getLicenseEffectiveData")
    public Map<String, Object> getLicenseEffectiveData() {
        Map<String, Object> returnMap = new HashMap<>();
        LicenseManager licenseManager = LicenseManagerHolder.getInstance(null);
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            LicenseContent licenseContent = licenseManager.verify();
            returnMap.put("result", "T");
            returnMap.put("message", "生效中");
            returnMap.put("issuedTime", format.format(licenseContent.getNotBefore()));
            returnMap.put("expiryTime", format.format(licenseContent.getNotAfter()));

        } catch (Exception e) {
            returnMap.put("result", "F");
            returnMap.put("message", "已失效");
        }
        return returnMap;
    }
}
