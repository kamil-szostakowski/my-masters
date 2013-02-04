package nosql;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author test
 */

public class PostgresDB extends BaseTest
{
    private Connection connection = null;
    private Statement statement = null;
    
    //private String url = "jdbc:postgresql://localhost/avilar";
    //private String user = "avilar";
    //private String password = "x11y85aa";
    
    private String url = "jdbc:postgresql://localhost/testdb";
    private String user = "postgres";
    private String password = "kamil123";    
    
    private String query = "INSERT INTO document (id, title, content) VALUES(%d, '%s', '%s')";    
    
    protected void InitTest()
    {
        super.InitTest();
        
        try 
        {
            this.connection = DriverManager.getConnection(this.url, this.user, this.password);
            this.statement = this.connection.createStatement();        
            
            this.WriteLog("PostgresDB: connection established");
        } 
        
        catch (SQLException ex) 
        {
            Logger.getLogger(PostgresDB.class.getName()).log(Level.SEVERE, null, ex);
        }
    }      
    
    public void PerformSelectOperation()
    {
        
    }
    
    public void PerformInsertOperation(int identifier)
    {
        try 
        {
            String title = String.format("Title %d", identifier);
            String content = this.test.GetContent(identifier);        
            
            this.statement.execute(String.format(this.query, identifier, title, content));
        } 
        
        catch (SQLException | IOException ex) 
        {
            Logger.getLogger(PostgresDB.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}