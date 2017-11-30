package com.yykj.oachat.service.resolver;

import io.netty.channel.Channel;

/**
 * Created by Lee on 2017/11/27.
 */
public interface DataResolver {

    void resolve(String jsonMessage, Channel channel);
}
