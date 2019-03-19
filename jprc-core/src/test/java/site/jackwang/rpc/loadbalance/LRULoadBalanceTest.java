package site.jackwang.rpc.loadbalance;

import java.util.LinkedHashMap;
import java.util.Map;
import org.junit.Test;

/**
 * @author wangjie<http://www.jackwang.site/>
 * @date 2019/3/19
 */
public class LRULoadBalanceTest {

    private static final int CACHE_ADDRESS_SIZE = 1024;

    private String address11 = "127.0.0.1:8888";
    private String address12 = "127.0.0.1:9999";
    private String address13 = "127.0.0.1:7777";
    private String address14 = "127.0.0.1:6666";

    private String address21 = "127.0.0.2:8888";
    private String address22 = "127.0.0.2:9999";
    private String address23 = "127.0.0.2:7777";
    private String address24 = "127.0.0.2:6666";

    @Test
    public void testLinkedHashMap() {
        LinkedHashMap<String, String> addresses = new LinkedHashMap<String, String>(16, 0.75f, true){
            @Override
            protected boolean removeEldestEntry(Map.Entry eldest) {
                return size() > CACHE_ADDRESS_SIZE;
            }
        };

        addresses.put(address11, address11);
        addresses.put(address12, address12);
        addresses.put(address13, address13);
        addresses.put(address14, address14);

        addresses.put(address21, address21);
        addresses.put(address22, address22);
        addresses.put(address23, address23);
        addresses.put(address24, address24);

        addresses.get(address12);
        addresses.get(address12);
        addresses.get(address13);

        for (Map.Entry<String, String> entry : addresses.entrySet()) {
            System.out.println(entry.getKey());
        }

        System.out.println();
        System.out.println(addresses.get(addresses.entrySet().iterator().next().getKey()));
    }
}
