package com.yykj.oachat.service.impl;

import com.google.common.collect.Lists;
import com.yykj.oachat.common.GenericBuilder;
import com.yykj.oachat.dao.FriendRequestMapper;
import com.yykj.oachat.dao.UserInfoMapper;
import com.yykj.oachat.dto.data.AddFriendRequestDTO;
import com.yykj.oachat.entity.FriendRequest;
import com.yykj.oachat.entity.UserInfo;
import com.yykj.oachat.service.IFriendRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Lee
 * @date 2017/11/28
 */
@Service
public class FriendRequestServiceImpl implements IFriendRequestService {

    @Autowired
    private FriendRequestMapper friendRequestMapper;

    @Autowired
    private UserInfoMapper userInfoMapper;

    @Override
    public boolean saveAddFriendRequest(AddFriendRequestDTO addFriendRequestDTO) {
        addFriendRequestDTO.setSendTime(System.currentTimeMillis());
        int resultCount = friendRequestMapper.insertSelective(
                GenericBuilder.of(FriendRequest::new)
                .with(FriendRequest::setFromUserId, addFriendRequestDTO.getFromUserId())
                .with(FriendRequest::setToUserId, addFriendRequestDTO.getToUserId())
                .with(FriendRequest::setSendTime, addFriendRequestDTO.getSendTime())
                .build()
        );
        return resultCount > 0;
    }

    @Override
    public List<AddFriendRequestDTO> listFriendRequest(Long userId) {
        List<FriendRequest> rawFriendRequestList = friendRequestMapper.listByToId(userId);
        List<AddFriendRequestDTO> friendRequestList = Lists.newArrayList();
        for (FriendRequest friendRequest : rawFriendRequestList){
            UserInfo userInfo = userInfoMapper.selectByPrimaryKey(friendRequest.getFromUserId());
            friendRequestList.add(
              GenericBuilder.of(AddFriendRequestDTO::new)
                    .with(AddFriendRequestDTO::setFromUserId, friendRequest.getFromUserId())
                    .with(AddFriendRequestDTO::setToUserId, friendRequest.getToUserId())
                    .with(AddFriendRequestDTO::setSendTime, friendRequest.getSendTime())
                    .with(AddFriendRequestDTO::setAccepted, friendRequest.getAccepted())
                    .with(AddFriendRequestDTO::setFromUsername, userInfo.getUsername())
                    .with(AddFriendRequestDTO::setFromNickName, userInfo.getNickName())
                    .with(AddFriendRequestDTO::setFromGender, userInfo.getGender())
                    .with(AddFriendRequestDTO::setFromIcon, userInfo.getIcon())
                    .build()
            );
        }
        return friendRequestList;
    }
}
