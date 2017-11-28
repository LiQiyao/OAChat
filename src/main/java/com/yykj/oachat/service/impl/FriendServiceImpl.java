package com.yykj.oachat.service.impl;

import com.yykj.oachat.common.GenericBuilder;
import com.yykj.oachat.dao.FriendMapper;
import com.yykj.oachat.dao.FriendRequestMapper;
import com.yykj.oachat.dto.data.AddFriendRequestDTO;
import com.yykj.oachat.entity.Friend;
import com.yykj.oachat.entity.FriendRequest;
import com.yykj.oachat.service.IFriendService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by Lee on 2017/11/27.
 */
public class FriendServiceImpl implements IFriendService {

    @Autowired
    private FriendMapper friendMapper;

    @Autowired
    private FriendRequestMapper friendRequestMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean addFriendRelationship(Long fromId, Long toId) {
        int resultCount1 = friendMapper.insertSelective(GenericBuilder.of(Friend::new)
                .with(Friend::setUser1Id, fromId)
                .with(Friend::setUser2Id, toId)
                .with(Friend::setCreateTime, System.currentTimeMillis())
                .build()
        );
        int resultCount2 = friendRequestMapper.updateAcceptedByFromIdAndToId(fromId, toId);
        return resultCount1 > 0 && resultCount2 > 0;
    }

    @Override
    public boolean deleteFriend(Long targetId, Long selfId) {
        return false;
    }
}
