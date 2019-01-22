package site.jackwang.rpc.remote.net.impl.netty.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import site.jackwang.rpc.remote.net.impl.netty.codec.NettyDecoder;
import site.jackwang.rpc.remote.net.impl.netty.codec.NettyEncoder;
import site.jackwang.rpc.remote.net.params.JRpcRequest;
import site.jackwang.rpc.remote.net.params.JRpcResponse;
import site.jackwang.rpc.remote.provider.JRpcProvider;
import site.jackwang.rpc.serialize.Serializer;

/**
 * Netty服务端
 *
 * @author wangjie<http://www.jackwang.site/>
 * @date 2019/1/15
 */
public class NettyServer {
    private static final Logger logger = LoggerFactory.getLogger(NettyServer.class);

    /**
     * 服务端工作线程
     */
    private Thread thread;

    /**
     * 服务端监听的端口号
     */
    private int port;


    public NettyServer(int port) {
        this.port = port;
    }

    public void start(final JRpcProvider jRpcProvider, final Serializer serializer) {
        thread = new Thread(new Runnable() {
            @Override
            public void run() {

                // param
//                final ThreadPoolExecutor serverHandlerPool = new ThreadPoolExecutor(
//                        60,
//                        300,
//                        60L,
//                        TimeUnit.SECONDS,
//                        new LinkedBlockingQueue<Runnable>(1000),
//                        new RejectedExecutionHandler() {
//                            @Override
//                            public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
//                                throw new JRpcException("jrpc NettyServer Thread pool is EXHAUSTED!");
//                            }
//                        });
                EventLoopGroup bossGroup = new NioEventLoopGroup();
                EventLoopGroup workerGroup = new NioEventLoopGroup();

                try {
                    // start server
                    ServerBootstrap bootstrap = new ServerBootstrap();
                    bootstrap.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class)
                            .childHandler(new ChannelInitializer<SocketChannel>() {
                                @Override
                                public void initChannel(SocketChannel channel) throws Exception {
                                    channel.pipeline()
                                            .addLast(new NettyDecoder(JRpcRequest.class, serializer))
                                            .addLast(new NettyEncoder(JRpcResponse.class, serializer))
                                            .addLast(new NettyServerHandler(jRpcProvider));
                                }
                            })
                            .option(ChannelOption.SO_TIMEOUT, 100)
                            .option(ChannelOption.SO_BACKLOG, 128)
                            .option(ChannelOption.TCP_NODELAY, true)
                            .option(ChannelOption.SO_REUSEADDR, true)
                            .childOption(ChannelOption.SO_KEEPALIVE, true);

                    // bind
                    ChannelFuture future = bootstrap.bind(port).sync();

                    logger.info(">>>>>>>>>>> jrpc remoting server start success, netType = {}, port = {}, serializeType = {}",
                            NettyServer.class.getName(), port, serializer.getClass().getName());

                    // wait util stop
                    future.channel().closeFuture().sync();

                } catch (Exception e) {
                    if (e instanceof InterruptedException) {
                        logger.info(">>>>>>>>>>> jrpc remoting server stop.");
                    } else {
                        logger.error(">>>>>>>>>>> jrpc remoting server error.", e);
                    }
                } finally {

                    // stop
//                    try {
//                        // shutdownNow
//                        serverHandlerPool.shutdown();
//                    } catch (Exception e) {
//                        logger.error(e.getMessage(), e);
//                    }
                    try {
                        workerGroup.shutdownGracefully();
                        bossGroup.shutdownGracefully();
                    } catch (Exception e) {
                        logger.error(e.getMessage(), e);
                    }

                }
            }
        });
//        thread.setDaemon(true);
        thread.start();
    }
}
