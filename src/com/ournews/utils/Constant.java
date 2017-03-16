package com.ournews.utils;

public class Constant {

    public static final String KEY = "a0f33a00f12b4a8eb6f626f03a1f140a";

    public static final int RANDOM_NUM = 20;

    public static final long CONNECT_OUT_TIME = 2 * 60 * 1000;

    public static boolean IS_DEBUG = true;

    // 服务器错误
    public static final int SERVER_ERROR = 100;
    // 所传参数有误
    public static final int VALUES_ERROR = 101;
    // 连接超时
    public static final int CONNECT_TIME_OUT = 102;
    // KEY错误
    public static final int KEY_ERROR = 103;
    // 用户名长度不正确
    public static final int LOGIN_NAME_LENGTH_ERROR = 1001;
    // 密码长度不正确
    public static final int PASSWORD_LENGTH_ERROR = 1002;
    // 用户名已存在
    public static final int LOGIN_NAME_IS_EXIST = 1003;
    // 用户名不存在
    public static final int LOGIN_NAME_NO_EXIST = 2001;
    // 密码错误
    public static final int PASSWORD_ERROR = 2002;
    // 修改用户信息错误或用户不存在
    public static final int CHANGE_INFO_ERROR = 3001;
    // token错误
    public static final int TOKEN_ERROR = 3002;
    // 用户被封禁
    public static final int USER_NO_ONLINE = 3003;
    // 用户不存在(ID)
    public static final int USER_NO_EXIST = 5001;
    // 查找用户不存在(ID)
    public static final int OTHER_USER_NO_EXIST = 5002;
    // 新闻不存在(ID)
    public static final int NEW_NO_EXIST = 7001;
    // 新闻未上线
    public static final int NEW_NO_ONLINE = 8001;
    // 已有收藏
    public static final int HAS_COLLECTION = 9001;
    // 没收藏过
    public static final int NO_COLLECTION = 9002;
    // 评论不存在
    public static final int NO_COMMENT = 9003;
    // 评论被封禁
    public static final int COMMENT_NO_ONLINE = 9004;
    // 此评论已点赞过
    public static final int HAS_LIKE_COMMENT = 9005;
    // 此评论没有点赞过
    public static final int NO_LIKE_COMMENT = 9006;

    // 上传文件不是图片
    public static final int UPLOAD_NO_IMAGE = 100001;
    // 上传文件过大
    public static final int UPLOAD_FILE_TOO_BIG = 100002;


    // 新增新闻失败
    public static final int ADD_NEWS_ERROR = 10001;
    // 连接激光服务器失败
    public static final int JIGUANG_INTERNET_ERROR = 10002;
}
