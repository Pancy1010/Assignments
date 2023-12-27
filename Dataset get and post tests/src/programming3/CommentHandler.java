package programming3;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.stream.Collectors;

import org.json.JSONException;
import org.json.JSONObject;

import com.sun.net.httpserver.*;

/**
 * @author Zhang Runyan
 */

public class CommentHandler implements HttpHandler {
    private CoordDatabase db;
    public MyExceptionHandler exHand;

    public CommentHandler(CoordDatabase db, MyExceptionHandler exHand) {
        this.db = db;
        this.exHand = exHand;
    }

    @Override
    public void handle(HttpExchange h) throws IOException {

        System.out.println("commenthandler");

        if ("POST".equals(h.getRequestMethod())) {
            //
            String jsonString = handleResponsePOST(h);
            //
            // Mandatory Field
            
            int id = -1;
            String comment = "";
            JSONObject obj = null;
            String sent = "";
            try {
                obj = new JSONObject(jsonString);
                id = obj.getInt("id");
                comment = obj.getString("comment");
                sent = obj.getString("sent");
            } catch (JSONException e) {
                failExceptionRet(h, e);
                return;
            }
            LocalDateTime localDateTime = Utils.utc2LocalDatetime(sent);
            if (localDateTime == null) {
                Utils.failRet(h, "not utc time");
                return;
            }
            String userName = Utils.getUserNameByAuth(h);
            if (Utils.strIsEmpty(userName)) {
                Utils.failRet(h, "authorization err");
                return;
            }
            db.saveComment(
                    id,
                    comment,
                    sent,
                    userName
            );

            h.sendResponseHeaders(200, 2);
            OutputStream outputStream = h.getResponseBody();
            outputStream.write("OK".getBytes());
            outputStream.flush();
            outputStream.close();
        } else {
            handleResponse(h, "Not supported");
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

