package org.noear.socketd.transport.core;

import java.io.Closeable;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.Map;
import java.util.function.Consumer;

/**
 * 会话
 *
 * @author noear
 * @since 2.0
 */
public interface Session extends Closeable {
    /**
     * 是否有效
     */
    boolean isValid();

    /**
     * 获取远程地址
     */
    InetSocketAddress getRemoteAddress() throws IOException;

    /**
     * 获取本地地址
     */
    InetSocketAddress getLocalAddress() throws IOException;

    /**
     * 获取握手信息
     */
    Handshake getHandshake();

    /**
     * 获取所有属性
     */
    Map<String, Object> getAttrMap();

    /**
     * 获取属性
     *
     * @param name 名字
     */
    <T> T getAttr(String name);

    /**
     * 获取属性或默认值
     *
     * @param name 名字
     * @param def  默认值
     */
    <T> T getAttrOrDefault(String name, T def);

    /**
     * 设置属性
     *
     * @param name  名字
     * @param value 值
     */
    <T> void setAttr(String name, T value);

    /**
     * 获取会话Id
     */
    String getSessionId();

    /**
     * 手动重连（一般是自动）
     */
    void reconnect() throws Exception;

    /**
     * 手动发送 Ping（一般是自动）
     */
    void sendPing() throws IOException;

    /**
     * 发送
     *
     * @param topic   主题
     * @param content 内容
     */
    void send(String topic, Entity content) throws IOException;

    /**
     * 发送并请求
     *
     * @param topic   主题
     * @param content 内容
     */
    Entity sendAndRequest(String topic, Entity content) throws IOException;

    /**
     * 发送并请求（限为一次答复；指定超时）
     *
     * @param topic   主题
     * @param content 内容
     * @param timeout 超时（毫秒）
     */
    Entity sendAndRequest(String topic, Entity content, long timeout) throws IOException;

    /**
     * 发送并订阅（答复结束之前，不限答复次数）
     *
     * @param topic    主题
     * @param content  内容
     * @param consumer 回调消费者
     */
    void sendAndSubscribe(String topic, Entity content, Consumer<Entity> consumer) throws IOException;

    /**
     * 答复
     *
     * @param from    来源消息
     * @param content 内容
     */
    void reply(Message from, Entity content) throws IOException;

    /**
     * 答复并结束（即最后一次答复）
     *
     * @param from    来源消息
     * @param content 内容
     */
    void replyEnd(Message from, Entity content) throws IOException;
}