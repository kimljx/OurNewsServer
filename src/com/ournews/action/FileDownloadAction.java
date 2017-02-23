package com.ournews.action;

import com.opensymphony.xwork2.ActionSupport;
import com.ournews.utils.MyUtils;
import org.apache.struts2.ServletActionContext;

import java.io.*;

/**
 * Created by Misutesu on 2017/1/15 0015.
 */
public class FileDownloadAction extends ActionSupport {

    private final String DEFAULT_IMAGE_NAME = "DefaultImage.png";

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
        String path = ServletActionContext.getServletContext().getRealPath(File.separator + "upload");
        File file = new File(path);
        if (MyUtils.isPhoto(fileName, file)) {
            file = new File(file, fileName);
        } else {
            file = new File(file, DEFAULT_IMAGE_NAME);
        }
        inputStream = new BufferedInputStream(new FileInputStream(file));
        return inputStream;
    }
}
