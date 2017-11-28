package com.yykj.oachat.dao;

import com.yykj.oachat.entity.FriendRequest;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface FriendRequestMapper {
    int deleteByPrimaryKey(Long id);

    int insert(FriendRequest record);

    int insertSelective(FriendRequest record);

    FriendRequest selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(FriendRequest record);

    int updateByPrimaryKey(FriendRequest record);

    List<FriendRequest> listByToId(Long toId);

    int updateAcceptedByFromIdAndToId(@Param("fromId") Long fromId, @Param("toId") Long toId);
}