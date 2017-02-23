package com.ournews.action.user;

import com.ournews.action.base.BaseAction;
import com.ournews.dao.impl.UserDaoImpl;
import com.ournews.utils.Constant;
import com.ournews.utils.MyUtils;
import org.apache.struts2.ServletActionContext;

import java.io.File;
import java.io.IOException;

/**
 * Created by Misutesu on 2017/1/15 0015.
 */
public class UserChangeInfoAction extends BaseAction {

    private String id;
    private String nickName;
    private String sex;
    private String photo;

    @Override
    public void action() {
        id = request.getParameter("id");
        nickName = request.getParameter("nickname");
        sex = request.getParameter("sex");
        photo = request.getParameter("photo");
        userDao = new UserDaoImpl();

        try {
            createJSON();
            sendJSON();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void createJSON() {
        if (MyUtils.isNumber(id)) {
            if (MyUtils.isNull(nickName) && !MyUtils.isNumber(sex, 1, 2) && !MyUtils.isPhoto(photo
                    , new File(ServletActionContext.getServletContext().getRealPath(File.separator + "upload")))) {
                setResult(false);
                setErrorCode(Constant.VALUES_ERROR);
            } else {
                if (userDao.changeInfo(id, nickName, sex, photo)) {
                    setResult(true);
                    if (photo != null)
                        try {
                            String uploadPath = ServletActionContext.getServletContext().getRealPath(File.separator + "upload");
                            MyUtils.zipImage(new File(uploadPath, photo));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                } else {
                    setResult(false);
                    setErrorCode(Constant.CHANGE_INFO_ERROR);
                }
            }
        } else {
            setResult(false);
            setErrorCode(Constant.VALUES_ERROR);
        }
    }
}
