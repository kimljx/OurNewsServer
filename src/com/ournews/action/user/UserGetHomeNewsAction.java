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
import java.util.Map;

/**
 * Created by Misutesu on 2017/1/16 0016.
 */
public class UserGetHomeNewsAction extends BaseAction {

    private String selectType;

    @Override
    public void action() {
        selectType = request.getParameter("type");
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
        if (MyUtils.isNumber(selectType, 1, 6)) {
            Map<Integer, List<New>> newMap = newPresenter.getHomeNews(selectType);
            int type = Integer.valueOf(selectType);
            JSONArray jsonArray = new JSONArray();
            List<New> aNews = newMap.get(type);
            for (New n : aNews) {
                JSONObject newJSON = new JSONObject();
                newJSON.put("id", n.getId());
                newJSON.put("title", n.getTitle());
                newJSON.put("cover", n.getCover());
                newJSON.put("abstract", n.getAbstractContent());
                newJSON.put("createtime", n.getCreateTime());
                newJSON.put("type", type);
                jsonArray.add(newJSON);
            }
            jsonObject.put(type, jsonArray);
            setResult(true);
        } else if (MyUtils.isNull(selectType)) {
            Map<Integer, List<New>> newMap = newPresenter.getHomeNews(null);
            for (int i = 1; i < 7; i++) {
                JSONArray jsonArray = new JSONArray();
                List<New> aNews = newMap.get(i);
                System.out.println(i);
                for (New n : aNews) {
                    JSONObject newJSON = new JSONObject();
                    newJSON.put("id", n.getId());
                    newJSON.put("title", n.getTitle());
                    newJSON.put("cover", n.getCover());
                    newJSON.put("abstract", n.getAbstractContent());
                    newJSON.put("createtime", n.getCreateTime());
                    newJSON.put("type", i);
                    jsonArray.add(newJSON);
                }
                jsonObject.put(i, jsonArray);
            }
            setResult(true);
        } else {
            setResult(false);
            setErrorCode(Constant.VALUES_ERROR);
        }
    }
}
