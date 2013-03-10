/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package nosql;

import runs.DBPrepareRun;
import runs.DBTestRun;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;
import basejobs.IDatabaseJob;
import runs.DBSchemaRun;
import runs.IDatabaseRun;
import tools.Configuration;

/**
 *
 * @author test
 */
public class NoSQL 
{        
    private static String HelpMSG = "help: couchdb|mongodb|postgresql --with-test --with-schema --with-prepare-data";        
    
    private static LinkedList<IDatabaseJob> jobs = new LinkedList<>();
    private static LinkedList<IDatabaseRun> runs = new LinkedList<>();
    
    // Metoda przygotowuje zadania do wykonania
    
    private static void PrepareJobs(String[] args)
    {        
        TestFactory.SetDbType(args[0]);
        
        boolean withTest = false;
        boolean withSchema = false;
        boolean withPrepareData = false;
        
        for(int iter=1; iter<args.length; iter++)
        {
            if("--with-test".equals(args[iter])) { withTest = true; }            
            if("--with-schema".equals(args[iter])) { withSchema = true; }            
            if("--with-prepare-data".equals(args[iter])) { withPrepareData = true; }
        } 
        
        if(withSchema)
        {
            NoSQL.jobs.add(TestFactory.GetSchemaJob());
            NoSQL.runs.add(new DBSchemaRun());             
        }
        
        if(withPrepareData)
        {
            NoSQL.jobs.add(TestFactory.GetPrepareJob());
            NoSQL.runs.add(new DBPrepareRun());
        }
                
        if(withTest)
        {                
            NoSQL.jobs.add(TestFactory.GetTestJob());
            NoSQL.runs.add(new DBTestRun());
        }
    }
    
    /**
     * @param args the command line arguments
     */
    
    public static void main(String[] args) 
    {      
        if(args.length < 1) 
        { 
            System.out.println(NoSQL.HelpMSG); 
        }
        
        else
        {
            Configuration.Prepare();
            
            NoSQL.PrepareJobs(args);                        
            
            System.out.println("DBJob: started");                        
            
            DBTestRunner testRunner = new DBTestRunner();
           
            for(int iter=0; iter<NoSQL.jobs.size(); iter++)
            {
                testRunner.SetJob(NoSQL.jobs.get(iter));
                testRunner.SetTest(NoSQL.runs.get(iter));
                testRunner.RunTest();
            }

            System.out.println("DBJob: finished");            
        }        
    }
}