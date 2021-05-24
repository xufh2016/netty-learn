package com.coolfish.day1netty;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

@SpringBootTest
class Day1NettyApplicationTests {


    @Test
    void contextLoads() {
    }

    @Test
    public void testServer() throws IOException {
        //1、创建ServerSocketChannel对象
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        //2、监听指定端口号，
        serverSocketChannel.socket().bind(new InetSocketAddress(9999));
        //3.构建一个Buffer对象，用于存储读取的数据
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        //4.等待客户端连接
        while (true){
            SocketChannel socketChannel = serverSocketChannel.accept();
            socketChannel.read(buffer);
            buffer.flip();
            System.out.println("-------Server RCV-----------:"+new String(buffer.array()));
            socketChannel.close();
        }
    }
    @Test
    public void testClient() throws IOException {
        SocketChannel socketChannel = SocketChannel.open();
        socketChannel.connect(new InetSocketAddress("127.0.0.1",9999));
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        buffer.put("hello server".getBytes());
        buffer.flip();
        socketChannel.write(buffer);
        socketChannel.close();
    }

}
