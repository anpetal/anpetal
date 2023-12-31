package org.noear.socketd.transport.core;

import javax.net.ssl.SSLContext;
import java.nio.charset.Charset;
import java.util.concurrent.ExecutorService;

/**
 * 配置类
 *
 * @author noear
 * @since 2.0
 */
public interface Config {
    /**
     * 流ID大小限制
     */
    int MAX_SIZE_SID = 64;
    /**
     * 主题大小限制
     */
    int MAX_SIZE_TOPIC = 512;
    /**
     * 元信息串大小限制
     */
    int MAX_SIZE_META_STRING = 4096;
    /**
     * 分片大小限制
     */
    int MAX_SIZE_FRAGMENT = 1024 * 1024 * 16; //16m


    /**
     * 是否客户端模式
     */
    boolean clientMode();

    /**
     * 获取协议架构
     */
    String getSchema();

    /**
     * 获取字符集
     */
    Charset getCharset();

    /**
     * 获取编解码器
     */
    Codec<BufferReader, BufferWriter> getCodec();

    /**
     * 获取Id生成器
     */
    IdGenerator getIdGenerator();

    /**
     * 获取分片处理器
     */
    FragmentHandler getFragmentHandler();

    /**
     * 获取 ssl 上下文
     */
    SSLContext getSslContext();

    /**
     * 执行器（第一优先，有些底层不支持）
     * */
    ExecutorService getExecutor();

    /**
     * 核心线程数（第二优先）
     */
    int getCoreThreads();

    /**
     * 最大线程数
     */
    int getMaxThreads();

    /**
     * 获取读缓冲大小
     */
    int getReadBufferSize();
    /**
     * 配置读缓冲大小
     */
    int getWriteBufferSize();

    /**
     * 获取连接空闲超时
     * */
    long getIdleTimeout();

    /**
     * 答复超时（单位：毫秒）
     */
    long getReplyTimeout();

    /**
     * 允许最大请求数
     */
    int getMaxRequests();

    /**
     * 允许最大UDP包大小
     */
    int getMaxUdpSize();
}
