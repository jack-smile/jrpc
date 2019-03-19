package site.jackwang.rpc.loadbalance.impl;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;
import site.jackwang.rpc.loadbalance.AbstractLoadBalance;

/**
 * 一致性hash
 * 首先根据 ip 或者其他的信息为缓存节点生成一个 hash，并将这个 hash 投射到 [0, 2^32 - 1] 的圆环上。
 * 当有查询或写入请求时，则为缓存项的 key 生成一个 hash 值。
 * 然后查找第一个大于或等于该 hash 值的缓存节点，并到这个节点中查询或写入缓存项。
 * 如果当前节点挂了，则在下一次查询或写入缓存时，为缓存项查找另一个大于其 hash 值的缓存节点即可。
 *
 * 虚拟节点：避免数据倾斜（热点问题）
 *
 * @author wangjie<http://www.jackwang.site/>
 * @date 2019/3/18
 */
public class ConsistentHashLoadBalance extends AbstractLoadBalance {

    /**
     * 默认虚拟节点数
     */
    private static final int DEFAULT_VIRTUAL_NODE_NUM = 160;

    @Override
    protected String doSelect(String serverName, HashSet<String> serverAddresses) {
        // 生成环形服务器虚拟节点
        // 为了提供高效的查询效率，这里使用TreeMap
        TreeMap<Long, String> virtualServerAddresses = new TreeMap<>();
        for (String serverAddress : serverAddresses) {
            for (int i = 0; i < DEFAULT_VIRTUAL_NODE_NUM / 4; i++) {
                byte[] digest = md5(serverAddress + i);
                for (int h = 0; h < 4; h++) {
                    long addressHash = hash(digest, h);
                    virtualServerAddresses.put(addressHash, serverAddress);
                }
            }
        }

        // 根据服务名称，获取提供服务的服务器地址
        long serverHash = hash(md5(serverName), 0);
        Map.Entry<Long, String> addressEntry = virtualServerAddresses.ceilingEntry(serverHash);
        if (Objects.nonNull(addressEntry)) {
            addressEntry = virtualServerAddresses.firstEntry();
        }

        return addressEntry.getValue();
    }

    /**
     * 对字符数组进行hash
     *
     * @param digest 摘要信息，字符数组
     * @param number 取0 时，取 digest 中下标为 0 ~ 3 的4个字节进行位运算
     *               取h = 1 时，取 digest 中下标为 4 ~ 7 的4个字节进行位运算
     *               取2或3 时过程同上
     * @return hash值
     */
    private long hash(byte[] digest, int number) {
        return (((long) (digest[3 + number * 4] & 0xFF) << 24)
            | ((long) (digest[2 + number * 4] & 0xFF) << 16)
            | ((long) (digest[1 + number * 4] & 0xFF) << 8)
            | (digest[number * 4] & 0xFF))
            & 0xFFFFFFFFL;
    }

    private byte[] md5(String value) {
        MessageDigest md5;
        try {
            md5 = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException(e.getMessage(), e);
        }
        md5.reset();
        byte[] bytes = value.getBytes(StandardCharsets.UTF_8);
        md5.update(bytes);
        return md5.digest();
    }
}
