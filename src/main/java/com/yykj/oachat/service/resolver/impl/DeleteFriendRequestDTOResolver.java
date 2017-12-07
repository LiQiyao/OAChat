package com.yykj.oachat.service.resolver.impl;

import com.google.gson.reflect.TypeToken;
import com.yykj.oachat.common.Const;
import com.yykj.oachat.common.GenericBuilder;
import com.yykj.oachat.dto.MessageDTO;
import com.yykj.oachat.dto.data.DeleteFriendRequestDTO;
import com.yykj.oachat.dto.data.DeleteFriendSuccessDTO;
import com.yykj.oachat.service.IFriendService;
import com.yykj.oachat.service.resolver.DataResolver;
import com.yykj.oachat.tcpconnection.ConnectionPool;
import com.yykj.oachat.util.GsonUtil;
import io.netty.channel.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.Type;

/**
 * @author Lee
 * @date 2017/12/7
 */
@Service
public class DeleteFriendRequestDTOResolver implements DataResolver {

    private Logger logger = LoggerFactory.getLogger(OnlineDTOResolver.class);

    @Autowired
    private IFriendService friendService;

    @Autowired
    private ConnectionPool connectionPool;

    @Override
    public void resolve(String jsonMessage, Channel channel) {
        logger.info("收到" +  jsonMessage);
        Type objectType = new TypeToken<MessageDTO<DeleteFriendRequestDTO>>(){}.getType();
        MessageDTO<DeleteFriendRequestDTO> message = GsonUtil.getInstance().fromJson(jsonMessage, objectType);
        DeleteFriendRequestDTO deleteFriendRequestDTO = message.getData();
        Long friendId = deleteFriendRequestDTO.getFriendId();
        Long selfId = message.getUserId();
        friendService.deleteFriend(friendId, selfId);

        MessageDTO<DeleteFriendSuccessDTO> messageToSelf = GenericBuilder.of(MessageDTO<DeleteFriendSuccessDTO>::new)
                .with(MessageDTO::setStatus, Const.Status.SUCCESS)
                .with(MessageDTO::setSign, Const.Sign.NOTICE)
                .with(MessageDTO::setData, GenericBuilder.of(DeleteFriendSuccessDTO::new).with(DeleteFriendSuccessDTO::setDeleteFriendId, friendId).build())
                .build();
        connectionPool.getChannel(selfId).writeAndFlush(messageToSelf);

        MessageDTO<DeleteFriendSuccessDTO> messageToFriend = GenericBuilder.of(MessageDTO<DeleteFriendSuccessDTO>::new)
                .with(MessageDTO::setStatus, Const.Status.SUCCESS)
                .with(MessageDTO::setSign, Const.Sign.NOTICE)
                .with(MessageDTO::setData, GenericBuilder.of(DeleteFriendSuccessDTO::new).with(DeleteFriendSuccessDTO::setDeleteFriendId, selfId).build())
                .build();
        connectionPool.getChannel(friendId).writeAndFlush(messageToFriend);
    }
}
