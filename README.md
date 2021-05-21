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
   





