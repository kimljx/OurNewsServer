package com.ournews.action.base;

import com.opensymphony.xwork2.ActionSupport;
import com.ournews.dao.NewDao;
import com.ournews.dao.UserDao;
import net.sf.json.JSONObject;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by Misutesu on 2017/1/15 0015.
 */
public abstract class BaseAction extends ActionSupport implements ServletRequestAware, ServletResponseAware{
    protected HttpServletRequest request;
    protected HttpServletResponse response;
    protected JSONObject jsonObject = new JSONObject();
    protected UserDao userDao;
    protected NewDao newPresenter;

    @Override
    public void setServletRequest(HttpServletRequest httpServletRequest) {
        request = httpServletRequest;
    }

    @Override
    public void setServletResponse(HttpServletResponse httpServletResponse) {
        response = httpServletResponse;
    }

    public abstract void action();

    public abstract void createJSON();

    public void setResult(boolean result) {
        if (result) {
            jsonObject.put("result", "success");
        } else {
            jsonObject.put("result", "error");
        }
    }

    public void setErrorCode(String errorCode) {
        jsonObject.put("error_code", errorCode);
    }

    public void sendJSON() throws IOException {
        if (response != null) {
            response.setContentType("text/html;charset=utf-8");
            response.setCharacterEncoding("UTF-8");
            response.getOutputStream().write(jsonObject.toString().getBytes("UTF-8"));
            response.getOutputStream().flush();
            response.getOutputStream().close();
        }
    }
}
