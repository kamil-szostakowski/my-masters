package http.handlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import jobs.couchdb.CouchdbPrepareJob;
import jobs.couchdb.CouchdbSchemaJob;
import jobs.couchdb.CouchdbTestJob;
import java.io.IOException;
import java.io.OutputStream;
import jobs.couchdb.street.CouchdbClusterFeed;
import nosql.DBTestRunner;

/**
 * Żądania obsługiwane przez handler.
 * 
 * http://localhost:9090/db/couchdb/job/prepare/run/name
 * http://localhost:9090/db/couchdb/job/schema/run/name
 * http://localhost:9090/db/couchdb/job/test/run/name
 * 
 * @author Kamil Szostakowski
 */
public class CouchdbJobRequest extends BaseJobRequest implements HttpHandler
{    
    /*
     * Metoda przygotowuje zadanie które ma zostać wykonane w bazie danych.
     */    
    
    @Override
    protected void PrepareJob()
    {
        if(super.jobName.equals("prepare")) { this.job = new CouchdbPrepareJob(); }
        if(super.jobName.equals("schema")) { this.job = new CouchdbSchemaJob(); }
        if(super.jobName.equals("test")) { this.job = new CouchdbTestJob(); }   
        if(super.jobName.equals("cluster-feed")) { this.job = new CouchdbClusterFeed(); }   
    }         
    
    /*
     * Metoda obsługująca http request.
     */    
    
    @Override
    public void handle(HttpExchange exchange) throws IOException 
    {        
        this.ParseParameters(exchange.getRequestURI().toString());
        
        this.PrepareJob();  // Parsowanie parametrów requestu        
        this.PrepareRun(); // Utworzenie przebiegu        
        this.PrepareDataSource(); // Utworzenie źródła danych
        
        String response = "CouchdbPrepareRequest started...";
        
        if(this.IsValid())
        {        
            DBTestRunner testRunner = new DBTestRunner();
        
            testRunner.SetDatabase(this.dbName);
            testRunner.SetJobIdentifier(this.jobIdentifier);
            testRunner.SetJob(this.job);
            testRunner.SetTest(this.run);
            testRunner.SetDataSource(this.dataSource);
            testRunner.RunTest();    
            
            System.out.println("Couchdb job run");
        }
        
        else
        {
            response = this.GetValidationMessage();
            
            System.out.println(String.format("Couchdb job error: %s", response));            
        }
        
        exchange.sendResponseHeaders(200, response.getBytes("UTF-8").length);
        
        OutputStream os = exchange.getResponseBody();
        
        os.write(response.getBytes());
        os.close();    
    }   
}