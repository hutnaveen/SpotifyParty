package server;

import com.sun.net.httpserver.HttpServer;
import handler.MessageHandler;

import java.io.IOException;
import java.net.InetSocketAddress;

public class HTTPServer {
    public static void main(String[] args) {
        try {
            HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);
            server.createContext("/hello", new MessageHandler("hi", "hi"));
            server.setExecutor(null);
            server.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
