package demo.demo04;

import org.noear.socketd.SocketD;
import org.noear.socketd.transport.core.Entity;
import org.noear.socketd.transport.core.Message;
import org.noear.socketd.transport.core.Session;
import org.noear.socketd.transport.core.entity.StringEntity;
import org.noear.socketd.transport.core.listener.BuilderListener;
import org.noear.socketd.transport.core.listener.SimpleListener;
import org.noear.socketd.transport.server.ServerConfig;

import java.io.IOException;

public class Demo04_BuilderListener {
    public static void main(String[] args) throws Throwable {
        //::启动服务端
        SocketD.createServer(new ServerConfig("sd:tcp").port(8602))
                .listen(new BuilderListener().onMessage((session, message) -> {
                    System.out.println("server::" + message);
                    session.send("/demo", new StringEntity("Me too!"));
                    session.send("/demo2", new StringEntity("Me too!"));
                }))
                .start();

        Thread.sleep(1000); //等会儿，确保服务端启动完成

        //::打开客户端会话
        Session session = SocketD.createClient("sd:tcp://127.0.0.1:8602/?u=a&p=2")
                .listen(new BuilderListener().onMessage((s, m) -> {
                    System.out.println("client::" + m);
                }).on("/demo", (s, m) -> { //带了主题路由的功能
                    System.out.println("on::" + m.getTopic() + "::" + m);
                }).on("/demo2", (s,m)->{

                }))
                .open();

        session.send("/order", new StringEntity("Hi"));
        session.send("/user", new StringEntity("Hi"));
    }
}
