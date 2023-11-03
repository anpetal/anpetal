package org.noear.socketd;

import org.noear.socketd.client.ClientFactory;
import org.noear.socketd.server.ServerFactory;
import org.noear.socketd.client.Client;
import org.noear.socketd.client.ClientConfig;
import org.noear.socketd.server.Server;
import org.noear.socketd.server.ServerConfig;

import java.util.HashMap;
import java.util.Map;
import java.util.ServiceLoader;

/**
 * @author noear
 * @since 2.0
 */
public class SocketD {
    static Map<String, ClientFactory> clientFactoryMap;
    static Map<String, ServerFactory> serverFactoryMap;

    static {
        clientFactoryMap = new HashMap<>();
        serverFactoryMap = new HashMap<>();

        ServiceLoader.load(ClientFactory.class).iterator().forEachRemaining(factory -> {
            for (String s : factory.schema()) {
                clientFactoryMap.put(s, factory);
            }
        });

        ServiceLoader.load(ServerFactory.class).iterator().forEachRemaining(factory -> {
            for (String s : factory.schema()) {
                serverFactoryMap.put(s, factory);
            }
        });
    }

    /**
     * 创建服务端
     */
    public static Server createServer(ServerConfig serverConfig) {
        ServerFactory factory = serverFactoryMap.get(serverConfig.getSchema());
        if (factory == null) {
            throw new IllegalStateException("No ServerBroker providers were found.");
        }

        return factory.createServer(serverConfig);
    }

    /**
     * 创建客户端（支持 url 自动识别）
     *
     * @param serverUrl 服务器地址
     */
    public static Client createClient(String serverUrl) {
        ClientConfig clientConfig = new ClientConfig(serverUrl);

        ClientFactory factory = clientFactoryMap.get(clientConfig.getSchema());
        if (factory == null) {
            throw new IllegalStateException("No ClientBroker providers were found.");
        }

        return factory.createClient(clientConfig);
    }
}