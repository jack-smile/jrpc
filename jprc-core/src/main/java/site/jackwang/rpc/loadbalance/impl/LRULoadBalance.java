package site.jackwang.rpc.loadbalance.impl;

import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import site.jackwang.rpc.common.util.CollectionUtils;
import site.jackwang.rpc.loadbalance.AbstractLoadBalance;

/**
 * LRU(Least Recently Used) 最近最少使用到的
 * 基于 LinkedHashMap(HashMap 和 双向链表) 实现LRU
 * 时间复杂度O(1)
 *
 * TODO LRF-K 优化
 *
 * @author wangjie<http://www.jackwang.site/>
 * @date 2019/3/19
 */
public class LRULoadBalance extends AbstractLoadBalance {
    /**
     * 以lru方式存储每个服务相应的服务器集群地址信息
     * key：服务名称
     * value：地址，LinkedHashMap底层就是HashMap和双向链表，而且本身已经实现了按照访问顺序的存储。
     */
    private Map<String, LinkedHashMap<String, String>> lruServerAddresses = new ConcurrentHashMap<>();

    /**
     * 每个服务所保存的最大集群地址个数
     */
    private static final int CACHE_ADDRESS_SIZE = 1024;

    @Override
    protected String doSelect(String serverName, HashSet<String> serverAddresses) {
        LinkedHashMap<String, String> addresses = lruServerAddresses.get(serverName);
        if (CollectionUtils.isEmptyMap(addresses)) {
            addresses = new LinkedHashMap<String, String>(16, 0.75f, true){
                @Override
                protected boolean removeEldestEntry(Map.Entry eldest) {
                    return size() > CACHE_ADDRESS_SIZE;
                }
            };

            lruServerAddresses.put(serverName, addresses);
        }

        // 将新的地址加入到缓存中
        for (String serverAddress : serverAddresses) {
            if (!addresses.containsKey(serverAddress)) {
                addresses.put(serverAddress, serverAddress);
            }
        }

        // 移出已注销的地址
        addresses.entrySet().removeIf(item -> !serverAddresses.contains(item.getKey()));

        // 需通过key来访问，LinkedHashMap内部可根据访问次数自动排序，最新访问的排在队尾
        return addresses.get(addresses.entrySet().iterator().next().getKey());
    }
}
