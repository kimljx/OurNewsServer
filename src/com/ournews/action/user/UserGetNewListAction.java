package com.ournews.action.user;

import com.ournews.action.base.BaseAction;
import com.ournews.bean.New;
import com.ournews.dao.impl.NewDaoImpl;
import com.ournews.utils.Constant;
import com.ournews.utils.MyUtils;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import java.io.IOException;
import java.util.List;

/**
 * Created by Misutesu on 2017/1/20 0020.
 */
public class UserGetNewListAction extends BaseAction {

    private String type;
    private String page;
    private String size;
    private String sort;

    @Override
    public void action() {
        type = request.getParameter("type");
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
        if (MyUtils.isNumber(type, 1, 5) && MyUtils.isNumber(page) && MyUtils.isNumber(size)) {
            if (!MyUtils.isNumber(sort, 1, 2))
                sort = "1";
            if (Integer.valueOf(size) > 20)
                size = "20";
            List<New> news = newDao.getNewList(Integer.valueOf(type), Integer.valueOf(page), Integer.valueOf(size), Integer.valueOf(sort));
            if (news != null) {
                JSONArray jsonArray = new JSONArray();
                for (New n : news) {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("id", n.getId());
                    jsonObject.put("title", n.getTitle());
                    jsonObject.put("cover", n.getCover());
                    jsonObject.put("abstract", n.getAbstractContent());
                    jsonObject.put("createtime", n.getCreateTime());
                    jsonObject.put("type", type);
                    jsonArray.add(jsonObject);
                }
                setResult(true);
                jsonObject.put("news", jsonArray);
                jsonObject.put("size", news.size());
            } else {
                setResult(false);
                setErrorCode(Constant.SERVER_ERROR);
            }
        } else {
            setResult(false);
            setErrorCode(Constant.VALUES_ERROR);
        }
    }
}
