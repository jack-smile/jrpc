package site.jackwang.rpc.serialize;

import org.junit.Test;
import site.jackwang.rpc.remote.net.params.JRpcRequest;

import java.util.Arrays;
import java.util.UUID;

/**
 * @author wangjie<http://www.jackwang.site/>
 * @date 2019/1/22
 */
public class SerializerTest {
    @Test
    public void testSerializer() {
        for (SerializeEnum serializeEnum : SerializeEnum.values()) {
            System.out.println("serialize type: " + serializeEnum.name());
            testRequest(serializeEnum.getSerializer());
            System.out.println("---------------------------------------");
            System.out.println();
        }
    }

    private void testRequest(Serializer serializer) {

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
