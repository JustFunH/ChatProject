package com.meguru.chatproject.websocket;


import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.stream.ChunkedWriteHandler;
import io.netty.handler.timeout.IdleStateHandler;
import io.netty.util.NettyRuntime;
import io.netty.util.concurrent.Future;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
public class NettyWebSocketServer {
    public static final int WEB_SOCKET_PORT = 8090;
    public static final NettyWebSocketServerHandler NETTY_WEB_SOCKET_SERVER_HANDLER = new NettyWebSocketServerHandler();
    // 创建线程执行器
    private EventLoopGroup bossGroup = new NioEventLoopGroup(1);
    private EventLoopGroup workerGroup = new NioEventLoopGroup(NettyRuntime.availableProcessors());

    /**
     * 启动 Netty 服务器
     */
    @PostConstruct
    public void start() throws InterruptedException {
        run();
    }

    /**
     * 销毁
     */
    @PreDestroy
    public void destroy() {
        Future<?> bossFuture = bossGroup.shutdownGracefully();
        Future<?> workerFuture = workerGroup.shutdownGracefully();
        bossFuture.syncUninterruptibly();
        workerFuture.syncUninterruptibly();
        log.info("关闭 websocket 成功");
    }

    public void run() throws InterruptedException {
        // 服务器启动引导对象
        ServerBootstrap serverBootstrap = new ServerBootstrap();

        // 配置服务器的线程组、通道类型、背压队列长度和保持连接的选项
        serverBootstrap.group(bossGroup, workerGroup)
                .channel(NioServerSocketChannel.class)
                .option(ChannelOption.SO_BACKLOG, 128)
                .option(ChannelOption.SO_KEEPALIVE, true)
                .handler(new LoggingHandler(LogLevel.INFO))
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel socketChannel) throws Exception {
                        ChannelPipeline pipeline = socketChannel.pipeline();
                        // 30秒客户端没有向服务器发送消息就关闭连接
                        pipeline.addLast(new IdleStateHandler(30, 0, 0));
                        // 使用 HTTP 编码器, 解码器
                        pipeline.addLast(new HttpServerCodec());
                        // 添加 chunkedWriter 支持异步大文件传输
                        pipeline.addLast(new ChunkedWriteHandler());
                        // 添加聚合器，将多个消息转换为一个单一的消息对象
                        pipeline.addLast(new HttpObjectAggregator(8192));
                        // 保存用户ip
                        pipeline.addLast(new HttpHeadersHandler());
                        // 添加 WebSocket 协议处理器
                        pipeline.addLast(new WebSocketServerProtocolHandler("/"));
                        // 自定义handler
                        pipeline.addLast(NETTY_WEB_SOCKET_SERVER_HANDLER);
                    }
                });
        serverBootstrap.bind(WEB_SOCKET_PORT).sync();
    }
}
