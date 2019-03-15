package site.jackwang.rpc.remote.net.impl.netty.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import site.jackwang.rpc.serialize.AbstractSerializer;

import java.util.List;

/**
 * netty框架相关的解码器
 *
 * @author wangjie<http://www.jackwang.site/>
 * @date 2019/1/13
 */
public class NettyDecoder extends ByteToMessageDecoder {

    private Class<?> genericClass;
    private AbstractSerializer serializer;

    public NettyDecoder(Class<?> genericClass, final AbstractSerializer serializer) {
        this.genericClass = genericClass;
        this.serializer = serializer;
    }

    @Override
    public final void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        if (in.readableBytes() < 4) {
            return;
        }
        in.markReaderIndex();
        int dataLength = in.readInt();
        if (dataLength < 0) {
            ctx.close();
        }
        if (in.readableBytes() < dataLength) {
            in.resetReaderIndex();
            // fix 1024k buffer splice limix
            return;
        }
        byte[] data = new byte[dataLength];
        in.readBytes(data);

        Object obj = serializer.deserialize(data, genericClass);
        out.add(obj);
    }
}
