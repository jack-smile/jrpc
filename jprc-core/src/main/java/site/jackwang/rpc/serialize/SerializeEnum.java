package site.jackwang.rpc.serialize;

import site.jackwang.rpc.serialize.impl.*;
import site.jackwang.rpc.common.util.exception.JRpcException;

/**
 * 序列化单例
 * 枚举单例会初始化全部实现，此处改为托管Class，避免无效的实例化
 * 不仅能避免多线程同步问题，还能防止反序列化重新创建新的对象
 * 这种方式是Effective Java作者Josh Bloch 提倡的方式
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

    private Class<? extends AbstractSerializer> serializerClass;

    SerializeEnum(Class<? extends AbstractSerializer> serializerClass) {
        this.serializerClass = serializerClass;
    }

    public AbstractSerializer getSerializer() {
        try {
            return serializerClass.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            throw new JRpcException(e);
        }
    }
}
