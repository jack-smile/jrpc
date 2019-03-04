package site.jackwang.rpc.remote.provider;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import site.jackwang.rpc.registry.ServerRegistry;
import site.jackwang.rpc.remote.net.impl.netty.server.NettyServer;
import site.jackwang.rpc.remote.net.params.JRpcRequest;
import site.jackwang.rpc.remote.net.params.JRpcResponse;
import site.jackwang.rpc.serialize.Serializer;
import site.jackwang.rpc.util.IpUtils;
import site.jackwang.rpc.util.ReflectUtils;
import site.jackwang.rpc.util.exception.ErrorCodes;
import site.jackwang.rpc.util.exception.JRpcException;

/**
 * 服务提供方
 *
 * @author wangjie<http://www.jackwang.site/>
 * @date 2019/1/15
 */
public class JRpcProvider {
    private static final Logger logger = LoggerFactory.getLogger(JRpcProvider.class);

    /**
     * 服务器的地址
     */
    private String ip;

    /**
     * 服务器的监听端口
     */
    private int port;

    /**
     * 服务器的地址，格式：ip:port
     */
    private String address;

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
     * 注册器
     */
    private ServerRegistry serverRegistry;

    /**
     * 序列化类
     */
    private Serializer serializer;


    /**
     * 初始化配置
     *
     * @param serverRegistry 注册器
     * @param serializer     序列化方式
     * @param port           监听端口
     */
    public void init(ServerRegistry serverRegistry, Serializer serializer, String ip, int port) {
        this.serverRegistry = serverRegistry;
        this.serializer = serializer;
        this.ip = ip;
        if (Objects.isNull(this.ip)) {
            this.ip = IpUtils.getIp();
        }
        this.port = port;
        this.address = IpUtils.getIpPort(this.ip, port);
    }

    /**
     * 启动
     */
    public void start() {
        NettyServer server = new NettyServer(port);
        server.start(this, serializer);

        serverRegistry.start();
    }

    /**
     * 发布服务
     * 服务提供方发布服务，给消费方调用
     *
     * @param serviceInterface 服务接口
     * @param serviceImpl      服务接口实现类
     */
    public void publishService(Class serviceInterface, Object serviceImpl) {
        if (interfaces.containsKey(serviceInterface.getName()) || interfaceImpls.containsKey(serviceInterface.getName())) {
            throw new JRpcException(ErrorCodes.PUBLISH_SERVICE_NOT_REGISTERED, serviceInterface.getName());
        }

        if (!serviceInterface.isInstance(serviceImpl)) {
            throw new JRpcException(ErrorCodes.PUBLISH_SERVICE_NOT_IMPLEMENT_INTERFACE, serviceImpl.toString(), serviceInterface.getName());
        }

        interfaces.put(serviceInterface.getName(), serviceInterface);
        interfaceImpls.put(serviceInterface.getName(), serviceImpl);

        if (Objects.nonNull(serverRegistry)) {
            serverRegistry.register(serviceInterface.getName(), address);
        }
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
        if (Objects.isNull(service)) {
            logger.error("jrpc provider doesn't publish service: " + serviceName);
            throw new JRpcException(ErrorCodes.PUBLISH_SERVICE_NON, serviceName);
        }

        try {
            Method method = ReflectUtils.getMethod(service, methodName, paramTypes);
            method.setAccessible(true);
            Object result = ReflectUtils.invoke(method, interfaceImpls.get(serviceName), params);

            response.setResult(result);
        } catch (JRpcException e) {
            logger.error("jrpc provider invokeService error.{}", e.getErrorMessage());
            response.setMessage("jprc provider invokeService error，" + e.getErrorMessage());
        } catch (Exception e) {
            logger.error("jrpc provider invokeService error.{}", e);
            response.setMessage("jprc provider invokeService error，" + e.toString());
        }

        return response;
    }
}
