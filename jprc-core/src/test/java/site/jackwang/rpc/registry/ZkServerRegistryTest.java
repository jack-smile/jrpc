package site.jackwang.rpc.registry;

import org.junit.Test;
import site.jackwang.rpc.registry.impl.ZkServerRegistry;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author wangjie<http://www.jackwang.site/>
 * @date 2019/3/9
 */
public class ZkServerRegistryTest {
    private String serverName = "demoServer";
    private String address = "127.0.0.1:8888";

    @Test
    public void testStart() throws InterruptedException {
        ZkServerRegistry zkServerRegistry = new ZkServerRegistry();

        Map<String, String> params = new HashMap<>();
        params.put(ZkServerRegistry.ZK_ADDRESS, "127.0.0.1:2181");
        params.put(ZkServerRegistry.ZK_DIGEST, "");
        params.put(ZkServerRegistry.ENV, "dev");
        zkServerRegistry.init(params);

        zkServerRegistry.start();

        System.out.println("discovery server: " + zkServerRegistry.lookupOne(serverName));

        zkServerRegistry.register(serverName, address);

        System.out.println("discovery server: " + zkServerRegistry.lookupOne(serverName));
    }
}
