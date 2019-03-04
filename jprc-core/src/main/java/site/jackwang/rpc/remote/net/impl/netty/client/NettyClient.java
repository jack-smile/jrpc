package site.jackwang.rpc.remote.net.impl.netty.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.SynchronousQueue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import site.jackwang.rpc.remote.net.impl.netty.codec.NettyDecoder;
import site.jackwang.rpc.remote.net.impl.netty.codec.NettyEncoder;
import site.jackwang.rpc.remote.net.params.JRpcRequest;
import site.jackwang.rpc.remote.net.params.JRpcResponse;
import site.jackwang.rpc.serialize.Serializer;

/**
 * Netty客户端
 *
 * @author wangjie<http://www.jackwang.site/>
 * @date 2019/1/14
 */
public class NettyClient {
    protected static final Logger logger = LoggerFactory.getLogger(NettyClient.class);

    /**
     * 一组事件轮询
     */
    private EventLoopGroup group;

    /**
     * 首发数据的管道
     */
    private Channel channel;

    /**
     * 保存每个发送出去的数据包的响应信息
     * key：数据包的请求id
     * value：数据包的响应信息
     */
    private static Map<String, SynchronousQueue<JRpcResponse>> packageMap = new ConcurrentHashMap<>();

    public static SynchronousQueue<JRpcResponse> getSynchronousQueue(String id) {
        return packageMap.get(id);
    }

    public static void putSunchronousQuee(String id, SynchronousQueue<JRpcResponse> queue) {
        packageMap.put(id, queue);
    }

    static void removeById(String id) {
        packageMap.remove(id);
    }


    /**
     * 初始化客户端
     *
     * @param host       ip地址
     * @param port       端口
     * @param serializer 序列化类
     */
    public void init(String host, int port, final Serializer serializer) throws InterruptedException {
        this.group = new NioEventLoopGroup();
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(group).channel(NioSocketChannel.class)
            .handler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel channel) throws Exception {
                    channel.pipeline()
                        .addLast(new NettyEncoder(JRpcRequest.class, serializer))
                        .addLast(new NettyDecoder(JRpcResponse.class, serializer))
                        .addLast(new NettyClientHandler());
                }
            })
            .option(ChannelOption.TCP_NODELAY, true)
            .option(ChannelOption.SO_REUSEADDR, true)
            .option(ChannelOption.SO_KEEPALIVE, true);

        this.channel = bootstrap.connect(host, port).sync().channel();

        // valid
        if (!isValidate()) {
            close();
            return;
        }

        logger.info(">>>>>>>>>>> jrpc netty client proxy, connect to server success at host:{}, port:{}", host, port);
    }

    public boolean isValidate() {
        if (this.channel != null) {
            return this.channel.isActive();
        }
        return false;
    }

    /**
     * 关闭通道
     */
    public void close() {
        if (this.channel != null) {
            if (this.channel.isOpen()) {
                this.channel.close();
            }
        }
    }

    public void send(JRpcRequest jRpcRequest) throws InterruptedException {
        this.channel.writeAndFlush(jRpcRequest).sync();
    }
}
