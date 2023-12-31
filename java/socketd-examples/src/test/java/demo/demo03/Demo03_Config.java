package demo.demo03;


import org.noear.socketd.SocketD;
import org.noear.socketd.transport.core.Session;
import org.noear.socketd.transport.server.ServerConfig;

public class Demo03_Config {
    public static void main(String[] args) throws Throwable {
        //::启动服务端
        SocketD.createServer(new ServerConfig("sd:ws").port(8602))
                .config(sc->sc.maxThreads(128).sslContext(null))
                .start();

        Thread.sleep(1000); //等会儿，确保服务端启动完成

        //::打开客户端会话
        Session session = SocketD.createClient("sd:ws://127.0.0.1:8602/?u=a&p=2")
                .config(cc->cc.sslContext(null))
                .open();
    }
}
