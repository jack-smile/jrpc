package site.jackwang.rpc.registry.impl;

import io.netty.util.internal.StringUtil;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import site.jackwang.rpc.registry.ServerRegistry;

/**
 * 本地缓存的服务注册器
 *
 * @author wangjie<http://www.jackwang.site/>
 * @date 2019/1/30
 */
public class LocalServerRegistry extends ServerRegistry {
    private static volatile LocalServerRegistry instance = new LocalServerRegistry();

    /**
     * 已注册的服务器信息
     * key：服务名称
     * value：服务对应的不同服务器地址集
     */
    private Map<String, HashSet<String>> registryServers = new HashMap<>();

    public static LocalServerRegistry getInstance() {
        return instance;
    }

    @Override
    public void start() {
//        registryServers = new HashMap<>();
    }

    @Override
    public void stop() {
        registryServers.clear();
    }

    @Override
    public HashSet<String> lookupOne(String serverName) {
        return registryServers.get(serverName);
    }

    @Override
    public boolean register(String serverName, String address) {
        if (StringUtil.isNullOrEmpty(serverName) || StringUtil.isNullOrEmpty(address)) {
            return false;
        }

        HashSet<String> addresses = registryServers.get(serverName);
        if (Objects.isNull(addresses)) {
            addresses = new HashSet<>();
            registryServers.put(serverName, addresses);
        }
        addresses.add(address);

        return true;
    }

    @Override
    public boolean remove(String serverName, String address) {
        if (StringUtil.isNullOrEmpty(serverName) || StringUtil.isNullOrEmpty(address)) {
            return false;
        }

        HashSet<String> addresses = registryServers.get(serverName);
        if (Objects.isNull(addresses)) {
            return true;
        }

        return addresses.remove(address);
    }
}
