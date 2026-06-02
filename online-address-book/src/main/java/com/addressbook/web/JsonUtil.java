package com.addressbook.web;

import java.util.LinkedHashMap;
import java.util.Map;

/** 提供当前模块使用的简单 JSON 编码和取值能力。 */
public final class JsonUtil {
    /** 禁止实例化工具类。 */
    private JsonUtil() {
    }

    /** 把键值映射转为 JSON 对象字符串。 */
    public static String object(Map<String, ?> values) {
        StringBuilder builder = new StringBuilder("{");
        boolean first = true;
        for (Map.Entry<String, ?> entry : values.entrySet()) {
            if (!first) {
                builder.append(",");
            }
            builder.append("\"").append(escape(entry.getKey())).append("\":");
            Object value = entry.getValue();
            if (value == null) {
                builder.append("null");
            } else if (value instanceof Number || value instanceof Boolean) {
                builder.append(value);
            } else {
                builder.append("\"").append(escape(String.valueOf(value))).append("\"");
            }
            first = false;
        }
        builder.append("}");
        return builder.toString();
    }

    /** 创建错误 JSON 响应体。 */
    public static String error(String message) {
        Map<String, Object> values = new LinkedHashMap<String, Object>();
        values.put("success", false);
        values.put("message", message);
        return object(values);
    }

    /** 从简单 JSON 字符串中提取字符串字段。 */
    public static String extractString(String json, String fieldName) {
        String pattern = "\"" + fieldName + "\":\"";
        int start = json.indexOf(pattern);
        if (start < 0) {
            return null;
        }
        int valueStart = start + pattern.length();
        int valueEnd = json.indexOf("\"", valueStart);
        if (valueEnd < 0) {
            return null;
        }
        return json.substring(valueStart, valueEnd);
    }

    /** 转义 JSON 字符串。 */
    private static String escape(String value) {
        return value.replace("\\", "\\\\").replace("\"", "\\\"");
    }
}
