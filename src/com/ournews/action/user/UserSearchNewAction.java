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
 * Created by Misutesu on 2017/1/23 0023.
 */
public class UserSearchNewAction extends BaseAction {

    private String name;
    private String page;
    private String size;
    private String sort;

    @Override
    public void action() {
        name = request.getParameter("name");
        page = request.getParameter("page");
        size = request.getParameter("size");
        sort = request.getParameter("sort");
        newPresenter = new NewDaoImpl();

        try {
            createJSON();
            sendJSON();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void createJSON() {
        if (!MyUtils.isNull(name) && MyUtils.isNumber(page) && MyUtils.isNumber(size)) {
            if (!MyUtils.isNumber(sort, 1, 2))
                sort = "1";
            if (Integer.valueOf(size) > 20)
                size = "20";
            List<New> news = newPresenter.searchNew(name, Integer.valueOf(page), Integer.valueOf(size), Integer.valueOf(sort));
            if (news != null) {
                JSONArray jsonArray = new JSONArray();
                for (New n : news) {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("id", n.getId());
                    jsonObject.put("title", n.getTitle());
                    jsonObject.put("cover", n.getCover());
                    jsonObject.put("abstract", n.getAbstractContent());
                    jsonObject.put("createtime", n.getCreateTime());
                    jsonObject.put("type", n.getType());
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
