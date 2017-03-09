package com.ournews.utils;

public class Constant {

    public static final String KEY = "a0f33a00f12b4a8eb6f626f03a1f140a";

    public static final int RANDOM_NUM = 20;

    public static final long CONNECT_OUT_TIME = 10000;

    public static final boolean IS_DEBUG = true;

    public static final String GET_METHOD_ERROR = "Error request method";

    public static final String POST_ERROR = "error";

    public static final String POST_SUCCESS = "success";

    // 服务器错误
    public static final String SERVER_ERROR = "100";
    // 所传参数有误
    public static final String VALUES_ERROR = "101";
    // 连接超时
    public static final String CONNECT_TIME_OUT = "102";
    // KEY错误
    public static final String KEY_ERROR = "103";
    // 用户名长度不正确
    public static final String LOGIN_NAME_LENGTH_ERROR = "1001";
    // 密码长度不正确
    public static final String PASSWORD_LENGTH_ERROR = "1002";
    // 用户名已存在
    public static final String LOGIN_NAME_IS_EXIST = "1003";
    // 用户名不存在
    public static final String LOGIN_NAME_NO_EXIST = "2001";
    // 密码错误
    public static final String PASSWORD_ERROR = "2002";
    // 修改用户信息错误或用户不存在
    public static final String CHANGE_INFO_ERROR = "3001";
    // token错误
    public static final String TOKEN_ERROR = "3002";
    // 用户不存在(ID)
    public static final String USER_NO_EXIST = "5001";
    // 新闻不存在(ID)
    public static final String NEW_NO_EXIST = "7001";
    // 新闻未上线
    public static final String NEW_NO_ONLINE = "8001";
    // 已有收藏
    public static final String HAS_COLLECTION = "9001";
    // 没收藏过
    public static final String NO_COLLECTION = "9002";

    // 新增新闻失败
    public static final String ADD_NEWS_ERROR = "10001";

    // 上传文件不是图片
    public static final String UPLOAD_NO_IMAGE = "100001";
    // 上传文件过大
    public static final String UPLOAD_FILE_TOO_BIG = "100002";
}
