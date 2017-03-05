//package com.ournews.model;
//
//import com.google.gson.annotations.SerializedName;
//
//import java.util.List;
//
///**
// * Created by Misutesu on 2017/3/5 0005.
// */
//public class SearchResult {
//
//    /**
//     * result : success
//     * errorCode : 0
//     * data : [{"id":123,"title":"微软首款Win10 VR设备3月出货：主要用于开发人员","cover":"20170118_144501_882.jpg","abstract":"微软首款Win10 VR设备3月出货：主要用于开发人员","createTime":"2017-1-18 14:46:32","type":5},{"id":116,"title":"一波攻击完虐日韩！解放军轰6混编总数有180架","cover":"mg1h981h8f2f21f12gf21g.png","abstract":"借4000元还10万都没还完","createTime":"2017-01-18 07:47:01","type":3},{"id":115,"title":"银行错打12亿 他返还获200元和一包烟","cover":"2841_770720_948566.jpg","abstract":"银行错打12亿 他返还获200元和一包烟","createTime":"2017.01.18 07:17:29","type":3}]
//     */
//
//    private String result;
//    @SerializedName("error_code")
//    private String errorCode;
//    private List<DataBean> data;
//
//    public void setErrorResult(String errorCode) {
//        setResult("error");
//        setErrorCode(errorCode);
//    }
//
//    public void setSuccessResult() {
//        setResult("success");
//        setErrorCode("0");
//    }
//
//    public String getResult() {
//        return result;
//    }
//
//    public void setResult(String result) {
//        this.result = result;
//    }
//
//    public String getErrorCode() {
//        return errorCode;
//    }
//
//    public void setErrorCode(String errorCode) {
//        this.errorCode = errorCode;
//    }
//
//    public List<DataBean> getData() {
//        return data;
//    }
//
//    public void setData(List<DataBean> data) {
//        this.data = data;
//    }
//
//    public static class DataBean {
//        /**
//         * id : 123
//         * title : 微软首款Win10 VR设备3月出货：主要用于开发人员
//         * cover : 20170118_144501_882.jpg
//         * abstract : 微软首款Win10 VR设备3月出货：主要用于开发人员
//         * createTime : 2017-1-18 14:46:32
//         * type : 5
//         */
//
//        private long id;
//        private String title;
//        private String cover;
//        @SerializedName("abstract")
//        private String abstractX;
//        @SerializedName("create_time")
//        private String createTime;
//        private String type;
//
//        public long getId() {
//            return id;
//        }
//
//        public void setId(long id) {
//            this.id = id;
//        }
//
//        public String getTitle() {
//            return title;
//        }
//
//        public void setTitle(String title) {
//            this.title = title;
//        }
//
//        public String getCover() {
//            return cover;
//        }
//
//        public void setCover(String cover) {
//            this.cover = cover;
//        }
//
//        public String getAbstractX() {
//            return abstractX;
//        }
//
//        public void setAbstractX(String abstractX) {
//            this.abstractX = abstractX;
//        }
//
//        public String getCreateTime() {
//            return createTime;
//        }
//
//        public void setCreateTime(String createTime) {
//            this.createTime = createTime;
//        }
//
//        public String getType() {
//            return type;
//        }
//
//        public void setType(String type) {
//            this.type = type;
//        }
//    }
//}
