/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package nosql;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author test
 */
public class BaseTest implements Runnable, ITestableDB
{
    private int threadID;
    private String logFilename;
    protected DBTest test;
    
    private FileWriter fstream;
    private BufferedWriter output;  
    
    private long testStart;    
    
    // Method initializes test before run.    
    protected void InitTest()
    {
        try 
        {
            this.fstream = new FileWriter(this.logFilename);            
            this.output = new BufferedWriter(fstream);            
        } 
        
        catch (IOException ex) 
        {
            Logger.getLogger(BaseTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    // Method cleans up ater test run.
    protected void CleanupTest()
    {
        long testFinish = System.currentTimeMillis();
        long testExecutionTime = testFinish-this.testStart;   
        
        this.WriteLog(String.format("Test finished %d miliseconds", testExecutionTime));
        
        try 
        {
            this.output.close();
        } 
        
        catch (IOException ex) 
        {
            Logger.getLogger(BaseTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    protected void WriteLog(String text)
    {
        try 
        {
            this.output.write(String.format("Thread %d: %s\n", this.threadID, text));
            this.output.newLine();
        } 
        
        catch (IOException ex) 
        {
            Logger.getLogger(BaseTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void SetConfiguration(DBTest config)
    {
        this.test = config;
    }
    
    public void SetLogFile(String filename)
    {
        this.logFilename = filename;
    }
    
    public void SetThreadID(int identifier)
    {
        this.threadID = identifier;
    }  
    
    public void RunTest() throws FileNotFoundException, IOException
    {
        this.InitTest();
        
        this.testStart = System.currentTimeMillis();        
        
        this.WriteLog("Test started");
        
        for(int iter=0; iter<test.GetRepeatCount(); iter++)
        {
            long start = System.currentTimeMillis();
            
            this.PerformInsertOperation(iter);
            
            long finish = System.currentTimeMillis();
            long executionTime = finish-start;
            
            this.WriteLog(String.format("Document %d inserted in %d miliseconds",iter, executionTime));
        }
        
        this.CleanupTest();
    }
    
    public void PerformSelectOperation()
    {
        
    }
    
    public void PerformInsertOperation(int identifier)
    {
        
    }
    
    @Override
    public void run() 
    {
        try 
        {
            this.RunTest();
        } 
        
        catch (FileNotFoundException ex) 
        {
            Logger.getLogger(CouchDB.class.getName()).log(Level.SEVERE, null, ex);
        } 
        
        catch (IOException ex) 
        {
            Logger.getLogger(CouchDB.class.getName()).log(Level.SEVERE, null, ex);
        }
    }    
}