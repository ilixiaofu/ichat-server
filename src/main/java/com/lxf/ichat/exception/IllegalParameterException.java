/**
 * Copyright (C), 2015-2018, XXX有限公司
 */
package com.lxf.ichat.exception;


/**
 * <pre>
 * @FileName: IllegalParameterException
 * @Author: 李晓福
 * @Date: 2018/11/23 17:09
 * @Description:
 * @since: 1.0.0
 * @History:
 * 作者姓名       修改时间             版本号           描述
 * lxf           2018/11/23 17:09     1.0.0           创建
 * </pre>
 */

public class IllegalParameterException extends Throwable {
    public IllegalParameterException() {
    }

    public IllegalParameterException(String message) {
        super(message);
    }

    public IllegalParameterException(String message, Throwable cause) {
        super(message, cause);
    }

    public IllegalParameterException(Throwable cause) {
        super(cause);
    }

    public IllegalParameterException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
