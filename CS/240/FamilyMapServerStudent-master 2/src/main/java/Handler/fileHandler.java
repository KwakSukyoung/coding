package Handler;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.nio.file.Files;
import java.util.Objects;

public class fileHandler implements HttpHandler{
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        /*
         * Register "/" with your file handler
         * I did that in server.java
         */

        /*
         * Ignore everything but GET requests
         *  - could send a 405 (Method Not Allowed)
         */
        if(!Objects.equals(exchange.getRequestMethod(), "GET")){
            exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_METHOD,0);
            exchange.close();
        }
        /*
         * Get the request URI from the exchange
         */
        String urlPath = exchange.getRequestURI().toString();
        if((urlPath==null)||(urlPath.equals("/"))){
            urlPath = "/index.html";
        }

        /*
         * Append urlPath to a relative path (no leading slash) to the
         * directory containing the files
         * Create a file object and check if the file exists
         */
        String filePath = "web"+urlPath;
        File file = new File(filePath);
        if(file.exists()){
            exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK,0);
            OutputStream respBody = exchange.getResponseBody();
            Files.copy(file.toPath(), respBody);
        }
        else{
            exchange.sendResponseHeaders(HttpURLConnection.HTTP_NOT_FOUND,0);
            OutputStream respBody = exchange.getResponseBody();
            Files.copy(new File("web/HTML/404.html").toPath(), respBody);
        }

        exchange.close();

    }

}
