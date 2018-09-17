package tfx0one.client;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * Created by 2fx0one on 17/9/2018.
 */
public class TimeClientHandler extends SimpleChannelInboundHandler<ByteBuf> {

    protected void channelRead0(ChannelHandlerContext channelHandlerContext, ByteBuf buf) throws Exception {
        byte[] request = new byte[buf.readableBytes()];
        buf.readBytes(request);
        String body = new String(request, "utf-8");
        System.out.println("channelRead0 client receive : " + body);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        byte[] req = "TIME".getBytes();
        ByteBuf msg = Unpooled.buffer(req.length);
        msg.writeBytes(req);
        ctx.writeAndFlush(msg);
        System.out.println("连接已建立!");
    }

//    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
//        ByteBuf buf = (ByteBuf) msg;
//        byte[] request = new byte[buf.readableBytes()];
//        buf.readBytes(request);
//        String body = new String(request, "utf-8");
//        System.out.println("channelRead client receive : " + body);
//    }

    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
