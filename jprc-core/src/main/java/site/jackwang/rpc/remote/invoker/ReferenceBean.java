package site.jackwang.rpc.remote.invoker;

import java.util.HashSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import site.jackwang.rpc.common.util.IpUtils;
import site.jackwang.rpc.common.util.exception.ErrorCodes;
import site.jackwang.rpc.common.util.exception.JRpcException;
import site.jackwang.rpc.loadbalance.AbstractLoadBalance;
import site.jackwang.rpc.registry.AbstractServerRegistry;
import site.jackwang.rpc.remote.net.impl.netty.client.NettyClient;
import site.jackwang.rpc.serialize.AbstractSerializer;
import site.jackwang.rpc.serialize.SerializeEnum;

/**
 * 接口实现bean
 *
 * @author wangjie<http://www.jackwang.site/>
 * @date 2019/3/12
 */
public class ReferenceBean<T> {
    private static Logger logger = LoggerFactory.getLogger(ReferenceBean.class);

    /**
     * netty客户端
     */
    private NettyClient client = new NettyClient();

    /**
     * 将要调用的接口类
     */
    private Class<?> interfaceClazz;

    /**
     * 将要调用的接口类名称
     */
    private String interfaceName;

    /**
     * 注册中心
     */
    private AbstractServerRegistry serverRegistry;

    /**
     * 序列化类
     */
    private AbstractSerializer serializer = SerializeEnum.HESSIAN.getSerializer();

    /**
     * 负载均衡
     */
    private AbstractLoadBalance loadBalance;


    public void setInterface(Class<?> interfaceClass) {
        if (interfaceClass != null && !interfaceClass.isInterface()) {
            throw new JRpcException(ErrorCodes.NOT_INTERFACE, interfaceClass);
        }
        this.interfaceClazz = interfaceClass;
        setInterface(interfaceClass == null ? null : interfaceClass.getName());
    }

    private void setInterface(String interfaceName) {
        this.interfaceName = interfaceName;
    }

    public void setServerRegistry(AbstractServerRegistry registry) {
        this.serverRegistry = registry;
    }

    public void setSerializer(AbstractSerializer serializer) {
        this.serializer = serializer;
    }

    public void setLoadBalance(AbstractLoadBalance loadBalance) {
        this.loadBalance = loadBalance;
    }

    @SuppressWarnings({"unchecked", "rawtypes", "deprecation"})
    public T get() {
        serverRegistry.start();

        HashSet<String> serverAddresses = serverRegistry.lookupOne(interfaceName);

        String address = loadBalance.select(interfaceName, serverAddresses);
        Object[] ipPort = IpUtils.parseIpPort(address);
        try {
            client.init((String) ipPort[0], (int) ipPort[1], serializer);
        } catch (InterruptedException e) {
            logger.warn("Fail to connect server: ", e);
        }

        return (T) RpcProxyFactory.getProxy(interfaceClazz, client);
    }
}
