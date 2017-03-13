package com.ournews.service.impl;

import com.ournews.dao.impl.PushDaoImpl;
import com.ournews.service.PushService;
import com.ournews.utils.Constant;
import com.ournews.utils.MyUtils;
import com.ournews.utils.ResultUtil;

/**
 * Created by Administrator on 2017/3/11.
 */
public class PushServiceImpl implements PushService {
    @Override
    public String pushNewToAll(String nid, String time) {
        if (!MyUtils.isNumber(nid) || !MyUtils.isTime(time)) {
            return ResultUtil.getErrorJSON(Constant.VALUES_ERROR).toString();
        } else if (System.currentTimeMillis() - Long.valueOf(time) > 10 * 1000) {
            return ResultUtil.getErrorJSON(Constant.CONNECT_TIME_OUT).toString();
        }
        return new PushDaoImpl().pushNewToAll(nid);
    }
}
