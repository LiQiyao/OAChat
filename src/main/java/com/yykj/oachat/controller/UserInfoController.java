package com.yykj.oachat.controller;

import com.yykj.oachat.dto.MessageDTO;
import com.yykj.oachat.dto.data.LoginResultDTO;
import com.yykj.oachat.entity.UserInfo;
import com.yykj.oachat.service.IUserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author Lee
 * @date 2017/11/28
 */
@RequestMapping("/api/users/")
public class UserInfoController {

    @Autowired
    private IUserInfoService userInfoService;

    @RequestMapping(value = "login", method = RequestMethod.POST)
    @ResponseBody
    public MessageDTO<LoginResultDTO> login(String username, String password){
        return userInfoService.verify(username, password);
    }

    @RequestMapping(value = "register", method = RequestMethod.POST)
    @ResponseBody
    public MessageDTO register(UserInfo userInfo){
        return userInfoService.register(userInfo);
    }

    @RequestMapping(value = "update", method = RequestMethod.POST)
    @ResponseBody
    public MessageDTO update(UserInfo userInfo, String token, Long userId){
        userInfo.setId(userId);
        return userInfoService.update(userInfo);
    }

    @RequestMapping(value = "find", method = RequestMethod.GET)
    @ResponseBody
    public MessageDTO findUser(String key, String token, Long userId){
        return userInfoService.findUsersByKey(key, userId);
    }

    @RequestMapping(value = "detail/{targetId}", method = RequestMethod.GET)
    @ResponseBody
    public MessageDTO getDetail(@PathVariable Long targetId, String token, Long userId){
        return userInfoService.getUserDetail(targetId, userId);
    }
}