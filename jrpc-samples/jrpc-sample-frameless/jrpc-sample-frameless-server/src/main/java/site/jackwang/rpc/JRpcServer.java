package site.jackwang.rpc;

import site.jackwang.rpc.remote.net.impl.netty.server.NettyServer;
import site.jackwang.rpc.remote.provider.JRpcProvider;
import site.jackwang.rpc.serialize.SerializeEnum;
import site.jackwang.rpc.service.CalculatorService;
import site.jackwang.rpc.service.HelloService;
import site.jackwang.rpc.service.impl.CalculatorServiceImpl;
import site.jackwang.rpc.service.impl.HelloServiceImpl;

/**
 * @author wangjie<http://www.jackwang.site/>
 * @date 2019/1/17
 */
public class JRpcServer {
    public static void main(String[] args) {
        JRpcProvider jRpcProvider = new JRpcProvider();
        jRpcProvider.publishService(CalculatorService.class, new CalculatorServiceImpl());
        jRpcProvider.publishService(HelloService.class, new HelloServiceImpl());

        NettyServer server = new NettyServer(8888);
        server.start(jRpcProvider, SerializeEnum.HESSIAN.getSerializer());
    }
}
