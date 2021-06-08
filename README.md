# netty-learn
这是一个用于netty学习的工程

##NIO基础
1. 三大组件  
   + Channel & Buffer  
     channel有点类似于stream，它就是读写数据的双向通道，可以从channel将数据读入buffer，也可以将buffer中的数据写入到channel
     中，而stream只能完成一种
   + 常见的Channel有  
       + FileChannel  
       + DatagramChannel （用于UDP网络编程）
       + SocketChannel    （用于TCP网络编程，客户端和服务器端都能用）
       + ServerSocketChannel  （用于TCP网络编程，专用与服务器端）
   + 常见的Buffer，用来缓冲读写数据。
     + ByteBuffer
       + MappedByteBuffer
       + DirectByteBuffer
       + HeapByteBuffer
     + ShortBuffer
     + IntBuffer
     + LongBuffer
     + FloatBuffer
     + DoubleBuffer
     + CharBuffer
   + Selector选择器  
      Selector的作用就是配合一个线程来管理多个channel，获取这些channel上发生的事件，这些channel工作再非阻塞模式下，不会让线程
      吊死在一个channel上。社和连接数特别多，但流量低的场景。
   + Channel通过Buffer(缓冲区)进行读写操作。read()表示读取通道数据到缓冲区，write()表示把缓冲区数据写入到通道。
     + read()  //从Buffer中读取数据。
     + write() //写入数据到Buffer中。
     + map()   //把管道中部分数据或者全部数据映射成MappedByteBuffer，本质也是一个ByteBuffer。map()方法参数（读写模式，映射起始位置，数据长度）。
     + force() //强制将此通道的元数据也写入包含该文件的存储设备。
2. ByteBuffer的正确使用姿势  
   1. 向buffer写入数据，例如调用channel.read(buffer)  
   2. 调用flip()切换至读模式  
   3. 从buffer中读取数据，例如调用buffer.get()  
   4. 调用clear()或compact()切换至写模式  
   5. 重复1~4步骤  
   例如：  
   ```java
   import java.io.FileInputStream;
   import java.io.IOException;
   import java.nio.ByteBuffer;
   import java.nio.channels.FileChannel;
   
   public class TestByteBuffer {
       public static void main(String[] args) {
           try {
               FileChannel channel = new FileInputStream("day1-netty/data.txt").getChannel();
               //准备缓冲区，并指定大小
               ByteBuffer byteBuffer = ByteBuffer.allocate(5);
               while (true) {
                   int line = channel.read(byteBuffer);//返回值是读到的实际字节数，如果是-1表示eos，
                   if (line == -1) {
                       break;
                   }
                   //打印buffer的内容
                   byteBuffer.flip();//切换至读模式
                   while (byteBuffer.hasRemaining()) {
                       byte b = byteBuffer.get();
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
   ```
   2.1 Buffer的常用方法  
      + flip()：确定缓冲区数据的起始点和终止点，为输出数据做准备(即写入通道)。此时：limit = position，position = 0。
      + clear()：缓冲区初始化，准备再次接收新数据到缓冲区。position = 0，limit = capacity。
      + hasRemaining()：判断postion到limit之间是否还有元素。
      + rewind()：postion设为0，则mark值无效。
      + limit(int newLt)：设置界限值，并返回一个缓冲区，该缓冲区的界限和limit()设置的一样。
      + get()和put()：获取元素和存放元素。使用clear()之后，无法直接使用get()获取元素，需要使用get(int index)根据索引值来获取相应元素。
      + 可以使用allocate方法为ByteBuffer分配空间，其它的Buffer类也有该方法，allocate是在JVM中分配内存
      + allocateDirect方法，是在OS（操作系统）中分配内存，是独立于JVM的
      + 向buffer写入数据  
        有两种方法  
        + 调用channel的readfangfa
        + 调用buffer自己的put方法
        ```java
        int readBytes = channel.read(buf);
        ```
        和  
        ```java
        buf.put((byte)127);
        ```
      + 从buffer读取数据  
        同样有两种方法  
        + 调用channel的write方法
        + 调用buffer自己的get方法

##Netty基础
1. Netty是什么  
   netty是一个异步基于事件驱动的网络应用框架，用于快速开发可维护、高性能的网络服务器和客户端

2. 组件  
   * EventLoop  
     本质是一个单线程执行器（同时维护了一个Selector），里面有run方法处理Channel上源源不断的IO事件
3. Channel
   * channel的主要作用  
     + close()可以用来关闭channel
     + closeFuture()用来处理channel的关闭
       + sync方法作用是同步等待channel关闭
       + 而addListener方法是异步等待channel关闭
     + pipeline()方法添加处理器
     + write()方法将数据写入
     + writeAndFlush()方法将数据写入并刷出     


###基础提示（关于netty流程的初始理解）
* 把channel理解为数据的通道
* 把msg理解为流动的数据，最开始输入是ByteBuf，但经过pipeline的加工，会变成其他类型对象，最后输出又变成ByteBuf
* 把handler理解为数据的处理工序  
  + 工序有多道，合在一起就是pipeline，pipeline负责发布事件（读、读取完成。。。）传播给每个handler，handler对自己感兴趣的  
    事件进行处理（重写了相应事件处理方法）
  + handler分Inbound和OutBound两类
* 把EventLoop理解为处理数据的工人
  + 工人可以管理多个channel的io操作，并且一旦工人负责了某个channel，就要负责到底（绑定）
  + 工人既可以执行io操作，也可以进行任务处理，每位工人有任务队列，队列里可以堆放多个channel的待处理任务，任务分为
    普通任务 、定时任务。
  + 工人按照pipeline顺序，依次按照handler的规划（代码）处理数据，可以为每道工序指定不同的工人



#关于协议

##Websocket
1. http协议只能由客户端发起，服务器端无法直接进行推送，这就导致如果服务器端有持续的变化客户端想要获知就比较麻烦。Websocket
   协议就是为了解决这个问题应运而生的。Websocket协议，客户端和服务端都可以主动的推送消息，可以文本也可以是二进制数据。而且没有
   同源策略的限制，不存在跨域问题。协议的标识符就是ws。
   Websocket是H5之后提供的一种网络通讯技术，属于应用层协议。它基于TCP传输协议，并复用HTTP的握手通道。
   
2. Websocket有两种帧
   + 数据帧 -- 用来传输数据  
     TextWebSocketFrame : 文本帧  
     BinaryWebSocketFrame : 二进制帧
   + 状态帧 -- 检测心跳  
     关闭帧  
     ping帧  
     pong帧  
     
     
     
     
##Http协议
1. Http请求消息（浏览器丢给服务器的），request  
   * 一个http请求代表客户端浏览器向服务器发送的数据。一个完整的http请求消息包含一个请求行，若干个消息头（请求头），换行，实体内容  
     请求行：描述客户端的请求方式、请求资源的名称、http协议的版本号。例如：GET/BOOK/JAVA.HTML HTTP/1.1
   * 请求头（消息头）包含（客户机请求的服务器主机名、客户机的环境信息等）：  
     Accept：用于告诉服务器，客户机支持的数据类型（例如：Accept：text/html，image/*）  
     Accept-Charset：用于告诉服务器，客户机采用的编码格式  
     Accept-Encoding：用于告诉服务器，客户机支持的数据压缩格式
     Accept-Language：客户机语言环境  
     Host：客户机通过这个服务器，想访问的主机名  
     If-Modified-Since：客户机通过这个头告诉服务器，资源的缓存时间  
     Referer：客户机通过这个头告诉服务器，客户端是从哪个资源来访问服务器的（用于防盗链）  
     User-Agent：客户机通过这个头告诉服务器，客户端的软件环境（例如操作系统、浏览器版本号）  
     Cookie：客户机通过这个头将Cookie信息带给服务器  
     Connection：告诉服务器，请求完成后，是否保持连接  
     Date：告诉服务器，当前的请求时间  
   * 实体内容：就是指浏览器通过http协议发送给服务器的实体数据。
2.  Http响应消息（服务器返回给浏览器的）response：  
   * 一个http响应代表服务器端向客户端会送的数据，包括：一个状态行，若干个消息头，以及实体内容
   * 响应头（消息头）包含：  
     Location：这个消息头配合302状态码，用于告诉客户端找谁  
     Server：服务器通过这个头，告诉浏览器服务器的类型  
     Content-Encoding：告诉浏览器，服务器的数据压缩格式  
     Content-Length：告诉浏览器，回送数据的长度  
     Content-Type：告诉浏览器，回送的数据类型  
     Last-Modified：告诉浏览器当前资源缓存时间  
     Refresh：告诉浏览器，隔多长时间刷新  
     Content-Disposition：告诉浏览器以下载的方式打开数据。例如：response.setHeader("Content-Disposition","attachment:filename=aa.jpg")  
     Transfer-Encoding：告诉浏览器，传送数据的编码格式  
     ETag：缓存相关的头（可以做到实时更新）  
     Expries：告诉浏览器回送的资源缓存多长时间。如果是-1或者0，表示不缓存  
     Cache-Control：控制浏览器不要缓存数据 no-cache  
     Pragma：控制浏览器不要缓存数据 no-cache  
     Connection：响应完成后，是否断开链接。close/Keep-Alive  
     Date：告诉浏览器，服务器响应时间  
     状态行：例如：HTTP/1.1 200 OK 表示协议的版本号是1.1，响应状态码为200，响应结果为OK  
     实体内容（实体头）：响应包含浏览器能够解析的静态内容，例如：html、纯文本、图片等等信息  
   
##JDK8涉及到的新特性
1. 重复注解

2. 在Java8中，Base64编码已经成为Java类库的标准。Java 8 内置了 Base64 编码的编码器和解码器。




##基础知识补充：
1. 格式化日期
   ```java
    //Date类型转String类型
    String date = DateFormatUtils.format(new Date(),"yyyy-MM-dd HH:mm:ss");
    //String类型转Date类型
    Date date = DateUtils.parseDate("2021-05-01 01:01:01", "yyyy-MM-dd HH:mm:ss");
    // 计算一个小时后的日期
    Date date = DateUtils.addHours(new Date(), 1);
    
   ```


2. 文件中的魔术数字
   * 通过对后缀名的判断只能够简单的验证文件,如果用户恶意更改了文件的后缀名,这种判断就不起作用了.
     所以还需要对文件的字节进行验证,每一个文件都有自己的魔术数字.也就是在一个文件的开头部分的字节码,
     用winhex-19.8(下载下来之后有个木马文件,删除掉就可以,不影响使用)查看一个文件的字节码,
     就能够知道该类型文件的魔术数字.通过判断魔术数字,就能够起到验证文件的作用.

