package site.jackwang.rpc.loadbalance;

import java.util.HashSet;

/**
 * 负载均衡
 *
 * @author wangjie<http://www.jackwang.site/>
 * @date 2019/3/15
 */
public interface LoadBalance {
    /**
     * 通过负载均衡算法，选出其中一个服务器地址
     *
     * @param serverName 服务名称
     * @param serverAddresses 服务相应的集群地址集，格式：ip:port
     * @return 服务器地址
     */
    String select(String serverName, HashSet<String> serverAddresses);
}
