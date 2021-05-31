package com.coolfish.day1netty.day1;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.codec.http.*;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.util.CharsetUtil;

import java.io.UnsupportedEncodingException;
import java.net.SocketAddress;
import java.nio.charset.Charset;
import java.util.concurrent.ConcurrentHashMap;

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
                            new ChannelInitializer<NioSocketChannel>() {

                                @Override
                                protected void initChannel(NioSocketChannel ch) {
                                    ChannelPipeline pipeline = ch.pipeline();
//                                    pipeline.addLast(new HttpServerCodec());
                                    //6、添加具体handler,注意一点，addLast中的对象不要搞成单例，需要多实例
//                                    pipeline.addLast(new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE, 0, 4, 0, 4));
//                                    pipeline.addLast(new LengthFieldPrepender(4));
                                    pipeline.addLast(new StringDecoder(CharsetUtil.UTF_8));//StringDecoder将ByteBuf转换为字符串,解码器，客户端向服务器端传输数据时需要编码器，
                                    pipeline.addLast(new StringEncoder(CharsetUtil.UTF_8));
                                    // 当服务器端收到数据后需要使用解码器进行解码。
                                    pipeline.addLast(new ChannelInboundHandlerAdapter() {//自定义handler

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


                                            ctx.channel().writeAndFlush("-----------server says hi---------------------");
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
                                            System.out.println("--------------channelActive------------");
                                        }

                                        @Override
                                        public void channelInactive(ChannelHandlerContext ctx) throws Exception {
                                            System.out.println("----------channelInactive--------");
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
                                    });
                                }
                            }).bind(8081).sync();//绑定监听端口

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
