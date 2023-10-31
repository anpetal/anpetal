package org.noear.socketd.protocol.impl;

import org.noear.socketd.protocol.*;

import java.io.IOException;
import java.net.InetAddress;
import java.util.HashMap;
import java.util.Map;

/**
 * 通道默认实现
 *
 * @author noear
 * @since 2.0
 */
public class ChannelDefault<S> extends ChannelBase implements Channel {
    private S source;

    private ChannelAssistant<S> assistant;
    private Session session;
    private Map<String, Acceptor> acceptorMap;

    public ChannelDefault(S source,  ChannelAssistant<S> assistant) {
        super();
        this.source = source;
        this.assistant = assistant;
        this.acceptorMap = new HashMap<>();
    }

    @Override
    public boolean isValid() {
        return assistant.isValid(source);
    }

    @Override
    public InetAddress getRemoteAddress() throws IOException {
        return assistant.getRemoteAddress(source);
    }

    @Override
    public InetAddress getLocalAddress() throws IOException {
        return assistant.getLocalAddress(source);
    }

    /**
     * 发送
     */
    @Override
    public void send(Frame frame, Acceptor acceptor) throws IOException {
        if (acceptor != null) {
            acceptorMap.put(frame.getPayload().getKey(), acceptor);
        }

        assistant.write(source, frame);
    }

    @Override
    public void retrieve(Frame frame) throws IOException {
        Acceptor acceptor = acceptorMap.get(frame.getPayload().getKey());

        if (acceptor != null) {
            if (acceptor.isSingle()) {
                acceptorMap.remove(frame.getPayload().getKey());
            }
            acceptor.accept(frame.getPayload());
        }
    }

    /**
     * 获取会话
     */
    @Override
    public Session getSession() {
        if (session == null) {
            session = new SessionDefault(this);
        }

        return session;
    }

    /**
     * 关闭
     */
    @Override
    public void close() throws IOException {
        assistant.close(source);
        acceptorMap.clear();
    }
}