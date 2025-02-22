package com.dap.common.core.constant;

/**
 * 缓存的key 常量
 *
 * @author Lychee
 */
public class CacheConstants
{
    /**
     * 令牌自定义标识
     */
    public static final String HEADER = "Authorization";

    /**
     * 令牌前缀
     */
    public static final String TOKEN_PREFIX = "Bearer ";

    /**
     * 权限缓存前缀
     */
    public final static String LOGIN_TOKEN_KEY = "login_tokens:";

    /**
     * 用户ID字段
     */
    public static final String DETAILS_USER_ID = "user_id";
    /**
     * redis 用户ID字段
     */
    public static final String LOGIN_USER_USER_ID = "userid";
    /**
    * 用户表自定义id
    * */
    public static final String DETAILS_CUSTOMIZE_ID = "id";

    /**
     * 用户名字段
     */
    public static final String DETAILS_USERNAME = "username";

    /**
     * 授权信息字段
     */
    public static final String AUTHORIZATION_HEADER = "authorization";
    /**
     * 用户关联ID
     */
    public static final String CUSTOMIZE_ID = "customizeId";
}
