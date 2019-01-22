package site.jackwang.rpc;

import site.jackwang.rpc.proxy.CalculatorServiceProxy;
import site.jackwang.rpc.remote.invoker.RpcProxyFactory;
import site.jackwang.rpc.remote.net.impl.netty.client.NettyClient;
import site.jackwang.rpc.serialize.SerializeEnum;
import site.jackwang.rpc.serialize.impl.ProtostuffSerializer;
import site.jackwang.rpc.service.CalculatorService;
import site.jackwang.rpc.service.HelloService;

/**
 * @author wangjie<http://www.jackwang.site/>
 * @date 2019/1/17
 */
public class JRpcClient {
    public static void main(String[] args) throws InterruptedException {
//        testFixed();

        testCalculatorService();

//        testHelloService();
    }

    private static void testFixed() {
        CalculatorServiceProxy calculatorServiceProxy = new CalculatorServiceProxy("127.0.0.1", 8888, new ProtostuffSerializer());
        System.out.println(calculatorServiceProxy.add(1.0f, 2.0f));
        calculatorServiceProxy.stop();
    }

    private static void testCalculatorService() throws InterruptedException {
        NettyClient client = new NettyClient();
        client.init("127.0.0.1", 8888, SerializeEnum.HESSIAN.getSerializer());
        CalculatorService calculatorService = RpcProxyFactory.getProxy(CalculatorService.class, client);
        System.out.println(calculatorService.add(1.0f, 2.0f));
        System.out.println(calculatorService.substract(1.0f, 2.0f));
    }

    private static void testHelloService() throws InterruptedException {
        NettyClient client = new NettyClient();
        client.init("127.0.0.1", 8888, SerializeEnum.HESSIAN.getSerializer());
        HelloService helloService = RpcProxyFactory.getProxy(HelloService.class, client);
        System.out.println(helloService.sayHi("xiaowang"));
    }
}
