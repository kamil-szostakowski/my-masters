package nosql;

import java.io.FileNotFoundException;
import java.io.IOException;

/**
 *
 * @author test
 */
public interface ITestableDB 
{
    public void RunTest() throws FileNotFoundException, IOException;
    
    public void SetConfiguration(DBTest config);
    
    public void SetThreadID(int identifier);   
    
    public void SetLogFile(String filename);
}