package com.coolfish.day02;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;

/**
 * 泛型是指handler要真正要处理的传递类型
 */

public class FishWebsocketChannelHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, TextWebSocketFrame textWebSocketFrame) throws Exception {
        String rcvMsg = textWebSocketFrame.text();
        System.out.println(rcvMsg);
    }
}