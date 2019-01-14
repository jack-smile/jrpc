package site.jackwang.rpc.remote.net.impl.netty.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Netty服务端
 *
 * @author wangjie<http://www.jackwang.site/>
 * @date 2019/1/14
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

    public void start() {

    }
}
