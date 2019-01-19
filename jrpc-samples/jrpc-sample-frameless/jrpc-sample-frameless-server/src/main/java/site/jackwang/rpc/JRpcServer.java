package site.jackwang.rpc;

import site.jackwang.rpc.domain.UserBo;
import site.jackwang.rpc.remote.net.impl.netty.server.NettyServer;
import site.jackwang.rpc.remote.provider.JRpcProvider;
import site.jackwang.rpc.serialize.impl.ProtostuffSerializer;
import site.jackwang.rpc.service.CalculatorService;
import site.jackwang.rpc.service.impl.CalculatorServiceImpl;

import java.util.Arrays;

/**
 * @author wangjie<http://www.jackwang.site/>
 * @date 2019/1/17
 */
public class JRpcServer {
    public static void main(String[] args) {
        JRpcProvider jRpcProvider = new JRpcProvider();
        jRpcProvider.publishService(CalculatorService.class, new CalculatorServiceImpl());

        NettyServer server = new NettyServer(8888);
        server.start(jRpcProvider, new ProtostuffSerializer());
    }

    private static void testPojo() {
        ProtostuffSerializer serializer = new ProtostuffSerializer();
        UserBo user = UserBo.builder().name("小王").word("hello").build();

        byte[] bytes = serializer.serialize(user);
        System.out.println("序列化后：" + Arrays.toString(bytes));
        System.out.println("序列化后，长度：" + bytes.length);

        UserBo userDeserialize = serializer.deserialize(bytes, UserBo.class);
        System.out.println("反序列化后：" + userDeserialize.toString());
    }
}
