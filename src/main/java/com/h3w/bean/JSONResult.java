package com.h3w.bean;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;

import java.lang.reflect.Type;
import java.util.TreeMap;

/**
 * 用于返回的JSON对象，便于统一
 * 依赖fastjson
 */
@SuppressWarnings({"WeakerAccess", "UnusedReturnValue", "unused"})
public class JSONResult extends JSONObject {
    public static final String JSON_VALUE_SUCCESS = "success";
    public static final String JSON_VALUE_ERROR = "error";
    public static final String JSON_KEY_STATUS = "status";
    public static final String JSON_KEY_MESSAGE = "message";
    public static final String JSON_KEY_DATA = "data";
    public static final String JSON_KEY_CODE = "code";
    public static final String JSON_KEY_COUNT = "count";
    public static final String JSON_KEY_DETAIL = "detail";

    public JSONResult() {
        this(false);
    }

    public JSONResult(Boolean showPermission) {
        super(new TreeMap<>());
        this.success(null).detail(null).data(new JSONObject()).count(0).code(200);
    }

    public static JSONResult init() {
        return new JSONResult();
    }

    public JSONResult code(Integer code) {
        this.put(JSON_KEY_CODE, code);
        return this;
    }


    public JSONResult count(Integer count) {
        this.put(JSON_KEY_COUNT, count);
        return this;
    }

    public JSONResult count(Long count) {
        this.put(JSON_KEY_COUNT, count);
        return this;
    }

    public JSONResult detail(String detail) {
        this.put(JSON_KEY_DETAIL, detail);
        return this;
    }

    public JSONResult data(Object data) {
        this.put(JSON_KEY_DATA, data);
        return this;
    }

    public JSONResult success(String message) {
        this.put(JSON_KEY_STATUS, JSON_VALUE_SUCCESS);
        this.message(message);
        return this;
    }

    public JSONResult message(String message) {
        if (message == null)
            message = "";
        this.put(JSON_KEY_MESSAGE, message);
        return this;
    }

    public JSONResult success() {
        this.success(null);
        return this;
    }

    public JSONResult error() {
        this.error(null);
        return this;
    }

    public JSONResult error(String message) {
        this.put(JSON_KEY_STATUS, JSON_VALUE_ERROR);
        this.message(message);
        return this;
    }

    public boolean isSuccess() {
        return this.getString(JSON_KEY_STATUS).equalsIgnoreCase(JSON_VALUE_SUCCESS);
    }

    public boolean isError() {
        return this.getString(JSON_KEY_STATUS).equalsIgnoreCase(JSON_VALUE_ERROR);
    }

    public String message() {
        return this.getString(JSON_KEY_MESSAGE);
    }

    public Object data() {
        return this.get(JSON_KEY_DATA);
    }

    public <T> T data(Class<T> clazz) {
        return this.getObject(JSON_KEY_DATA, clazz);
    }

    public <T> T data(Type type) {
        return this.getObject(JSON_KEY_DATA, type);
    }

    public <T> T data(TypeReference typeReference) {
        return this.getObject(JSON_KEY_DATA, typeReference);
    }

    public String detail() {
        return this.getString(JSON_KEY_DETAIL);
    }

    public Long count() {
        return this.getLong(JSON_KEY_COUNT);
    }
}
