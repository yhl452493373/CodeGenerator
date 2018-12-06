package com.github.yhl452493373.bean;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.alibaba.fastjson.serializer.SerializeConfig;
import com.alibaba.fastjson.serializer.SerializeFilter;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.serializer.SimplePropertyPreFilter;
import com.github.yhl452493373.config.CommonConfig;

import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.List;
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

    /**
     * 过滤属性并返回JSON
     * <p>
     * 使用示例:
     * JSONResult json = JSONResult.init();
     * json.data(object,JSONResult.Pattern.INCLUDE,"id","name")
     * </p>
     *
     * @param data 对象
     * @param keys 要过滤的属性
     */
    public JSONResult data(Object data, Pattern pattern, String... keys) {
        Class clazz = data.getClass();
        List<String> keyList = Arrays.asList(keys);
        SimplePropertyPreFilter filter = new SimplePropertyPreFilter();
        if (pattern.equals(Pattern.EXCLUDE))
            filter.getExcludes().addAll(keyList);
        else if (pattern.equals(Pattern.INCLUDE))
            filter.getIncludes().addAll(keyList);
        stringToJSON(data, JSON.toJSONString(data, SerializeConfig.globalInstance, new SimplePropertyPreFilter[]{filter}, CommonConfig.getDateFormat(), JSON.DEFAULT_GENERATE_FEATURE, SerializerFeature.DisableCircularReferenceDetect));
        return this;
    }

    /**
     * json字符串转为json
     *
     * @param object 字符串来源对象实例
     * @param string 字符串
     */
    private void stringToJSON(Object object, String string) {
        JSON json;
        if (object instanceof Iterable) {
            json = JSON.parseArray(string);
        } else {
            json = JSON.parseObject(string);
        }
        this.data(json);
    }

    /**
     * 过滤属性并返回JSON
     * <p>
     * 使用示例:
     * JSONResult json = JSONResult.init();
     * JSONResult.PropertyFilter[] propertyFilters = new JSONResult.PropertyFilter[]{
     * new JSONResult.PropertyFilter(JSONResult.Pattern.INCLUDE, BeanA.class, "id", "name", "beanB"),
     * new JSONResult.PropertyFilter(JSONResult.Pattern.INCLUDE, BeanB.class, "id", "name")
     * };
     * json.data(object,propertyFilters)
     * </p>
     *
     * @param data            对象
     * @param propertyFilters 属性过滤集合
     */
    public JSONResult data(Object data, PropertyFilter... propertyFilters) {
        SerializeFilter[] filters = new SerializeFilter[propertyFilters.length];
        for (int i = 0; i < propertyFilters.length; i++) {
            PropertyFilter propertyFilter = propertyFilters[i];
            SimplePropertyPreFilter filter = new SimplePropertyPreFilter(propertyFilter.getClazz());
            Pattern pattern = propertyFilter.getPattern();
            String[] keys = propertyFilter.getKeys();
            filter.getExcludes().addAll(Arrays.asList(keys));
            filter.getIncludes().addAll(Arrays.asList(keys));
            filters[i] = filter;
        }
        stringToJSON(data, JSON.toJSONString(data, SerializeConfig.globalInstance, filters, CommonConfig.getDateFormat(), JSON.DEFAULT_GENERATE_FEATURE, SerializerFeature.DisableCircularReferenceDetect));
        return this;
    }

    public enum Pattern {
        /**
         * 包含模式
         */
        INCLUDE,
        /**
         * 排除模式
         */
        EXCLUDE
    }

    /**
     * 属性过滤封装,主要用于对不同对象的属性过滤
     */
    public static class PropertyFilter {
        Pattern pattern;//模式:是包含还是排除
        Class clazz;//对应是哪个对象
        String[] keys;//要包含或者排除出的属性名

        public PropertyFilter() {
        }

        public PropertyFilter(Pattern pattern, Class clazz, String... keys) {
            this.pattern = pattern;
            this.clazz = clazz;
            this.keys = keys;
        }

        public Pattern getPattern() {
            return pattern;
        }

        public void setPattern(Pattern pattern) {
            this.pattern = pattern;
        }

        public Class getClazz() {
            return clazz;
        }

        public void setClazz(Class clazz) {
            this.clazz = clazz;
        }

        public String[] getKeys() {
            return keys;
        }

        public void setKeys(String... keys) {
            this.keys = keys;
        }
    }
}
