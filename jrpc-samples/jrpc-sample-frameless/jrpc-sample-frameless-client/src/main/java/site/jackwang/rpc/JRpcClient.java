package site.jackwang.rpc;

import site.jackwang.rpc.remote.invoker.RpcProxyFactory;
import site.jackwang.rpc.remote.net.impl.netty.client.NettyClient;
import site.jackwang.rpc.serialize.impl.ProtostuffSerializer;
import site.jackwang.rpc.service.CalculatorService;

/**
 * @author wangjie<http://www.jackwang.site/>
 * @date 2019/1/17
 */
public class JRpcClient {
    public static void main(String[] args) throws InterruptedException {
//        CalculatorServiceProxy calculatorServiceProxy = new CalculatorServiceProxy("127.0.0.1", 8888, new ProtostuffSerializer());
//        System.out.println(calculatorServiceProxy.add(1.0f, 2.0f));
//        calculatorServiceProxy.stop();

        NettyClient client = new NettyClient();
        client.init("127.0.0.1", 8888, new ProtostuffSerializer());
        CalculatorService calculatorService = RpcProxyFactory.getProxy(CalculatorService.class, client);
        System.out.println(calculatorService.add(1.0f, 2.0f));
    }
}
