package org.noear.socketd.transport.core;

import java.io.IOException;
import java.net.InetSocketAddress;

/**
 * 通道助理
 *
 * @author noear
 * @since 2.0
 */
public interface ChannelAssistant<T> {
    /**
     * 写入
     *
     * @param target 目标
     * @param frame  帧
     */
    void write(T target, Frame frame) throws IOException;

    /**
     * 是否有效
     */
    boolean isValid(T target);

    /**
     * 关闭
     */
    void close(T target) throws IOException;

    /**
     * 获取远程地址
     */
    InetSocketAddress getRemoteAddress(T target) throws IOException;

    /**
     * 获取本地地址
     */
    InetSocketAddress getLocalAddress(T target) throws IOException;
}