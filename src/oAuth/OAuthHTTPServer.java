package oAuth;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

public class OAuthHTTPServer{
    private String code;
    private HttpServer server = null;
    private BlockingQueue<String> queue;
    public OAuthHTTPServer(int port) {
            try {
                queue = new LinkedBlockingQueue<>();
                server = HttpServer.create(new InetSocketAddress(port), 0);
            } catch (IOException e) {
                e.printStackTrace();
            }
            server.createContext("/hello", new MyHandler());
            server.setExecutor(null); // creates a default executor
            server.start();
    }
    class MyHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange t) throws IOException {
            code = (t.getRequestURI()).toString();
           // System.out.println(code);
            code = code.substring(code.indexOf("code=") + 5);
            String response = "You can open the SpotifyParty app now";
            t.sendResponseHeaders(200, response.length());
            OutputStream os = t.getResponseBody();
            os.write(response.getBytes());
            os.close();
            t.close();
            queue.offer(code);
            server.stop(1);
        }
    }
    public String getCode() throws Exception {
        return queue.poll(60000, TimeUnit.MILLISECONDS);
    }
}

