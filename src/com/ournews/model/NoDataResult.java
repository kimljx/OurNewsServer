//package com.ournews.model;
//
//import com.google.gson.annotations.SerializedName;
//
///**
// * Created by Misutesu on 2017/3/5 0005.
// */
//public class NoDataResult {
//
//    /**
//     * result : success
//     * error_code : 0
//     * data :
//     */
//
//    private String result;
//    @SerializedName("error_code")
//    private String errorCode;
//    private String data;
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
//}
