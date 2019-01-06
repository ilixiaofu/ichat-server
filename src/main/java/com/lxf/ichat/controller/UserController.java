package com.lxf.ichat.controller;


import com.alibaba.fastjson.JSONObject;
import com.lxf.ichat.base.BaseController;
import com.lxf.ichat.po.ChangePasswordPO;
import com.lxf.ichat.po.UserPO;
import com.lxf.ichat.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Controller
public class UserController extends BaseController {

    @Autowired
    private UserService userService;

    @RequestMapping(value = "register.do")
    public void register(HttpServletRequest request, HttpServletResponse response) {
        String parameter = request.getParameter("ichat_register");
        logger.info("请求参数:{}", parameter);
        UserPO userPO = JSONObject.parseObject(parameter, UserPO.class);
        JSONObject data = userService.register(userPO);
        logger.info("返回数据:{}", data);
        try {
            response.getWriter().write(data.toJSONString());
        } catch (IOException e) {
            logger.error("", e.getMessage());
        }
    }

    // http://localhost:8080/ichat/login.do?uNo=1&password=1
    @RequestMapping(value = "login.do")
    public void login(HttpServletRequest request, HttpServletResponse response) {
        String UID = request.getParameter("UID");
        String password = request.getParameter("password");
        UserPO userPo = new UserPO();
        userPo.setUID(UID);
        userPo.setPassword(password);
        logger.info("请求参数:{}", userPo);
        JSONObject data = userService.login(userPo);
        logger.info("返回数据:{}", data);
        try {
            response.getWriter().write(data.toJSONString());
        } catch (IOException e) {
            logger.error("", e.getMessage());
        }
    }

    // http://localhost:8080/ichat/logout.do?uNo=111
    @RequestMapping(value = "logout.do")
    public void logout(HttpServletRequest request, HttpServletResponse response) {
        String UID = request.getParameter("UID");
        logger.info("请求参数:{}", UID);
        UserPO userPo = new UserPO();
        userPo.setUID(UID);
        JSONObject data = userService.logout(userPo);
        logger.info("返回数据:{}", data);
        try {
            response.getWriter().write(data.toJSONString());
        } catch (IOException e) {
            logger.error("", e.getMessage());
        }
    }

    @RequestMapping(value = "updateUserInfo.do")
    public void updateUserInfo(HttpServletRequest request, HttpServletResponse response) {
        String parameter = request.getParameter("ichat_updateUserInfo");
        logger.info("请求参数:{}", parameter);
        UserPO userPO = JSONObject.parseObject(parameter, UserPO.class);
        JSONObject data = userService.updateUserInfo(userPO);
        logger.info("返回数据:{}", data);
        try {
            response.getWriter().write(data.toJSONString());
        } catch (IOException e) {
            logger.error("", e.getMessage());
        }
    }

    // http://localhost:8080/ichat/loadUserInfo.do?uNo=123
    @RequestMapping(value = "loadUserInfo.do")
    public void loadUserInfo(HttpServletRequest request, HttpServletResponse response) {
        String UID = request.getParameter("UID");
        logger.info("请求参数:{}", UID);
        UserPO userPo = new UserPO();
        userPo.setUID(UID);
        JSONObject data = userService.findUser(userPo);
        logger.info("返回数据:{}", data);
        try {
            response.getWriter().write(data.toJSONString());
        } catch (IOException e) {
            logger.error("", e.getMessage());
        }
    }


    // http://localhost:8080/ichat/loadFriends.do?uNo=1220324958
    @RequestMapping(value = "loadFriends.do")
    public void loadAllFriend(HttpServletRequest request, HttpServletResponse response) {
        String UID = request.getParameter("UID");
        logger.info("请求参数:{}", UID);
        UserPO userPo = new UserPO();
        userPo.setUID(UID);
        JSONObject data = userService.loadAllFriend(userPo);
        logger.info("返回数据:{}", data);
        try {
            response.getWriter().write(data.toJSONString());
        } catch (IOException e) {
            logger.error("", e.getMessage());
        }
    }

    // http://localhost:8080/ichat/addFriend.do?FID=1220324958&FID=123456789
    @RequestMapping(value = "addFriend.do")
    public void addFrind(HttpServletRequest request, HttpServletResponse response) {
        String UID = request.getParameter("UID");
        String FID = request.getParameter("FID");
        logger.info("请求参数:{} {}", UID, FID);
        JSONObject data = userService.addFriend(UID, FID);
        logger.info("{}", data);
        logger.info("返回数据:{}", data);
        try {
            response.getWriter().write(data.toJSONString());
        } catch (IOException e) {
            logger.error("", e.getMessage());
        }
    }

    // http://localhost:8080/ichat/delFriend.do?UID=1220324958&FID=123456789
    @RequestMapping(value = "delFriend.do")
    public void delFrind(HttpServletRequest request, HttpServletResponse response) {
        String UID = request.getParameter("UID");
        String FID = request.getParameter("FID");
        logger.info("请求参数:{} {}", UID, FID);
        JSONObject data = userService.delFriend(UID, FID);
        logger.info("返回数据:{}", data);
        try {
            response.getWriter().write(data.toJSONString());
        } catch (IOException e) {
            logger.error("", e.getMessage());
        }
    }

    @RequestMapping(value = "changePassword.do")
    public void changePassword(HttpServletRequest request, HttpServletResponse response) {
        String parameter = request.getParameter("ichat_changePassword");
        logger.info("请求参数:{}", parameter);
        ChangePasswordPO changePasswordPO = JSONObject.parseObject(parameter, ChangePasswordPO.class);
        JSONObject data = userService.changePassword(changePasswordPO);
        logger.info("返回数据:{}", data);
        try {
            response.getWriter().write(data.toJSONString());
        } catch (IOException e) {
            logger.error("", e.getMessage());
        }
    }

}
