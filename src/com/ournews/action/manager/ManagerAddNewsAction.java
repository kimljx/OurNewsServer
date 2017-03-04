package com.ournews.action.manager;

import com.ournews.action.base.BaseAction;
import com.ournews.dao.impl.NewDaoImpl;
import com.ournews.utils.Constant;
import com.ournews.utils.MyUtils;
import org.apache.struts2.ServletActionContext;

import java.io.File;
import java.io.IOException;

/**
 * Created by Misutesu on 2017/1/16 0016.
 */
public class ManagerAddNewsAction extends BaseAction {

    private String title;
    private String cover;
    private String abstractContent;
    private String content;
    private String createTime;
    private String type;

    @Override
    public void action() {
        title = request.getParameter("title");
        cover = request.getParameter("cover");
        abstractContent = request.getParameter("abstract");
        content = request.getParameter("content");
        createTime = request.getParameter("createtime");
        type = request.getParameter("type");
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
        if (!MyUtils.isNull(title) && !MyUtils.isNull(cover) && !MyUtils.isNull(abstractContent)
                && !MyUtils.isNull(content) && !MyUtils.isNull(createTime) && !MyUtils.isNumber(type, 0, 6)) {
            if (newDao.addNews(title, cover, abstractContent, content, createTime, type)) {
                setResult(true);
                try {
                    String uploadPath = ServletActionContext.getServletContext().getRealPath("upload");
                    MyUtils.zipImage(new File(uploadPath, cover));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                setResult(false);
                setErrorCode(Constant.ADD_NEWS_ERROR);
            }
        } else {
            setResult(false);
            setErrorCode(Constant.VALUES_ERROR);
        }
    }
}
