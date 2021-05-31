package com.coolfish.day1netty.day1;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

public class HelloServer {
    public static void main(String[] args) {
        NioEventLoopGroup bossGroup = new NioEventLoopGroup();
        NioEventLoopGroup workerGroup = new NioEventLoopGroup();

        //1、启动器，负责组装netty组件，启动服务器
        try {
            ChannelFuture channelFuture = new ServerBootstrap()
                    //2、BossEventLoop、WorkEventLoop（selector，thread），group组
                    .group(bossGroup, workerGroup)
                    //3、选择服务器的ServerSocketChannel实现
                    .channel(NioServerSocketChannel.class)
                    //4、boss负责处理连接，worker（child）负责读写，childHandler决定了将来child能执行哪些操作（handler）
                    .childHandler(
                            //5、代表和客户端进行数据读写的通道Initializer初始化，负责添加别的handler
                            new HelloServerInitializer<NioSocketChannel>()).bind(8081).sync();//绑定监听端口
            channelFuture.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
        System.out.println("-------------------------------");
    }
}
