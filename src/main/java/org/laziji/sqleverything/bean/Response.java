package org.laziji.sqleverything.bean;

public class Response {

    private String code;
    private String message;
    private Object data;

    public static Response success(Object data) {
        Response res = new Response();
        res.code = "0";
        res.data = data;
        return res;
    }

    public static Response success() {
        return success(null);
    }

    public static Response error(String code, String message) {
        Response res = new Response();
        res.code = code;
        res.message = message;
        return res;
    }

    public static Response error(String message) {
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
