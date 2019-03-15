package site.jackwang.rpc;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import site.jackwang.rpc.proxy.CalculatorServiceProxy;
import site.jackwang.rpc.registry.impl.LocalServerRegistry;
import site.jackwang.rpc.registry.impl.ZkServerRegistry;
import site.jackwang.rpc.remote.invoker.ReferenceBean;
import site.jackwang.rpc.remote.invoker.RpcProxyFactory;
import site.jackwang.rpc.remote.net.impl.netty.client.NettyClient;
import site.jackwang.rpc.serialize.SerializeEnum;
import site.jackwang.rpc.serialize.impl.ProtostuffSerializer;
import site.jackwang.rpc.service.CalculatorService;
import site.jackwang.rpc.service.HelloService;
import site.jackwang.rpc.common.util.IpUtils;

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
//        testCalculatorServiceNoneRegistry();

//        testCalculatorServiceLocalRegistry();

        testCalculatorServiceZkRegistry();
    }

    private static void testCalculatorServiceLocalRegistry() throws InterruptedException {
        NettyClient client = new NettyClient();
        HashSet<String> serverAddresses = LocalServerRegistry.getInstance().lookupOne(CalculatorService.class.getName());
        Iterator<String> it = serverAddresses.iterator();
        Object[] ipPort = IpUtils.parseIpPort(it.next());
        client.init((String) ipPort[0], (int) ipPort[1], SerializeEnum.HESSIAN.getSerializer());

        CalculatorService calculatorService = RpcProxyFactory.getProxy(CalculatorService.class, client);
        System.out.println(calculatorService.add(1.0f, 2.0f));
        System.out.println(calculatorService.substract(1.0f, 2.0f));
    }

//    private static void testCalculatorServiceZkRegistry() throws InterruptedException {
//        NettyClient client = new NettyClient();
//
//        ZkServerRegistry serverRegistry = new ZkServerRegistry();
//        Map<String, String> params = new HashMap<>();
//        params.put(ZkServerRegistry.ZK_ADDRESS, "127.0.0.1:2181");
//        params.put(ZkServerRegistry.ZK_DIGEST, "");
//        params.put(ZkServerRegistry.ENV, "dev");
//        serverRegistry.init(params);
//        serverRegistry.start();
//
//        HashSet<String> serverAddresses = serverRegistry.lookupOne(CalculatorService.class.getName());
//        Iterator<String> it = serverAddresses.iterator();
//        Object[] ipPort = IpUtils.parseIpPort(it.next());
//        client.init((String) ipPort[0], (int) ipPort[1], SerializeEnum.HESSIAN.getSerializer());
//
//        CalculatorService calculatorService = RpcProxyFactory.getProxy(CalculatorService.class, client);
//        System.out.println(calculatorService.add(1.0f, 2.0f));
//        System.out.println(calculatorService.substract(1.0f, 2.0f));
//    }

    private static void testCalculatorServiceZkRegistry() throws InterruptedException {
        ReferenceBean<CalculatorService> bean = new ReferenceBean<>();

        ZkServerRegistry serverRegistry = new ZkServerRegistry();
        Map<String, String> params = new HashMap<>();
        params.put(ZkServerRegistry.ZK_ADDRESS, "127.0.0.1:2181");
        params.put(ZkServerRegistry.ZK_DIGEST, "");
        params.put(ZkServerRegistry.ENV, "dev");
        serverRegistry.init(params);
        bean.setServerRegistry(serverRegistry);

        bean.setSerializer(SerializeEnum.HESSIAN.getSerializer());
        bean.setInterface(CalculatorService.class);
        CalculatorService calculatorService = bean.get();

        System.out.println(calculatorService.add(1.0f, 2.0f));
        System.out.println(calculatorService.substract(1.0f, 2.0f));
    }

    private static void testCalculatorServiceNoneRegistry() throws InterruptedException {
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
