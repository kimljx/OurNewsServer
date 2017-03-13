package com.ournews.service.impl;

import com.ournews.dao.impl.CommentDaoImpl;
import com.ournews.dao.impl.UserDaoImpl;
import com.ournews.service.CommentService;
import com.ournews.utils.Constant;
import com.ournews.utils.MD5Util;
import com.ournews.utils.MyUtils;
import com.ournews.utils.ResultUtil;

/**
 * Created by Misutesu on 2017/3/11 0011.
 */
public class CommentServiceImpl implements CommentService {
    @Override
    public String writeComment(String uid, String nid, String content, String time, String token, String key) {
        if (!MyUtils.isNumber(uid) || !MyUtils.isNumber(nid) || !MyUtils.isVarchar(content)
                || !MyUtils.isTime(time) || MyUtils.isNull(token) || MyUtils.isNull(key)) {
            return ResultUtil.getErrorJSON(Constant.VALUES_ERROR).toString();
        } else if (MyUtils.isConnectTimeOut(Long.valueOf(time))) {
            return ResultUtil.getErrorJSON(Constant.CONNECT_TIME_OUT).toString();
        } else {
            int isTrueToken = new UserDaoImpl().tokenIsTrue(uid, token);
            if (isTrueToken == 1) {
                return ResultUtil.getErrorJSON(Constant.SERVER_ERROR).toString();
            } else if (isTrueToken == 2) {
                return ResultUtil.getErrorJSON(Constant.USER_NO_EXIST).toString();
            } else if (isTrueToken == 3) {
                return ResultUtil.getErrorJSON(Constant.TOKEN_ERROR).toString();
            } else if (isTrueToken == 4) {
                return ResultUtil.getErrorJSON(Constant.USER_NO_ONLINE).toString();
            }
            if (!key.equals(MD5Util.getMD5(Constant.KEY + token + time))) {
                return ResultUtil.getErrorJSON(Constant.KEY_ERROR).toString();
            }
            return new CommentDaoImpl().writeComment(uid, nid, content);
        }
    }

    @Override
    public String getComment(String uid, String nid, String page, String size, String sort) {
        if (!MyUtils.isNumber(nid) || !MyUtils.isNumber(page) && !MyUtils.isNumber(size)
                || (!MyUtils.isNull(uid) && !MyUtils.isNumber(uid))) {
            return ResultUtil.getErrorJSON(Constant.VALUES_ERROR).toString();
        }
        if (!MyUtils.isNumber(sort, 1, 2))
            sort = "1";
        if (Integer.valueOf(page) < 1)
            page = "1";
        if (Integer.valueOf(size) > 20)
            size = "20";
        if (MyUtils.isNull(uid))
            return new CommentDaoImpl().getComment(nid, page, size, sort);
        return new CommentDaoImpl().getCommentUseUid(uid, nid, page, size, sort);
    }

    @Override
    public String writeChildComment(String uid, String cid, String content, String time, String token, String key) {
        if (!MyUtils.isNumber(uid) || !MyUtils.isNumber(cid) || !MyUtils.isVarchar(content)
                || !MyUtils.isTime(time) || MyUtils.isNull(token) || MyUtils.isNull(key)) {
            return ResultUtil.getErrorJSON(Constant.VALUES_ERROR).toString();
        } else if (MyUtils.isConnectTimeOut(Long.valueOf(time))) {
            return ResultUtil.getErrorJSON(Constant.CONNECT_TIME_OUT).toString();
        } else {
            int isTrueToken = new UserDaoImpl().tokenIsTrue(uid, token);
            if (isTrueToken == 1) {
                return ResultUtil.getErrorJSON(Constant.SERVER_ERROR).toString();
            } else if (isTrueToken == 2) {
                return ResultUtil.getErrorJSON(Constant.USER_NO_EXIST).toString();
            } else if (isTrueToken == 3) {
                return ResultUtil.getErrorJSON(Constant.TOKEN_ERROR).toString();
            } else if (isTrueToken == 4) {
                return ResultUtil.getErrorJSON(Constant.USER_NO_ONLINE).toString();
            }
            if (!key.equals(MD5Util.getMD5(Constant.KEY + token + time))) {
                return ResultUtil.getErrorJSON(Constant.KEY_ERROR).toString();
            }
            return new CommentDaoImpl().writeChildComment(uid, cid, content);
        }
    }

    @Override
    public String lickComment(String cid, String uid, String token, String type) {
        if (!MyUtils.isNumber(cid) || !MyUtils.isNumber(uid) || MyUtils.isNull(token)
                || !MyUtils.isNumber(type, 0, 1)) {
            return ResultUtil.getErrorJSON(Constant.VALUES_ERROR).toString();
        }
        int isTrueToken = new UserDaoImpl().tokenIsTrue(uid, token);
        if (isTrueToken == 1) {
            return ResultUtil.getErrorJSON(Constant.SERVER_ERROR).toString();
        } else if (isTrueToken == 2) {
            return ResultUtil.getErrorJSON(Constant.USER_NO_EXIST).toString();
        } else if (isTrueToken == 3) {
            return ResultUtil.getErrorJSON(Constant.TOKEN_ERROR).toString();
        } else if (isTrueToken == 4) {
            return ResultUtil.getErrorJSON(Constant.USER_NO_ONLINE).toString();
        }
        return new CommentDaoImpl().likeComment(cid, uid, type);
    }
}
