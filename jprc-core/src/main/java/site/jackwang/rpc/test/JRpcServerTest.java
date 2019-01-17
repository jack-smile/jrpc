package site.jackwang.rpc.test;

import site.jackwang.rpc.remote.net.impl.netty.server.NettyServer;
import site.jackwang.rpc.remote.provider.JRpcProvider;
import site.jackwang.rpc.serialize.impl.ProtostuffSerializer;
import site.jackwang.rpc.test.impl.CalculatorServiceImpl;

/**
 * @author wangjie<http://www.jackwang.site/>
 * @date 2019/1/17
 */
public class JRpcServerTest {
    public static void main(String[] args) {
        JRpcProvider jRpcProvider = new JRpcProvider();
        jRpcProvider.publishService(CalculatorService.class, new CalculatorServiceImpl());

        NettyServer server = new NettyServer(8888);
        server.start(jRpcProvider, new ProtostuffSerializer());
    }
}
