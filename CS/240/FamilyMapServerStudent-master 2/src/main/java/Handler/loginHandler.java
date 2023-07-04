package Handler;

import DataAccess.DataAccessException;
import Help.readString;
import Help.writeString;
import Request.LoginRequest;
import Result.LoginResult;
import Service.LoginService;
import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.*;
import java.net.HttpURLConnection;

/*
	The loginHandler is the HTTP handler that processes
	incoming HTTP requests that contain the "/user/login" URL path.

	Notice that loadHandler implements the HttpHandler interface,
	which is defined by Java. This interface contains only one method
	named "handle".  When the HttpServer object (declared in the Server class)
	receives a request containing the "/user/login" URL path, it calls
	loginHandler.handle() which actually processes the request.
*/

public class loginHandler implements HttpHandler {
    // Handles HTTP requests containing the "/user/login" URL path.
    // The "exchange" parameter is an HttpExchange object, which is
    // defined by Java.
    // In this context, an "exchange" is an HTTP request/response pair
    // (i.e., the client and server exchange a request and response).
    // The HttpExchange object gives the handler access to all of the
    // details of the HTTP request (Request type [GET or POST],
    // request headers, request body, etc.).
    // The HttpExchange object also gives the handler the ability
    // to construct an HTTP response and send it back to the client
    // (Status code, headers, response body, etc.).

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        // This handler
        // logs the user in
        // +
        // returns an authtoken

        try {
            // Determine the HTTP request type (GET, POST, etc.).
            // Only allow POST requests for this operation.
            // This operation requires a POST request, because the
            // client is "posting" information to the server for processing.
            if (exchange.getRequestMethod().toLowerCase().equals("post")) {

                //Extract the JSON string from the request body
                //Get the request body input stream
                InputStream reqBody = exchange.getRequestBody();
                readString read = new readString();
                String reqData = read.readString(reqBody);
                System.out.println(reqData);

                //get the loadRequest as an input
                Gson gson1 = new Gson();
                LoginRequest loginRequest = gson1.fromJson(reqData, LoginRequest.class);

                //call the load service
                LoginService service = new LoginService();
                LoginResult result = service.login(loginRequest);

                // Start sending the HTTP response to the client, starting with
                // the status code and any defined headers.
                if (result.isSuccess()){
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
                }else exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);

                //get the box for the result
                OutputStream resBody = exchange.getResponseBody();

                //change result string types
                Gson gson2 = new Gson();
                String json = gson2.toJson(result);
                writeString write =  new writeString();
                write.writeString(json, resBody);

                resBody.close();
                exchange.getResponseBody().close();
            }

        }
        catch (IOException | DataAccessException e) {
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







