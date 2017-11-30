package com.yykj.oachat.service.resolver.impl;

import com.google.gson.reflect.TypeToken;
import com.yykj.oachat.dao.ChatLogMapper;
import com.yykj.oachat.dto.MessageDTO;
import com.yykj.oachat.dto.data.ChatLogListDTO;
import com.yykj.oachat.entity.ChatLog;
import com.yykj.oachat.service.IChatLogService;
import com.yykj.oachat.service.resolver.DataResolver;
import com.yykj.oachat.tcpconnection.ConnectionPool;
import com.yykj.oachat.util.GsonUtil;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.util.CharsetUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.Type;
import java.util.Date;

/**
 * @author Lee
 * @date 2017/11/29
 */
@Service
public class ChatLogResolver implements DataResolver {

    @Autowired
    private ConnectionPool connectionPool;

    @Autowired
    private ChatLogMapper chatLogMapper;

    @Override
    public void resolve(String jsonMessage, Channel channel) {
        Type objectType = new TypeToken<MessageDTO<ChatLog>>(){}.getType();
        MessageDTO<ChatLog> message = GsonUtil.getInstance().fromJson(jsonMessage, objectType);
        ChatLog chatLog = message.getData();
        chatLog.setSendTime(System.currentTimeMillis());
        chatLogMapper.insertSelective(chatLog);
        Channel targetChannel = connectionPool.getChannel(chatLog.getReceiverId());
        if (targetChannel != null){
            targetChannel.writeAndFlush(Unpooled.copiedBuffer(GsonUtil.getInstance().toJson(message), CharsetUtil.UTF_8));
        }
    }
}
