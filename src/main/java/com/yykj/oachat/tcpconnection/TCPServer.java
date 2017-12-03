package com.yykj.oachat.tcpconnection;

import com.yykj.oachat.common.Const;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

/**
 * @author Lee
 * @date 2017/11/30
 */
@Component
public class TCPServer {

    private int port = Const.PORT;

    private Logger logger = LoggerFactory.getLogger(TCPServer.class);

    @Autowired
    private SimpleChannelInitializer simpleChannelInitializer;

    private EventLoopGroup bossGroup;

    private EventLoopGroup workGroup;

    public TCPServer() {
    }

    @PostConstruct
    private void bind(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                logger.info("TCP服务器初始化===========1");
                bossGroup = new NioEventLoopGroup();
                workGroup = new NioEventLoopGroup();

                ServerBootstrap bootstrap = new ServerBootstrap();
                bootstrap.group(bossGroup, workGroup)
                        .channel(NioServerSocketChannel.class)
                        .childHandler(simpleChannelInitializer)
                        .option(ChannelOption.SO_BACKLOG, 1024);
                logger.info("TCP服务器初始化===========2");
                try {
                    ChannelFuture channelFuture = bootstrap.bind(port).sync();
                    logger.info("TCP服务器初始化===========3");
                    channelFuture.channel().closeFuture().sync();
                    logger.info("TCP服务器初始化===========4");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    bossGroup.shutdownGracefully();
                    workGroup.shutdownGracefully();
                }
            }
        }).start();
    }

    @PreDestroy
    private void closeServer(){
        bossGroup.shutdownGracefully();
        workGroup.shutdownGracefully();
    }
}
