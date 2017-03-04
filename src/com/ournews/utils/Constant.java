package com.ournews.utils;

public class Constant {

    public static final String KEY = "a0f33a00f12b4a8eb6f626f03a1f140a";

    public static final int RANDOM_NUM = 20;

    public static final long CONNECT_OUT_TIME = 10000;

    public static final boolean IS_DEBUG = true;

    public static final String GET_METHOD_ERROR = "Error request method";

    public static final String POST_ERROR = "error";

    public static final String POST_SUCCESS = "success";

    // 所传参数有误
    public static final String VALUES_ERROR = "101";
    // 连接超时
    public static final String CONNECT_TIME_OUT = "102";
    // 用户名或密码长度不正确
    public static final String NAME_OR_PASSWORD_LENGTH_ERROR = "1001";
    // 用户名已存在
    public static final String LOGIN_NAME_IS_EXIST = "1002";
    // 用户名不存在或密码错误
    public static final String LOGIN_NAME_NO_EXIST_OR_PASSWORD_ERROR = "2001";
    // 修改用户信息错误或用户不存在
    public static final String CHANGE_INFO_ERROR = "3001";
    // 服务器错误
    public static final String SERVER_ERROR = "4001";
    // 用户不存在(ID)
    public static final String USER_NO_HAVE = "5001";
    // 用户或新闻不存在(ID)
    public static final String USER_OR_NEW_NO_HAVE = "6001";
    // 新闻不存在(ID)
    public static final String NEW_NO_HAVE = "7001";

    // 新增新闻失败
    public static final String ADD_NEWS_ERROR = "10001";

    // 上传文件不是图片
    public static final String UPLOAD_NO_IMAGE = "100001";
    // 上传文件过大
    public static final String UPLOAD_FILE_TOO_BIG = "100002";
}
