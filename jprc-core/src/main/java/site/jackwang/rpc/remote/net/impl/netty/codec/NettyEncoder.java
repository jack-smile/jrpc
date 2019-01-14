package site.jackwang.rpc.remote.net.impl.netty.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import site.jackwang.rpc.serialize.Serializer;

/**
 * netty框架相关的编码器
 *
 * @author wangjie<http://www.jackwang.site/>
 * @date 2019/1/13
 */
public class NettyEncoder extends MessageToByteEncoder<Object> {
    private Class<?> genericClass;
    private Serializer serializer;

    public NettyEncoder(Class<?> genericClass, final Serializer serializer) {
        this.genericClass = genericClass;
        this.serializer = serializer;
    }

    @Override
    public void encode(ChannelHandlerContext ctx, Object in, ByteBuf out) throws Exception {
        if (genericClass.isInstance(in)) {
            byte[] data = serializer.serialize(in);
            out.writeInt(data.length);
            out.writeBytes(data);
        }
    }
}
