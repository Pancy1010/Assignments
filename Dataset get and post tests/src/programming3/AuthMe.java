package programming3;

import com.sun.net.httpserver.*;

/**
 * @author Zhang Runyan
 */

class AuthMe extends BasicAuthenticator {

    private CoordDatabase db;

    public AuthMe(String realm, CoordDatabase db) {
        super(realm);
        this.db = db;
    }

    public boolean isEmpty(String s) {
        return s == null || "".equals(s);
    }

    @Override
    public boolean checkCredentials(String username, String password) {

        System.out.println("username" + username + ",password" + password);
        if (isEmpty(username) || isEmpty(password)) {
            return false;
        }
        String dbPassword = (db).getPassword(username);
        if (isEmpty(dbPassword)) {
            return false;
        }
        System.out.println("password in db is:" + dbPassword);
        try {
            String password_hash = MD5Hash.doHash(password);
            System.out.println("password_hash:" + password_hash);
            if (dbPassword.equals(password_hash)) {
                System.out.println("The user name and password match successfully");
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("User name and password matching failed");
        return false;

    }

}
