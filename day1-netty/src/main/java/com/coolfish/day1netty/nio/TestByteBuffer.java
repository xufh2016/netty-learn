package com.coolfish.day1netty.nio;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class TestByteBuffer {
    public static void main(String[] args) {
        try {
            FileChannel channel = new FileInputStream("day1-netty/data.txt").getChannel();
            ByteBuffer byteBuffer = ByteBuffer.allocate(5);
            while (true) {
                int line = channel.read(byteBuffer);//返回值是读到的实际字节数，如果是-1表示eos，
                if (line == -1) {
                    break;
                }
                //打印buffer的内容，也就是写出数据
                byteBuffer.flip();//切换至读模式
                while (byteBuffer.hasRemaining()) {
                    byte b = byteBuffer.get();//写
                    System.out.println((char) b);
                }
                //切换为写模式
                byteBuffer.clear();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
