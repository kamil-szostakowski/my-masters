package http.handlers;

import basejobs.IDatabaseJob;
import data.source.FileDataSource;
import data.source.IDataSource;
import runs.DatabaseRun;
import runs.IDatabaseRun;

/**
 *
 * @author Kamil Szostakowski
 */

public class BaseJobRequest 
{
    protected String jobName;
    protected String runName;
    protected String dataSourceName;
    
    /*
     * Komponenty zadania.
     */
    
    protected IDatabaseJob job;
    protected IDatabaseRun run; 
    protected IDataSource dataSource;    
    
    /*
     * Metoda przygotowuje zadanie które ma zostać wykonane w bazie danych.
     */    
    
    protected void PrepareJob()
    {        
    }
    
    /*
     * Metoda przygotowuje przebieg dla zadania które ma zostać wykonane
     * w bazie danych.
     */
    
    protected void PrepareRun()
    {
        this.dataSource = new FileDataSource("input2.txt");
        
        this.run = new DatabaseRun(String.format("%s-run.json", this.runName));
        this.run.SetDataSource(this.dataSource);
    }    
    
    /*
     * Metoda parsuje parametry wejściowe metody.
     */
    
    protected void ParseParameters(String uri)
    {
        String[] params = uri.split("/");
        
        for(int iter=1; iter<params.length; iter+=2)
        {                        
            if(params[iter].equals("job")) { this.jobName = params[iter+1]; }            
            if(params[iter].equals("run")) { this.runName = params[iter+1]; }
            if(params[iter].equals("datasource")) { this.dataSourceName = params[iter+1]; }
        }
    }
}