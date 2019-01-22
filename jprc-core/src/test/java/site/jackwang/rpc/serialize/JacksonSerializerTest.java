package site.jackwang.rpc.serialize;

import org.junit.Test;
import site.jackwang.rpc.domain.UserBo;
import site.jackwang.rpc.remote.net.params.JRpcRequest;
import site.jackwang.rpc.serialize.impl.JacksonSerializer;

import java.util.Arrays;
import java.util.UUID;

/**
 * @author wangjie<http://www.jackwang.site/>
 * @date 2019/1/21
 */
public class JacksonSerializerTest {
    @Test
    public void testPojo() {
        JacksonSerializer serializer = new JacksonSerializer();
        UserBo user = UserBo.builder().name("小王").word("hello").build();

        byte[] bytes = serializer.serialize(user);
        System.out.println("序列化后：" + Arrays.toString(bytes));
        System.out.println("序列化后，长度：" + bytes.length);

        UserBo userDeserialize = serializer.deserialize(bytes, UserBo.class);
        System.out.println("反序列化后：" + userDeserialize.toString());
    }

    @Test
    public void testRequest() {
        JacksonSerializer serializer = new JacksonSerializer();

        JRpcRequest request = new JRpcRequest();
        request.setServiceName("site.jackwang.rpc.test.CalculatorService");
        request.setMethodName("add");
        request.setId(UUID.randomUUID().toString());
        request.getParams()[0] = 1.0f;
        request.getParams()[1] = 1.2f;
        request.setParamTypes(new Class[]{double.class, double.class});

        byte[] bytes = serializer.serialize(request);
        System.out.println("序列化后：" + Arrays.toString(bytes));
        System.out.println("序列化后，长度：" + bytes.length);

        JRpcRequest requestDeserialize = serializer.deserialize(bytes, JRpcRequest.class);
        System.out.println("反序列化后：" + requestDeserialize.toString());
    }
}
