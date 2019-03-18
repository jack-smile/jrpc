package site.jackwang.rpc.loadbalance;

import site.jackwang.rpc.common.util.exception.JRpcException;
import site.jackwang.rpc.loadbalance.impl.ConsistentHashLoadBalance;
import site.jackwang.rpc.loadbalance.impl.RandomLoadBalance;
import site.jackwang.rpc.loadbalance.impl.RoundRobinLoadBalance;

/**
 * 负载均衡，单例
 *
 * @author wangjie<http://www.jackwang.site/>
 * @date 2019/3/15
 */
public enum LoadBalanceEnum {
    ROUND_ROBIN(RoundRobinLoadBalance.class),
    RANDOM(RandomLoadBalance.class),
    CONSISTENT_HASH(ConsistentHashLoadBalance.class);

    private Class<? extends AbstractLoadBalance> loadbalanceClass;

    LoadBalanceEnum(Class<? extends AbstractLoadBalance> loadbalanceClass) {
        this.loadbalanceClass = loadbalanceClass;
    }

    public AbstractLoadBalance getLoadBalance() {
        try {
            return loadbalanceClass.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            throw new JRpcException(e);
        }
    }
}
