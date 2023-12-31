package org.noear.socketd.transport.java_udp;

import org.noear.socketd.transport.client.ClientBase;
import org.noear.socketd.transport.client.ClientChannel;
import org.noear.socketd.transport.client.ClientConfig;
import org.noear.socketd.transport.client.ClientConnector;
import org.noear.socketd.transport.core.Channel;
import org.noear.socketd.transport.core.Session;
import org.noear.socketd.transport.core.impl.SessionDefault;

/**
 * Udp 客户端实现
 *
 * @author Urara
 * @since 2.0
 */
public class UdpBioClient extends ClientBase<UdpBioChannelAssistant> {
    public UdpBioClient(ClientConfig config) {
        super(config, new UdpBioChannelAssistant(config));
    }

    @Override
    public Session open() throws Exception {
        ClientConnector connector = new UdpBioClientConnector(this);
        Channel channel = new ClientChannel(connector.connect(), connector);
        return new SessionDefault(channel);
    }
}