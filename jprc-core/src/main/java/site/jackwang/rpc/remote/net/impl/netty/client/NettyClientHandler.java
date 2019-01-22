package site.jackwang.rpc.remote.net.impl.netty.client;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import site.jackwang.rpc.remote.net.params.JRpcResponse;

import java.util.concurrent.SynchronousQueue;

/**
 * Netty客户端的逻辑处理handler，处理客户端读取到服务器发来的响应包
 *
 * @author wangjie<http://www.jackwang.site/>
 * @date 2019/1/14
 */
public class NettyClientHandler extends SimpleChannelInboundHandler<JRpcResponse> {
    private static final Logger logger = LoggerFactory.getLogger(NettyClientHandler.class);

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, JRpcResponse jRpcResponse) throws Exception {
        final String id = jRpcResponse.getId();
        final SynchronousQueue<JRpcResponse> synchronousQueue = NettyClient.getSynchronousQueue(id);
        synchronousQueue.put(jRpcResponse);

        // 另外一个线程使用jRpcResponse完成后，移除
        NettyClient.removeById(id);
    }
}
