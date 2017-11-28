package com.yykj.oachat.dao;

import com.yykj.oachat.entity.Friend;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface FriendMapper {
    int deleteByPrimaryKey(Long id);

    int insert(Friend record);

    int insertSelective(Friend record);

    Friend selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(Friend record);

    int updateByPrimaryKey(Friend record);

    List<Long> listFriendIdUserId(Long userId);

    int checkRelationship(@Param("selfId") Long selfId, @Param("userId") Long userId);
}