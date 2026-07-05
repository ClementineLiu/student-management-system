package com.student.entity;

import java.io.Serializable;

/**
 * 自定义运行时异常
 */
public class EIException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private String msg;
    private int code = 500;

    public EIException(String msg) {
        super(msg);
        this.msg = msg;
    }

    public EIException(String msg, int code) {
        super(msg);
        this.msg = msg;
        this.code = code;
    }

    public String getMsg() { return msg; }
    public int getCode() { return code; }
}
