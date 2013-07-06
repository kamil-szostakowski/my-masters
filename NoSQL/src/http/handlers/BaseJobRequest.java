package http.handlers;

import basejobs.IDatabaseJob;
import data.source.FileDataSource;
import data.source.IDataSource;
import data.source.XmlRegionsDataSource;
import data.source.XmlStreetsDataSource;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import runs.DatabaseRun;
import runs.IDatabaseRun;

/**
 *
 * @author Kamil Szostakowski
 */

public class BaseJobRequest 
{
    protected String dbName;
    protected String jobName;
    protected String runName;
    protected String dataSourceName;
    protected String jobIdentifier;
    
    protected List<String> validationErrors;
    
    /*
     * Komponenty zadania.
     */
    
    protected IDatabaseJob job;
    protected IDatabaseRun run; 
    protected IDataSource dataSource;    
    
    public BaseJobRequest()
    {
        this.validationErrors = new ArrayList<String>();
    }
    
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
        this.run = new DatabaseRun(String.format("Runs/%s-run.json", this.runName));        
    }    
    
    /*
     * Metoda przygotowuje źródło danych dla zadania.
     */
    
    protected void PrepareDataSource()
    {                        
        if(this.dataSourceName.equals("xml-street")) { this.dataSource = new XmlStreetsDataSource(); }        
        else if(this.dataSourceName.equals("xml-region")) { this.dataSource = new XmlRegionsDataSource(); }        
        else { this.dataSource = new FileDataSource("input2.txt"); }
    }
    
    /*
     * Metoda parsuje parametry wejściowe metody.
     */
    
    protected void ParseParameters(String uri)
    {
        String[] params = uri.split("/");
        
        for(int iter=1; iter<params.length; iter+=2)
        {   
            if(params[iter].equals("db")) { this.dbName = params[iter+1]; }
            if(params[iter].equals("job")) { this.jobName = params[iter+1]; }       
            if(params[iter].equals("run")) { this.runName = params[iter+1]; }
            if(params[iter].equals("datasource")) { this.dataSourceName = params[iter+1]; }
            if(params[iter].equals("id")) { this.jobIdentifier = params[iter+1]; }
        }                
    }
    
    /*
     * Metoda sprawdza czy zadanie jest poprawnie zdefiniowane.
     */
    
    protected boolean IsValid()
    {                
        boolean valid = true;
        
        if(!this.validationErrors.isEmpty())
        {
            this.validationErrors.clear();
        }
        
        File logpath = new File(String.format("Logs/%s/%s", this.dbName, this.jobIdentifier));
        
        if(logpath.exists())
        {
            this.validationErrors.add(String.format("Job %s for %s already exists ...", this.jobIdentifier, this.dbName));
            
            valid = false;
        }
        
        return valid;
    }
    
    /*
     * Metoda zwraca sformatowaną informację z błędami walidacji zadania.
     */
    
    protected String GetValidationMessage()
    {
        String message = "";
        
        if(this.validationErrors.isEmpty())
        {
            return null;
        }
        
        for(int iter=0; iter<this.validationErrors.size(); iter++)
        {
            message += String.format("%s\n", this.validationErrors.get(iter));
        }
        
        return message;
    }
}