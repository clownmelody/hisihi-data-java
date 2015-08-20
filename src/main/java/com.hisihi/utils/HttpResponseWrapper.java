package com.hisihi.utils;

/**
 * Created by andyYang on 2015/8/20.
 */
public class HttpResponseWrapper {
    public boolean isNeedRetry;
    public int httpCode;
    public String content;

    public HttpResponseWrapper() {
    }

    public HttpResponseWrapper(int httpCode, String content) {
        this.httpCode = httpCode;
        this.content = content;
    }

    @Override
    public String toString() {
        return "{httpCode:" + httpCode + ", content:" + content + "}";
    }

    public boolean isOK() {
        return httpCode >= 200 && httpCode < 300;
    }
}
