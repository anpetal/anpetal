
关键交互接口

### 1、监听器（Listener）

```java
public interface Listener {
    //打开时（握手完成后）
    void onOpen(Session session);
    //收到消息时
    void onMessage(Session session, Message message) throws IOException;
    //关闭时
    void onClose(Session session);
    //出错时
    void onError(Session session, Throwable error);
}
```

### 2、会话接口（Session）

```java
public interface Session {
    //是否有效
    boolean isValid();
    //获取远程地址
    InetAddress getRemoteAddress() throws IOException;
    //获取本地地址
    InetAddress getLocalAddress() throws IOException;
    //获取握手信息
    Handshake getHandshake();
    //获取附件
    <T> T getAttachment(Class<T> type);
    //设置附件
    <T> void setAttachment(Class<T> type, T value);
   //获取会话Id
    String getSessionId();
    //发送 Ping
    void sendPing() throws IOException;
    //发送
    void send(String topic, Entity content) throws IOException;
    //发送并请求（限为一次答复）
    Entity sendAndRequest(String topic, Entity content) throws IOException;
    //发送并请求（限为一次答复；指定超时）
    Entity sendAndRequest(String topic, Entity content, long timeout) throws IOException;
    //发送并订阅（答复结束之前，不限答复次数）
    void sendAndSubscribe(String topic, Entity content, Consumer<Entity> consumer) throws IOException;
    //答复
    void reply(Message from, Entity content) throws IOException;
    //答复并结束（即最后一次答复）
    void replyEnd(Message from, Entity content) throws IOException;
}
```


### 3、消息

```java
public interface Message {
    //是否为请求
    boolean isRequest();
    //是否为订阅
    boolean isSubscribe();
    //获取消息流Id（用于消息交互、分片）
    String getSid();
    //获取消息主题
    String getTopic();
    //获取消息实体
    Entity getEntity();
}
```


### 4、消息实体


```java
public interface Entity {
    //获取元信息字符串（queryString style）
    String getMetaString();
    //获取元信息
    Map<String, String> getMetaMap();
    //获取元信息
    String getMeta(String name);
    //获取数据
    InputStream getData();
    //获取数据并转为字符串
    String getDataAsString();
}
```