package site.jackwang.rpc.serialize.impl;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import site.jackwang.rpc.serialize.Serializer;
import site.jackwang.rpc.util.exception.JRpcException;

/**
 * jdk自带的序列化工具
 * 一个对象可以被表示为一个字节序列，该字节序列包括该对象的数据、有关对象的类型的信息和存储在对象中数据的类型。
 * !!! the serialize class must implement java.io.Serializable
 *
 * @author wangjie<http://www.jackwang.site/>
 * @date 2019/1/23
 */
public class JDKSerializer extends Serializer {
    @Override
    public <T> byte[] serialize(T obj) {
        ByteArrayOutputStream os = new ByteArrayOutputStream();

        try {
            ObjectOutputStream oos = new ObjectOutputStream(os);
            oos.writeObject(obj);
            return os.toByteArray();
        } catch (IOException e) {
            throw new JRpcException(e);
        }
    }

    @Override
    public <T> T deserialize(byte[] bytes, Class<T> clazz) {
        try {
            ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(bytes));
            return (T) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new JRpcException(e);
        }
    }
}
