//package com.ournews.model;
//
//import com.google.gson.annotations.SerializedName;
//
//public class LoginResult {
//
//    /**
//     * result : success
//     * errorCode : 0
//     * data : {"id":1,"loginName":"Test","nickName":"Test","sex":1,"photo":"NoImage","token":"token"}
//     */
//
//    private String result;
//    @SerializedName("error_code")
//    private String errorCode;
//    private DataBean data;
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
//    public DataBean getData() {
//        return data;
//    }
//
//    public void setData(DataBean data) {
//        this.data = data;
//    }
//
//    public static class DataBean {
//        /**
//         * id : 1
//         * loginName : Test
//         * nickName : Test
//         * sex : 1
//         * photo : NoImage
//         * token : token
//         */
//
//        private long id;
//        @SerializedName("login_name")
//        private String loginName;
//        @SerializedName("nick_name")
//        private String nickName;
//        private int sex;
//        private String photo;
//        private String token;
//
//        public long getId() {
//            return id;
//        }
//
//        public void setId(long id) {
//            this.id = id;
//        }
//
//        public String getLoginName() {
//            return loginName;
//        }
//
//        public void setLoginName(String loginName) {
//            this.loginName = loginName;
//        }
//
//        public String getNickName() {
//            return nickName;
//        }
//
//        public void setNickName(String nickName) {
//            this.nickName = nickName;
//        }
//
//        public int getSex() {
//            return sex;
//        }
//
//        public void setSex(int sex) {
//            this.sex = sex;
//        }
//
//        public String getPhoto() {
//            return photo;
//        }
//
//        public void setPhoto(String photo) {
//            this.photo = photo;
//        }
//
//        public String getToken() {
//            return token;
//        }
//
//        public void setToken(String token) {
//            this.token = token;
//        }
//    }
//}
