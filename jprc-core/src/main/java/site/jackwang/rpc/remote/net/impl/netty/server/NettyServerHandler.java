package site.jackwang.rpc.remote.net.impl.netty.server;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import site.jackwang.rpc.remote.net.params.JRpcRequest;
import site.jackwang.rpc.remote.net.params.JRpcResponse;
import site.jackwang.rpc.remote.provider.JRpcProvider;

/**
 * Netty服务端
 *
 * @author wangjie<http://www.jackwang.site/>
 * @date 2019/1/15
 */
public class NettyServerHandler extends SimpleChannelInboundHandler<JRpcRequest> {
    private static final Logger logger = LoggerFactory.getLogger(NettyServerHandler.class);

    private JRpcProvider jRpcProvider;

    public NettyServerHandler(JRpcProvider jRpcProvider) {
        this.jRpcProvider = jRpcProvider;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, JRpcRequest jRpcRequest) throws Exception {
        // rpc提供方处理请求包
        JRpcResponse jRpcResponse = jRpcProvider.invokeService(jRpcRequest);

        // 回复响应包
        ctx.writeAndFlush(jRpcResponse);
    }
}
