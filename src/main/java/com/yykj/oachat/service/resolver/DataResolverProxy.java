package com.yykj.oachat.service.resolver;

import com.google.gson.Gson;
import com.yykj.oachat.dto.MessageDTO;
import com.yykj.oachat.util.GsonUtil;
import io.netty.channel.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

/**
 * Created by Lee on 2017/11/27.
 * @author Lee
 */
@Service
public class DataResolverProxy implements ApplicationContextAware{

    private ApplicationContext applicationContext;

    private Logger logger = LoggerFactory.getLogger(DataResolverProxy.class);

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    public void doAction(String jsonMessage, Channel channel){
        logger.info("resolverProxy收到消息" + jsonMessage);
        MessageDTO messageDTO = GsonUtil.getInstance().fromJson(jsonMessage, MessageDTO.class);
        String dataName = messageDTO.getDataName();
        DataResolver dataResolver = (DataResolver)applicationContext.getBean(dataName + "Resolver");
        if (dataResolver != null){
            dataResolver.resolve(jsonMessage, channel);
        }
    }
}
