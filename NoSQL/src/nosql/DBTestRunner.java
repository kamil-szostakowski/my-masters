package nosql;

import basejobs.IDatabaseJob;
import data.source.IDataSource;
import runs.IDatabaseRun;

/**
 *
 * @author test
 */
public class DBTestRunner 
{
    private IDatabaseRun run;  
    private IDatabaseJob job;
    private IDataSource datasource;
    private Thread[] threads;     
    
    private String jobIdentifier;
    
    /*
     * Metoda pozwala na ustawienie identyfikatora zadania
     */
    
    public void SetJobIdentifier(String id)
    {
        this.jobIdentifier = id;
    }
    
    /*
     * Metoda pozwala na ustawienie źródła danych dla zadania.
     */
    
    public void SetDataSource(IDataSource source)
    {
        this.datasource = source;
    }
    
    /*
     * Metoda pozwala na zdefiniowanie przebiegu dla zadania.
     */
    
    public void SetTest(IDatabaseRun test)
    {
        this.run = test;
    }
    
    /*
     * Metoda pozwala na zdefiniowanie zadania do wykonania w bazie danych.
     */
    
    public void SetJob(IDatabaseJob job)
    {
        this.job = job;
    }
    
    /*
     * Metoda uruchamia zadanie w bazie.
     */
    
    public void RunTest()
    {        
        this.threads = new Thread[run.GetNumberOfThreads()];                
        
        for(int iter=0; iter<run.GetNumberOfThreads(); iter++)
        {                                              
            String dbName = this.job.GetDbName();
            
            this.threads[iter] = new Thread((Runnable)this.job);
                        
            this.job.SetThreadID(iter);            
            this.job.SetJobIdentifier(this.jobIdentifier);
            this.job.SetLogFile(String.format("Logs/%s/%s/thread-%d.log", dbName, this.jobIdentifier, iter));
            this.job.SetConfiguration(this.run);
            this.job.SetDataSource(this.datasource);
                
            this.threads[iter].start();  
            
            this.job = this.job.Clone();
        }             
    }    
}
