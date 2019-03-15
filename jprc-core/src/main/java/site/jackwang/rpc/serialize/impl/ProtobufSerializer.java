package site.jackwang.rpc.serialize.impl;

import io.protostuff.LinkedBuffer;
import io.protostuff.ProtostuffIOUtil;
import io.protostuff.Schema;
import io.protostuff.runtime.RuntimeSchema;
import org.objenesis.Objenesis;
import org.objenesis.ObjenesisStd;
import site.jackwang.rpc.serialize.Serializer;
import site.jackwang.rpc.common.util.exception.ErrorCodes;
import site.jackwang.rpc.common.util.exception.JRpcException;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 基于谷歌的protobuf实现序列化
 *
 * @author wangjie<http://www.jackwang.site/>
 * @date 2019/1/13
 */
public class ProtobufSerializer extends Serializer {
    private static Objenesis objenesis = new ObjenesisStd(true);
    private static Map<Class<?>, Schema<?>> schemaCache = new ConcurrentHashMap<>();

    @Override
    public <T> byte[] serialize(T obj) {
        @SuppressWarnings("unchecked")
        Class<T> cls = (Class<T>) obj.getClass();
        LinkedBuffer buffer = LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE);
        try {
            Schema<T> schema = getSchema(cls);
            return ProtostuffIOUtil.toByteArray(obj, schema, buffer);
        } catch (Exception e) {
            throw new JRpcException(e, ErrorCodes.SERIALIZE_FAILURE, obj);
        } finally {
            buffer.clear();
        }
    }

    @Override
    public <T> T deserialize(byte[] bytes, Class<T> clazz) {
        try {
            T message = objenesis.newInstance(clazz);
            Schema<T> schema = getSchema(clazz);
            ProtostuffIOUtil.mergeFrom(bytes, message, schema);
            return message;
        } catch (Exception e) {
            throw new JRpcException(e, ErrorCodes.DESERIALIZE_FAILURE, clazz.getName());
        }
    }

    private static <T> Schema<T> getSchema(Class<T> clazz) {
        @SuppressWarnings("unchecked")
        Schema<T> schema = (Schema<T>) schemaCache.get(clazz);
        if (schema == null) {
            schema = RuntimeSchema.createFrom(clazz);
            if (schema != null) {
                schemaCache.put(clazz, schema);
            }
        }
        return schema;
    }
}
