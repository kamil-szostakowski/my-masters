/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package nosql;

import java.awt.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import basejobs.BaseJob;
import basejobs.IDatabaseJob;
import runs.IDatabaseRun;

/**
 *
 * @author test
 */
public class DBTestRunner 
{
    private IDatabaseRun test;  
    private IDatabaseJob job;
    private Thread[] threads;
    
    public void SetTest(IDatabaseRun test)
    {
        this.test = test;
    }
    
    public void SetJob(IDatabaseJob job)
    {
        this.job = job;
    }
    
    public void RunTest()
    {
        this.threads = new Thread[test.GetNumberOfThreads()];
        
        for(int iter=0; iter<test.GetNumberOfThreads(); iter++)
        {                                                    
            this.threads[iter] = new Thread((Runnable)this.job);
                
            this.job.SetThreadID(iter);            
            this.job.SetLogFile(String.format("%s-thread-%d.log", this.job.getClass().toString(), iter));
            this.job.SetConfiguration(test);
                
            this.threads[iter].start();  
            
            this.job = this.job.Clone();
        }
        
        for(int iter=0; iter<test.GetNumberOfThreads(); iter++)
        {
            try 
            {
                this.threads[iter].join();
            } 
            
            catch (InterruptedException ex) 
            {
                Logger.getLogger(DBTestRunner.class.getName()).log(Level.SEVERE, null, ex);
            }
        }                 
    }
}
