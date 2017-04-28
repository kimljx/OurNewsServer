package com.ournews.utils;

public class Constant {

    public static final String KEY = "a0f33a00f12b4a8eb6f626f03a1f140a";

    public static final int RANDOM_NUM = 20;

    public static final long CONNECT_OUT_TIME = 2 * 60 * 1000;

    public static boolean IS_DEBUG = false;

    // 服务器错误
    public static final int SERVER_ERROR = 100;
    // 所传参数有误
    public static final int VALUES_ERROR = 101;
    // 连接超时
    public static final int CONNECT_TIME_OUT = 102;
    // KEY错误
    public static final int KEY_ERROR = 103;
    // 检测更新失败
    public static final int CHECK_UPDATE_ERROR = 104;
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

    // 没找到上传的文件
    public static final int NO_FIND_UPLOAD_FILE = 100000;
    // 上传文件不是图片
    public static final int UPLOAD_NO_IMAGE = 100001;
    // 上传文件过大
    public static final int UPLOAD_FILE_TOO_BIG = 100002;

    // 手机号格式错误
    public static final int PHONE_NUMBER_ERROR = 10001;
    // 获取验证码时间过短
    public static final int GET_CODE_TIME_TOO_DISTANCE = 10002;
    // 请先获取验证码
    public static final int PLEASE_GET_CODE_FIRST = 10003;
    // 验证码过期
    public static final int MESSAGE_CODE_TIME_OUT = 10004;
    // 验证码错误
    public static final int MESSAGE_CODE_ERROR = 10005;
    // TOKEN过期
    public static final int TOKEN_TIME_OUT = 10006;
    // 新增新闻失败
    public static final int ADD_NEWS_ERROR = 10010;
    // 连接激光服务器失败
    public static final int JIGUANG_INTERNET_ERROR = 10020;
}
