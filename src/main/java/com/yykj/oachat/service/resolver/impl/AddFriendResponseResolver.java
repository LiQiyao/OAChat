package com.yykj.oachat.service.resolver.impl;

import com.google.gson.reflect.TypeToken;
import com.yykj.oachat.dto.MessageDTO;
import com.yykj.oachat.dto.data.AddFriendResponseDTO;
import com.yykj.oachat.service.IFriendRequestService;
import com.yykj.oachat.service.IFriendService;
import com.yykj.oachat.service.resolver.DataResolver;
import com.yykj.oachat.tcpconnection.ConnectionPool;
import com.yykj.oachat.util.GsonUtil;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.util.CharsetUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.Type;

/**
 * @author Lee
 * @date 2017/11/29
 */
@Service
public class AddFriendResponseResolver implements DataResolver{

    @Autowired
    private ConnectionPool connectionPool;

    @Autowired
    private IFriendService friendService;

    @Override
    public void resolve(String jsonMessage, Channel channel) {
        Type objectType = new TypeToken<MessageDTO<AddFriendResponseDTO>>(){}.getType();
        MessageDTO<AddFriendResponseDTO> message = GsonUtil.getInstance().fromJson(jsonMessage, objectType);
        AddFriendResponseDTO addFriendResponseDTO = message.getData();

        Channel targetChannel1 = connectionPool.getChannel(addFriendResponseDTO.getToUserId());
        Channel targetChannel2 = connectionPool.getChannel(addFriendResponseDTO.getFromUserId());
        friendService.addFriendRelationship(addFriendResponseDTO.getFromUserId(), addFriendResponseDTO.getToUserId());

        /*
        if (targetChannel1 != null){
            targetChannel1.writeAndFlush(Unpooled.copiedBuffer(GsonUtil.getInstance().toJson(newMessage), CharsetUtil.UTF_8));
        }
        if (targetChannel2 != null){
            targetChannel2.writeAndFlush(Unpooled.copiedBuffer(GsonUtil.getInstance().toJson(newMessage), CharsetUtil.UTF_8));
        }*/
    }
}
