package site.jackwang.rpc.registry;

import java.util.HashSet;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 服务器注册器
 *
 * @author wangjie<http://www.jackwang.site/>
 * @date 2019/1/30
 */
public abstract class AbstractServerRegistry implements RegistryServer, LookupServer {
    /**
     * 已注册的服务器信息
     * key：服务名称
     * value：服务对应的不同服务器地址集
     */
    protected Map<String, HashSet<String>> registryServers = new ConcurrentHashMap<>();

    /**
     * 初始化，主要是初始化参数配置
     *
     * @param param 参数配置
     */
    public abstract void init(Map<String, String> param);

    /**
     * 开始
     */
    public abstract void start();

    /**
     * 结束
     */
    public abstract void stop();
}
