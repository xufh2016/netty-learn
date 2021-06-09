package com.coolfish.day02;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.stream.ChunkedWriteHandler;

public class FishChannelInitialezer extends ChannelInitializer<SocketChannel> {
    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        ChannelPipeline pipeline = socketChannel.pipeline();
        pipeline.addLast(new HttpServerCodec());
        pipeline.addLast(new ChunkedWriteHandler());
        //
        pipeline.addLast(new HttpObjectAggregator(8192));
        //WebSocketServerProtocolHandler这个处理器可以完成所有的websocket繁重的操作，
        pipeline.addLast(new WebSocketServerProtocolHandler("/ws"));

        pipeline.addLast(new FishWebsocketChannelHandler());
    }
}
