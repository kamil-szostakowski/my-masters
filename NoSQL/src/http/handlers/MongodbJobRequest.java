package http.handlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import jobs.couchdb.CouchdbPrepareJob;
import jobs.couchdb.CouchdbSchemaJob;
import jobs.couchdb.CouchdbTestJob;
import java.io.IOException;
import java.io.OutputStream;
import nosql.DBTestRunner;

/**
 *
 * @author Kamil Szostakowski
 */

public class MongodbJobRequest extends BaseJobRequest implements HttpHandler
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
    }    
    
    /*
     * Metoda obsługująca http request.
     */    
    
    @Override
    public void handle(HttpExchange exchange) throws IOException 
    {
        this.ParseParameters(exchange.getRequestURI().toString());
        
        this.PrepareJob(); 
        this.PrepareRun();
        
        String response = "MongodbPrepareJob started...";
        
        DBTestRunner testRunner = new DBTestRunner();
        
        testRunner.SetJob(this.job);
        testRunner.SetTest(this.run);
        testRunner.RunTest();        
        
        exchange.sendResponseHeaders(200, response.getBytes("UTF-8").length);
        
        try (OutputStream os = exchange.getResponseBody()) 
        {
            os.write(response.getBytes());
        } 
    }   
}