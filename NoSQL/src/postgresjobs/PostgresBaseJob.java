/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package postgresjobs;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import basejobs.BaseJob;
import tools.Configuration;

/**
 *
 * @author test
 */
public abstract class PostgresBaseJob extends BaseJob
{
    protected Connection connection = null;
    protected Statement statement = null;   
    
    private String url = (String) Configuration.GetParam("postgresql:url"); //"jdbc:postgresql://localhost/testdb";
    private String user = (String) Configuration.GetParam("postgresql:user"); //"postgres";
    private String password = (String) Configuration.GetParam("postgresql:password"); //"kamil123";     
    
    // Metoda dokonująca zestawienia połączenia z bazą danych
    
    @Override
    public void Connect() 
    {
        try 
        {
            this.connection = DriverManager.getConnection(this.url, this.user, this.password);
            this.statement = this.connection.createStatement();        
            
            this.WriteLog("PostgresDB: connection established");
        } 
        
        catch (SQLException ex) 
        {
            Logger.getLogger(PostgresTestJob.class.getName()).log(Level.SEVERE, null, ex);
        }    
    }

    // Metoda kończąca połączenie z bazą danych
    
    @Override
    public void Disconnect() 
    {
        try 
        {
            this.connection.close();
        } 
        
        catch (SQLException ex) 
        {
            Logger.getLogger(PostgresTestJob.class.getName()).log(Level.SEVERE, null, ex);
        }    
    }    
}