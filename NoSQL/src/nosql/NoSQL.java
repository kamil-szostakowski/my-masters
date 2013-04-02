package nosql;

import java.util.logging.Level;
import java.util.logging.Logger;

import java.io.IOException;

import tools.Configuration;

import com.sun.net.httpserver.HttpServer;
import http.handlers.CouchdbJobRequest;
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

        server.createContext("/db/couchdb", new CouchdbJobRequest());
        server.createContext("/db/mongodb", new MongodbJobRequest());
        server.createContext("/db/postgresql", new PostgresqlJobRequest());
        server.setExecutor(null);
        server.start();
    }
    
    private static void PrepareJobs(String[] args)
    {        
        TestFactory.SetDbType(args[0]);
        
        boolean withTest = false;
        boolean withSchema = false;
        boolean withPrepareData = false;
        
        for(int iter=1; iter<args.length; iter++)
        {
            if("--with-test".equals(args[iter])) { withTest = true; }            
            if("--with-schema".equals(args[iter])) { withSchema = true; }            
            if("--with-prepare-data".equals(args[iter])) { withPrepareData = true; }
        } 
        
        if(withSchema)
        {
//            NoSQL.jobs.add(TestFactory.GetSchemaJob());
//            NoSQL.runs.add(new DBSchemaRun());             
        }
        
        if(withPrepareData)
        {
//            NoSQL.jobs.add(TestFactory.GetPrepareJob());
//            NoSQL.runs.add(new DBPrepareRun());
        }
                
        if(withTest)
        {                
//            NoSQL.jobs.add(TestFactory.GetTestJob());
//            NoSQL.runs.add(new DBTestRun());
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
            
            NoSQL.InitializeHttpServer();                            
                
            NoSQL.PrepareJobs(args);                                              
        } 
        
        catch (IOException ex) 
        {
            Logger.getLogger(NoSQL.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}