package com.dbapp.service.sender.udp;

import com.dbapp.bean.EffectiveConfig;
import com.dbapp.bean.SenderConfig;
import com.dbapp.service.sender.AbstractSender;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioDatagramChannel;

import java.util.List;

public class UdpSender extends AbstractSender {

    private Channel channel;
    private NioEventLoopGroup eventLoopGroup;
    private static byte[] line;
    private Bootstrap bootstrap;

    @Override
    public String getName() {
        return "UdpSender";
    }

    @Override
    public void init(EffectiveConfig config) throws Exception {
        this.eventLoopGroup = new NioEventLoopGroup();
        this.bootstrap = new Bootstrap();
        this.bootstrap.group(this.eventLoopGroup)
                .remoteAddress(config.getIp(), config.getPort())
                .channel(NioDatagramChannel.class)
                .handler(new ChannelInitializer<NioDatagramChannel>() {
                    @Override
                    protected void initChannel(NioDatagramChannel ch) throws Exception {
                        ch.pipeline().fireChannelActive();
                    }
                });
        this.channel = this.bootstrap.connect().sync().channel();
        line = System.lineSeparator().getBytes(SenderConfig.getLogEncoding());
    }

    @Override
    public void send(List<byte[]> msg) {
        for (byte[] b : msg) {
            byte[] d = new byte[b.length + line.length];
            System.arraycopy(b, 0, d, 0, b.length);
            System.arraycopy(line, 0, d, b.length, line.length);
            this.channel.writeAndFlush(Unpooled.copiedBuffer(d));
        }
    }

    @Override
    public void stop() {
        if (this.channel != null) {
            this.channel.close();
            this.channel = null;
        }
        if (this.eventLoopGroup != null) {
            this.eventLoopGroup.shutdownGracefully();
            this.eventLoopGroup = null;
        }
        if (this.bootstrap != null) {
            bootstrap = null;
        }
    }
}
