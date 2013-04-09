package http.handlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;
import java.util.LinkedList;
import java.util.List;
import tools.StringTools;

/**
 * Handler obsługujący request z żądaniem pobrania stanu
 * wykonania zadania.
 * 
 * http://localhost:9090/info/db/mongodb/job/prepare/run/prepare
 * 
 * @author Kamil Szostakowski
 */

public class InfoRequest implements HttpHandler
{
    private String db;
    private String job;
    private String run;
    
    List<String> files;
    
    public InfoRequest()
    {
        this.files = new LinkedList<>();
    }
    
    private void ParseParameters(String uri)
    {
        String[] params = uri.split("/");
        
        for(int iter=2; iter<params.length; iter+=2)
        {                        
            if(params[iter].equals("db")) { this.db = params[iter+1]; }
            if(params[iter].equals("job")) { this.job = params[iter+1]; }
            if(params[iter].equals("run")) { this.run = params[iter+1]; }
        }
    } 
    
    private void ReadDir()
    {
        try
        {
            File dir = new File(".");
            File[] contents = dir.listFiles();
        
            for(int iter=0; iter<contents.length; iter++)
            {
                String filename = contents[iter].getName();
                
                if(filename.contains(String.format("%s-%s", db, job)))
                {                    
                    this.files.add(filename);
                }                                            
            }
        }
        
        catch(Exception ex)
        {
            System.out.println(ex.getMessage());
        }
    }
    
    @Override
    public void handle(HttpExchange he) throws IOException 
    {
        String response = "";
        
        this.ParseParameters(he.getRequestURI().toString());        
        this.ReadDir();
        
        if(this.files.size() > 0)
        {
            for(int iter=0; iter<files.size(); iter++)
            {                        
                String content = StringTools.ReadFile(files.get(iter));
            
                response += String.format("<thread id=\"%d\">%s</thread>", iter, content); 
            }            
        }
        
        else
        {
            response = "No such job found";
        }            
        
        response = String.format("<job db=\"%s\" name=\"%s\" run=\"%s\">%s</job>", this.db, this.job, this.run, response);
        
        he.sendResponseHeaders(200, response.getBytes("UTF-8").length);        
        
        try (OutputStream os = he.getResponseBody()) 
        {            
            os.write(response.getBytes());
        }         
        
        this.files.clear();
    }    
}