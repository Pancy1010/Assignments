package programming3;

import com.sun.net.httpserver.*;

import java.net.InetSocketAddress;

import java.security.KeyStore;


import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLParameters;
import javax.net.ssl.TrustManagerFactory;

import java.io.*;

/**
 * @author Zhang Runyan
 */

class Server {
    public static void main(String[] args) throws Exception {
        MyExceptionHandler exchand = MyExceptionHandler.getMyExceptionHandler();
        CoordDatabase db = CoordDatabase.getInstance(exchand);
        AuthMe authchecker = new AuthMe("coordinates",db);

        HttpsServer server = HttpsServer.create(new InetSocketAddress(8000),0);
        final HttpContext coordinateContext = server.createContext("/coordinates",new CoordinateHandler(db,exchand));
        final HttpContext registrationContext = server.createContext("/registration",new RegistrationHandler(db,exchand));
        final HttpContext commentContext = server.createContext("/comment",new CommentHandler(db,exchand));

        coordinateContext.setAuthenticator(authchecker);

        // registrationContext.setAuthenticator(authchecker);

        commentContext.setAuthenticator(authchecker);

        char[] passphrase = "zhangqiling".toCharArray();
        KeyStore ks = KeyStore.getInstance("JKS");
        ks.load(new FileInputStream("./certificate.jks"), passphrase);
     
        KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");
        kmf.init(ks, passphrase);
     
        TrustManagerFactory tmf = TrustManagerFactory.getInstance("SunX509");
        tmf.init(ks);
     
        SSLContext ssl = SSLContext.getInstance("TLS");
        ssl.init(kmf.getKeyManagers(), tmf.getTrustManagers(), null);

        server.setHttpsConfigurator (new HttpsConfigurator(ssl) {
            @Override
            public void configure (HttpsParameters params) {

            InetSocketAddress remote = params.getClientAddress();
    
            SSLContext c = getSSLContext();
    

            SSLParameters sslparams = c.getDefaultSSLParameters();

            params.setSSLParameters(sslparams);
            }
        });
        server.setExecutor(null); 
        server.start(); 
    }
}
