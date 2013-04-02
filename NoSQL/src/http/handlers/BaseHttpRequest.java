package http.handlers;

/**
 *
 * @author Kamil Szostakowski
 */

public class BaseHttpRequest 
{
    protected String jobName;
    protected String runName;
    protected String dataSourceName;
    
    protected void ParseParameters(String uri)
    {
        String[] params = uri.split("/");
        
        for(int iter=1; iter<params.length; iter+=2)
        {                        
            if(params[iter].equals("job")) { this.jobName = params[iter+1]; }            
            if(params[iter].equals("run")) { this.runName = params[iter+1]; }
        }
    }
}