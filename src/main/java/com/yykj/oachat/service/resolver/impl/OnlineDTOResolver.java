package com.yykj.oachat.service.resolver.impl;

import com.google.gson.reflect.TypeToken;
import com.yykj.oachat.common.Const;
import com.yykj.oachat.common.GenericBuilder;
import com.yykj.oachat.common.TokenPool;
import com.yykj.oachat.dto.MessageDTO;
import com.yykj.oachat.dto.data.LoginResultDTO;
import com.yykj.oachat.dto.data.OnlineDTO;
import com.yykj.oachat.service.IUserInfoService;
import com.yykj.oachat.service.resolver.DataResolver;
import com.yykj.oachat.tcpconnection.ConnectionPool;
import com.yykj.oachat.util.GsonUtil;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.util.CharsetUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.Type;

/**
 * @author Lee
 * @date 2017/11/30
 */
@Service
public class OnlineDTOResolver implements DataResolver {

    @Autowired
    private ConnectionPool connectionPool;

    private Logger logger = LoggerFactory.getLogger(OnlineDTOResolver.class);

    @Autowired
    private IUserInfoService userInfoService;

    @Override
    public void resolve(String jsonMessage, Channel channel) {
        logger.info("收到" +  jsonMessage);
        Type objectType = new TypeToken<MessageDTO<OnlineDTO>>(){}.getType();
        MessageDTO<OnlineDTO> message = GsonUtil.getInstance().fromJson(jsonMessage, objectType);
        connectionPool.addChannel(message.getUserId(), channel);

        message.setSign(Const.Sign.RESPONSE);
        if (TokenPool.checkToken(message.getToken())){
            logger.info("id为" + message.getUserId() + "上线");
            //String sentMsgJson = GsonUtil.getInstance().toJson(userInfoService.getAllInformation(message.getUserId()));
            message.setStatus(Const.Status.SUCCESS);
            String sentMsgJson = GsonUtil.getInstance().toJson(message);
            logger.info("sentMessage" + sentMsgJson);
            channel.writeAndFlush(
                    Unpooled.copiedBuffer(sentMsgJson, CharsetUtil.UTF_8)
            );
        } else {
            message.setStatus(Const.Status.FAILED);
            channel.writeAndFlush(Unpooled.copiedBuffer(GsonUtil.getInstance().toJson(message), CharsetUtil.UTF_8));
            logger.info("id为" + message.getUserId() + "上线失败！");
            channel.close();
        }

    }
}
