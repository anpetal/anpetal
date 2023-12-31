package org.noear.socketd.transport.netty.tcp.impl;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.AttributeKey;
import org.noear.socketd.exception.SocketdConnectionException;
import org.noear.socketd.transport.client.ClientHandshakeResult;
import org.noear.socketd.transport.netty.tcp.TcpNioClient;
import org.noear.socketd.transport.core.Channel;
import org.noear.socketd.transport.core.Flag;
import org.noear.socketd.transport.core.Frame;
import org.noear.socketd.transport.core.impl.ChannelDefault;

import java.util.concurrent.CompletableFuture;

/**
 * @author noear
 * @since 2.0
 */
public class NettyClientInboundHandler extends SimpleChannelInboundHandler<Frame> {
    private static AttributeKey<Channel> CHANNEL_KEY = AttributeKey.valueOf("CHANNEL_KEY");

    private final TcpNioClient client;
    private final CompletableFuture<ClientHandshakeResult> handshakeFuture = new CompletableFuture<>();
    private Channel channel;

    public NettyClientInboundHandler(TcpNioClient client) {
        this.client = client;
    }

    public CompletableFuture<ClientHandshakeResult> getHandshakeFuture() {
        return handshakeFuture;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);

        channel = new ChannelDefault<>(ctx.channel(), client.config(), client.assistant());
        ctx.attr(CHANNEL_KEY).set(channel);

        //开始握手
        channel.sendConnect(client.config().getUrl());
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Frame frame) throws Exception {
        Channel channel = ctx.attr(CHANNEL_KEY).get();

        try {
            client.processor().onReceive(channel, frame);

            if (frame.getFlag() == Flag.Connack) {
                //握手完成，通道可用了
                handshakeFuture.complete(new ClientHandshakeResult(channel, null));
            }
        } catch (SocketdConnectionException e) {
            //说明握手失败了
            handshakeFuture.complete(new ClientHandshakeResult(channel, e));
        }
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        super.channelInactive(ctx);

        Channel channel = ctx.attr(CHANNEL_KEY).get();
        client.processor().onClose(channel);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        Channel channel = ctx.attr(CHANNEL_KEY).get();
        client.processor().onError(channel, cause);
        ctx.close();
    }
}