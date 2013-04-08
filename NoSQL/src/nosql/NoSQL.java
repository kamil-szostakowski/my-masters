package nosql;

import java.util.logging.Level;
import java.util.logging.Logger;

import java.io.IOException;

import tools.Configuration;

import com.sun.net.httpserver.HttpServer;
import http.handlers.CouchdbJobRequest;
import http.handlers.InfoRequest;
import http.handlers.MongodbJobRequest;
import http.handlers.PostgresqlJobRequest;
import java.net.InetSocketAddress;

/**
 *
 * @author test
 */
public class NoSQL 
{        
    private static String HelpMSG = "help: couchdb|mongodb|postgresql --with-test --with-schema --with-prepare-data";            
    
    private static HttpServer server;
    
    private static void InitializeHttpServer() throws IOException
    {
        server = HttpServer.create(new InetSocketAddress(9090), 0);

        server.createContext("/info", new InfoRequest());
        server.createContext("/db/couchdb", new CouchdbJobRequest());
        server.createContext("/db/mongodb", new MongodbJobRequest());
        server.createContext("/db/postgresql", new PostgresqlJobRequest());
        
        server.setExecutor(null);
        server.start();
    }    
    
    /**
     * @param args the command line arguments
     */
    
    public static void main(String[] args) 
    {      
        try 
        {
            Configuration.Prepare();
            
            NoSQL.InitializeHttpServer();                                                                                                      
        } 
        
        catch (IOException ex) 
        {
            Logger.getLogger(NoSQL.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}