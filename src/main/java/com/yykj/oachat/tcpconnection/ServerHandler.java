package com.yykj.oachat.tcpconnection;

import com.yykj.oachat.service.resolver.DataResolverProxy;
import com.yykj.oachat.util.SecurityUtil;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author Lee
 * @date 2017/11/30
 */
@Component
@ChannelHandler.Sharable
public class ServerHandler extends ChannelInboundHandlerAdapter {

    private static Logger logger = LoggerFactory.getLogger(ServerHandler.class);

    @Autowired
    private ConnectionPool connectionPool;

    @Autowired
    private DataResolverProxy dataResolverProxy;

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        logger.info(ctx.name() + "客户端上线");
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        connectionPool.removeChannel(ctx.channel());
        logger.info(ctx.name() + "客户端下线");
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf byteBuf = (ByteBuf) msg;
        byte[] req = new byte[byteBuf.readableBytes()];
        byteBuf.readBytes(req);

        //req = Base64.decodeBase64(req);
        String messageString = new String(req, "UTF-8");
        logger.info("收到加密的消息：" + messageString);
        String decodedMessage = SecurityUtil.decode(messageString);
        logger.info("解密的消息：" + messageString);
        dataResolverProxy.doAction(decodedMessage, ctx.channel());
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        connectionPool.removeChannel(ctx.channel());
        ctx.close();
        cause.printStackTrace();
        logger.error(cause.getMessage());
    }
}
