package com.yykj.oachat.dto.data;

import com.yykj.oachat.entity.ChatLog;

import java.util.List;

/**
 * Created by Lee on 2017/11/26.
 * 某个好友的所有消息
 */
public class ChatLogListDTO {

    private boolean read;//所有消息是否都已读

    private Long lastChatLogTime;

    private Integer unReadChatLogCount;

    private List<ChatLog> chatLogs;//消息列表

    public boolean isRead() {
        return read;
    }

    public void setRead(boolean read) {
        this.read = read;
    }

    public List<ChatLog> getChatLogs() {
        return chatLogs;
    }

    public void setChatLogs(List<ChatLog> chatLogs) {
        this.chatLogs = chatLogs;
    }

    public Long getLastChatLogTime() {
        return lastChatLogTime;
    }

    public void setLastChatLogTime(Long lastChatLogTime) {
        this.lastChatLogTime = lastChatLogTime;
    }

    public void setUnReadChatLogCount(Integer unReadChatLogCount) {
        this.unReadChatLogCount = unReadChatLogCount;
    }
}
