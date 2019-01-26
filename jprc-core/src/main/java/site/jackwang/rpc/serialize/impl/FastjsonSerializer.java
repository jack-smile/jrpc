package site.jackwang.rpc.serialize.impl;

import com.alibaba.fastjson.JSON;
import site.jackwang.rpc.serialize.Serializer;

/**
 * fastjson序列化
 * 支持多种方式定制序列化，速度快、使用广泛、测试完备、功能完备
 * 通过@JSONField定制序列化
 * 通过@JSONType定制序列化
 * 通过SerializeFilter定制序列化
 * 通过ParseProcess定制反序列化
 *
 * @author wangjie<http://www.jackwang.site/>
 * @date 2019/1/26
 */
public class FastjsonSerializer extends Serializer {

    @Override
    public <T> byte[] serialize(T obj) {
        return JSON.toJSONBytes(obj);
    }

    @Override
    public <T> T deserialize(byte[] bytes, Class<T> clazz) {
        return JSON.parseObject(bytes, clazz);
    }
}
