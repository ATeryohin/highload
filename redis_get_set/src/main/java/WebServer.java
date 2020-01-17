import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;

import redis.clients.jedis.Jedis;

@SuppressWarnings("WeakerAccess")
public class WebServer {

    int PORT = 8000;
    String REDIS_HOST = "localhost";
    int REDIS_PORT = 6379;
    Jedis redis_connection;

    public WebServer(){

    }

    public void start() throws IOException {
//       Создаем подключение к редису
        this.redis_connection = new Jedis(this.REDIS_HOST, this.REDIS_PORT);
        this.redis_connection.connect();

//        Создаем переменные в редисе, чтобы потом можно было юзать их
        this.redis_connection.set("firstTypeRequestsCount", "0");
        this.redis_connection.set("secondTypeRequestsCount", "0");

//        Выводим адреса в sout чтобы по ним можно было тыкать не вводя в браузере
        System.out.println(String.format("http://localhost:%s/firstTypeRequest/", PORT));
        System.out.println(String.format("http://localhost:%s/secondTypeRequest/", PORT));
        System.out.println(String.format("http://localhost:%s/firstTypeRequestsCount/", PORT));
        System.out.println(String.format("http://localhost:%s/secondTypeRequestsCount/", PORT));

//        создаем сервис который будет бегать в редис
        HttpServer server = HttpServer.create(new InetSocketAddress(PORT), 10);
        server.createContext("/firstTypeRequest", this::handleFirstTypeRequest);
        server.createContext("/secondTypeRequest", this::handleSecondTypeRequest);
        server.createContext("/firstTypeRequestsCount", this::handleFirstTypeRequestsCount);
        server.createContext("/secondTypeRequestsCount", this::handleSecondTypeRequestsCount);
        server.setExecutor(null);
        server.start();
    }

//    Метод для вывода первой переменной в броузер
    private void handleFirstTypeRequestsCount(HttpExchange httpExchange) throws IOException {
        if (httpExchange.getRequestMethod().equals("GET")) {
            String response = this.redis_connection.get("firstTypeRequestsCount");
            httpExchange.sendResponseHeaders(200, response.length());
            OutputStream os = httpExchange.getResponseBody();
            os.write(response.getBytes());
            os.close();
        }
    }

//    Метод для вывода первой переменной в броузер
    private void handleSecondTypeRequestsCount(HttpExchange httpExchange) throws IOException {
        if (httpExchange.getRequestMethod().equals("GET")) {
            String response = this.redis_connection.get("secondTypeRequestsCount");
            httpExchange.sendResponseHeaders(200, response.length());
            OutputStream os = httpExchange.getResponseBody();
            os.write(response.getBytes());
            os.close();
        }
    }

//    метод для инкрементирования первой переменной
    private void handleSecondTypeRequest(HttpExchange httpExchange) throws IOException {
        if (httpExchange.getRequestMethod().equals("GET")) {
            this.redis_connection.incr("secondTypeRequestsCount");

            String response = this.redis_connection.get("secondTypeRequestsCount");
            httpExchange.sendResponseHeaders(200, response.length());
            OutputStream os = httpExchange.getResponseBody();
            os.write(response.getBytes());
            os.close();
        }
    }

//    метод для инкрементирования второй переменной
    private void handleFirstTypeRequest(HttpExchange httpExchange) throws IOException {
        if (httpExchange.getRequestMethod().equals("GET")) {
            this.redis_connection.incr("firstTypeRequestsCount");

            String response = this.redis_connection.get("firstTypeRequestsCount");
            httpExchange.sendResponseHeaders(200, response.length());
            OutputStream os = httpExchange.getResponseBody();
            os.write(response.getBytes());
            os.close();
        }
    }
}