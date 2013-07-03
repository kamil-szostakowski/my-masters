package http.handlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import jobs.mongodb.MongodbPrepareJob;
import jobs.mongodb.MongodbSchemaJob;
import jobs.mongodb.MongodbTestJob;
import java.io.IOException;
import java.io.OutputStream;
import nosql.DBTestRunner;

/**
 * Żądania obsługiwane przez handler.
 * 
 * http://localhost:9090/db/mongodb/job/prepare/run/name
 * http://localhost:9090/db/mongodb/job/schema/run/name
 * http://localhost:9090/db/mongodb/job/test/run/name
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
        if(super.jobName.equals("prepare")) { this.job = new MongodbPrepareJob(); }
        if(super.jobName.equals("schema")) { this.job = new MongodbSchemaJob(); }
        if(super.jobName.equals("test")) { this.job = new MongodbTestJob(); }                
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
        this.PrepareDataSource();
        
        String response = "MongodbPrepareJob started...";
        
        if(this.IsValid())
        {        
            DBTestRunner testRunner = new DBTestRunner();
        
            testRunner.SetDatabase(this.dbName);
            testRunner.SetJobIdentifier(this.jobIdentifier);
            testRunner.SetJob(this.job);
            testRunner.SetTest(this.run);
            testRunner.SetDataSource(this.dataSource);
            testRunner.RunTest();        
        }
        
        else
        {
            response = this.GetValidationMessage();
        }        
        
        exchange.sendResponseHeaders(200, response.getBytes("UTF-8").length);
        
        OutputStream os = exchange.getResponseBody();
        
        os.write(response.getBytes());
        os.close();
    }   
}