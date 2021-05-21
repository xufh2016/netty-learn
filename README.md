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
   + Selector  
      






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
   





