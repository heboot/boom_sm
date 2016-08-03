package com.codingfeel.sm.model;

/**
 * Created by Heboot on 16/6/27.
 */
public class BaseModel {
    private Integer error;
    private String error_text;
    private String error_msg;
    private String result;

    public Integer getError() {
        return error;
    }

    public void setError(Integer error) {
        this.error = error;
    }

    public String getError_text() {
        return error_text;
    }

    public void setError_text(String error_text) {
        this.error_text = error_text;
    }

    public String getError_msg() {
        return error_msg;
    }

    public void setError_msg(String error_msg) {
        this.error_msg = error_msg;
    }

    public BaseModel(Integer error, String error_text, String error_msg) {
        this.error = error;
        this.error_text = error_text;
        this.error_msg = error_msg;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public BaseModel() {

    }
}
