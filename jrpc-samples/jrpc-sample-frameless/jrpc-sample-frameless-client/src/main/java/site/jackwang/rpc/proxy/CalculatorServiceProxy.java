package site.jackwang.rpc.proxy;

import site.jackwang.rpc.remote.net.impl.netty.client.NettyClient;
import site.jackwang.rpc.remote.net.params.JRpcRequest;
import site.jackwang.rpc.remote.net.params.JRpcResponse;
import site.jackwang.rpc.serialize.AbstractSerializer;
import site.jackwang.rpc.service.CalculatorService;
import site.jackwang.rpc.common.util.exception.JRpcException;

import java.util.UUID;
import java.util.concurrent.SynchronousQueue;

/**
 * @author wangjie<http://www.jackwang.site/>
 * @date 2019/1/17
 */
public class CalculatorServiceProxy implements CalculatorService {
    private NettyClient client;

    public CalculatorServiceProxy(String host, int port, final AbstractSerializer serializer) {
        client = new NettyClient();
        try {
            client.init(host, port, serializer);
        } catch (InterruptedException e) {
            throw new JRpcException(e);
        }
    }

    @Override
    public double add(double num1, double num2) {
        JRpcRequest request = new JRpcRequest();
        request.setId(UUID.randomUUID().toString());
        request.setServiceName(CalculatorService.class.getName());
        request.setMethodName("add");
        request.getParams()[0] = num1;
        request.getParams()[1] = num2;
        request.setParamTypes(new Class[]{double.class, double.class});

        JRpcResponse response = sendRequest(request);

        return (double) response.getResult();
    }

    @Override
    public double substract(double num1, double num2) {
        return 0;
    }

    private JRpcResponse sendRequest(JRpcRequest request) {
        SynchronousQueue<JRpcResponse> queue = new SynchronousQueue<>();
        NettyClient.putSunchronousQuee(request.getId(), queue);

        try {
            client.send(request);
            return queue.take();
        } catch (InterruptedException e) {
            throw new JRpcException(e);
        }
    }

    public void stop() {
        client.close();
    }
}
