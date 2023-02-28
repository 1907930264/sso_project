package com.yxj.ssoserver.common;

import java.io.Serializable;

public class RestResponse<T> implements Serializable {

    private static final long serialVersionUID = 3728877563912075885L;

    private Integer code;
    private String msg;
    private T data;
    private Boolean success;

    public RestResponse(){

    }

    public RestResponse(Integer code, String message, T data, Boolean success) {
        this.code = code;
        this.setMsg(message);
        this.data = data;
        this.success = success;
    }

    private RestResponse(Integer code, T data, Boolean success) {
        this.code = code;
        this.data = data;
        this.success = success;
    }

    private RestResponse(Integer code, String message,Boolean success) {
        this.code = code;
        this.setMsg(message);
        this.success = success;
    }

    /**
     * 成功时-返回data
     * @param <T>
     * @return
     */
    public static <T> RestResponse<T> success(T data){
        return new RestResponse<T>(200, "成功", data,true);
    }

    /**
     * 成功-不返回data
     * @param <T>
     * @return
     */
    public static <T> RestResponse<T> successNoData(){
        return new RestResponse<T>(200, "成功",true);
    }

    /**
     * 成功-返回data+msg
     * @param <T>
     * @return
     */
    public static <T> RestResponse<T> success(String msg, T data){
        return new RestResponse<T>(200, msg, data,true);
    }

    /**
     * 失败
     * @param <T>
     * @return
     */
    public static <T> RestResponse<T> fail(String msg){
        return new RestResponse<T>(500, msg,false);
    }

    /**
     * 失败-code
     * @param <T>
     * @return
     */
    public static <T> RestResponse<T> fail(int code, String msg){
        return new RestResponse<T>(code, msg,false);
    }


    public Integer getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }


    public T getData() {
        return data;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public void setData(T data) {
        this.data = data;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    @Override
    public String toString() {
        return "RestResponse{" +
                "code=" + code +
                ", msg='" + msg + '\'' +
                ", data=" + data +
                ", success=" + success +
                '}';
    }
}
