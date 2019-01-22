package site.jackwang.rpc.serialize;

/**
 * 序列化抽象类，定义了序列化的框架
 * 模板模式
 *
 * @author wangjie<http://www.jackwang.site/>
 * @date 2019/1/13
 */
public abstract class Serializer {
    /**
     * 序列化
     *
     * @param obj 将要被序列化的对象
     * @return 序列化后的字符数组
     */
    public abstract <T> byte[] serialize(T obj);

    /**
     * 反序列化
     *
     * @param bytes 将要被反序列化的字符数组
     * @param clazz 将要被反序列化成一个什么样的对象
     * @return 反序列化后的对象实例
     */
    public abstract <T> T deserialize(byte[] bytes, Class<T> clazz);
}
