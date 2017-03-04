package com.ournews.action.user;

import com.ournews.action.base.BaseAction;
import com.ournews.bean.Comment;
import com.ournews.bean.User;
import com.ournews.dao.impl.NewDaoImpl;
import com.ournews.utils.Constant;
import com.ournews.utils.MyUtils;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import java.io.IOException;
import java.util.List;

/**
 * Created by Misutesu on 2017/1/16 0016.
 */
public class UserGetNewCommentAction extends BaseAction {

    private String nId;
    private String page;
    private String size;
    private String sort;

    @Override
    public void action() {
        nId = request.getParameter("nid");
        page = request.getParameter("page");
        size = request.getParameter("size");
        sort = request.getParameter("sort");
        newDao = new NewDaoImpl();

        try {
            createJSON();
            sendJSON();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void createJSON() {
        if (MyUtils.isNumber(nId) && MyUtils.isNumber(page) && MyUtils.isNumber(size)) {
            if (!MyUtils.isNumber(sort, 1, 2))
                sort = "1";
            if (Integer.valueOf(size) > 20)
                size = "20";
            List<Comment> comments = newDao.getComment(Long.valueOf(nId), Integer.valueOf(page), Integer.valueOf(size), Integer.valueOf(sort));
            if (comments != null) {
                JSONArray jsonArray = new JSONArray();
                for (Comment comment : comments) {
                    JSONObject commentJSON = new JSONObject();
                    commentJSON.put("id", comment.getId());
                    commentJSON.put("content", comment.getContent());
                    commentJSON.put("createtime", comment.getCreateTime());
                    User user = comment.getUser();
                    JSONObject userJSON = new JSONObject();
                    userJSON.put("id", user.getId());
                    userJSON.put("nickname", user.getNickName());
                    userJSON.put("sex", user.getSex());
                    userJSON.put("photo", user.getPhoto());
                    commentJSON.put("user", userJSON);
                    jsonArray.add(commentJSON);
                }
                jsonObject.put("comments", jsonArray);
                jsonObject.put("size", comments.size());
                setResult(true);
            } else {
                setResult(false);
                setErrorCode(Constant.NEW_NO_HAVE);
            }
        } else {
            setResult(false);
            setErrorCode(Constant.VALUES_ERROR);
        }
    }
}
