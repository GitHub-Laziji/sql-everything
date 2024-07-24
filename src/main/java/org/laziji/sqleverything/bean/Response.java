package org.laziji.sqleverything.bean;

public class Response<T> {

    private String code;
    private String message;
    private T data;

    public static <T> Response<T> success(T data) {
        Response<T> res = new Response<>();
        res.code = "0";
        res.data = data;
        return res;
    }

    public static Response<Object> success() {
        return success(null);
    }

    public static Response<Object> error(String code, String message) {
        Response<Object> res = new Response<>();
        res.code = code;
        res.message = message;
        return res;
    }

    public static Response<Object> error(String message) {
        return error("1", message);
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public Object getData() {
        return data;
    }

}
