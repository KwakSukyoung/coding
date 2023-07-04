import Handler.*;
import com.sun.net.httpserver.HttpServer;

import java.io.*;
import java.net.*;

/*
	The Server class is the "main" class for the server (i.e., it contains the
		"main" method for the server program).
	When the server runs, all command-line arguments are passed in to Server.main.
	For this server, the only command-line argument is the port number on which
		the server should accept incoming client connections.
*/

public class Server {
    private static final int MAX_WAITING_CONNECTIONS = 12;

    // Java provides an HttpServer class that can be used to embed
    // an HTTP server in any Java program.
    // Using the HttpServer class, you can easily make a Java
    // program that can receive incoming HTTP requests, and respond
    // with appropriate HTTP responses.
    // HttpServer is the class that actually implements the HTTP network
    // protocol (be glad you don't have to).
    // The "server" field contains the HttpServer instance for this program,
    // which is initialized in the "run" method below.
    private HttpServer server;

    // This method initializes and runs the server.
    // The "portNumber" parameter specifies the port number on which the
    // server should accept incoming client connections.
    private void run(String portNumber) {

        // Since the server has no "user interface", it should display "log"
        // messages containing information about its internal activities.
        // This allows a system administrator (or you) to know what is happening
        // inside the server, which can be useful for diagnosing problems
        // that may occur.
        System.out.println("Initializing HTTP Server");

        try {
            // Create a new HttpServer object.
            // Rather than calling "new" directly, we instead create
            // the object by calling the HttpServer.create static factory method.
            // Just like "new", this method returns a reference to the new object.
            server = HttpServer.create(
                    new InetSocketAddress(Integer.parseInt(portNumber)),
                    MAX_WAITING_CONNECTIONS);
        }
        catch (IOException e) {
            e.printStackTrace();
            return;
        }

        // Indicate that we are using the default "executor".
        server.setExecutor(null);

        // Log message indicating that the server is creating and installing
        // its HTTP handlers.
        // The HttpServer class listens for incoming HTTP requests.  When one
        // is received, it looks at the URL path inside the HTTP request, and
        // forwards the request to the handler for that URL path.
        System.out.println("Creating contexts");
        /**
         * Starts of URLs
         */

        // Create and install the HTTP handler for the "/user/register" URL path.
        // When the HttpServer receives an HTTP request containing the
        // "/user/register" URL path, it will forward the request to registerHandler.
        server.createContext("/user/register", new registerHandler());

        // Create and install the HTTP handler for the "/user/login" URL path.
        // When the HttpServer receives an HTTP request containing the
        // "/user/login" URL path, it will forward the request to loginHandler.
        server.createContext("/user/login", new loginHandler());

        // Create and install the HTTP handler for the "/clear" URL path.
        // When the HttpServer receives an HTTP request containing the
        // "/clear" URL path, it will forward the request to clearHandler.
        server.createContext("/clear", new clearHandler());

        // Create and install the HTTP handler for the "/fill/[username]/{generations}" URL path.
        // When the HttpServer receives an HTTP request containing the
        // "/fill/[username]/{generations}" URL path, it will forward the request to fillHandler.
        server.createContext("/fill/", new fillHandler());

        // Create and install the HTTP handler for the "/load" URL path.
        // When the HttpServer receives an HTTP request containing the
        // "/load" URL path, it will forward the request to loadHandler.
        server.createContext("/load", new loadHandler());

        // Create and install the HTTP handler for the "/person/[personID]" URL path.
        // When the HttpServer receives an HTTP request containing the
        // "/person/[personID]" URL path, it will forward the request to personIDHandler.
        server.createContext("/person/", new personIDHandler());

        // Create and install the HTTP handler for the "/person" URL path.
        // When the HttpServer receives an HTTP request containing the
        // "/person" URL path, it will forward the request to personHandler.
        server.createContext("/person", new personHandler());

        // Create and install the HTTP handler for the "/event/[eventID]" URL path.
        // When the HttpServer receives an HTTP request containing the
        // "/event/[eventID]" URL path, it will forward the request to eventIDHandler.
        server.createContext("/event/", new eventIDHandler());

        // Create and install the HTTP handler for the "/event" URL path.
        // When the HttpServer receives an HTTP request containing the
        // "/event" URL path, it will forward the request to eventHandler.
        server.createContext("/event", new eventHandler());

        // Create and install the "default" (or "file") HTTP handler.
        // All requests that do not match the other handler URLs
        // will be passed to this handle.
        // These are requests to download a file from the server
        // (e.g., web site files)
        server.createContext("/", new fileHandler());

        /**
         * End of URLs
         */

        // Log message indicating that the HttpServer is about the start accepting
        // incoming client connections.
        System.out.println("Starting server");

        // Tells the HttpServer to start accepting incoming client connections.
        // This method call will return immediately, and the "main" method
        // for the program will also complete.
        // Even though the "main" method has completed, the program will continue
        // running because the HttpServer object we created is still running
        // in the background.
        server.start();

        // Log message indicating that the server has successfully started.
        System.out.println("Server started");
    }

    // "main" method for the server program
    // "args" should contain one command-line argument, which is the port number
    // on which the server should accept incoming client connections.

    public static void main(String[] args) {
        String portNumber = args[0];
        new Server().run(portNumber);
    }
}






