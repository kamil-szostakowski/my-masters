/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package nosql;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author test
 */
public class NoSQL {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) 
    {
        if(args.length < 2)
        {
            System.out.println("help: --test all|couchdb|mongodb|postgresql");
        }
        
        if(!"--test".equals(args[0]))
        {
            System.out.println("help: --test all|couchdb|mongodb|postgresql");
        }

        System.out.println("DBTest: started");
            
        TestFactory.SetDbType(args[1]);
            
        DBTestRunner testRunner = new DBTestRunner();
           
        testRunner.SetTest(new DBTest());
        testRunner.RunTest();                                    

        System.out.println("DBTest: finished");
    }
}