package test;

import com.lxf.ichat.utils.ParameterVerifyUtil;
import org.junit.Test;

public class ParameterVerifyUtilTest {

    @Test
    public void isUID() {
        String UID = "11111";
        System.out.println(ParameterVerifyUtil.isUID(UID));
    }

    @Test
    public void isPassword() {
        String password = "11zzzz111";
        System.out.println(ParameterVerifyUtil.isPassword(password));
    }

    @Test
    public void isEmail() {
        String email = "122032124fgfdg8@qq.com";
        System.out.println(ParameterVerifyUtil.isEmail(email));
    }
}