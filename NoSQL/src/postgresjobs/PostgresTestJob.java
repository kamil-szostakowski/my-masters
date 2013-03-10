package postgresjobs;

import basejobs.BaseJob;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Time;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import basejobs.IDatabaseJob;
import tools.Configuration;
import tools.StringTools;

/**
 *
 * @author test
 */

public class PostgresTestJob extends PostgresBaseJob
{    
    //private String url = "jdbc:postgresql://localhost/avilar";
    //private String user = "avilar";
    //private String password = "x11y85aa";       
    
    private String query = "INSERT INTO document (id, threadid, title, content) VALUES(%d, %d, '%s', '%s')";    
    private String select = "SELECT * FROM document where id IN (%s)";           
    
    public int PerformSelectOperation(int identifier)
    {
        try
        {                                               
            this.statement.execute(String.format(this.select, StringTools.Join(this.GetDocumentListForIter(identifier), ",", false)));                                                  
        }
        
        catch (SQLException ex) 
        {
            Logger.getLogger(PostgresTestJob.class.getName()).log(Level.SEVERE, null, ex);
        }        
        
        return 0;
    }
    
    public void PerformInsertOperation(int identifier)
    {
        try 
        {
            String title = String.format("Title %d", identifier);
            String content = this.test.GetContent(identifier);        
            
            this.statement.execute(String.format(this.query, identifier, this.threadID, title, content));
        } 
        
        catch (SQLException | IOException ex) 
        {
            Logger.getLogger(PostgresTestJob.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public IDatabaseJob Clone()
    {
        return new PostgresTestJob();
    }
}