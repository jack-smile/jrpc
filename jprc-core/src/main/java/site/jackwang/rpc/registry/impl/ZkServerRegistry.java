package site.jackwang.rpc.registry.impl;

import io.netty.util.internal.StringUtil;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import org.apache.zookeeper.Watcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import site.jackwang.rpc.registry.AbstractServerRegistry;
import site.jackwang.rpc.util.JZkClient;

/**
 * 使用zookeeper作为注册中心
 *
 * @author wangjie<http://www.jackwang.site/>
 * @date 2019/3/7
 */
public class ZkServerRegistry extends AbstractServerRegistry {
    private static Logger logger = LoggerFactory.getLogger(ZkServerRegistry.class);

    /**
     * zookeeper地址，支持集群
     * 格式：ip1:port1,ip2:port2,ip3:port3
     */
    public static final String ZK_ADDRESS = "zkAddress";

    /**
     * 环境，这里用作节点的相对路径
     */
    public static final String ENV = "env";

    /**
     * zookeeper认证摘要
     */
    public static final String ZK_DIGEST = "zkDigest";

    /**
     * zookeeper节点根路径
     */
    private static final String ZK_BASE_PATH = "/jrpc";

    /**
     * zookeeper地址
     */
    private String zkAddress;

    /**
     * zookeeper客户端
     */
    private JZkClient zkClient;

    /**
     * 节点路径
     */
    private String zkPath;

    /**
     * 认证摘要
     */
    private String zkDigest;

    /**
     * 后台刷新数据线程
     */
    private Thread refreshThread;

    /**
     * 后台刷新数据线程停止标记
     */
    private volatile boolean refreshThreadStop;


    @Override
    public void init(Map<String, String> param) {
        this.zkAddress = param.get(ZK_ADDRESS);
        this.zkPath = ZK_BASE_PATH.concat("/").concat(param.get(ENV));
        this.zkDigest = param.get(ZK_DIGEST);
    }

    @Override
    public void start() {
        zkClient = new JZkClient(zkAddress, zkPath, zkDigest, event -> {
            if (Watcher.Event.KeeperState.Expired == event.getState()) {
                // 过期，关闭旧连接，重新创建新连接
                zkClient.destroy();
                zkClient.getClient();

                logger.info(">>>>>>>>>>> jrpc, zk re-connect reloadAll success.");
            }

            String path = event.getPath();
            String serverName = pathToServerName(path);
            try {
                if (Objects.nonNull(path)) {
                    // 继续监听
                    zkClient.getClient().exists(path, null);

                    if (Watcher.Event.EventType.NodeChildrenChanged == event.getType()) {
                        refreshLocalRegistryServers(serverName);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        // 后台开启线程定时刷新本地缓存
        refreshThread = new Thread(() -> {
            while (!refreshThreadStop) {
                try {
                    TimeUnit.SECONDS.sleep(60);

                    refreshLocalRegistryServers(null);
                } catch (Exception e) {
                    logger.error(">>>>>>>>>>> jrpc, refresh thread error: ", e);
                }
            }
        });
        refreshThread.setName("jrpc zk refresh server thread");
        refreshThread.setDaemon(true);
        refreshThread.start();

        logger.info(">>>>>>>>>>> jrpc, ZkServerRegistry init success. [env={}]", zkPath);
    }

    @Override
    public void stop() {
        if (Objects.nonNull(zkClient)) {
            zkClient.destroy();
        }

        if (Objects.nonNull(refreshThread)) {
            refreshThreadStop = true;
            refreshThread.interrupt();
        }
    }

    @Override
    public HashSet<String> lookupOne(String serverName) {
        HashSet<String> addresses = registryServers.get(serverName);
        if (Objects.isNull(addresses)) {
            refreshLocalRegistryServers(serverName);

            addresses = registryServers.get(serverName);
        }
        return addresses;
    }

    @Override
    public boolean register(String serverName, String address) {
        if (StringUtil.isNullOrEmpty(serverName) || StringUtil.isNullOrEmpty(address)) {
            return false;
        }

        // 本地存储
        HashSet<String> addresses = registryServers.get(serverName);
        if (Objects.isNull(addresses)) {
            addresses = new HashSet<>();
            registryServers.put(serverName, addresses);
        }
        addresses.add(address);

        // zk存储
        String path = serverNameToPath(serverName);
        zkClient.setChildPathData(path, address, "");

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

        addresses.remove(address);

        String path = serverNameToPath(serverName);
        try {
            zkClient.deleteChildPath(path, address);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 根据服务器名称生成相应的节点路径
     *
     * @param serverName 服务器名称
     * @return 相应的节点路径
     */
    private String serverNameToPath(String serverName) {
        return StringUtil.isNullOrEmpty(serverName) ? zkPath : zkPath + "/" + serverName;
    }

    /**
     * 根据节点路径获取服务名称
     *
     * @param nodePath 节点路径
     * @return 相应的服务名称
     */
    private String pathToServerName(String nodePath) {
        if (StringUtil.isNullOrEmpty(nodePath) || nodePath.length() <= zkPath.length() || !nodePath.startsWith(zkPath)) {
            return null;
        }

        return nodePath.substring(zkPath.length(), nodePath.length());
    }

    /**
     * 刷新本地缓存的服务及相应的服务器地址信息
     *
     * @param serverName 指定刷新的服务名称；如果传null或者""，则刷新所有
     */
    private void refreshLocalRegistryServers(String serverName) {
        Set<String> serverNames = new HashSet<>();
        if (StringUtil.isNullOrEmpty(serverName)) {
            if (registryServers.size() > 0) {
                serverNames.addAll(registryServers.keySet());
            }
        } else {
            serverNames.add(serverName);
        }

        for (String serverNameItem : serverNames) {
            String path = serverNameToPath(serverNameItem);
            Map<String, String> address = zkClient.getChildPathData(path);

            HashSet<String> addresses = registryServers.get(serverNameItem);
            if (Objects.isNull(addresses)) {
                addresses = new HashSet<>();
                registryServers.put(serverNameItem, addresses);
            }
            if (Objects.nonNull(address) && address.size() > 0) {
                addresses.clear();
                addresses.addAll(address.keySet());
            }
        }

        logger.info(">>>>>>>>>>> jrpc, refresh discovery data success, discoveryData = {}", registryServers);
    }
}
