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
    public String addNew(String mid, String token, String title, String cover, String abstractContent, String content, String type, String push) {
        if (!MyUtils.isNumber(mid) || MyUtils.isNull(token) || MyUtils.isNull(title)
                || MyUtils.isNull(cover) || MyUtils.isNull(abstractContent)
                || MyUtils.isNull(content) || !MyUtils.isNumber(type, 0, 6)
                || !MyUtils.isNumber(push, 0, 1)) {
            return ResultUtil.getErrorJSON(Constant.VALUES_ERROR).toString();
        }
        int isTrueToken = new UserDaoImpl().managerTokenIsTrue(mid, token);
        if (isTrueToken == 1) {
            return ResultUtil.getErrorJSON(Constant.SERVER_ERROR).toString();
        } else if (isTrueToken == 2) {
            return ResultUtil.getErrorJSON(Constant.USER_NO_EXIST).toString();
        } else if (isTrueToken == 3) {
            return ResultUtil.getErrorJSON(Constant.TOKEN_ERROR).toString();
        } else if (isTrueToken == 4) {
            return ResultUtil.getErrorJSON(Constant.TOKEN_TIME_OUT).toString();
        }
        return new NewDaoImpl().addNews(mid, title, cover, abstractContent, content, type, push);
    }

    @Override
    public String getOwnNew(String mid, String token, String page, String size, String sort) {
        if (!MyUtils.isNumber(mid) || MyUtils.isNull(token) || !MyUtils.isNumber(page) && !MyUtils.isNumber(size)) {
            return ResultUtil.getErrorJSON(Constant.VALUES_ERROR).toString();
        }
        int isTrueToken = new UserDaoImpl().managerTokenIsTrue(mid, token);
        if (isTrueToken == 1) {
            return ResultUtil.getErrorJSON(Constant.SERVER_ERROR).toString();
        } else if (isTrueToken == 2) {
            return ResultUtil.getErrorJSON(Constant.USER_NO_EXIST).toString();
        } else if (isTrueToken == 3) {
            return ResultUtil.getErrorJSON(Constant.TOKEN_ERROR).toString();
        } else if (isTrueToken == 4) {
            return ResultUtil.getErrorJSON(Constant.TOKEN_TIME_OUT).toString();
        }
        if (!MyUtils.isNumber(sort, 1, 2))
            sort = "1";
        if (Integer.valueOf(page) < 1)
            page = "1";
        if (Integer.valueOf(size) > 20)
            size = "20";
        return new NewDaoImpl().getOwnNew(mid, page, size, sort);
    }

    @Override
    public String changeNewState(String id, String token, String nid, String state) {
        if (!MyUtils.isNumber(id) || MyUtils.isNull(token) || !MyUtils.isNumber(nid)
                || !MyUtils.isNumber(state, 0, 1)) {
            return ResultUtil.getErrorJSON(Constant.VALUES_ERROR).toString();
        }

        int isTrueToken = new UserDaoImpl().managerTokenIsTrue(id, token);
        if (isTrueToken == 1) {
            return ResultUtil.getErrorJSON(Constant.SERVER_ERROR).toString();
        } else if (isTrueToken == 2) {
            return ResultUtil.getErrorJSON(Constant.USER_NO_EXIST).toString();
        } else if (isTrueToken == 3) {
            return ResultUtil.getErrorJSON(Constant.TOKEN_ERROR).toString();
        } else if (isTrueToken == 4) {
            return ResultUtil.getErrorJSON(Constant.TOKEN_TIME_OUT).toString();
        }

        return new NewDaoImpl().changeNewState(id, nid, state);
    }

    @Override
    public String getHomeNew(String type) {
        if (MyUtils.isNull(type)) {
            return new NewDaoImpl().getHomeNews(null);
        }
        if (!MyUtils.isNumber(type, 1, 6)) {
            return ResultUtil.getErrorJSON(Constant.VALUES_ERROR).toString();
        }
        return new NewDaoImpl().getHomeNews(type);
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
    public String getContent(String uid, String nid, int type) {
        if (MyUtils.isNull(uid)) {
            if (!MyUtils.isNumber(nid)) {
                return ResultUtil.getErrorJSON(Constant.VALUES_ERROR).toString();
            }
            if (type == 1) {
                return new NewDaoImpl().getNewContent(nid);
            } else {
                return new NewDaoImpl().getNewContentForWeb(nid);
            }
        } else {
            if (!MyUtils.isNumber(uid) || !MyUtils.isNumber(nid)) {
                return ResultUtil.getErrorJSON(Constant.VALUES_ERROR).toString();
            }
            if (type == 1) {
                return new NewDaoImpl().getNewContentUser(uid, nid);
            } else {
                return new NewDaoImpl().getNewContentUserForWeb(uid, nid);
            }
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
        if (!MyUtils.isNumber(id) || MyUtils.isNull(token) || !MyUtils.isNumber(uid) || !MyUtils.isNumber(page) && !MyUtils.isNumber(size)) {
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
        if (!MyUtils.isNumber(id) || MyUtils.isNull(token) || !MyUtils.isNumber(uid) || !MyUtils.isNumber(page) && !MyUtils.isNumber(size)) {
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
