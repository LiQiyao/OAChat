package com.yykj.oachat.service.resolver.impl;

import com.google.gson.reflect.TypeToken;
import com.yykj.oachat.dto.MessageDTO;
import com.yykj.oachat.dto.data.OnlineDTO;
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
 * @date 2017/11/30
 */
@Service
public class OnlineResovler implements DataResolver {

    @Autowired
    private ConnectionPool connectionPool;

    @Autowired
    private Logger logger = LoggerFactory.getLogger(OnlineResovler.class);

    @Override
    public void resolve(String jsonMessage, Channel channel) {
        Type objectType = new TypeToken<MessageDTO<OnlineDTO>>(){}.getType();
        MessageDTO<OnlineDTO> message = GsonUtil.getInstance().fromJson(jsonMessage, objectType);
        connectionPool.addChannel(message.getUserId(), channel);
        logger.info("id为" + message.getUserId() + "上线");
    }
}
