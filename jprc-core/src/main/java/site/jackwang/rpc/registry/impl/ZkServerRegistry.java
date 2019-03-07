package site.jackwang.rpc.registry.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import site.jackwang.rpc.registry.AbstractServerRegistry;
import site.jackwang.rpc.util.JZkClient;

import java.util.HashSet;
import java.util.Map;

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

        });
    }

    @Override
    public void stop() {

    }

    @Override
    public HashSet<String> lookupOne(String serverName) {
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
