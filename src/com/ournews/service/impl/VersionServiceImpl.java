package com.ournews.service.impl;

import com.ournews.dao.impl.VersionDaoImpl;
import com.ournews.service.VersionService;
import com.ournews.utils.Constant;
import com.ournews.utils.MD5Util;
import com.ournews.utils.MyUtils;
import com.ournews.utils.ResultUtil;

/**
 * Created by Administrator on 2017/5/3.
 */
public class VersionServiceImpl implements VersionService {
    @Override
    public String getVersionInfo(String time, String key) {
        if (MyUtils.isNull(time) || MyUtils.isNull(key)) {
            return ResultUtil.getErrorJSON(Constant.VALUES_ERROR).toString();
        } else if (!MyUtils.isTime(time)) {
            return ResultUtil.getErrorJSON(Constant.CONNECT_TIME_OUT).toString();
        } else if (!MD5Util.getMD5(time + Constant.KEY).equals(key)) {
            return ResultUtil.getErrorJSON(Constant.KEY_ERROR).toString();
        }
        return new VersionDaoImpl().getVersionInfo();
    }
}
