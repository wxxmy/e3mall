package cn.e3.utils;

import java.io.Serializable;

public class KindEditerModel implements Serializable{
    private Integer error;
    private String message;
    private String url;
    public Integer getError() {
        return error;
    }
    public void setError(Integer error) {
        this.error = error;
    }
    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }
    public String getUrl() {
        return url;
    }
    public void setUrl(String url) {
        this.url = url;
    }
}
