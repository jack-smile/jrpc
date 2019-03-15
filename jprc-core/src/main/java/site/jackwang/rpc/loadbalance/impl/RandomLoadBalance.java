package site.jackwang.rpc.loadbalance.impl;

import site.jackwang.rpc.loadbalance.AbstractLoadBalance;

import java.util.HashSet;
import java.util.Random;

/**
 * 随机
 *
 * @author wangjie<http://www.jackwang.site/>
 * @date 2019/3/15
 */
public class RandomLoadBalance extends AbstractLoadBalance {
    @Override
    protected String doSelect(String serverName, HashSet<String> serverAddresses) {
        String[] addresses = serverAddresses.toArray(new String[0]);

        return addresses[new Random().nextInt(addresses.length)];
    }
}
