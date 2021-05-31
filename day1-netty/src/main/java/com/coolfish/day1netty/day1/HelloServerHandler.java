package com.coolfish.day1netty.day1;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;

import java.io.UnsupportedEncodingException;
import java.net.SocketAddress;

public class HelloServerHandler<NioSocketChannel> extends ChannelInboundHandlerAdapter {
    private static ChannelGroup channelGroup = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    @Override
    //读事件
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws UnsupportedEncodingException {
        Channel channel = ctx.channel();
        SocketAddress socketAddress = channel.remoteAddress();
        String string = socketAddress.toString();

        System.out.println(ctx.channel().remoteAddress() + "--------------------------------------");
        String str = (String) msg;
        System.out.println("执行channelRead~~~~~~~~~~~~");
        System.out.println("---------------Server接收到来自客户端的消息-------------" + str);

        channelGroup.forEach(item -> {
            if (item != channel) {
                item.writeAndFlush(item.remoteAddress() + "----send msg---" + str);
            } else {
                item.writeAndFlush("[self]----" + str);
            }
        });

        ctx.channel().writeAndFlush("-----------server says hi---------------------" + ctx.channel().remoteAddress());
//                                            ByteBuf byteBuf = Unpooled.copiedBuffer("Hello , This is Server!!!", CharsetUtil.UTF_8);
//                                            FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK, byteBuf);
//                                            response.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/plain");
//                                            response.headers().set(HttpHeaderNames.CONTENT_LENGTH, byteBuf.readableBytes());
//                                            ctx.writeAndFlush(response);
    }

    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        System.out.println("------------------channelRegistered---------");
    }

    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
        System.out.println("-----------channelUnregistered----------");
    }

    @Override
    //该方法表示通道已连接，连接好之后会自动回调
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        channelGroup.writeAndFlush("---------------------[Server]-" + channel.remoteAddress() + "----------join in----------");

        channelGroup.add(channel);
        System.out.println("--------------channelActive------------");
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        System.out.println("----------channelInactive--------");
        channelGroup.writeAndFlush("[Server]-" + channel.remoteAddress() + "-----left----");
        channelGroup.remove(channel);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        System.out.println("---------------channelReadComplete--------------");
    }

    @Override
    public void channelWritabilityChanged(ChannelHandlerContext ctx) throws Exception {
        System.out.println("------------channelWritabilityChanged---------");
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        System.out.println("---------------userEventTriggered--------------");
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        System.out.println("-----------exceptionCaught----------");
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        System.out.println("--------------handlerRemoved------------");
    }

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        System.out.println("---------------handlerAdded------------");
    }
}
