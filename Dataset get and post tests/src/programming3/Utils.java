package programming3;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.io.OutputStream;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

/**
 * @author Zhang Runyan
 */

public class Utils {

    /**
     * 字符串为空
     *
     * @param s
     * @return
     */
    public static boolean strIsEmpty(String s) {
        return s == null || "".equals(s);
    }

    /**
     * 字符长度大于 len
     *
     * @param s
     * @param len
     * @return
     */
    public static boolean strGtLength(String s, int len) {
        if (strIsEmpty(s)) {
            return false;
        }
        return s.toCharArray().length > len;
    }

    /**
     * // "2019-04-24T11:00:00.000Z" utc时间格式
     *
     * @param utc utc时间格式
     * @return LocalDateTime
     */
    public static LocalDateTime utc2LocalDatetime(String utc) {
        if (strIsEmpty(utc)) {
            return null;
        }
        try {
            Instant instant = Instant.parse(utc);
            LocalDateTime ldt = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
            return ldt;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 错误返回 的消息
     *
     * @param exchange
     * @param msg
     * @throws IOException
     */
    public static void failRet(HttpExchange exchange, String msg) throws IOException {
        exchange.sendResponseHeaders(200, msg.getBytes().length);
        OutputStream outputStream = exchange.getResponseBody();
        outputStream.write(msg.getBytes());
        outputStream.flush();
        outputStream.close();
    }

    /**
     * 从鉴权头拿用户信息
     *
     * @param m
     * @return
     */
    public static String getUserNameByAuth(HttpExchange m) {
        String userName = "";
        try {
            Headers headers = m.getRequestHeaders();
            String authorization = headers.get("Authorization").get(0).split(" ")[1];
            userName = MD5Hash.reHash(authorization).split(":")[0];
        } catch (Exception e) {
            return "";
        }
        return userName;
    }


}
