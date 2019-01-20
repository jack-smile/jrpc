package site.jackwang.rpc.remote.invoker;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import site.jackwang.rpc.remote.net.impl.netty.client.NettyClient;
import site.jackwang.rpc.remote.net.params.JRpcRequest;
import site.jackwang.rpc.remote.net.params.JRpcResponse;
import site.jackwang.rpc.util.exception.JRpcException;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.UUID;
import java.util.concurrent.SynchronousQueue;

/**
 * JDK动态代理
 * 代理需要远程调用的接口类
 *
 * @author wangjie<http://www.jackwang.site/>
 * @date 2019/1/20
 */
public class RpcInvocationHandler implements InvocationHandler {
    private static final Logger logger = LoggerFactory.getLogger(RpcInvocationHandler.class);

    /**
     * netty客户端
     */
    private NettyClient client;

    /**
     * 代理接口类
     */
    private Class<?> interfaceClazz;

    public RpcInvocationHandler(Class<?> clazz, NettyClient client) {
        this.client = client;
        this.interfaceClazz = clazz;
    }


    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        // filter method like "Object.toString()"
        String className = method.getDeclaringClass().getName();
        if (Object.class.getName().equals(className)) {
            logger.info(">>>>>>>>>>> jrpc proxy class-method not support [{}.{}]", className, method.getName());
            throw new JRpcException("jrpc proxy class-method not support");
        }

        JRpcRequest request = buildRequest(method, args);

        JRpcResponse response = sendRequest(request);
        return response.getResult();
    }

    /**
     * 构建请求包
     *
     * @param method 调用远端接口的方法
     * @param args   方法的参数
     * @return 请求包
     */
    private JRpcRequest buildRequest(Method method, Object[] args) {
        JRpcRequest request = new JRpcRequest();
        request.setId(UUID.randomUUID().toString());
        request.setServiceName(interfaceClazz.getName());
        request.setMethodName(method.getName());
        request.setParams(args);
        request.setParamTypes(method.getParameterTypes());
        return request;
    }

    /**
     * 向rpc远端服务器发送请求包
     *
     * @param request 请求包
     * @return 服务器返回的响应包
     */
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
}
