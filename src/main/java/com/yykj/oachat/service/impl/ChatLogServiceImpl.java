package com.yykj.oachat.service.impl;

import com.yykj.oachat.common.Const;
import com.yykj.oachat.common.GenericBuilder;
import com.yykj.oachat.common.MapValueComparator;
import com.yykj.oachat.dao.ChatLogMapper;
import com.yykj.oachat.dao.FriendMapper;
import com.yykj.oachat.dto.MessageDTO;
import com.yykj.oachat.dto.data.ChatLogListDTO;
import com.yykj.oachat.dto.data.UserDetailDTO;
import com.yykj.oachat.entity.ChatLog;
import com.yykj.oachat.service.IChatLogService;
import com.yykj.oachat.util.GsonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Created by Lee on 2017/11/27.
 */
@Service
public class ChatLogServiceImpl implements IChatLogService {

    @Autowired
    private ChatLogMapper chatLogMapper;

    @Autowired
    private FriendMapper friendMapper;

    @Override
    public MessageDTO saveChatLog(ChatLog chatLog) {
        chatLog.setSendTime(System.currentTimeMillis());
        return GenericBuilder.of(MessageDTO::new)
                .with(MessageDTO::setStatus, chatLogMapper.insertSelective(chatLog))
                .build();
    }

    @Override
    public ChatLogListDTO assembleChatLogListDTO(Long selfId, Long friendId) {
        List<ChatLog> chatLogs = chatLogMapper.listBySelfIdAndFriendId(selfId, friendId);
        int unReadChatLogCount = chatLogMapper.countUnReadBySelfIdAndFriendId(selfId, friendId);
        return GenericBuilder.of(ChatLogListDTO::new)
                .with(ChatLogListDTO::setFriendId, friendId)
                .with(ChatLogListDTO::setChatLogs, chatLogs)
                .with(ChatLogListDTO::setLastChatLogTime, chatLogs.get(chatLogs.size() - 1).getSendTime())
                .with(ChatLogListDTO::setRead, unReadChatLogCount > 0)
                .with(ChatLogListDTO::setUnReadChatLogCount, unReadChatLogCount).build();
    }

    @Override
    public Map<Long, ChatLogListDTO> getChatLogMap(Long userId) {
        List<Long> allFriendIds = friendMapper.listFriendIdUserId(userId);
        Map<Long, ChatLogListDTO> base = new HashMap<>(16);
        for (Long friendId : allFriendIds){
            base.put(friendId, assembleChatLogListDTO(userId, friendId));
        }
        System.out.println(GsonUtil.getInstance().toJson(base));
        Map<Long, ChatLogListDTO> chatLogMap = new TreeMap<>(new MapValueComparator(base));
        chatLogMap.putAll(base);
        return chatLogMap;
    }

    @Override
    public MessageDTO updateReadStatus(Long userId) {
        chatLogMapper.updateReadStatusByReceiverId(userId);
        return GenericBuilder.of(MessageDTO::new)
                .with(MessageDTO::setStatus, Const.Status.SUCCESS)
                .build();
    }
}
