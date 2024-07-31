package com.generalnetdisk.enums;


public enum ResponseCodeEnum {
    CODE_200(200, "请求成功"),
    CODE_404(404, "请求地址不存在"),
    CODE_500(500, "服务器返回错误"),
    CODE_600(600, "请求参数错误"),
    CODE_601(601, "信息已存在"),
    CODE_901(901, "登录超时，请重新登录");

    private int code;
    private String message;

    ResponseCodeEnum(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    // 可以添加一个方法来获取枚举的描述信息
    public static String getDescription(int code) {
        for (ResponseCodeEnum responseCode : ResponseCodeEnum.values()) {
            if (responseCode.getCode() == code) {
                return responseCode.getMessage();
            }
        }
        return null; // 如果没有找到对应的code，返回null
    }
}
