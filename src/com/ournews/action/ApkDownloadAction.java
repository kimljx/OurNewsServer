package com.ournews.action;

import com.opensymphony.xwork2.ActionSupport;
import org.apache.struts2.ServletActionContext;

import java.io.*;

/**
 * Created by Misutesu on 2017/4/20 0020.
 */
public class ApkDownloadAction extends ActionSupport {

    //    //读取下载文件的目录
//    private String inputPath;
    //读取下载文件的输入流
    private InputStream inputStream;
    //下载文件的文件名
    private String fileName;
//    //下载文件的类型
//    private String contentType;

    @Override
    public String execute() throws Exception {
        return SUCCESS;
    }

    public String getFileName() {
        return fileName;
    }

    public InputStream getInputStream() throws FileNotFoundException {
        fileName = ServletActionContext.getRequest().getParameter("name");
        String path = ServletActionContext.getServletContext().getRealPath(File.separator + "apk");
        File file = new File(path, !fileName.endsWith(".apk") ? "1" : fileName);
        if (file.exists()) {
            inputStream = new BufferedInputStream(new FileInputStream(file));
        }
        return inputStream;
    }
}
