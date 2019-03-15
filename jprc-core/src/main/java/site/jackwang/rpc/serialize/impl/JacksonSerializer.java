package site.jackwang.rpc.serialize.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import site.jackwang.rpc.serialize.Serializer;
import site.jackwang.rpc.common.util.exception.ErrorCodes;
import site.jackwang.rpc.common.util.exception.JRpcException;

import java.io.IOException;

/**
 * 基于jckson的序列化
 * 可以轻松的将Java对象转换成json对象和xml文档
 *
 * @author wangjie<http://www.jackwang.site/>
 * @date 2019/1/21
 */
public class JacksonSerializer extends Serializer {
    private final static ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public <T> byte[] serialize(T obj) {
        try {
            return objectMapper.writeValueAsBytes(obj);
        } catch (JsonProcessingException e) {
            throw new JRpcException(e, ErrorCodes.SERIALIZE_FAILURE, obj);
        }
    }

    @Override
    public <T> T deserialize(byte[] data, Class<T> clazz) {
        try {
            return objectMapper.readValue(data, clazz);
        } catch (IOException e) {
            throw new JRpcException(e, ErrorCodes.DESERIALIZE_FAILURE, clazz.getName());
        }
    }
}
