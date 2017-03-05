package com.ournews.action;

import com.ournews.action.base.BaseAction;
import com.ournews.utils.Constant;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.struts2.ServletActionContext;

import java.io.*;
import java.util.UUID;

/**
 * Created by Misutesu on 2017/1/15 0015.
 */
public class FileUploadAction extends BaseAction {

    //以下三个属性，fileUpload拦截器自动闯入
    private File[] upload;//上传文件
    private String[] uploadContentType;//上传文件类型
    private String[] uploadFileName;//上传文件名

    public void setUpload(File[] upload) {
        this.upload = upload;
    }

    public void setUploadContentType(String[] uploadContentType) {
        this.uploadContentType = uploadContentType;
    }

    public void setUploadFileName(String[] uploadFileName) {
        this.uploadFileName = uploadFileName;
    }

    @Override
    public void action() throws IOException {
        sendJSON(getJSON().toString());
    }

    public JSONObject getJSON() {
        JSONObject jsonObject;
        InputStream in = null;
        OutputStream out = null;
        try {
            JSONArray jsonArray = new JSONArray();
            for (int i = 0; i < upload.length; i++) {
                if (!uploadContentType[i].startsWith("image/")) {
                    jsonObject = new JSONObject();
                    jsonObject.put("result", "error");
                    jsonObject.put("error_code", Constant.UPLOAD_NO_IMAGE);
                    jsonObject.put("data", "");
                    return jsonObject;
                } else {
                    if (upload[i].length() > 5 * 1024 * 1024) {
                        jsonObject = new JSONObject();
                        jsonObject.put("result", "error");
                        jsonObject.put("error_code", Constant.UPLOAD_FILE_TOO_BIG);
                        jsonObject.put("data", "");
                        return jsonObject;
                    } else {
                        String path = ServletActionContext.getServletContext().getRealPath(File.separator + "upload");
                        File file = new File(path);
                        if (!file.exists())
                            file.mkdirs();
                        in = new FileInputStream(upload[i]);
                        String fileName = UUID.randomUUID().toString().replace("-", "")
                                + "." + uploadContentType[i].substring(6, uploadContentType[i].length());
                        file = new File(file, fileName);
                        out = new FileOutputStream(file);
                        int len;
                        byte[] by = new byte[1024];
                        while ((len = in.read(by)) > 0) {
                            out.write(by, 0, len);
                        }
                        jsonArray.add(fileName);
                    }
                }
            }
            jsonObject = new JSONObject();
            jsonObject.put("data", jsonArray);
            jsonObject.put("result", "success");
            jsonObject.put("error_code", "0");
            return jsonObject;
        } catch (IOException e) {
            e.printStackTrace();
            jsonObject = new JSONObject();
            jsonObject.put("result", "error");
            jsonObject.put("error_code", Constant.SERVER_ERROR);
            jsonObject.put("data", "");
            return jsonObject;
        } finally {
            try {
                if (in != null)
                    in.close();
                if (out != null) {
                    out.flush();
                    out.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
