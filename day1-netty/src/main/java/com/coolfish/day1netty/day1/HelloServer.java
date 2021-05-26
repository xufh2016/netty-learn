package com.coolfish.day1netty.day1;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;

public class HelloServer {
    public static void main(String[] args) {
        //1、启动器，负责组装netty组件，启动服务器
        new ServerBootstrap()
                //2、BossEventLoop、WorkEventLoop（selector，thread），group组
                .group(new NioEventLoopGroup())
                //3、选择服务器的ServerSocketChannel实现
                .channel(NioServerSocketChannel.class)
                //4、boss负责处理连接，worker（child）负责读写，childHandler决定了将来child能执行哪些操作（handler）
                .childHandler(
                        //5、代表和客户端进行数据读写的通道Initializer初始化，负责添加别的handler
                        new ChannelInitializer<NioSocketChannel>() {

                            @Override
                            protected void initChannel(NioSocketChannel ch) {
                                //6、添加具体handler
                                ch.pipeline().addLast(new StringDecoder());//StringDecoder将ByteBuf转换为字符串,解码器，客户端向服务器端传输数据时需要编码器，
                                                                           // 当服务器端收到数据后需要使用解码器进行解码。
                                ch.pipeline().addLast(new ChannelInboundHandlerAdapter() {//自定义handler
                                    @Override
                                    //读事件
                                    public void channelRead(ChannelHandlerContext ctx, Object msg) {
                                        String message = (String) msg;
                                        System.out.println("---------------Server-------------" + message);
                                    }
                                });
                            }

                        }).bind(8081);//绑定监听端口
        System.out.println("-------------------------------");
    }
}
