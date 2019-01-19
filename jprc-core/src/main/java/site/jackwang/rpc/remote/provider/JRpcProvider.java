package site.jackwang.rpc.remote.provider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import site.jackwang.rpc.remote.net.params.JRpcRequest;
import site.jackwang.rpc.remote.net.params.JRpcResponse;
import site.jackwang.rpc.util.exception.JRpcException;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 服务提供方
 *
 * @author wangjie<http://www.jackwang.site/>
 * @date 2019/1/15
 */
public class JRpcProvider {
    private static final Logger logger = LoggerFactory.getLogger(JRpcProvider.class);

    /**
     * 接口名和对应的接口类
     * key：接口名称
     * value：接口名对应的类
     */
    private Map<String, Class> interfaces = new ConcurrentHashMap<>();

    /**
     * 接口名和对应的实现类
     * key：接口名称
     * value：实现类
     */
    private Map<String, Object> interfaceImpls = new ConcurrentHashMap<>();

    /**
     * 发布服务
     * 服务提供方发布服务，给消费方调用
     *
     * @param serviceInterface 服务接口
     * @param serviceImpl      服务接口实现类
     */
    public void publishService(Class serviceInterface, Object serviceImpl) {
        if (interfaces.containsKey(serviceInterface.getName()) || interfaceImpls.containsKey(serviceInterface.getName())) {
            throw new JRpcException("serviceInterface " + serviceInterface.getName() + " has been registered!");
        }

        if (!serviceInterface.isInstance(serviceImpl)) {
            throw new JRpcException("serviceImpl " + serviceImpl.toString() + " must implement the interface " + serviceInterface.getName());
        }

        interfaces.put(serviceInterface.getName(), serviceInterface);
        interfaceImpls.put(serviceInterface.getName(), serviceImpl);
    }

    /**
     * 处理消费端发来的请求包
     *
     * @param request 请求包
     * @return 返回响应包
     */
    public JRpcResponse invokeService(JRpcRequest request) {
        String serviceName = request.getServiceName();
        String methodName = request.getMethodName();
        Class<?>[] paramTypes = request.getParamTypes();
        Object[] params = request.getParams();

        JRpcResponse response = new JRpcResponse();
        response.setId(request.getId());

        // 调用方法
        Class<?> service = interfaces.get(serviceName);
        try {
            Method method = service.getMethod(methodName, paramTypes);
            Object result = method.invoke(interfaceImpls.get(serviceName), params);

            response.setResult(result);
        } catch (Exception e) {
            logger.error("jrpc provider invokeService error.{}", e);
            response.setMessage("jprc provider invokeService error，" + e.toString());
        }

        return response;
    }
}
