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
import java.io.File;
import java.net.InetSocketAddress;

/**
 *
 * @author test
 */
public class NoSQL 
{        
    private static String HelpMSG = "help: couchdb|mongodb|postgresql --with-test --with-schema --with-prepare-data";            
    
    private static HttpServer server;
    
    /*
     * Metoda inicjalizuje web serwis do komunikacji z aplikacją.
     */
    
    private static void InitializeHttpServer() throws IOException
    {
        Long port = (Long) Configuration.GetParam("main:port");
              
        server = HttpServer.create(new InetSocketAddress(port.intValue()), 0);

        server.createContext("/info", new InfoRequest());
        server.createContext("/db/couchdb", new CouchdbJobRequest());
        server.createContext("/db/mongodb", new MongodbJobRequest());
        server.createContext("/db/postgresql", new PostgresqlJobRequest());
        
        server.setExecutor(null);
        server.start();
    }   
    
    /*
     * Metoda inicjuje kontekst aplikacji.
     * 
     * - Tworzy katalog na logi.
     */
        
    public static void InitializeApplicationContext()
    {
        File logDirectory = new File("Logs");
        
        if(!logDirectory.exists())
        {
            logDirectory.mkdir();
        }
        
        String[] dbs = new String[3];
        
        dbs[0] = "couchdb";
        dbs[1] = "mongodb";
        dbs[2] = "postgresql";
        
        for(int iter=0; iter<dbs.length; iter++)
        {
            File dbDirectory = new File(String.format("Logs/%s", dbs[iter]));
            
            if(!dbDirectory.exists())
            {
                dbDirectory.mkdir();                
            }
        }
    }
    
    /**
     * @param args the command line arguments
     */
    
    public static void main(String[] args) 
    {      
        try 
        {            
            Configuration.Prepare();
         
            NoSQL.InitializeApplicationContext();
            NoSQL.InitializeHttpServer();                                                                                                      
        } 
        
        catch (IOException ex) 
        {
            Logger.getLogger(NoSQL.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}