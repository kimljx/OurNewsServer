//package com.ournews.model;
//
//import java.util.List;
//
///**
// * Created by Misutesu on 2017/3/6 0006.
// */
//public class CommentResult {
//
//    /**
//     * result : success
//     * error_code : 0
//     * data : [{"id":10,"content":"aaa","create_time":"2017","user":{"id":10,"nick_name":"ddd","sex":1,"photo":"eee.png"}},{"id":10,"content":"aaa","create_time":"2017","user":{"id":10,"nick_name":"ddd","sex":1,"photo":"eee.png"}}]
//     */
//
//    private String result;
//    private String error_code;
//    private List<DataBean> data;
//
//    public String getResult() {
//        return result;
//    }
//
//    public void setResult(String result) {
//        this.result = result;
//    }
//
//    public String getError_code() {
//        return error_code;
//    }
//
//    public void setError_code(String error_code) {
//        this.error_code = error_code;
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
//         * id : 10
//         * content : aaa
//         * create_time : 2017
//         * user : {"id":10,"nick_name":"ddd","sex":1,"photo":"eee.png"}
//         */
//
//        private long id;
//        private String content;
//        private String create_time;
//        private UserBean user;
//
//        public long getId() {
//            return id;
//        }
//
//        public void setId(long id) {
//            this.id = id;
//        }
//
//        public String getContent() {
//            return content;
//        }
//
//        public void setContent(String content) {
//            this.content = content;
//        }
//
//        public String getCreate_time() {
//            return create_time;
//        }
//
//        public void setCreate_time(String create_time) {
//            this.create_time = create_time;
//        }
//
//        public UserBean getUser() {
//            return user;
//        }
//
//        public void setUser(UserBean user) {
//            this.user = user;
//        }
//
//        public static class UserBean {
//            /**
//             * id : 10
//             * nick_name : ddd
//             * sex : 1
//             * photo : eee.png
//             */
//
//            private long id;
//            private String nick_name;
//            private int sex;
//            private String photo;
//
//            public long getId() {
//                return id;
//            }
//
//            public void setId(long id) {
//                this.id = id;
//            }
//
//            public String getNick_name() {
//                return nick_name;
//            }
//
//            public void setNick_name(String nick_name) {
//                this.nick_name = nick_name;
//            }
//
//            public int getSex() {
//                return sex;
//            }
//
//            public void setSex(int sex) {
//                this.sex = sex;
//            }
//
//            public String getPhoto() {
//                return photo;
//            }
//
//            public void setPhoto(String photo) {
//                this.photo = photo;
//            }
//        }
//    }
//}
