package com.yykj.oachat.service.resolver.impl;

import com.google.gson.reflect.TypeToken;
import com.yykj.oachat.dto.MessageDTO;
import com.yykj.oachat.dto.data.AddFriendRequestDTO;
import com.yykj.oachat.service.IFriendRequestService;
import com.yykj.oachat.service.resolver.DataResolver;
import com.yykj.oachat.tcpconnection.ConnectionPool;
import com.yykj.oachat.util.GsonUtil;
import com.yykj.oachat.util.SecurityUtil;
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
public class AddFriendRequestDTOResolver implements DataResolver {

    @Autowired
    private ConnectionPool connectionPool;

    @Autowired
    private IFriendRequestService friendRequestService;

    @Override
    public void resolve(String jsonMessage, Channel channel) {
        Type objectType = new TypeToken<MessageDTO<AddFriendRequestDTO>>(){}.getType();
        MessageDTO<AddFriendRequestDTO> message = GsonUtil.getInstance().fromJson(jsonMessage, objectType);
        AddFriendRequestDTO addFriendRequestDTO = message.getData();
        if (friendRequestService.checkExistence(addFriendRequestDTO.getFromUserId(), addFriendRequestDTO.getToUserId())){
            return;
        }
        addFriendRequestDTO.setSendTime(System.currentTimeMillis());
        friendRequestService.saveAddFriendRequest(addFriendRequestDTO);
        Channel targetChannel = connectionPool.getChannel(addFriendRequestDTO.getToUserId());
        if (targetChannel != null){
            targetChannel.writeAndFlush(Unpooled.copiedBuffer(SecurityUtil.encode(GsonUtil.getInstance().toJson(message)), CharsetUtil.UTF_8));
        }
    }
}
