package com.yykj.oachat.tcpconnection;

import com.yykj.oachat.common.Const;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.springframework.stereotype.Component;

/**
 * @author Lee
 * @date 2017/11/30
 */
@Component
public class TCPServer {

    private int port = Const.PORT;

    public TCPServer() {
    }

    private void bind(){
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workGroup = new NioEventLoopGroup();

        ServerBootstrap bootstrap = new ServerBootstrap();
        bootstrap.group(bossGroup, workGroup)
                .channel(NioServerSocketChannel.class)
                .childHandler(new SimpleChannelInitializer())
                .option(ChannelOption.SO_BACKLOG, 1024);
        try {
            ChannelFuture channelFuture = bootstrap.bind(port).sync();

        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            bossGroup.shutdownGracefully();
            workGroup.shutdownGracefully();
        }

    }
}
