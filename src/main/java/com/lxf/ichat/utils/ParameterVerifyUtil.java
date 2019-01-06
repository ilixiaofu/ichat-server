/**
 * Copyright (C), 2015-2018, XXX有限公司
 */
package com.lxf.ichat.utils;


import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * <pre>
 * @FileName:    ParameterVerifyUtil
 * @Author:      李晓福
 * @Date:        2018/11/30 11:18
 * @Description: 参数验证
 * @since:       1.0.0
 * @History:
 * 作者姓名       修改时间             版本号           描述
 * lxf           2018/11/30 11:18     1.0.0           创建
 * </pre>
 */
 
public class ParameterVerifyUtil {

    private static final String CHINESE = "[\u4E00-\u9FA5]";  // 匹配中文
    private static final String ENGLISH = "[a-zA-Z]";  // 匹配英文
    private static final String NUM = "[0-9]";  // 匹配数字

    private static final String UID_REGEX = "^[0-9a-zA-Z]+$"; // ID
    private static final String PASSWORD_REGEX = "^[0-9a-zA-Z]+$"; // 密码
    private static final String EMAIL_REGEX = "^[0-9a-zA-Z]+@[0-9a-zA-Z]+(\\.[a-zA-Z]+)+$"; // 邮箱



    public static boolean isUID(CharSequence input) {
        Pattern pattern = Pattern.compile(UID_REGEX);
        Matcher matcher = pattern.matcher(input);
        return isEmail(input) ? isEmail(input): matcher.find();
    }

    public static boolean isPassword(CharSequence input) {
        Pattern pattern = Pattern.compile(PASSWORD_REGEX);
        Matcher matcher = pattern.matcher(input);
        return matcher.find();
    }

    public static boolean isEmail(CharSequence input) {
        Pattern pattern = Pattern.compile(EMAIL_REGEX);
        Matcher matcher = pattern.matcher(input);
        return matcher.find();
    }
}
