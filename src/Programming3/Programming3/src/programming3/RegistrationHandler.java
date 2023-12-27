package programming3;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

import org.json.JSONException;
import org.json.JSONObject;

import com.sun.net.httpserver.*;

/**
 * @author Zhang Runyan
 */

public class RegistrationHandler implements HttpHandler {

    private CoordDatabase db;
    private MyExceptionHandler exhand;
    private Object requestParamValue;

    public RegistrationHandler(CoordDatabase db, MyExceptionHandler exhand) {
        this.db = db;
        this.exhand = exhand;
    }


    @Override
    public void handle(HttpExchange exchange) throws IOException {

        if ("POST".equals(exchange.getRequestMethod())) {
            System.out.println("post of registration.");

            //remember to check if the user provided string is actually a JSON object

            String jsonString = handleResponsePOST(exchange);
            String username = "";
            String password = "";
            String email = "";
            try {
                JSONObject obj = new JSONObject(jsonString);
                username = obj.getString("username");
                password = obj.getString("password");
                email = obj.getString("email");
            } catch (JSONException e) {
                failExceptionRet(exchange, e);
                return;
            }
            
            //Save user registration information to the database

            if (!Utils.strIsEmpty(db.getPassword(username))) {
                String rspMsg = "user is exists";

                exchange.sendResponseHeaders(200, rspMsg.getBytes().length);
                OutputStream outputStream = exchange.getResponseBody();
                outputStream.write(rspMsg.getBytes());
                outputStream.flush();
                outputStream.close();
                return;
            }

            db.saveRegistration(
                    username,
                    password,
                    email
            );
            
            exchange.sendResponseHeaders(200, 2);
            OutputStream outputStream = exchange.getResponseBody();
            outputStream.write("OK".getBytes());
            outputStream.flush();
            outputStream.close();
        } else {
            handleResponse(exchange, "Not supported");
        }

    }


    private String handleResponsePOST(HttpExchange httpExchange) throws IOException {
        InputStream stream = httpExchange.getRequestBody();
        String text = new BufferedReader(new InputStreamReader(stream, StandardCharsets.UTF_8)).lines().collect(Collectors.joining("\n"));
        return text;
    }


    private void handleResponse(HttpExchange httpExchange, String requestParamValue) throws IOException {
        OutputStream outputStream = httpExchange.getResponseBody();
        httpExchange.sendResponseHeaders(200, -1);
        outputStream.flush();
        outputStream.close();
    }

    private void failExceptionRet(HttpExchange exchange, JSONException e) throws IOException {
        String msg = e.getMessage();
        exchange.sendResponseHeaders(200, msg.getBytes().length);
        OutputStream outputStream = exchange.getResponseBody();
        outputStream.write(msg.getBytes());
        outputStream.flush();
        outputStream.close();
    }

}
