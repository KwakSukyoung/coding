package Handler;

import java.io.*;
import java.net.*;

import DataAccess.DataAccessException;
import Result.ClearResult;
import Service.ClearService;
import com.google.gson.Gson;
import com.sun.net.httpserver.*;
import Help.writeString;

/*
	The clearHandler is the HTTP handler that processes
	incoming HTTP requests that contain the "/clear" URL path.

	Notice that clearHandler implements the HttpHandler interface,
	which is defined by Java. This interface contains only one method
	named "handle".  When the HttpServer object (declared in the Server class)
	receives a request containing the "/clear" URL path, it calls
	clearHandler.handle() which actually processes the request.
*/
public class clearHandler implements HttpHandler {

    // Handles HTTP requests containing the "/clear" URL path.
    // The "exchange" parameter is an HttpExchange object, which is
    // defined by Java.
    // In this context, an "exchange" is an HTTP request/response pair
    // (i.e., the client and server exchange a request and response).
    // The HttpExchange object gives the handler access to all the
    // details of the HTTP request (Request type [GET or POST],
    // request headers, request body, etc.).
    // The HttpExchange object also gives the handler the ability
    // to construct an HTTP response and send it back to the client
    // (Status code, headers, response body, etc.).

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        // This handler
        // deletes ALL data from the database,including
        // user, authtoken, person, and event data.

        try {
            // Determine the HTTP request type (GET, POST, etc.).
            // Only allow POST requests for this operation.
            // This operation requires a POST request, because the
            // client is "posting" information to the server for processing.
            if (exchange.getRequestMethod().toLowerCase().equals("post")) {

                //call the clear service
                ClearService service = new ClearService();
                ClearResult result = service.clear();

                // Start sending the HTTP response to the client, starting with
                // the status code and any defined headers.
                exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);

                //get the box for the result
                OutputStream resBody = exchange.getResponseBody();
                //change result string types
                Gson gson = new Gson();
                String json = gson.toJson(result);
                writeString write =  new writeString();
                write.writeString(json, resBody);

                resBody.close();
            }

        }
        catch (IOException | DataAccessException e ) {
            // Some kind of internal error has occurred inside the server (not the
            // client's fault), so we return an "internal server error" status code
            // to the client.
            exchange.sendResponseHeaders(HttpURLConnection.HTTP_SERVER_ERROR, 0);

            // We are not sending a response body, so close the response body
            // output stream, indicating that the response is complete.
            exchange.getResponseBody().close();

            // Display/log the stack trace
            e.printStackTrace();
        }
    }
}





