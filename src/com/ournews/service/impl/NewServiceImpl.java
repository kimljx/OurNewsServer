package com.ournews.service.impl;

import com.ournews.dao.impl.NewDaoImpl;
import com.ournews.dao.impl.UserDaoImpl;
import com.ournews.service.NewService;
import com.ournews.utils.Constant;
import com.ournews.utils.MyUtils;
import com.ournews.utils.ResultUtil;

/**
 * Created by Misutesu on 2017/3/5 0005.
 */
public class NewServiceImpl implements NewService {

    @Override
    public String addNew(String title, String cover, String abstractContent, String content, String type) {
        if (MyUtils.isNull(title) || MyUtils.isNull(cover) || MyUtils.isNull(abstractContent)
                || MyUtils.isNull(content) || !MyUtils.isNumber(type, 0, 6)) {
            return ResultUtil.getErrorJSON(Constant.VALUES_ERROR).toString();
        } else {
            return new NewDaoImpl().addNews(title, cover, abstractContent, content, type);
        }
    }

    @Override
    public String getHomeNew(String type) {
        if (MyUtils.isNull(type)) {
            return new NewDaoImpl().getHomeNews(null);
        } else {
            if (!MyUtils.isNumber(type, 1, 6)) {
                return ResultUtil.getErrorJSON(Constant.VALUES_ERROR).toString();
            } else {
                return new NewDaoImpl().getHomeNews(type);
            }
        }
    }

    @Override
    public String getTypeNew(String type, String page, String size, String sort) {
        if (!MyUtils.isNumber(type, 1, 5) || !MyUtils.isNumber(page) && !MyUtils.isNumber(size)) {
            return ResultUtil.getErrorJSON(Constant.VALUES_ERROR).toString();
        }
        if (!MyUtils.isNumber(sort, 1, 2))
            sort = "1";
        if (Integer.valueOf(page) < 1)
            page = "1";
        if (Integer.valueOf(size) > 20)
            size = "20";
        return new NewDaoImpl().getNewList(type, page, size, sort);
    }

    @Override
    public String getSearchNew(String name, String page, String size, String sort) {
        if (MyUtils.isNull(name) || !MyUtils.isNumber(page) && !MyUtils.isNumber(size)) {
            return ResultUtil.getErrorJSON(Constant.VALUES_ERROR).toString();
        }
        if (!MyUtils.isNumber(sort, 1, 2))
            sort = "1";
        if (Integer.valueOf(page) < 1)
            page = "1";
        if (Integer.valueOf(size) > 20)
            size = "20";
        return new NewDaoImpl().getSearchNew(name, page, size, sort);
    }

    @Override
    public String getContent(String uid, String nid) {
        if (MyUtils.isNull(uid)) {
            if (!MyUtils.isNumber(nid)) {
                return ResultUtil.getErrorJSON(Constant.VALUES_ERROR).toString();
            }
            return new NewDaoImpl().getNewContent(nid);
        } else {
            if (!MyUtils.isNumber(uid) || !MyUtils.isNumber(nid)) {
                return ResultUtil.getErrorJSON(Constant.VALUES_ERROR).toString();
            }
            return new NewDaoImpl().getNewContentUser(uid, nid);
        }
    }

    @Override
    public String collectionNew(String nid, String uid, String token, String type) {
        if (!MyUtils.isNumber(nid) || !MyUtils.isNumber(uid) || MyUtils.isNull(token) || !MyUtils.isNumber(type, 0, 1)) {
            return ResultUtil.getErrorJSON(Constant.VALUES_ERROR).toString();
        } else {
            int isTrueToken = new UserDaoImpl().tokenIsTrue(uid, token);
            if (isTrueToken == 1) {
                return ResultUtil.getErrorJSON(Constant.SERVER_ERROR).toString();
            } else if (isTrueToken == 2) {
                return ResultUtil.getErrorJSON(Constant.USER_NO_EXIST).toString();
            } else if (isTrueToken == 3) {
                return ResultUtil.getErrorJSON(Constant.USER_NO_ONLINE).toString();
            } else if (isTrueToken == 4) {
                return ResultUtil.getErrorJSON(Constant.TOKEN_ERROR).toString();
            }
            return new NewDaoImpl().collectNew(nid, uid, type);
        }
    }

    @Override
    public String getCollections(String id, String token, String uid, String page, String size, String sort) {
        if (!MyUtils.isNumber(uid) || MyUtils.isNull(token) || !MyUtils.isNumber(uid) || !MyUtils.isNumber(page) && !MyUtils.isNumber(size)) {
            return ResultUtil.getErrorJSON(Constant.VALUES_ERROR).toString();
        }
        int isTrueToken = new UserDaoImpl().tokenIsTrue(id, token);
        if (isTrueToken == 1) {
            return ResultUtil.getErrorJSON(Constant.SERVER_ERROR).toString();
        } else if (isTrueToken == 2) {
            return ResultUtil.getErrorJSON(Constant.USER_NO_EXIST).toString();
        } else if (isTrueToken == 3) {
            return ResultUtil.getErrorJSON(Constant.USER_NO_ONLINE).toString();
        } else if (isTrueToken == 4) {
            return ResultUtil.getErrorJSON(Constant.TOKEN_ERROR).toString();
        }
        if (!MyUtils.isNumber(sort, 1, 2))
            sort = "1";
        if (Integer.valueOf(page) < 1)
            page = "1";
        if (Integer.valueOf(size) > 20)
            size = "20";
        return new NewDaoImpl().getCollections(id, uid, page, size, sort);
    }

    @Override
    public String getHistory(String id, String token, String uid, String page, String size, String sort) {
        if (!MyUtils.isNumber(uid) || MyUtils.isNull(token) || !MyUtils.isNumber(uid) || !MyUtils.isNumber(page) && !MyUtils.isNumber(size)) {
            return ResultUtil.getErrorJSON(Constant.VALUES_ERROR).toString();
        }
        int isTrueToken = new UserDaoImpl().tokenIsTrue(id, token);
        if (isTrueToken == 1) {
            return ResultUtil.getErrorJSON(Constant.SERVER_ERROR).toString();
        } else if (isTrueToken == 2) {
            return ResultUtil.getErrorJSON(Constant.USER_NO_EXIST).toString();
        } else if (isTrueToken == 3) {
            return ResultUtil.getErrorJSON(Constant.USER_NO_ONLINE).toString();
        } else if (isTrueToken == 4) {
            return ResultUtil.getErrorJSON(Constant.TOKEN_ERROR).toString();
        }
        if (!MyUtils.isNumber(sort, 1, 2))
            sort = "1";
        if (Integer.valueOf(page) < 1)
            page = "1";
        if (Integer.valueOf(size) > 20)
            size = "20";
        return new NewDaoImpl().getHistory(id, uid, page, size, sort);
    }
}
