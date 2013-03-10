/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package postgresjobs;

import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import basejobs.IDatabaseJob;

/**
 *
 * @author test
 */
public class PostgresSchemaJob extends PostgresBaseJob
{
    private String schema = "CREATE TABLE document(id integer NOT NULL, threadid integer, title character varying(200), content text)";
    
    public void PerformInsertOperation(int identifier)   
    {
        try 
        {                                
            this.statement.execute(schema);                                    
        } 
        
        catch (SQLException ex) 
        {
            Logger.getLogger(PostgresTestJob.class.getName()).log(Level.SEVERE, null, ex);
        }        
    }
    
    public IDatabaseJob Clone()
    {
        return new PostgresSchemaJob();
    }    
}