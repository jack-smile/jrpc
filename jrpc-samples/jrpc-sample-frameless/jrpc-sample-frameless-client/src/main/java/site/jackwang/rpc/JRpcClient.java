package site.jackwang.rpc;

import site.jackwang.rpc.proxy.CalculatorServiceProxy;
import site.jackwang.rpc.serialize.impl.ProtostuffSerializer;

/**
 * @author wangjie<http://www.jackwang.site/>
 * @date 2019/1/17
 */
public class JRpcClient {
    public static void main(String[] args) {
        CalculatorServiceProxy calculatorServiceProxy = new CalculatorServiceProxy("127.0.0.1", 8888, new ProtostuffSerializer());
        System.out.println(calculatorServiceProxy.add(1.0f, 2.0f));
        calculatorServiceProxy.stop();
    }
}
