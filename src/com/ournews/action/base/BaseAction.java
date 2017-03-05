package com.ournews.action.base;

import com.opensymphony.xwork2.ActionSupport;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by Misutesu on 2017/1/15 0015.
 */
public abstract class BaseAction extends ActionSupport implements ServletRequestAware, ServletResponseAware {
    protected HttpServletRequest request;
    protected HttpServletResponse response;

    @Override
    public void setServletRequest(HttpServletRequest httpServletRequest) {
        request = httpServletRequest;
    }

    @Override
    public void setServletResponse(HttpServletResponse httpServletResponse) {
        response = httpServletResponse;
    }

    public abstract void action() throws IOException;

    public void sendJSON(String result) throws IOException {
//            response.setContentType("text/html;charset=utf-8");
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.getOutputStream().write(result.getBytes("UTF-8"));
        response.getOutputStream().flush();
        response.getOutputStream().close();
    }
}
