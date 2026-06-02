package com.addressbook.web;

/** 表示 JSON 接口响应。 */
public class JsonResponse {
    private final int statusCode;
    private final String body;

    /** 创建 JSON 响应对象。 */
    public JsonResponse(int statusCode, String body) {
        this.statusCode = statusCode;
        this.body = body;
    }

    /** 返回 HTTP 状态码。 */
    public int getStatusCode() {
        return statusCode;
    }

    /** 返回 JSON 响应体。 */
    public String getBody() {
        return body;
    }
}
