package nosql;

import basejobs.IDatabaseJob;
import java.util.logging.Level;
import java.util.logging.Logger;
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
        try
        {
            this.threads = new Thread[test.GetNumberOfThreads()];
        
            for(int iter=0; iter<test.GetNumberOfThreads(); iter++)
            {                                                    
                this.threads[iter] = new Thread((Runnable)this.job);
            
                this.job.SetThreadID(iter);            
                this.job.SetLogFile(String.format("%s-thread-%d.log", this.job.GetName(), iter));
                this.job.SetConfiguration(test);
                
                this.threads[iter].start();  
            
                this.job = this.job.Clone();
            }
        }
        
        catch (Exception ex)
        {
              Logger.getLogger(DBTestRunner.class.getName()).log(Level.SEVERE, null, ex);      
        }
    }
}
