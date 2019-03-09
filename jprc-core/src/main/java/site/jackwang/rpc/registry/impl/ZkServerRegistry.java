package site.jackwang.rpc.registry.impl;

import io.netty.util.internal.StringUtil;
import org.apache.zookeeper.Watcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import site.jackwang.rpc.registry.AbstractServerRegistry;
import site.jackwang.rpc.util.JZkClient;

import java.util.HashSet;
import java.util.Map;
import java.util.Objects;

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
                // 继续监听
                zkClient.getClient().exists(path, null);

                if (Watcher.Event.EventType.NodeChildrenChanged == event.getType()) {
                    refreshRegistryServers(serverName);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        // 后台开启线程定时刷新

        logger.info(">>>>>>>>>>> jrpc, ZkServerRegistry init success. [env={}]", zkPath);
    }

    @Override
    public void stop() {

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

        String path = serverNameToPath(serverName);
        zkClient.setChildPathData(path, address, "");

        return true;
    }

    @Override
    public boolean remove(String serverName, String address) {
        return false;
    }

    /**
     * 根据服务器名称生成相应的节点路径
     *
     * @param serverName 服务器名称
     * @return 相应的节点路径
     */
    private String serverNameToPath(String serverName) {
        return zkPath + "/" + serverName;
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

    private void refreshRegistryServers(String serverName) {
    }
}
