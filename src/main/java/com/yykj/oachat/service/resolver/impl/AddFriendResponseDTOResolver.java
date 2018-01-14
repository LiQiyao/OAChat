package com.yykj.oachat.service.resolver.impl;

import com.google.gson.reflect.TypeToken;
import com.yykj.oachat.common.Const;
import com.yykj.oachat.common.GenericBuilder;
import com.yykj.oachat.dao.FriendRequestMapper;
import com.yykj.oachat.dto.MessageDTO;
import com.yykj.oachat.dto.data.AddFriendResponseDTO;
import com.yykj.oachat.dto.data.AddFriendSuccessDTO;
import com.yykj.oachat.dto.data.UserDetailDTO;
import com.yykj.oachat.service.IFriendRequestService;
import com.yykj.oachat.service.IFriendService;
import com.yykj.oachat.service.IUserInfoService;
import com.yykj.oachat.service.resolver.DataResolver;
import com.yykj.oachat.tcpconnection.ConnectionPool;
import com.yykj.oachat.util.GsonUtil;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.util.CharsetUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Type;

/**
 * @author Lee
 * @date 2017/11/29
 */
@Service
public class AddFriendResponseDTOResolver implements DataResolver{

    @Autowired
    private ConnectionPool connectionPool;

    @Autowired
    private IFriendService friendService;

    @Autowired
    private FriendRequestMapper friendRequestMapper;

    @Autowired
    private IUserInfoService userInfoService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void resolve(String jsonMessage, Channel channel) {
        Type objectType = new TypeToken<MessageDTO<AddFriendResponseDTO>>(){}.getType();
        MessageDTO<AddFriendResponseDTO> message = GsonUtil.getInstance().fromJson(jsonMessage, objectType);
        AddFriendResponseDTO addFriendResponseDTO = message.getData();

        Long toUserId = addFriendResponseDTO.getToUserId();
        Long fromUserId = addFriendResponseDTO.getFromUserId();

        friendRequestMapper.updateAcceptedByFromIdAndToId(fromUserId, toUserId);
        if (friendService.checkFriendShipExistence(fromUserId, toUserId)){
            return;
        }
        friendService.addFriendRelationship(addFriendResponseDTO.getFromUserId(), addFriendResponseDTO.getToUserId());

        UserDetailDTO fromUserDetail = userInfoService.getUserDetail(fromUserId, toUserId).getData();
        UserDetailDTO toUserDetail = userInfoService.getUserDetail(toUserId, fromUserId).getData();
        MessageDTO<AddFriendSuccessDTO> fromUserMessage = GenericBuilder.of(MessageDTO<AddFriendSuccessDTO>::new)
                .with(MessageDTO::setData, new AddFriendSuccessDTO(toUserDetail))
                .with(MessageDTO::setDataName, "addFriendSuccessDTO")
                .with(MessageDTO::setStatus, Const.Status.SUCCESS)
                .with(MessageDTO::setStatusDetail, "您已和" + toUserDetail.getUsername() + "成为好友啦")
                .build();
        MessageDTO<AddFriendSuccessDTO> toUserMessage = GenericBuilder.of(MessageDTO<AddFriendSuccessDTO>::new)
                .with(MessageDTO::setData, new AddFriendSuccessDTO(fromUserDetail))
                .with(MessageDTO::setDataName, "addFriendSuccessDTO")
                .with(MessageDTO::setStatus, Const.Status.SUCCESS)
                .with(MessageDTO::setStatusDetail, "您已和" + fromUserDetail.getUsername() + "成为好友啦")
                .build();

        Channel toUserChannel = connectionPool.getChannel(toUserId);
        Channel fromUserChannel = connectionPool.getChannel(fromUserId);



        if (toUserChannel != null){
            toUserChannel.writeAndFlush(Unpooled.copiedBuffer(GsonUtil.getInstance().toJson(toUserMessage), CharsetUtil.UTF_8));
        }
        if (fromUserChannel != null){
            fromUserChannel.writeAndFlush(Unpooled.copiedBuffer(GsonUtil.getInstance().toJson(fromUserMessage), CharsetUtil.UTF_8));
        }
    }
}
