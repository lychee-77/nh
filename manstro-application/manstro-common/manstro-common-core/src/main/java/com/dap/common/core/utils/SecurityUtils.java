package com.dap.common.core.utils;

import javax.servlet.http.HttpServletRequest;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.logging.LogFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import com.dap.common.core.constant.CacheConstants;
import com.dap.common.core.text.Convert;

/**
 * 权限获取工具类
 *
 * @author Lychee
 */
public class SecurityUtils {

    private static Logger logger = LoggerFactory.getLogger(SecurityUtils.class);
    private static RedisTemplate redisTemplate = null;

    public static synchronized RedisTemplate getRedisTemplate() {
        if (redisTemplate == null) {
            redisTemplate = SpringUtils.getBean(StringRedisTemplate.class);
        }
        return redisTemplate;
    }


    /**
     * 获取登录用户信息
     * @return
     */
    public static JSONObject getUserInfo() {
        ValueOperations<String, String> operation = getRedisTemplate().opsForValue();
        String token = getToken(ServletUtils.getRequest());
        JSONObject obj = JSONObject.parseObject(operation.get(CacheConstants.LOGIN_TOKEN_KEY + token));
        return obj;
    }
    /**
     * 获取用户名
     */
    public static String getUsername() {
        JSONObject userInfo = getUserInfo();
        String userName = StringUtils.isNotNull(userInfo) && userInfo.containsKey(CacheConstants.DETAILS_USERNAME) ? userInfo.getString(CacheConstants.DETAILS_USERNAME) : "admin";
        return ServletUtils.urlDecode(userName);
    }

    /**
     * 获取用户ID
     */
    public static Long getUserId()
    {

        return Convert.toLong(ServletUtils.getRequest().getHeader(CacheConstants.DETAILS_USER_ID));
    }

    /**
     * 获取用户ID
     */
    public static String getLoginUserId() {

        JSONObject userInfo = getUserInfo();
        String userId = StringUtils.isNotNull(userInfo) && userInfo.containsKey(CacheConstants.LOGIN_USER_USER_ID) ? userInfo.getString(CacheConstants.LOGIN_USER_USER_ID) : null;
        return userId;

    }

    /**
     * 获取用户关联ID
     */
    public static String getLoginCustomizeId() {

        JSONObject userInfo = getUserInfo();
        String customizeId = StringUtils.isNotNull(userInfo) && userInfo.containsKey(CacheConstants.CUSTOMIZE_ID) ? userInfo.getString(CacheConstants.CUSTOMIZE_ID) : null;
        return customizeId;

    }





    /**
     * 获取用户表自定义id
     */
    public static Long getUserCustomizeId()
    {
        return Convert.toLong(ServletUtils.getRequest().getHeader(CacheConstants.DETAILS_CUSTOMIZE_ID));
    }
    /**
     * 获取请求token
     */
    public static String getToken()
    {
        return getToken(ServletUtils.getRequest());
    }

    /**
     * 根据request获取请求token
     */
    public static String getToken(HttpServletRequest request)
    {
        String token = ServletUtils.getRequest().getHeader(CacheConstants.HEADER);
        if (StringUtils.isNotEmpty(token) && token.startsWith(CacheConstants.TOKEN_PREFIX))
        {
            token = token.replace(CacheConstants.TOKEN_PREFIX, "");
        }
        return token;
    }

    /**
     * 是否为管理员
     *
     * @param userId 用户ID
     * @return 结果
     */
    public static boolean isAdmin(Long userId)
    {
        return userId != null && 1L == userId;
    }

    /**
     * 生成BCryptPasswordEncoder密码
     *
     * @param password 密码
     * @return 加密字符串
     */
    public static String encryptPassword(String password)
    {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        return passwordEncoder.encode(password);
    }

    /**
     * 判断密码是否相同
     *
     * @param rawPassword 真实密码
     * @param encodedPassword 加密后字符
     * @return 结果
     */
    public static boolean matchesPassword(String rawPassword, String encodedPassword)
    {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }
}
