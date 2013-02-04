/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package nosql;

/**
 *
 * @author test
 */
public class DBTestRunner 
{
    private DBTest test;    
    
    public void SetTest(DBTest test)
    {
        this.test = test;
    }
    
    public void RunTest()
    {
        for(int iter=0; iter<test.GetNumberOfThreads(); iter++)
        {      
            ITestableDB database = TestFactory.GetInstance();
            
            Thread thread = new Thread((Runnable)database);
            
            database.SetThreadID(iter);            
            database.SetLogFile(String.format("%s-thread-%d.log", database.getClass().toString(), iter));
            database.SetConfiguration(test);
            
            thread.start();
        }
    }
}
