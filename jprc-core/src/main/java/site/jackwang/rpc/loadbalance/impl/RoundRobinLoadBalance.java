package site.jackwang.rpc.loadbalance.impl;

import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import site.jackwang.rpc.loadbalance.AbstractLoadBalance;

/**
 * 轮询
 *
 * @author <http://www.jackwang.site/>
 * @date 2019/3/15
 */
public class RoundRobinLoadBalance extends AbstractLoadBalance {
    /**
     * 记录当前所使用的机器在集群中的序号
     */
    private static Map<String, Integer> serverSequences = new ConcurrentHashMap<>();

    /**
     * {@link serverSequences}上一次复位时间
     */
    private static long CACHE_LAST_RESET_TIME = System.currentTimeMillis();

    /**
     * {@link serverSequences}存活时间
     */
    private static final int CACHE_SURVIVAL_TIME = 1000 * 60 * 60 * 24;


    @Override
    protected String doSelect(String serverName, HashSet<String> serverAddresses) {
        String[] addresses = serverAddresses.toArray(new String[0]);

        Integer sequence = getSequence(serverName);

        return addresses[sequence % addresses.length];
    }

    /**
     * 获取下一个服务器的轮询序号
     *
     * @param serverName 服务名称
     * @return 序号
     */
    private Integer getSequence(String serverName) {
        if (System.currentTimeMillis() - CACHE_LAST_RESET_TIME > CACHE_SURVIVAL_TIME) {
            serverSequences.clear();
            CACHE_LAST_RESET_TIME = System.currentTimeMillis() + CACHE_SURVIVAL_TIME;
        }

        Integer sequence = serverSequences.get(serverName);
        if (Objects.isNull(sequence)) {
            // 第一次调用时，使用随机数，防止所有服务的请求都打到第一台服务器上，缓解压力
            sequence = new Random().nextInt(100);
        }

        serverSequences.put(serverName, sequence++);
        return sequence;
    }
}
