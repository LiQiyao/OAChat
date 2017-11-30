package com.yykj.oachat.tcpconnection;

import com.google.common.collect.Maps;
import org.springframework.stereotype.Component;

import io.netty.channel.Channel;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Lee
 * @date 2017/11/29
 */
@Component
public class ConnectionPool {

    private ConcurrentHashMap<Long, Channel> channelMap = new ConcurrentHashMap<>();

    public void addChannel(Long userId, Channel channel){
        channelMap.put(userId, channel);
    }

    public void removeChannel(Long userId){
        channelMap.remove(userId);
    }

    public void removeChannel(Channel channel){
        if (channelMap.contains(channel)){
            channelMap.remove(channel);
        }
    }

    public Channel getChannel(Long userId){
        return channelMap.get(userId);
    }
}
