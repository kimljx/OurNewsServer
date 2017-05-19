package com.ournews.action.user;

import com.ournews.action.base.BaseAction;
import com.ournews.service.impl.UserServiceImpl;

import java.io.IOException;

/**
 * Created by Misutesu on 2017/4/20 0020.
 */
public class UserCheckUpdateAction extends BaseAction {
    @Override
    public void action() throws IOException {
        //判断是否为POST请求
        if (isPost()) {
            String time = request.getParameter("time");
            String key = request.getParameter("key");
            //sendJSON这个方法是基类里面的
            //统一返回一个String字符串到客户端
            //服务器接收到参数后交给Service层处理
            //所有方法都是返回的String字符串
            sendJSON(new UserServiceImpl().checkUpdate(time, key));
        } else {
            sendJSON(getNoPostResponse());
        }
    }
}
