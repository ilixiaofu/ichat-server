/**
 * Copyright (C), 2015-2018, XXX有限公司
 */
package com.lxf.ichat.po;


/**
 * <pre>
 * @FileName:    MsgPO
 * @Author:      李晓福
 * @Date:        2018/11/20 20:22
 * @Description: MsgPO
 * @since:       1.0.0
 * @History:
 * 作者姓名       修改时间             版本号           描述
 * lxf           2018/11/20 20:22     1.0.0           创建
 * </pre>
 */
 
public class MsgPO<T> {

    private String code;
    private String msg;
    private String error;
    private T data;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
