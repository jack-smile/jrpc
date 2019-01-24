package site.jackwang.rpc.remote.invoker;

import site.jackwang.rpc.remote.net.impl.netty.client.NettyClient;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;

/**
 * 生成代理对象
 *
 * @author wangjie<http://www.jackwang.site/>
 * @date 2019/1/20
 */
public final class RpcProxyFactory {
    public static <T> T getProxy(Class<T> proxyClass, NettyClient client) {
        InvocationHandler handler = new RpcInvocationHandler(proxyClass, client);

        return proxyClass.cast(Proxy.newProxyInstance(handler.getClass().getClassLoader(), new Class[]{proxyClass}, handler));
    }
}
