package basejobs;

import runs.DBTestRun;
import runs.IDatabaseRun;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 *
 * @author test
 */
public interface IDatabaseJob 
{
    public void RunTest() throws FileNotFoundException, IOException;
    
    public void SetConfiguration(IDatabaseRun config);
    
    public void SetThreadID(int identifier);   
    
    public void SetLogFile(String filename);
    
    public void Connect();
    
    public void Disconnect();            
    
    public int PerformSelectOperation(int identifier);
    
    public void PerformInsertOperation(int identifier);   
    
    public IDatabaseJob Clone();
}