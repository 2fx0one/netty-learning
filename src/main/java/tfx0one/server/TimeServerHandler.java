package tfx0one.server;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.FileRegion;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.internal.logging.InternalLoggerFactory;

import java.util.Date;
import java.util.logging.Logger;

/**
 * Created by 2fx0one on 17/9/2018.
 */
public class TimeServerHandler extends SimpleChannelInboundHandler<ByteBuf> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ByteBuf buf) throws Exception {
        //读
        byte[] req = new byte[buf.readableBytes()];
        buf.readBytes(req);
        String body = new String(req, "UTF-8");
        System.out.println("receive: " + body);

        //写
        String currentTime = "TIME".equalsIgnoreCase(body) ? new Date().toString() : "BAD request";
        ByteBuf response = Unpooled.copiedBuffer(currentTime.getBytes());
        ctx.write(response);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("活动 channelActive :  " + ctx.channel().remoteAddress());
        super.channelActive(ctx);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("断开 channelInactive :  " + ctx.channel().remoteAddress());
        super.channelInactive(ctx);
    }


//    @Override
//    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception{
//        ByteBuf buf = (ByteBuf) msg;
//
//        //读
//        byte[] req = new byte[buf.readableBytes()];
//        buf.readBytes(req);
//        String body = new String(req, "UTF-8");
//        System.out.println("receive: " + body);
//
//        //写
//        String currentTime = "TIME".equalsIgnoreCase(body) ? new Date().toString() : "BAD request";
//        ByteBuf response = Unpooled.copiedBuffer(currentTime.getBytes());
//        ctx.write(response);
//    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }

    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }

}
