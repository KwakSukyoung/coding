package com.example.familymapclient;

import com.google.gson.Gson;

import java.io.*;
import java.net.*;

import Request.LoginRequest;
import Request.RegisterRequest;
import Result.EventResult;
import Result.LoginResult;
import Result.PersonResult;
import Result.RegisterResult;

/*
	The ServerProxy class shows how to call a web API operation from
	a Java program.
*/

public class ServerProxy {//Server Facade
    // The ServerProxy's "main" method.
    // The "args" parameter should contain two command-line arguments:
    // 1. The IP address or domain name of the machine running the server
    // 2. The port number on which the server is accepting client connections

    public ServerProxy() {}

    //Login
    public LoginResult loginResult(LoginRequest request, String serverHost, String serverPort) {

        // This method shows how to send a POST request to a server

        try {
            // Create a URL indicating where the server is running, and which
            // web API operation we want to call
            URL url = new URL("http://" + serverHost + ":" + serverPort + "/user/login");

            // Start constructing our HTTP request
            HttpURLConnection http = (HttpURLConnection)url.openConnection();

            // Specify that we are sending an HTTP POST request
            http.setRequestMethod("POST");

            // Indicate that this request will contain an HTTP request body
            http.setDoOutput(true);

            // Specify that we would like to receive the server's response in JSON
            // format by putting an HTTP "Accept" header on the request (this is not
            // necessary because our server only returns JSON responses, but it
            // provides one more example of how to add a header to an HTTP request).
            http.addRequestProperty("Accept", "application/json");

            // Connect to the server and send the HTTP request
            http.connect();

            //change the request to Json
            Gson gson = new Gson();
            String reqData = gson.toJson(request);

            // Get the output stream containing the HTTP request body
            OutputStream reqBody = http.getOutputStream();

            // Write the JSON data to the request body
            writeString(reqData, reqBody);

            // Close the request body output stream, indicating that the
            // request is complete
            reqBody.close();

            //now ready to receive the result
            InputStream respBody;

            // By the time we get here, the HTTP response has been received from the server.
            // Check to make sure that the HTTP response from the server contains a 200
            // status code, which means "success".  Treat anything else as a failure.
            if (http.getResponseCode() == HttpURLConnection.HTTP_OK) {
                // Get the input stream containing the HTTP response body
                respBody = http.getInputStream();
            }
            else {
                // The HTTP response status code indicates an error
                // occurred, so print out the message from the HTTP response
                System.out.println("ERROR: " + http.getResponseMessage());
                // Get the error stream containing the HTTP response body (if any)
                respBody = http.getErrorStream();
            }

            // Extract data from the HTTP response body
            String respData = readString(respBody);
            LoginResult result = gson.fromJson(respData, LoginResult.class);

            // Close the request body output stream, indicating that the
            // request is complete
            respBody.close();
            return result;

        }
        catch (IOException e) {
            // An exception was thrown, so display the exception's stack trace
            e.printStackTrace();
            return new LoginResult("didn't work.....!",false);
        }

    }

    //Register
    public RegisterResult registerResult(RegisterRequest request, String serverHost, String serverPort){

        // This method shows how to send a POST request to a server

        try {
            // Create a URL indicating where the server is running, and which
            // web API operation we want to call
            URL url = new URL("http://" + serverHost + ":" + serverPort + "/user/register");

            // Start constructing our HTTP request
            HttpURLConnection http = (HttpURLConnection)url.openConnection();

            // Specify that we are sending an HTTP POST request
            http.setRequestMethod("POST");

            // Indicate that this request will contain an HTTP request body
            http.setDoOutput(true);

            // Specify that we would like to receive the server's response in JSON
            // format by putting an HTTP "Accept" header on the request (this is not
            // necessary because our server only returns JSON responses, but it
            // provides one more example of how to add a header to an HTTP request).
            http.addRequestProperty("Accept", "application/json");

            // Connect to the server and send the HTTP request
            http.connect();

            //change the request to Json
            Gson gson = new Gson();
            String reqData = gson.toJson(request);

            // Get the output stream containing the HTTP request body
            OutputStream reqBody = http.getOutputStream();

            // Write the JSON data to the request body
            writeString(reqData, reqBody);

            // Close the request body output stream, indicating that the
            // request is complete
            reqBody.close();

            //now ready to receive the result
            InputStream respBody;

            // By the time we get here, the HTTP response has been received from the server.
            // Check to make sure that the HTTP response from the server contains a 200
            // status code, which means "success".  Treat anything else as a failure.
            if (http.getResponseCode() == HttpURLConnection.HTTP_OK) {
                // Get the input stream containing the HTTP response body
                respBody = http.getInputStream();
            }
            else {
                // The HTTP response status code indicates an error
                // occurred, so print out the message from the HTTP response
                System.out.println("ERROR: " + http.getResponseMessage());
                // Get the error stream containing the HTTP response body (if any)
                respBody = http.getErrorStream();
            }

            // Extract data from the HTTP response body
            String respData = readString(respBody);
            RegisterResult result = gson.fromJson(respData, RegisterResult.class);

            // Close the request body output stream, indicating that the
            // request is complete
            respBody.close();
            return result;

        }
        catch (IOException e) {
            // An exception was thrown, so display the exception's stack trace
            e.printStackTrace();
            return new RegisterResult("didn't work.....!",false);
        }

    }

    //getPeople
    public PersonResult personResult(String serverHost, String serverPort, String authToken){
        // This method shows how to send a GET request to a server

        try {
            // Create a URL indicating where the server is running, and which
            // web API operation we want to call
            URL url = new URL("http://" + serverHost + ":" + serverPort + "/person");

            // Start constructing our HTTP request
            HttpURLConnection http = (HttpURLConnection)url.openConnection();

            // Specify that we are sending an HTTP POST request
            http.setRequestMethod("GET");

            // Indicate that this request will not contain an HTTP request body
            http.setDoOutput(false);

            // Add an auth token to the request in the HTTP "Authorization" header
            http.addRequestProperty("Authorization", authToken);

            // Specify that we would like to receive the server's response in JSON
            // format by putting an HTTP "Accept" header on the request (this is not
            // necessary because our server only returns JSON responses, but it
            // provides one more example of how to add a header to an HTTP request).
            http.addRequestProperty("Accept", "application/json");

            // Connect to the server and send the HTTP request
            http.connect();

            //now ready to receive the result
            InputStream respBody;

            // By the time we get here, the HTTP response has been received from the server.
            // Check to make sure that the HTTP response from the server contains a 200
            // status code, which means "success".  Treat anything else as a failure.
            if (http.getResponseCode() == HttpURLConnection.HTTP_OK) {
                // Get the input stream containing the HTTP response body
                respBody = http.getInputStream();
            }
            else {
                // The HTTP response status code indicates an error
                // occurred, so print out the message from the HTTP response
                System.out.println("ERROR: " + http.getResponseMessage());
                // Get the error stream containing the HTTP response body (if any)
                respBody = http.getErrorStream();
            }

            // Extract data from the HTTP response body
            String respData = readString(respBody);
            Gson gson = new Gson();
            PersonResult result = gson.fromJson(respData, PersonResult.class);

            // Close the request body output stream, indicating that the
            // request is complete
            respBody.close();
            return result;

        }
        catch (IOException e) {
            // An exception was thrown, so display the exception's stack trace
            e.printStackTrace();
            return new PersonResult("didn't work.....!",false);
        }
    }

    //getEvents
    public EventResult eventResult(String serverHost, String serverPort, String authToken){
        // This method shows how to send a GET request to a server

        try {
            // Create a URL indicating where the server is running, and which
            // web API operation we want to call
            URL url = new URL("http://" + serverHost + ":" + serverPort + "/event");

            // Start constructing our HTTP request
            HttpURLConnection http = (HttpURLConnection)url.openConnection();

            // Specify that we are sending an HTTP POST request
            http.setRequestMethod("GET");

            // Indicate that this request will not contain an HTTP request body
            http.setDoOutput(false);

            // Add an auth token to the request in the HTTP "Authorization" header
            http.addRequestProperty("Authorization", authToken);

            // Specify that we would like to receive the server's response in JSON
            // format by putting an HTTP "Accept" header on the request (this is not
            // necessary because our server only returns JSON responses, but it
            // provides one more example of how to add a header to an HTTP request).
            http.addRequestProperty("Accept", "application/json");

            // Connect to the server and send the HTTP request
            http.connect();

            //now ready to receive the result
            InputStream respBody;

            // By the time we get here, the HTTP response has been received from the server.
            // Check to make sure that the HTTP response from the server contains a 200
            // status code, which means "success".  Treat anything else as a failure.
            if (http.getResponseCode() == HttpURLConnection.HTTP_OK) {
                // Get the input stream containing the HTTP response body
                respBody = http.getInputStream();
            }
            else {
                // The HTTP response status code indicates an error
                // occurred, so print out the message from the HTTP response
                System.out.println("ERROR: " + http.getResponseMessage());
                // Get the error stream containing the HTTP response body (if any)
                respBody = http.getErrorStream();
            }

            // Extract data from the HTTP response body
            String respData = readString(respBody);
            Gson gson = new Gson();
            EventResult result = gson.fromJson(respData, EventResult.class);

            // Close the request body output stream, indicating that the
            // request is complete
            respBody.close();
            return result;

        }
        catch (IOException e) {
            // An exception was thrown, so display the exception's stack trace
            e.printStackTrace();
            return new EventResult("didn't work.....!",false);
        }
    }

    /*
    The readString method shows how to read a String from an InputStream.
    */
    private static String readString(InputStream is) throws IOException {
        StringBuilder sb = new StringBuilder();
        InputStreamReader sr = new InputStreamReader(is);
        char[] buf = new char[1024];
        int len;
        while ((len = sr.read(buf)) > 0) {
            sb.append(buf, 0, len);
        }
        return sb.toString();
    }
    /*
        The writeString method shows how to write a String to an OutputStream.
    */
    private static void writeString(String str, OutputStream os) throws IOException {
        OutputStreamWriter sw = new OutputStreamWriter(os);
        sw.write(str);
        sw.flush();
    }

}