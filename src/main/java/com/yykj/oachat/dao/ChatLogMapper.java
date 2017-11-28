package com.yykj.oachat.dao;

import com.yykj.oachat.entity.ChatLog;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ChatLogMapper {
    int deleteByPrimaryKey(Long id);

    int insert(ChatLog record);

    int insertSelective(ChatLog record);

    ChatLog selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(ChatLog record);

    int updateByPrimaryKey(ChatLog record);

    List<ChatLog> listBySelfIdAndFriendId(@Param("selfId") Long selfId, @Param("friendId") Long friendId);

    int countUnReadBySelfIdAndFriendId(@Param("selfId") Long selfId, @Param("friendId") Long friendId);

    int updateReadStatusByReceiverId(Long receiverId);
}