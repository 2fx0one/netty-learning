package tfx0one.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

import java.nio.charset.Charset;

/**
 * Created by 2fx0one on 17/9/2018.
 */


public class NettyServer {
    public void bind(int port) throws Exception {
        final ByteBuf buf = Unpooled.unreleasableBuffer(Unpooled.copiedBuffer("Hi!\r\n", Charset.forName("UTF-8")));

        //配置服务端NIO线程组
        EventLoopGroup bossGroup = new NioEventLoopGroup(); // accepts an incoming connection
        EventLoopGroup workerGroup = new NioEventLoopGroup(); //handles the traffic of the accepted connection once the boss accepts the connection and registers the accepted connection to the worker

        try {
            ServerBootstrap bootstrap = new ServerBootstrap(); // helper class that sets up a server.
            bootstrap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG, 1024) //
                    .childOption(ChannelOption.SO_KEEPALIVE, true) //
                    .handler(new LoggingHandler(LogLevel.INFO))
                    .childHandler(new TimeServerChildChannelHandler());

            // 绑定端口
            ChannelFuture future = bootstrap.bind(port).sync();
            System.out.println("服务器运行成功, bind port = " + port);

            // 等待服务端监听端口关闭
            future.channel().closeFuture().sync();
        } finally {

            // 释放线程池资源
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }

    public static void main(String[] args) throws Exception{
        int port;
        if (args.length > 0) {
            port = Integer.parseInt(args[0]);
        } else {
            port = 8894;
        }
        new NettyServer().bind(port);
    }
}



