package site.jackwang.rpc.registry.impl;

import io.netty.util.internal.StringUtil;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import site.jackwang.rpc.registry.AbstractServerRegistry;

/**
 * 本地缓存的服务注册器
 *
 * @author wangjie<http://www.jackwang.site/>
 * @date 2019/1/30
 */
public class LocalServerRegistry extends AbstractServerRegistry {
    private static volatile LocalServerRegistry instance = new LocalServerRegistry();

    public static LocalServerRegistry getInstance() {
        return instance;
    }

    @Override
    public void init(Map<String, String> param) {

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
