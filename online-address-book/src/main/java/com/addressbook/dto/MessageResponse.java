package com.addressbook.dto;

/** 表示通用消息响应。 */
public class MessageResponse {
    private final boolean success;
    private final String message;

    /** 创建消息响应。 */
    public MessageResponse(boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    /** 返回是否成功。 */
    public boolean isSuccess() {
        return success;
    }

    /** 返回消息内容。 */
    public String getMessage() {
        return message;
    }
}
