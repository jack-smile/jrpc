package site.jackwang.rpc.serialize.impl;

import io.protostuff.LinkedBuffer;
import io.protostuff.ProtostuffIOUtil;
import io.protostuff.Schema;
import io.protostuff.runtime.RuntimeSchema;
import site.jackwang.rpc.serialize.AbstractSerializer;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 基于的谷歌的protobuf实现的序列化方案，不需要定义.proto文件，也不需要去下载protoc.exe编译，方便java开发
 *
 * @author wangjie<http://www.jackwang.site/>
 * @date 2019/1/13
 */
public class ProtostuffSerializer extends AbstractSerializer {
    private static Map<Class<?>, Schema<?>> schemaCache = new ConcurrentHashMap<>();

    @SuppressWarnings("unchecked")
    public <T> byte[] serialize(T obj) {
        // 获取泛型对象的类型
        Class<T> clazz = (Class<T>) obj.getClass();
        // 创建泛型对象的schema对象
        RuntimeSchema<T> schema = RuntimeSchema.createFrom(clazz);
        // 创建LinkedBuffer对象
        LinkedBuffer buffer = LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE);
        // 序列化
        // 返回序列化对象
        return ProtostuffIOUtil.toByteArray(obj, schema, buffer);
    }

    public <T> T deserialize(byte[] data, Class<T> clazz) {
        // 创建泛型对象的schema对象
        RuntimeSchema<T> schema = RuntimeSchema.createFrom(clazz);
        // 根据schema实例化对象
        T message = schema.newMessage();
        // 将字节数组中的数据反序列化到message对象
        ProtostuffIOUtil.mergeFrom(data, message, schema);
        // 返回反序列化对象
        return message;
    }

    /**
     * 根据获取相应类型的schema方法
     *
     * @param clazz 类
     * @return 相应类型的schema方法
     */
    @SuppressWarnings({"unchecked", "unused"})
    private <T> RuntimeSchema<T> getSchema(Class<T> clazz) {
        // 先尝试从缓存schema map中获取相应类型的schema
        RuntimeSchema<T> schema = (RuntimeSchema<T>) schemaCache.get(clazz);
        // 如果没有获取到对应的schema，则创建一个该类型的schema
        // 同时将其添加到schema map中
        if (Objects.isNull(schema)) {
            schema = RuntimeSchema.createFrom(clazz);
            schemaCache.put(clazz, schema);
        }
        // 返回schema对象
        return schema;
    }
}
