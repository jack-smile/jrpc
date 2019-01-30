package site.jackwang.rpc.registry;

/**
 * 服务器注册器
 *
 * @author wangjie<http://www.jackwang.site/>
 * @date 2019/1/30
 */
public abstract class ServerRegistry implements RegistryServer, LookupServer {
    /**
     * 开始
     */
    public abstract void start();

    /**
     * 结束
     */
    public abstract void stop();
}
