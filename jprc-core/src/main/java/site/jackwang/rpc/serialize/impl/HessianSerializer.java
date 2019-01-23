package site.jackwang.rpc.serialize.impl;

import com.caucho.hessian.io.Hessian2Input;
import com.caucho.hessian.io.Hessian2Output;
import site.jackwang.rpc.serialize.Serializer;
import site.jackwang.rpc.util.exception.JRpcException;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Hessian序列化
 * 一个性能较优且兼容性较好的二进制序列化协议。RPC
 * !!! the serialize class must implement java.io.Serializable
 *
 * @author wangjie<http://www.jackwang.site/>
 * @date 2019/1/22
 */
public class HessianSerializer extends Serializer {
    @Override
    public <T> byte[] serialize(T obj) {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        Hessian2Output ho = new Hessian2Output(os);
        try {
            ho.writeObject(obj);
            ho.flush();
            return os.toByteArray();
        } catch (IOException e) {
            throw new JRpcException(e);
        } finally {
            try {
                ho.close();
            } catch (IOException e) {
                throw new JRpcException(e);
            }
            try {
                os.close();
            } catch (IOException e) {
                throw new JRpcException(e);
            }
        }
    }

    @Override
    public <T> T deserialize(byte[] bytes, Class<T> clazz) {
        ByteArrayInputStream is = new ByteArrayInputStream(bytes);
        Hessian2Input hi = new Hessian2Input(is);
        try {
            Object result = hi.readObject();
            return (T) result;
        } catch (IOException e) {
            throw new JRpcException(e);
        } finally {
            try {
                hi.close();
            } catch (Exception e) {
                throw new JRpcException(e);
            }
            try {
                is.close();
            } catch (IOException e) {
                throw new JRpcException(e);
            }
        }
    }
}
