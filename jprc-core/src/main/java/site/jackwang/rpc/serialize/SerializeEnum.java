package site.jackwang.rpc.serialize;

import site.jackwang.rpc.serialize.impl.*;
import site.jackwang.rpc.common.util.exception.JRpcException;

/**
 * 序列化单例
 * 枚举单例会初始化全部实现，此处改为托管Class，避免无效的实例化
 *
 * @author wangjie<http://www.jackwang.site/>
 * @date 2019/1/22
 */
public enum SerializeEnum {
    HESSIAN(HessianSerializer.class),
    JACKSON(JacksonSerializer.class),
    PROTOBUF(ProtobufSerializer.class),
    PROTOSTUFF(ProtostuffSerializer.class),
    JDK_SERIALIZE(JDKSerializer.class),
    FASTJSON_SERIALIZE(FastjsonSerializer.class);

    private Class<? extends Serializer> serializerClass;

    SerializeEnum(Class<? extends Serializer> serializerClass) {
        this.serializerClass = serializerClass;
    }

    public Serializer getSerializer() {
        try {
            return serializerClass.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            throw new JRpcException(e);
        }
    }
}
