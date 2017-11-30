package com.yykj.oachat.service.resolver;

import com.google.gson.Gson;
import com.yykj.oachat.dto.MessageDTO;
import com.yykj.oachat.util.GsonUtil;
import io.netty.channel.Channel;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

/**
 * Created by Lee on 2017/11/27.
 */
@Service
public class DataResolverProxy implements ApplicationContextAware{

    private ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    public void doAction(String jsonMessage, Channel channel){
        MessageDTO messageDTO = GsonUtil.getInstance().fromJson(jsonMessage, MessageDTO.class);
        String dataName = messageDTO.getDataName();
        DataResolver dataResolver = (DataResolver)applicationContext.getBean(dataName + "Resolver");
        if (dataResolver != null){
            dataResolver.resolve(jsonMessage, channel);
        }
    }
}
