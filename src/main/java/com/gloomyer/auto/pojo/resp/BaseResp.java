package com.gloomyer.auto.pojo.resp;

public class BaseResp<T> {
    private int code;
    private String msg;
    private T data;

    public static <P> BaseResp<P> success(P data) {
        return new BaseResp<>(200, "success", data);
    }

    public BaseResp() {
    }

    public BaseResp(int code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "BaseResp{" +
                "code=" + code +
                ", msg='" + msg + '\'' +
                ", data=" + data +
                '}';
    }
}
