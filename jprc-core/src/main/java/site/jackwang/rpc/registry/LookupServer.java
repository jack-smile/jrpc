package site.jackwang.rpc.registry;

import java.util.HashSet;

/**
 * 查找活跃的服务器信息
 *
 * @author wangjie<http://www.jackwang.site/>
 * @date 2019/1/30
 */
public interface LookupServer {
    /**
     * 查找一个活跃的服务器集群地址
     *
     * @param serverName 服务名称
     * @return 服务器地址
     */
    HashSet<String> lookupOne(String serverName);
}
