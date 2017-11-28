package com.yykj.oachat.service;

import com.yykj.oachat.dto.MessageDTO;
import com.yykj.oachat.dto.data.ChatLogListDTO;
import com.yykj.oachat.entity.ChatLog;

import java.util.Map;

/**
 * Created by Lee on 2017/11/27.
 * @author Lee
 */
public interface IChatLogService {

    MessageDTO saveChatLog(ChatLog chatLog);

    ChatLogListDTO assembleChatLogListDTO(Long selfId, Long friendId);

    Map<Long, ChatLogListDTO> getChatLogMap(Long userId);

    MessageDTO updateReadStatus(Long userId);
}
