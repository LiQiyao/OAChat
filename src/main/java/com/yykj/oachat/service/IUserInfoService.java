package com.yykj.oachat.service;

import com.yykj.oachat.dto.MessageDTO;
import com.yykj.oachat.dto.data.FoundUsersDTO;
import com.yykj.oachat.dto.data.LoginResultDTO;
import com.yykj.oachat.dto.data.UserDetailDTO;
import com.yykj.oachat.entity.UserInfo;

import java.util.List;

/**
 * Created by Lee on 2017/11/27.
 * @author lee
 * 关于用户的业务逻辑
 */
public interface IUserInfoService {

    MessageDTO verify(String username, String password);

    MessageDTO<LoginResultDTO> getAllInformation(Long selfId);

    MessageDTO<UserDetailDTO> getUserDetail(Long userId, Long selfId);

    MessageDTO register(UserInfo userInfo);

    MessageDTO update(UserInfo userInfo);

    UserDetailDTO assembleUserDetail(UserInfo userInfo, Long selfId);

    MessageDTO<FoundUsersDTO> findUsersByKey(String key, Long selfId);

    MessageDTO logout(Long userId, String token);
}
