package com.coolfish.day1netty.day1;

import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.util.Timer;

import java.util.concurrent.TimeUnit;

public class TestEventLoop {
    public static void main(String[] args) {
        //1.创建事件循环组
        EventLoopGroup eventLoopGroup = new NioEventLoopGroup(2);//io事件、普通任务、定时任务
//        EventLoopGroup eventLoopGroup = new DefaultEventLoop();//普通任务、定时任务
        //2.获取下一个事件循环对象
        System.out.println(eventLoopGroup.next());
        System.out.println(eventLoopGroup.next());
        System.out.println(eventLoopGroup.next());
//        System.out.println(eventLoopGroup.next());
//        System.out.println(eventLoopGroup.next());
//        System.out.println(eventLoopGroup.next());
//        System.out.println(eventLoopGroup.next());
//        System.out.println(eventLoopGroup.next());
//        System.out.println(eventLoopGroup.next());
//        System.out.println(eventLoopGroup.next());
        //3.执行普通任务
        eventLoopGroup.next().execute(()->{
            try {
                Thread.sleep(1000);
                System.out.println("---------------普通任务-------------");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        //4.定时任务，
        eventLoopGroup.next().scheduleAtFixedRate(()->{
            System.out.println("------------------定时任务--------------");
        },10, 1000,TimeUnit.SECONDS);
    }
}
