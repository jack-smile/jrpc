package site.jackwang.rpc.loadbalance;

import java.util.HashSet;
import site.jackwang.rpc.common.util.CollectionUtils;

/**
 * 负载均衡抽象类
 * TODO 权重、是否存活
 *
 * @author <http://www.jackwang.site/>
 * @date 2019/3/15
 */
public abstract class AbstractLoadBalance implements LoadBalance {
    @Override
    public String select(String serverName, HashSet<String> serverAddresses) {
        if (CollectionUtils.isEmpty(serverAddresses)) {
            return null;
        }

        if (serverAddresses.size() == 1) {
            return serverAddresses.iterator().next();
        }

        return doSelect(serverName, serverAddresses);
    }

    /**
     * 具体的负载均衡算法由子类实现
     */
    protected abstract String doSelect(String serverName, HashSet<String> serverAddresses);
}
