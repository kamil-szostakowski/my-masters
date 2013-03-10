/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package basejobs;

import couchdbjobs.CouchdbTestJob;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import runs.DBTestRun;
import runs.IDatabaseRun;

/**
 *
 * @author test
 */
public abstract class BaseJob implements Runnable, IDatabaseJob
{
    protected int threadID;
    private String logFilename;
    protected IDatabaseRun test;
    
    private FileWriter fstream;
    private BufferedWriter output;  
    
    private long testStart;    
    
    private Random randomizer;    
    
    // Method initializes test before run.    
    public void InitTest()
    {
        try 
        {
            this.fstream = new FileWriter(this.logFilename);            
            this.output = new BufferedWriter(fstream);   
            this.randomizer = new Random(System.nanoTime());
            
            this.Connect();
        } 
        
        catch (IOException ex) 
        {
            Logger.getLogger(BaseJob.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    // Method cleans up ater test run.
    public void CleanupTest()
    {
        long testFinish = System.currentTimeMillis();
        long testExecutionTime = testFinish-this.testStart;   
        
        this.WriteLog(String.format("Test finished %d miliseconds", testExecutionTime));
        
        try 
        {
            this.output.close();
            this.Disconnect();
        } 
        
        catch (IOException ex) 
        {
            Logger.getLogger(BaseJob.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    protected void WriteLog(String text)
    {
        if(this.output == null)
        {
            return;
        }
        
        try 
        {
            this.output.write(String.format("Thread %d: %s\n", this.threadID, text));
            this.output.newLine();
        } 
        
        catch (IOException ex) 
        {
            Logger.getLogger(BaseJob.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    protected int[] GetDocumentListForIter(int iter)
    {
        int[] docs = new int[this.test.GetSelectDocumentCount()];
            
        for(int document=0; document<docs.length; document++)
        {
            docs[document] = (int) Math.abs(this.randomizer.nextInt(1000));                    
        }   
        
        return docs;
    }
    
    public void SetConfiguration(IDatabaseRun config)
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
        System.out.println(String.format("Starting thread name: %s id: %d", this.getClass().toString(), this.threadID));                 
        
        this.InitTest();
        
        this.testStart = System.currentTimeMillis();        
        
        this.WriteLog("Test started");
        
        for(int iter=0; iter<test.GetRepeatCount(); iter++)
        {
            long start = System.currentTimeMillis();
            String operationType;
            String extra;
            
            int rand = Math.abs(this.randomizer.nextInt(100));
            
            if(rand < this.test.GetInsertRate())
            {
                this.PerformInsertOperation(iter);
                operationType = "insert";
                extra = "";
            }
            
            else
            {
                int selected = this.PerformSelectOperation(iter);
                operationType = "select";
                extra = String.format("selected %d documents", selected);
            }                        
            
            long finish = System.currentTimeMillis();
            long executionTime = finish-start;
            
            this.WriteLog(String.format("Operation[%s] %d finished in %d miliseconds %s",operationType, iter, executionTime, extra));
        }                
        
        System.out.println(String.format("Thread name: %s id: %d finished", this.getClass().toString(), this.threadID));
        
        this.CleanupTest();
    }
    
    public int PerformSelectOperation(int identifier)
    {        
        return 0;
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
            Logger.getLogger(CouchdbTestJob.class.getName()).log(Level.SEVERE, null, ex);
        } 
        
        catch (IOException ex) 
        {
            Logger.getLogger(CouchdbTestJob.class.getName()).log(Level.SEVERE, null, ex);
        }
    }    
}