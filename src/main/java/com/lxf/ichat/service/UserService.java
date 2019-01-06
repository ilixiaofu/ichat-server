package com.lxf.ichat.service;

import com.alibaba.fastjson.JSONObject;
import com.lxf.ichat.po.ChangePasswordPO;
import com.lxf.ichat.po.UserPO;

public interface UserService {

    public JSONObject register(UserPO userPO);

    public JSONObject login(UserPO userPO);

    public JSONObject loadAllFriend(UserPO userPO);

    public JSONObject findUser(UserPO userPO);

    public JSONObject updateUserInfo(UserPO userPO);

    public JSONObject logout(UserPO userPO);

    public JSONObject addFriend(String UID, String FID);

    public JSONObject delFriend(String UID, String FID);

    public JSONObject changePassword(ChangePasswordPO changePasswordPO);
}
