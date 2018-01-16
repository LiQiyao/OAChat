package com.yykj.oachat.service.impl;

import com.google.common.collect.Lists;
import com.yykj.oachat.common.Const;
import com.yykj.oachat.common.GenericBuilder;
import com.yykj.oachat.common.TokenFactory;
import com.yykj.oachat.common.TokenPool;
import com.yykj.oachat.dao.FriendMapper;
import com.yykj.oachat.dao.FriendRequestMapper;
import com.yykj.oachat.dao.UserInfoMapper;
import com.yykj.oachat.dto.MessageDTO;
import com.yykj.oachat.dto.data.*;
import com.yykj.oachat.entity.FriendRequest;
import com.yykj.oachat.entity.UserInfo;
import com.yykj.oachat.service.IChatLogService;
import com.yykj.oachat.service.IUserInfoService;
import com.yykj.oachat.tcpconnection.ConnectionPool;
import com.yykj.oachat.util.GsonUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Lee on 2017/11/27.
 */
@Service
public class UserInfoServiceImpl implements IUserInfoService {

    @Autowired
    private UserInfoMapper userInfoMapper;

    @Autowired
    private FriendMapper friendMapper;

    @Autowired
    private IChatLogService chatLogService;

    @Autowired
    private ConnectionPool connectionPool;

    @Autowired
    private FriendRequestMapper friendRequestMapper;

    @Override
    public MessageDTO verify(String username, String password) {
        UserInfo self = userInfoMapper.getByUsernameAndPassword(username, password);
        if (self == null){
            return GenericBuilder.of(MessageDTO::new)
                    .with(MessageDTO::setStatus, Const.Status.FAILED)
                    .build();
        }
        MessageDTO messageDTO =  GenericBuilder.of(MessageDTO::new)
                .with(MessageDTO::setStatus, Const.Status.SUCCESS)
                .with(MessageDTO::setToken, TokenFactory.generate())
                .with(MessageDTO::setUserId, self.getId())
                .build();
        return messageDTO;
    }

    @Override
    public MessageDTO<LoginResultDTO> getAllInformation(Long selfId) {
        UserInfo self = userInfoMapper.selectByPrimaryKey(selfId);
        List<UserInfo> rawFriendList = userInfoMapper.listFriendByUserId(selfId);
        List<UserDetailDTO> friendList  = Lists.newArrayList();
        Map<Long, ChatLogListDTO> chatLogMap = chatLogService.getChatLogMap(selfId);
        ChatLogListDTO chatLogListDTO;
        for (UserInfo userInfo : rawFriendList) {
            friendList.add(assembleUserDetail(userInfo, selfId));
            chatLogListDTO = chatLogMap.get(userInfo.getId());
            if (chatLogListDTO != null){
                chatLogListDTO.setFriendUsername(userInfo.getUsername());
                chatLogListDTO.setFriendIcon(userInfo.getIcon());
                chatLogListDTO.setFriendNickName(userInfo.getNickName());
            }
        }
        List<FriendRequest> friendRequestList = friendRequestMapper.listByToId(selfId);
        List<AddFriendRequestDTO> addFriendRequestDTOList = Lists.newArrayList();
        AddFriendRequestDTO addFriendRequestDTO;
        UserInfo userInfo;
        for (FriendRequest friendRequest : friendRequestList){
            userInfo = userInfoMapper.selectByPrimaryKey(friendRequest.getFromUserId());
            addFriendRequestDTO = GenericBuilder.of(AddFriendRequestDTO::new)
                    .with(AddFriendRequestDTO::setFromUserId, friendRequest.getFromUserId())
                    .with(AddFriendRequestDTO::setFromUsername, userInfo.getUsername())
                    .with(AddFriendRequestDTO::setFromIcon, userInfo.getIcon())
                    .with(AddFriendRequestDTO::setToUserId, friendRequest.getToUserId())
                    .with(AddFriendRequestDTO::setSendTime, friendRequest.getSendTime())
                    .with(AddFriendRequestDTO::setAccepted, friendRequest.getAccepted())
                    .with(AddFriendRequestDTO::setFromNickName, userInfo.getNickName())
                    .with(AddFriendRequestDTO::setFromGender, userInfo.getGender())
                    .build();
            addFriendRequestDTOList.add(addFriendRequestDTO);
        }
        LoginResultDTO loginResultDTO = GenericBuilder.of(LoginResultDTO::new)
                .with(LoginResultDTO::setSelf, self)
                .with(LoginResultDTO::setFriendList, friendList)
                .with(LoginResultDTO::setChatLogMap, chatLogMap)
                .with(LoginResultDTO::setAddFriendRequestList, addFriendRequestDTOList)
                .build();
        return GenericBuilder.of(MessageDTO<LoginResultDTO>::new)
                .with(MessageDTO::setStatus, Const.Status.SUCCESS)
                .with(MessageDTO::setData, loginResultDTO)
                .with(MessageDTO::setDataName, "loginResultDTO")
                .with(MessageDTO::setToken, TokenFactory.generate())
                .with(MessageDTO::setUserId, selfId)
                .build();
    }


    @Override
    public MessageDTO<UserDetailDTO> getUserDetail(Long userId, Long selfId) {
        UserDetailDTO userDetailDTO = assembleUserDetail(userInfoMapper.selectByPrimaryKey(userId), selfId);
        return GenericBuilder.of(MessageDTO<UserDetailDTO>::new)
                .with(MessageDTO::setStatus, Const.Status.SUCCESS)
                .with(MessageDTO::setDataName, "userDetailDTO")
                .with(MessageDTO::setData, userDetailDTO)
                .build();
    }

    @Override
    public MessageDTO register(UserInfo userInfo) {
        int resultCount = userInfoMapper.insertSelective(userInfo);
        MessageDTO messageDTO = null;
        if (resultCount > 0){
            messageDTO = GenericBuilder.of(MessageDTO::new)
                    .with(MessageDTO::setStatus, Const.Status.SUCCESS)
                    .with(MessageDTO::setStatusDetail, "注册成功！")
                    .build();
        } else {
            messageDTO = GenericBuilder.of(MessageDTO::new)
                    .with(MessageDTO::setStatus, Const.Status.FAILED)
                    .with(MessageDTO::setStatusDetail, "注册失败！")
                    .build();
        }
        return messageDTO;
    }

    @Override
    public MessageDTO update(UserInfo userInfo) {
        int resultCount = userInfoMapper.updateByPrimaryKeySelective(userInfo);
        MessageDTO messageDTO = null;
        if (resultCount > 0){
            messageDTO = GenericBuilder.of(MessageDTO::new)
                    .with(MessageDTO::setStatus, Const.Status.SUCCESS)
                    .with(MessageDTO::setStatusDetail, "更改成功！")
                    .build();
        } else {
            messageDTO = GenericBuilder.of(MessageDTO::new)
                    .with(MessageDTO::setStatus, Const.Status.FAILED)
                    .with(MessageDTO::setStatusDetail, "更改失败！")
                    .build();
        }
        return messageDTO;
    }

    @Override
    public UserDetailDTO assembleUserDetail(UserInfo userInfo, Long selfId) {
        UserDetailDTO userDetailDTO = new UserDetailDTO();
        BeanUtils.copyProperties(userInfo, userDetailDTO);
        userDetailDTO.setAlreadyFriend(friendMapper.checkRelationship(selfId, userInfo.getId()) > 0);
        return userDetailDTO;
    }

    @Override
    public MessageDTO<FoundUsersDTO> findUsersByKey(String key, Long selfId) {
        List<UserInfo> userInfoList = userInfoMapper.listFoundUsersByKey("%" + key + "%");
        List<UserDetailDTO> foundUsers = Lists.newArrayList();
        for (UserInfo userInfo : userInfoList){
            foundUsers.add(assembleUserDetail(userInfo, selfId));
        }
        FoundUsersDTO foundUsersDTO =  GenericBuilder.of(FoundUsersDTO::new)
                .with(FoundUsersDTO::setCount, foundUsers.size())
                .with(FoundUsersDTO::setUsers, foundUsers)
                .build();
        return GenericBuilder.of(MessageDTO<FoundUsersDTO>::new)
                .with(MessageDTO::setStatus, Const.Status.SUCCESS)
                .with(MessageDTO::setData, foundUsersDTO)
                .with(MessageDTO::setDataName, "foundUsersDTO")
                .build();
    }

    @Override
    public MessageDTO logout(Long userId, String token) {
        TokenPool.removeToken(token);
        connectionPool.removeChannel(userId);
        return GenericBuilder.of(MessageDTO::new)
                .with(MessageDTO::setStatus, Const.Status.SUCCESS)
                .build();
    }
}
