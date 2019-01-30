package site.jackwang.rpc.registry.impl;

import java.util.HashSet;
import java.util.Map;
import java.util.TreeSet;
import site.jackwang.rpc.registry.ServerRegistry;

/**
 * 本地缓存的服务注册器
 *
 * @author wangjie<http://www.jackwang.site/>
 * @date 2019/1/30
 */
public class LocalServerRegistry extends ServerRegistry {
    /**
     * 已注册的服务器信息
     * key：服务名称
     * value：服务对应的不同服务器地址集
     */
    private Map<String, HashSet<String>> registryServer;

    @Override
    public void start() {

    }

    @Override
    public void stop() {

    }

    @Override
    public TreeSet<String> lookupOne(String serverName) {
        return null;
    }

    @Override
    public boolean register(String serverName, String address) {
        return false;
    }

    @Override
    public boolean remove(String serverName, String address) {
        return false;
    }
}
