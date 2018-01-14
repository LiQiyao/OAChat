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
        if (chatLogs.size() == 0){
            return null;
        }
        int unReadChatLogCount = chatLogMapper.countUnReadBySelfIdAndFriendId(selfId, friendId);
        ChatLogListDTO chatLogListDTO = GenericBuilder.of(ChatLogListDTO::new)
                .with(ChatLogListDTO::setFriendId, friendId)
                .with(ChatLogListDTO::setChatLogs, chatLogs)
                .with(ChatLogListDTO::setLastChatLogTime, chatLogs.get(chatLogs.size() - 1).getSendTime())
                .with(ChatLogListDTO::setRead, unReadChatLogCount > 0)
                .with(ChatLogListDTO::setUnReadChatLogCount, unReadChatLogCount).build();
        /*if (chatLogs.size() > 0){
            chatLogListDTO.setLastChatLogTime(chatLogs.get(chatLogs.size() - 1).getSendTime());
        } else {
            chatLogListDTO.setLastChatLogTime(0L);
        }*/
        return chatLogListDTO;
    }

    @Override
    public Map<Long, ChatLogListDTO> getChatLogMap(Long userId) {
        List<Long> allFriendIds = friendMapper.listFriendIdUserId(userId);
        Map<Long, ChatLogListDTO> base = new HashMap<>(16);
        for (Long friendId : allFriendIds){
            ChatLogListDTO chatLogListDTO = assembleChatLogListDTO(userId, friendId);
            if (chatLogListDTO != null) {
                base.put(friendId, chatLogListDTO);
            }
        }
        System.out.println(GsonUtil.getInstance().toJson(base));
        Map<Long, ChatLogListDTO> chatLogMap = new TreeMap<>(new MapValueComparator(base));
        chatLogMap.putAll(base);
        return base;
    }

    @Override
    public MessageDTO updateReadStatus(Long selfId, Long friendId) {
        chatLogMapper.updateReadStatus(friendId, selfId);
        return GenericBuilder.of(MessageDTO::new)
                .with(MessageDTO::setStatus, Const.Status.SUCCESS)
                .build();
    }

    @Override
    public MessageDTO updateReceiveStatus(Long selfId, Long friendId) {
        chatLogMapper.updateReceiveStatus(friendId, selfId);
        return GenericBuilder.of(MessageDTO::new)
                .with(MessageDTO::setStatus, Const.Status.SUCCESS)
                .build();
    }

    @Override
    public MessageDTO<ChatLogListDTO> getFriendChatLogListDTO(Long selfId, Long friendId) {
        return GenericBuilder.of(MessageDTO<ChatLogListDTO>::new)
                .with(MessageDTO::setStatus, Const.Status.SUCCESS)
                .with(MessageDTO::setData, assembleChatLogListDTO(selfId, friendId))
                .build();
    }


}
