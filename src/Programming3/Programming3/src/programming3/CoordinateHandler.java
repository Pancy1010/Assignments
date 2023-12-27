package programming3;

import com.sun.net.httpserver.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

import java.time.LocalDateTime;

import java.util.stream.Collectors;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author Zhang Runyan
 */

public class CoordinateHandler implements HttpHandler {

    private CoordDatabase db;
    private MyExceptionHandler exhand;
    private Coordinate coordinate;

    public CoordinateHandler(CoordDatabase db, MyExceptionHandler exhand) {
        this.db = db;
        this.exhand = exhand;
    }

    //Process query request
    void processQuery(HttpExchange t, JSONObject obj) throws IOException {
        String query = obj.getString("query");
        if ("user".equals(query)) {  
            
            //Query records by user name

            String nickname = "";
            try {
                nickname = obj.getString("nickname");
            } catch (JSONException e) {
                failExceptionRet(t, e);
                return;
            }
            JSONArray messages = db.getMessages(nickname);
            byte[] ret = messages.toString().getBytes(StandardCharsets.UTF_8);
            t.sendResponseHeaders(200, ret.length);
            OutputStream outputStream = t.getResponseBody();
            outputStream.write(ret);
            outputStream.flush();
            outputStream.close();
        } else if ("time".equals(query)) {  //Query records according to time
            String timestart = "";
            String timeend = "";
            try {
                timestart = obj.getString("timestart");
                timeend = obj.getString("timeend");
            } catch (JSONException e) {
                failExceptionRet(t, e);
                return;
            }
            String username = Utils.getUserNameByAuth(t);

            if (Utils.strIsEmpty(username)) {
                Utils.failRet(t, "auth err");
                return;
            }
            JSONArray messages = db.getMessages(timestart, timeend, username);
            byte[] ret;

            if (messages.length() == 0) {
                ret = "no data".getBytes(StandardCharsets.UTF_8);
            } else {
                ret = messages.toString().getBytes(StandardCharsets.UTF_8);
            }

            t.sendResponseHeaders(200, ret.length);
            OutputStream outputStream = t.getResponseBody();
            outputStream.write(ret);
            outputStream.flush();
            outputStream.close();
        }

    }


    @Override
    public void handle(HttpExchange exchange) throws IOException {

        if ("GET".equals(exchange.getRequestMethod())) {
            handleResponseGET(exchange, null);
        } else if ("POST".equals(exchange.getRequestMethod())) {
            //remember to check if the user provides string a JSON object
            String jsonString = handleResponsePOST(exchange);

            //Parse the body of the http post into a JSON object
            JSONObject param;
            try {
                param = new JSONObject(jsonString);
            } catch (JSONException e) {
                failExceptionRet(exchange, e);
                return;
            }

            String query = "";
            try {
                query = param.getString("query");
            } catch (JSONException e) {
                System.out.println("not query");
            }
            if (!Utils.strIsEmpty(query)) {
                processQuery(exchange, param);
            } else {
                Coordinate coordinate = null;
                String s_sent = "";
                try {
                    coordinate = new Coordinate(s_sent,
                            param.getBigDecimal("latitude"),
                            param.getBigDecimal("longitude"));
                    s_sent = param.getString("sent");
                } catch (JSONException e) {
                    failExceptionRet(exchange, e);
                    return;
                }
                String username = "";
                try {
                    username = param.getString("username");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (Utils.strIsEmpty(username)) {
                    username = Utils.getUserNameByAuth(exchange);
                }
                if (Utils.strIsEmpty(username)) {
                    failRet(exchange, "auth err");
                    return;
                }
                coordinate.setUsername(username);
                if (Utils.strIsEmpty(s_sent)) {
                    failRet(exchange, "sent not empty");
                    return;
                }
                LocalDateTime localDateTime = Utils.utc2LocalDatetime(s_sent);
                if (localDateTime == null) {
                    Utils.failRet(exchange, "not utc time");
                    return;
                }
                coordinate.setTime(s_sent);
                //json => coordinate
                try {
                    String description = param.getString("description");
                    if (Utils.strIsEmpty(description)) {
                        description = "nodata";
                    } else {
                        if (Utils.strGtLength(description, 1024)) {
                            failRet(exchange, "description limited to 1024 characters");
                            return;
                        }
                    }
                    coordinate.setDescription(description);
                } catch (JSONException e) {
                    coordinate.setDescription("nodata");
                }

                System.out.println("xxxxxx");
                
                //send coordinate to database

                if (db.setMessage(coordinate)) {
                    exchange.sendResponseHeaders(200, 2);
                    OutputStream outputStream = exchange.getResponseBody();
                    outputStream.write("OK".getBytes());
                    outputStream.flush();
                    outputStream.close();
                } else {
                    failRet(exchange, "Unknown exception");
                }

            }

        }

    }

    private void failRet(HttpExchange exchange, String msg) throws IOException {
        exchange.sendResponseHeaders(200, msg.getBytes().length);
        OutputStream outputStream = exchange.getResponseBody();
        outputStream.write(msg.getBytes());
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


    private String handleResponsePOST(HttpExchange httpExchange) throws IOException {
        InputStream stream = httpExchange.getRequestBody();
        String text = new BufferedReader(new InputStreamReader(stream, StandardCharsets.UTF_8)).lines().collect(Collectors.joining("\n"));
        return text;
    }


    private void handleResponseGET(HttpExchange httpExchange, String requestParamValue) throws IOException {
        String userName = Utils.getUserNameByAuth(httpExchange);
        if (Utils.strIsEmpty(userName)) {
            Utils.failRet(httpExchange, "authorization err");
            return;
        }
        JSONArray ret = db.getMessages(userName);
        OutputStream outputStream = httpExchange.getResponseBody();
        int rspCode = 0;
        String rspMsg = "";

        if (ret.length() == 0) {
            rspCode = 204;
            rspMsg = "no data";
        } else {
            rspCode = 200;
            rspMsg = ret.toString();
        }
        byte[] res = rspMsg.getBytes(StandardCharsets.UTF_8);
        
        httpExchange.sendResponseHeaders(rspCode, res.length);
        outputStream.write(res);
        outputStream.flush();
        outputStream.close();
    }

}

