package programming3;

import java.beans.Statement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;


import org.json.JSONArray;
import org.json.JSONObject;

/**
 * @author Zhang Runyan
 */

public class CoordDatabase {

    private Connection dbConnection = null;
    private static CoordDatabase dbInstance = null;
    private static MyExceptionHandler exchand;


    public static synchronized CoordDatabase getInstance(MyExceptionHandler exc) {
        exchand = exc;

        if (null == dbInstance) {
            dbInstance = new CoordDatabase();
        }
        return dbInstance;
    }

    private CoordDatabase() {

        try {
            init();
        } catch (SQLException e) {
            System.out.println("Log - SQLexception");
        }

    }

    private boolean init() throws SQLException {

        String dbName = "MyDatabase";

        String database = "jdbc:sqlite:" + dbName;
        dbConnection = DriverManager.getConnection(database);

        try {
            if (null != dbConnection) {
                String createBasicDB = "create table if not exists data(username varchar(100) NOT NULL,latitude varchar (100) not null,longitude varchar (100) not null,sent varchar (100) not null,description varchar (1024)  )";
               
                String sqlUser = "create table if not exists user(username varchar(100) NOT NULL primary key, password varchar(32) not null ,email varchar(64) NOT NULL)";
                
                String sqlComment = "create table if not exists comment (comment varchar(100) not null, sent varchar (100) not null,username varchar(100) not null,message_id integer not null)";
                
                java.sql.Statement statement = dbConnection.createStatement();
                statement.executeUpdate(sqlUser);
                statement.executeUpdate(sqlComment);
                statement.executeUpdate(createBasicDB);
                statement.close();
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }


    public void closeDB() throws SQLException {
        try {
            if (null != dbConnection) {
                dbConnection.close();
                System.out.println("closing db connection");
                dbConnection = null;
            }
        } catch (SQLException e) {
            exchand.handleException(e);
        }
    }

    public JSONArray getMessages(String timestart, String timeend, String username) {
        JSONArray jsonArray = new JSONArray();
        String getMessageString = "select * from data where username= '" + username + "'";
        LocalDateTime start = Utils.utc2LocalDatetime(timestart);
        LocalDateTime end = Utils.utc2LocalDatetime(timeend);
        if (start == null || end == null) {
            return jsonArray;
        }
        try {
            java.sql.Statement statement = dbConnection.createStatement();
            ResultSet rs = statement.executeQuery(getMessageString);
            while (rs.next()) {
                JSONObject obj = new JSONObject();
                obj.put("username", rs.getString("username"));
                obj.put("latitude", rs.getBigDecimal("latitude"));
                obj.put("longitude", rs.getBigDecimal("longitude"));
                String sent = rs.getString("sent");
                LocalDateTime sentLocal = Utils.utc2LocalDatetime(sent);
                if (sentLocal == null) {
                    continue;
                }
                if ((sentLocal.isAfter(start) || sentLocal.isEqual(start))
                        && (sentLocal.isBefore(end) || sentLocal.isEqual(end))) {
                    obj.put("sent", sent);
                    jsonArray.put(obj);
                }
            }
            rs.close();
            statement.close();
        } catch (Exception e) {
            e.printStackTrace();

        }
        return jsonArray;
    }

    public JSONArray getMessages(String username) {
        JSONArray jsonArray = new JSONArray();
        String getMessageString = "select rowid, username, latitude, longitude, sent, description from data " + "where username ='" + username + "'";

        try {
            java.sql.Statement statement = dbConnection.createStatement();
            ResultSet rs = statement.executeQuery(getMessageString);
            while (rs.next()) {
                JSONObject obj = new JSONObject();
                obj.put("id", rs.getInt("rowid"));
                obj.put("username", rs.getString("username"));
                obj.put("latitude", rs.getBigDecimal("latitude"));
                obj.put("longitude", rs.getBigDecimal("longitude"));

                String sent = rs.getString("sent");

                obj.put("sent", sent);
                if (!Utils.strIsEmpty(rs.getString("description"))) {
                    obj.put("description", rs.getString("description"));
                } else {
                    obj.put("description", "nodata");
                }
                obj.put("comments", getComments(rs.getString("username"), rs.getInt("rowid")));
                jsonArray.put(obj);
            }
            rs.close();
            statement.close();
        } catch (Exception e) {
            e.printStackTrace();

        }

        return jsonArray;
    }

    public JSONArray getMessgaes(String startTime, String endTime) {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss:SSSX");
        LocalDateTime start = LocalDateTime.parse(startTime, formatter);
        LocalDateTime end = LocalDateTime.parse(endTime, formatter);
        long L_start = start.toInstant(ZoneOffset.of("Z")).toEpochMilli();
        long L_end = end.toInstant(ZoneOffset.of("Z")).toEpochMilli();

        System.out.println("l_start: " + L_start + ", l_end: " + L_end);

        Statement queryStatement = null;

        JSONArray jsonArray = new JSONArray();

        String getMessageString = "select rowid, username, latitude, longitude, sent, description from data" + "where username" + "where sent>=" +
                L_start + "and sent<=" + L_end;

        try {
            queryStatement = (Statement) dbConnection.createStatement();

            ResultSet rs = ((java.sql.Statement) queryStatement).executeQuery(getMessageString);

            while (rs.next()) {
                JSONObject obj = new JSONObject();
                obj.put("id", rs.getInt("rowid"));
                obj.put("username", rs.getString("username"));
                obj.put("latitude", rs.getBigDecimal("latitude"));
                obj.put("longitude", rs.getBigDecimal("longitude"));
                long l_sent = rs.getLong("sent");
                ZonedDateTime now = ZonedDateTime.ofInstant(Instant.ofEpochMilli(l_sent), ZoneId.of("UTF_8"));
                String dataText = now.format(formatter);
                obj.put("sent", dataText);
                if (rs.getString("description") != null) {
                    obj.put("description", rs.getString("description"));
                } else {
                    obj.put("description", "nodata");
                }
                obj.put("comments", getComments(rs.getString("username"), rs.getInt("rowid")));
                jsonArray.put(obj);
            }
            rs.close();
            ((Connection) queryStatement).close();
        } catch (SQLException e) {
            exchand.handleException(e);
        }

        return jsonArray;
    }

    public boolean setMessage(Coordinate coordinate) {

        //sql format just like
        //import into data values

        String setMessageString = null;

        if (!Utils.strIsEmpty(coordinate.getDescription())) {
            setMessageString = "insert into data(username, latitude, longitude, description,sent)" + "Values" + "('"
                    + coordinate.getUsername() + "','" + coordinate.getLatitude() + "','" + coordinate.getLongitude() + "','" + coordinate.getDescription() + "','"
                    + coordinate.getTime() + "')";
        }

        try {
            java.sql.Statement statement = dbConnection.createStatement();
            statement.executeUpdate(setMessageString);
            statement.close();
        } catch (SQLException e) {
            exchand.handleException(e);
            return false;
        }
        return true;
    }

    public void saveRegistration(String username, String password, String email) {
        String password_hash = null;
        try {
            password_hash = MD5Hash.doHash(password);
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
        String sql = "insert into user(username,password,email) values ('" + username + "','" + password_hash + "','" + email + "')";
        try {
            java.sql.Statement statement = dbConnection.createStatement();
            statement.executeUpdate(sql);
            statement.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getPassword(String username) {
        String password = null;
        String sql = "select password from user where username ='" + username + "'";
        try {
            ResultSet resultSet = dbConnection.createStatement().executeQuery(sql);
            while (resultSet.next()) {
                password = resultSet.getString("password");
            }
            resultSet.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return password;
    }

    public JSONArray getComments(String username, int messageId) {
        String sql = "select comment, sent,message_id from comment where username = '" + username + "' and message_id=" + messageId;
        JSONArray jsonArray = new JSONArray();
        try {
            java.sql.Statement statement = dbConnection.createStatement();
            ResultSet rs = statement.executeQuery(sql);
            while (rs.next()) {
                JSONObject obj = new JSONObject();
                obj.put("id", rs.getInt("message_id"));
                obj.put("comment", rs.getString("comment"));
                String send = rs.getString("sent");
                obj.put("sent", send);
                jsonArray.put(obj);
            }
            rs.close();
            statement.close();
        } catch (SQLException e) {
            exchand.handleException(e);
        }

        return jsonArray;
    }

    public void saveComment(int id, String comment, String sent, String userName) {
        String sql = "insert into comment(message_id,comment,sent,username) values (" + id + ",'" + comment + "','"
                + sent + "','" + userName + "')";
        try {
            java.sql.Statement statement = dbConnection.createStatement();
            statement.executeUpdate(sql);
            statement.close();
        } catch (SQLException e) {
            exchand.handleException(e);
        }
    }

    public void delMessages() {
        try {
            Statement createStatement;
            createStatement = (Statement) dbConnection.createStatement();
            ((java.sql.Statement) createStatement).executeUpdate("delete from data");
            ((Connection) createStatement).close();
        } catch (SQLException e) {
            exchand.handleException(e);
        }
    }

}