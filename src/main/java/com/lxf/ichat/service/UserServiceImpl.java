package com.lxf.ichat.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.lxf.ichat.base.BaseObservable;
import com.lxf.ichat.base.BaseService;
import com.lxf.ichat.constant.CodeConstant;
import com.lxf.ichat.constant.Constant;
import com.lxf.ichat.dto.*;
import com.lxf.ichat.exception.IllegalParameterException;
import com.lxf.ichat.mapper.FriendDTOMapper;
import com.lxf.ichat.mapper.HeadportraitDTOMapper;
import com.lxf.ichat.mapper.StatusDTOMapper;
import com.lxf.ichat.mapper.UserDTOMapper;
import com.lxf.ichat.po.ChangePasswordPO;
import com.lxf.ichat.po.MsgPO;
import com.lxf.ichat.po.UserPO;
import com.lxf.ichat.utils.ParameterVerifyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.*;

@Service
@Transactional
public class UserServiceImpl extends BaseService implements UserService, Observer {

    @Autowired
    private UserDTOMapper userDTOMapper;

    @Autowired
    private StatusDTOMapper statusDTOMapper;

    @Autowired
    private HeadportraitDTOMapper headportraitDTOMapper;

    @Autowired
    private FriendDTOMapper friendDTOMapper;

    public UserServiceImpl() {
        BaseObservable observable = BaseObservable.getBaseObservable();
        observable.addObserver(this);
    }


    @Override
    public JSONObject register(UserPO userPO) {
        MsgPO<String> msgPO = new MsgPO<>();
        UserDTO userDTO = new UserDTO();
        try {
            if (userPO.getUID().isEmpty()) {
                throw new IllegalParameterException("账号不能为空");
            }

            if (userPO.getUID().length() < 6) {
                throw new IllegalParameterException("账号长度不能小于6位");
            }

            if (!ParameterVerifyUtil.isUID(userPO.getUID())) {
                throw new IllegalParameterException("账号必须是字母或数字");
            }
            userDTO.setId(userPO.getUID());

            if (userPO.getNickname().isEmpty()) {
                throw new IllegalParameterException("昵称不能为空");
            }
            userDTO.setNickname(URLDecoder.decode(userPO.getNickname(), "UTF-8"));

            if (userPO.getPassword().isEmpty()) {
                throw new IllegalParameterException("密码不能为空");
            }
            if (userPO.getPassword().length() < 6 || userPO.getPassword().length() > 20) {
                throw new IllegalParameterException("密码长度必须是6-20位字母数字");
            }

            if (!ParameterVerifyUtil.isPassword(userPO.getPassword())) {
                throw new IllegalParameterException("密码必须是字母数字");
            }
            userDTO.setPassword(userPO.getPassword());
            userDTO.setSex(URLDecoder.decode(userPO.getSex(), "UTF-8"));

            if (userPO.getEmail() != null) {
                if (!ParameterVerifyUtil.isEmail(userPO.getEmail())) {
                    throw new IllegalParameterException("email格式不正确");
                }
                userDTO.setEmail(userPO.getEmail());
            }

            if (userPO.getPlace() != null) {
                if (userPO.getPlace().isEmpty()) {
                    throw new IllegalParameterException("地区不能为空");
                }
                userDTO.setPlace(URLDecoder.decode(userPO.getPlace(), "UTF-8"));
            }

            if (userPO.getHometown() != null) {
                if (userPO.getHometown().isEmpty()) {
                    throw new IllegalParameterException("故乡不能为空");
                }
                userDTO.setHometown(URLDecoder.decode(userPO.getHometown(), "UTF-8"));
            }

            if (userPO.getSignature() != null) {
                if (userPO.getSignature().isEmpty()) {
                    throw new IllegalParameterException("个性签名不能为空");
                }
                userDTO.setSignature(URLDecoder.decode(userPO.getSignature(), "UTF-8"));
            }

            Long regDate = new Date().getTime();
            userDTO.setRegDate(regDate);
            userDTO.setBirthday(regDate);

            // 随机设置默认头像
            HeadportraitDTOExample headportraitDTOExample = new HeadportraitDTOExample();
            HeadportraitDTOExample.Criteria criteria = headportraitDTOExample.createCriteria();
            List<HeadportraitDTO> headportraitDTOList = headportraitDTOMapper.selectByExample(headportraitDTOExample);
            int listSize = headportraitDTOList.size();
            int index = (int) (Math.random() * listSize) + 1;
            HeadportraitDTO headportraitDTO = headportraitDTOList.get(index);
            userDTO.setHeadportraitID(headportraitDTO.getId());

            try {
                // 保存注册信息
                userDTOMapper.insertSelective(userDTO);
                // 设置转态
                StatusDTO statusDTO = new StatusDTO();
                statusDTO.setId(userDTO.getId());
                statusDTO.setStatus(StatusDTO.OFFLINE);
                statusDTOMapper.insert(statusDTO);
                msgPO.setCode(CodeConstant.SUCCESS);
                msgPO.setMsg("注册成功");
            } catch (Exception e) {
                msgPO.setCode(CodeConstant.FAILED);
                msgPO.setMsg("账号已被注册");
                logger.error("插入新用户失败", e);
            }

        } catch (NullPointerException npe) {
            msgPO.setCode(CodeConstant.FAILED);
            msgPO.setMsg("请求参数不能为空");
            logger.error("请求参数为空错误", npe);
        } catch (UnsupportedEncodingException uee) {
            msgPO.setCode(CodeConstant.FAILED);
            msgPO.setMsg("不支持的编码类型");
            logger.error("不支持的编码类型错误", uee);
        } catch (IllegalParameterException ilpe) {
            msgPO.setCode(CodeConstant.FAILED);
            msgPO.setMsg(ilpe.getMessage());
            logger.error(ilpe.getMessage(), ilpe);
        }
        return (JSONObject) JSONObject.toJSON(msgPO);
    }


    @Override
    public JSONObject login(UserPO userPO) {
        MsgPO<JSONObject> msgPO = new MsgPO<>();
        try {
            if (userPO.getUID().isEmpty()) {
                throw new IllegalParameterException("账号不能为空");
            }
            if (userPO.getPassword().isEmpty()) {
                throw new IllegalParameterException("密码不能为空");
            }

            UserDTO userDTO = userDTOMapper.selectByPrimaryKey(userPO.getUID());
            if (userDTO.getPassword().equals(userPO.getPassword())) {
                StatusDTO statusDTO = statusDTOMapper.selectByPrimaryKey(userDTO.getId());
                statusDTO.setStatus(StatusDTO.ONLINE);
                try {
                    statusDTOMapper.updateByPrimaryKey(statusDTO);
                    msgPO.setCode(CodeConstant.SUCCESS);
                    msgPO.setMsg("状态修改成功");
                } catch (Exception e) {
                    msgPO.setCode(CodeConstant.FAILED);
                    msgPO.setMsg("状态修改失败");
                    logger.error("修改状态错误", e);
                }
            } else {
                throw new IllegalParameterException("密码错误");
            }
        } catch (NullPointerException npe) {
            msgPO.setCode(CodeConstant.FAILED);
            msgPO.setMsg("账号不存在");
            logger.error("账号不存在错误", npe);
        } catch (IllegalParameterException e) {
            msgPO.setCode(CodeConstant.FAILED);
            msgPO.setMsg(e.getMessage());
            logger.error(e.getMessage(), e);
        }
        return (JSONObject) JSONObject.toJSON(msgPO);
    }

    @Override
    public JSONObject logout(UserPO userPO) {
        MsgPO<JSONObject> msgPO = new MsgPO<>();
        try {
            UserDTO userDTO = userDTOMapper.selectByPrimaryKey(userPO.getUID());
            StatusDTO statusDTO = statusDTOMapper.selectByPrimaryKey(userDTO.getId());
            statusDTO.setStatus(StatusDTO.OFFLINE);
            try {
                statusDTOMapper.updateByPrimaryKey(statusDTO);
                msgPO.setCode(CodeConstant.SUCCESS);
                msgPO.setMsg("状态修改成功");
            } catch (Exception e) {
                msgPO.setCode(CodeConstant.FAILED);
                msgPO.setMsg("状态修改失败");
                logger.error("更新状态错误", e);
            }
        } catch (NullPointerException npe) {
            msgPO.setCode(CodeConstant.FAILED);
            msgPO.setMsg("退出失败");
            logger.error("账号不存在错误", npe);
        }
        return (JSONObject) JSONObject.toJSON(msgPO);
    }


    @Override
    public JSONObject updateUserInfo(UserPO userPO) {
        MsgPO<JSONObject> msgPO = new MsgPO<>();
        try {
            UserDTO userDTO = userDTOMapper.selectByPrimaryKey(userPO.getUID());

            if (userPO.getNickname() != null) {
                if (userPO.getNickname().isEmpty()) {
                    throw new IllegalParameterException("昵称不能为空");
                }
                userDTO.setNickname(URLDecoder.decode(userPO.getNickname(), "UTF-8"));
            }

            if (userPO.getSex() != null) {
                userDTO.setSex(URLDecoder.decode(userPO.getSex(), "UTF-8"));
            }

            if (userPO.getPlace() != null) {
                if (userPO.getPlace().isEmpty()) {
                    throw new IllegalParameterException("地区不能为空");
                }
                userDTO.setPlace(URLDecoder.decode(userPO.getPlace(), "UTF-8"));
            }
            if (userPO.getHometown() != null) {
                if (userPO.getHometown().isEmpty()) {
                    throw new IllegalParameterException("故乡不能为空");
                }
                userDTO.setHometown(URLDecoder.decode(userPO.getHometown(), "UTF-8"));
            }
            if (userPO.getSignature() != null) {
                if (userPO.getSignature().isEmpty()) {
                    throw new IllegalParameterException("个性签名不能为空");
                }
                userDTO.setSignature(URLDecoder.decode(userPO.getSignature(), "UTF-8"));
            }
            if (userPO.getEmail() != null) {
                if (!ParameterVerifyUtil.isEmail(userPO.getEmail())) {
                    throw new IllegalParameterException("email格式不正确");
                }
                userDTO.setEmail(URLDecoder.decode(userPO.getEmail(), "UTF-8"));
            }
            if (userPO.getBirthday() != null) {
                userDTO.setBirthday(userPO.getBirthday());
            }
            try {
                userDTOMapper.updateByPrimaryKeySelective(userDTO);
                msgPO.setCode(CodeConstant.SUCCESS);
                msgPO.setMsg("更新成功");
            } catch (Exception e) {
                msgPO.setCode(CodeConstant.FAILED);
                msgPO.setMsg("输入内容超过限制 更新失败!");
                logger.error("输入内容超过限制错误", e);
            }
        } catch (NullPointerException npe) {
            msgPO.setCode(CodeConstant.FAILED);
            msgPO.setMsg("账号不存在");
            logger.error("账号不存在错误", npe);
        } catch (UnsupportedEncodingException uee) {
            msgPO.setCode(CodeConstant.FAILED);
            msgPO.setMsg("不支持的编码类型");
            logger.error("不支持的编码类型错误", uee);
        } catch (IllegalParameterException epe) {
            msgPO.setCode(CodeConstant.FAILED);
            msgPO.setMsg(epe.getMessage());
            logger.error(epe.getMessage(), epe);
        }
        return (JSONObject) JSONObject.toJSON(msgPO);
    }


    @Override
    public JSONObject findUser(UserPO userPO) {
        MsgPO<UserPO> msgPO = new MsgPO<>();
        try {
            UserDTO userDTO = userDTOMapper.selectByPrimaryKey(userPO.getUID());
            userPO.setNickname(userDTO.getNickname());
            userPO.setSex(userDTO.getSex());
            userPO.setEmail(userDTO.getEmail());
            userPO.setPlace(userDTO.getPlace());
            userPO.setHometown(userDTO.getHometown());
            userPO.setSignature(userDTO.getSignature());
            userPO.setBirthday(userDTO.getBirthday());
            HeadportraitDTO headportraitDTO = headportraitDTOMapper.selectByPrimaryKey(userDTO.getHeadportraitID());
            userPO.setHeadportraitURL(Constant.REMOTE_URL + "images/" + headportraitDTO.getUrl());
            msgPO.setCode(CodeConstant.SUCCESS);
            msgPO.setMsg("查找成功");
            msgPO.setData(userPO);
        } catch (NullPointerException e) {
            msgPO.setCode(CodeConstant.FAILED);
            msgPO.setMsg("未查找到相关用户信息");
            logger.error("请求账号不存在错误", e);
        }
        return (JSONObject) JSONObject.toJSON(msgPO);
    }

    @Override
    public JSONObject addFriend(String UID, String FID) {
        MsgPO<JSONObject> msgPO = new MsgPO<>();
        try {
            UserDTO uDTO = userDTOMapper.selectByPrimaryKey(UID);
            UserDTO fDTO = userDTOMapper.selectByPrimaryKey(FID);
            if (uDTO.getId().equals(fDTO.getId())) {
                msgPO.setCode(CodeConstant.FAILED);
                msgPO.setMsg("不能添加自己为好友");
            } else {
                FriendDTOExample friendDTOExample = new FriendDTOExample();
                FriendDTOExample.Criteria criteria = friendDTOExample.createCriteria();
                criteria.andUidEqualTo(uDTO.getId());
                criteria.andFidEqualTo(fDTO.getId());
                List<FriendDTO> list = friendDTOMapper.selectByExample(friendDTOExample);
                if (list.size() > 0) {
                    msgPO.setCode(CodeConstant.FAILED);
                    msgPO.setMsg("该用户已经是您的好友了,不能重复添加!");
                } else {
                    try {
                        // 添加对方为好友
                        FriendDTO ufriendDTO = new FriendDTO();
                        ufriendDTO.setFid(fDTO.getId());
                        ufriendDTO.setUid(uDTO.getId());
                        friendDTOMapper.insert(ufriendDTO);
                        // 对方添加自己为好友
                        FriendDTO ffriendDTO = new FriendDTO();
                        ffriendDTO.setFid(uDTO.getId());
                        ffriendDTO.setUid(fDTO.getId());
                        friendDTOMapper.insert(ffriendDTO);
                        msgPO.setCode(CodeConstant.SUCCESS);
                        msgPO.setMsg("添加好友成功");
                    } catch (Exception e) {
                        msgPO.setCode(CodeConstant.FAILED);
                        msgPO.setMsg("添加好友失败");
                        logger.error("添加好友失败错误", e);
                    }
                }
            }
        } catch (NullPointerException e) {
            msgPO.setCode(CodeConstant.FAILED);
            msgPO.setMsg("请求账号不存在");
            logger.error("请求账号不存在错误", e);
        }
        return (JSONObject) JSONObject.toJSON(msgPO);
    }


    @Override
    public JSONObject delFriend(String UID, String FID) {
        MsgPO<JSONObject> msgPO = new MsgPO<>();
        try {
            UserDTO uDTO = userDTOMapper.selectByPrimaryKey(UID);
            UserDTO fDTO = userDTOMapper.selectByPrimaryKey(FID);
            if (uDTO.getId().equals(fDTO.getId())) {
                throw new IllegalParameterException("请求参数错误");
            } else {
                try {
                    FriendDTOExample ufriendDTOExample = new FriendDTOExample();
                    FriendDTOExample.Criteria ucriteria = ufriendDTOExample.createCriteria();
                    ucriteria.andUidEqualTo(uDTO.getId());
                    ucriteria.andFidEqualTo(fDTO.getId());
                    friendDTOMapper.deleteByExample(ufriendDTOExample);

                    FriendDTOExample ffriendDTOExample = new FriendDTOExample();
                    FriendDTOExample.Criteria fcriteria = ffriendDTOExample.createCriteria();
                    fcriteria.andUidEqualTo(fDTO.getId());
                    fcriteria.andFidEqualTo(uDTO.getId());
                    friendDTOMapper.deleteByExample(ffriendDTOExample);
                    msgPO.setCode(CodeConstant.SUCCESS);
                    msgPO.setMsg("删除好友成功");
                } catch (Exception e) {
                    msgPO.setCode(CodeConstant.FAILED);
                    msgPO.setMsg("删除好友失败");
                    logger.error("删除好友失败错误", e);
                }
            }
        } catch (NullPointerException e) {
            msgPO.setCode(CodeConstant.FAILED);
            msgPO.setMsg("请求账号不存在");
            logger.error("请求账号不存在错误", e);
        } catch (IllegalParameterException e) {
            msgPO.setCode(CodeConstant.FAILED);
            msgPO.setMsg(e.getMessage());
            logger.error(e.getMessage(), e);
        }
        return (JSONObject) JSONObject.toJSON(msgPO);
    }


    @Override
    public JSONObject changePassword(ChangePasswordPO changePasswordPO) {
        MsgPO<JSONObject> msgPO = new MsgPO<>();
        try {
            if (changePasswordPO.getuID().isEmpty()) {
                throw new IllegalParameterException("请求参数不能为空");
            }
            if (changePasswordPO.getOldPassword().isEmpty()) {
                throw new IllegalParameterException("旧密码不能为空");
            }
            if (changePasswordPO.getNewPassword().isEmpty()) {
                throw new IllegalParameterException("新密码不能为空");
            }
            if (changePasswordPO.getConfirmPassword().isEmpty()) {
                throw new IllegalParameterException("确认密码不能为空");
            }

            if (changePasswordPO.getNewPassword().length() < 6 || changePasswordPO.getNewPassword().length() > 20) {
                throw new IllegalParameterException("密码长度必须是6-20位字母数字");
            }

            if (!ParameterVerifyUtil.isPassword(changePasswordPO.getNewPassword())) {
                throw new IllegalParameterException("密码必须是字母数字");
            }

            if (!changePasswordPO.getNewPassword().equals(changePasswordPO.getConfirmPassword())) {
                throw new IllegalParameterException("确认密码不一致");
            }

            try {
                UserDTO tempDTO = new UserDTO();
                tempDTO.setId(changePasswordPO.getuID());
                tempDTO.setPassword(changePasswordPO.getOldPassword());
                UserDTO userDTO = userDTOMapper.selectByIDAndPaswd(tempDTO);

                if (userDTO.getPassword().equals(changePasswordPO.getNewPassword())) {
                    throw new IllegalParameterException("新密码不能与旧密码相同");
                }
                userDTO.setPassword(changePasswordPO.getNewPassword());
                try {
                    userDTOMapper.updateByPrimaryKeySelective(userDTO);
                    msgPO.setCode(CodeConstant.SUCCESS);
                    msgPO.setMsg("密码修改成功");
                } catch (Exception ex) {
                    msgPO.setCode(CodeConstant.FAILED);
                    msgPO.setMsg("密码长度超过20位 修改失败");
                    logger.error("修改密码错误", ex);
                }
            } catch (NullPointerException e) {
                msgPO.setCode(CodeConstant.FAILED);
                msgPO.setMsg("旧密码错误");
                logger.error("旧密码错误", e);
            }
        } catch (IllegalParameterException e) {
            msgPO.setCode(CodeConstant.FAILED);
            msgPO.setMsg(e.getMessage());
            logger.error(e.getMessage(), e);
        }
        return (JSONObject) JSONObject.toJSON(msgPO);
    }

    @Override
    public JSONObject loadAllFriend(UserPO userPO) {
        MsgPO<JSONArray> msgPO = new MsgPO<>();
        try {
            UserDTO userDTO = userDTOMapper.selectByPrimaryKey(userPO.getUID());
            Set<UserDTO> userDTOSet = userDTOMapper.selectUserFriendsPrimaryKey(userDTO.getId());
            JSONArray array = new JSONArray(0);
            for (UserDTO tempDTO : userDTOSet) {
                StatusDTO statusDTO = statusDTOMapper.selectByPrimaryKey(tempDTO.getId());
                HeadportraitDTO headportraitDTO = headportraitDTOMapper.selectByPrimaryKey(tempDTO.getHeadportraitID());
                UserPO tempPO = new UserPO();
                tempPO.setUID(tempDTO.getId());
                tempPO.setNickname(tempDTO.getNickname());
                tempPO.setSex(tempDTO.getSex());
                tempPO.setEmail(tempDTO.getEmail());
                tempPO.setPlace(tempDTO.getPlace());
                tempPO.setHometown(tempDTO.getHometown());
                tempPO.setSignature(tempDTO.getSignature());
                tempPO.setBirthday(tempDTO.getBirthday());
                tempPO.setStatus(statusDTO.getStatus());
                tempPO.setHeadportraitURL(Constant.REMOTE_URL + "images/" + headportraitDTO.getUrl());
                array.add(tempPO);
            }
            msgPO.setCode(CodeConstant.SUCCESS);
            msgPO.setMsg("加载成功");
            msgPO.setData(array);
        } catch (NullPointerException e) {
            msgPO.setCode(CodeConstant.FAILED);
            msgPO.setMsg("请求账号不存在");
            logger.error("请求账号不存在错误", e);
        }
        return (JSONObject) JSONObject.toJSON(msgPO);
    }

    @Override
    public void update(Observable o, Object arg) {
        logger.info("{}下线", arg.toString());
        try {
            String UID = (String) arg;
            UserPO userPO = new UserPO();
            userPO.setUID(UID);
            logout(userPO);
        } catch (NullPointerException e) {
            logger.error("下线错误", e);
        }
    }
}
