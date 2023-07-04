package Handler;

import DataAccess.DataAccessException;
import Help.writeString;
import Result.PersonResult;
import Service.PersonService;
import com.google.gson.Gson;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;

/*
	The personHandler is the HTTP handler that processes
	incoming HTTP requests that contain the "/person" URL path.

	Notice that personHandler implements the HttpHandler interface,
	which is defined by Java.  This interface contains only one method
	named "handle".  When the HttpServer object (declared in the Server class)
	receives a request containing the "/person" URL path, it calls
	personHandler.handle() which actually processes the request.
*/
public class personHandler implements HttpHandler {
    // Handles HTTP requests containing the "/person" URL path.
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
        // returns ALL family members of the current user.
        // The current user is determined by the provided authtoken.

        try {
            // Determine the HTTP request type (GET, POST, etc.).
            // Only allow GET requests for this operation.
            // This operation requires a GET request, because the
            // client is "getting" information from the server, and
            // the operation is "read only" (i.e., does not modify the
            // state of the server).
            if (exchange.getRequestMethod().toLowerCase().equals("get")) {

                // Get the HTTP request headers
                Headers reqHeaders = exchange.getRequestHeaders();

                // Check to see if an "Authorization" header is present
                if (reqHeaders.containsKey("Authorization")) {

                    // Extract the auth token from the "Authorization" header
                    String authToken = reqHeaders.getFirst("Authorization");

                    //call the person service
                    PersonService service = new PersonService();
                    PersonResult result = service.person(authToken);

                    // Start sending the HTTP response to the client, starting with
                    // the status code and any defined headers.
                    if (result.isSuccess()){
                        exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
                    }
                    else {
                        exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);
                    }

                    // Now that the status code and headers have been sent to the client,
                    // next we send the JSON data in the HTTP response body.

                    // Get the response body output stream.
                    OutputStream respBody = exchange.getResponseBody();

                    //change result string types
                    // Write the JSON string to the output stream.
                    Gson gson = new Gson();
                    String json = gson.toJson(result);
                    writeString write =  new writeString();
                    write.writeString(json, respBody);

                    // Close the output stream.  This is how Java knows we are done
                    // sending data and the response is complete.
                    respBody.close();
                }
            }
        }
        catch (IOException| DataAccessException e) {
            // Some kind of internal error has occurred inside the server (not the
            // client's fault), so we return an "internal server error" status code
            // to the client.
            exchange.sendResponseHeaders(HttpURLConnection.HTTP_SERVER_ERROR, 0);
            // Since the server is unable to complete the request, the client will
            // not receive the list of games, so we close the response body output stream,
            // indicating that the response is complete.
            exchange.getResponseBody().close();

            // Display/log the stack trace
            e.printStackTrace();
        }
    }
}