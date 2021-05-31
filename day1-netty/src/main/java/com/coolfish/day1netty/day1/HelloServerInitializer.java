package com.coolfish.day1netty.day1;

import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.util.CharsetUtil;

public class HelloServerInitializer<NioSocketChannel> extends ChannelInitializer {

    @Override
    protected void initChannel(Channel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();
//                                    pipeline.addLast(new HttpServerCodec());
        //6、添加具体handler,注意一点，addLast中的对象不要搞成单例，需要多实例
//                                    pipeline.addLast(new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE, 0, 4, 0, 4));
//                                    pipeline.addLast(new LengthFieldPrepender(4));
        pipeline.addLast(new StringDecoder(CharsetUtil.UTF_8));//StringDecoder将ByteBuf转换为字符串,解码器，客户端向服务器端传输数据时需要编码器，
        pipeline.addLast(new StringEncoder(CharsetUtil.UTF_8));
        // 当服务器端收到数据后需要使用解码器进行解码。
        pipeline.addLast(new HelloServerHandler<NioSocketChannel>());
    }
}
