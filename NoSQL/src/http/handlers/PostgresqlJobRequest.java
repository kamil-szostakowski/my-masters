package http.handlers;

import basejobs.IDatabaseJob;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import java.io.IOException;
import java.io.OutputStream;
import nosql.DBTestRunner;
import postgresjobs.PostgresPrepareJob;
import postgresjobs.PostgresSchemaJob;
import postgresjobs.PostgresTestJob;
import runs.DatabaseRun;
import runs.IDatabaseRun;

/**
 *
 * @author Kamil Szostakowski
 */
public class PostgresqlJobRequest extends BaseHttpRequest implements HttpHandler
{
    private IDatabaseJob job;
    private IDatabaseRun run;
    
    private void PrepareJob()
    {
        if(super.jobName.equals("prepare")) { this.job = new PostgresPrepareJob(); }
        if(super.jobName.equals("schema")) { this.job = new PostgresSchemaJob(); }
        if(super.jobName.equals("test")) { this.job = new PostgresTestJob(); }
        
        this.run = new DatabaseRun(String.format("%s-run.json", this.runName));
    }    
    
    @Override
    public void handle(HttpExchange exchange) throws IOException 
    {
        this.ParseParameters(exchange.getRequestURI().toString());
        
        this.PrepareJob();         
        
        String response = "PostgresqlPrepareJob started...";
        
        DBTestRunner testRunner = new DBTestRunner();
        
        testRunner.SetJob(this.job);
        testRunner.SetTest(this.run);
        testRunner.RunTest();          
        
        exchange.sendResponseHeaders(200, response.length());
        
        try (OutputStream os = exchange.getResponseBody()) 
        {
            os.write(response.getBytes());
        }     
    }     
}