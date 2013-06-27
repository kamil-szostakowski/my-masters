/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jobs.couchdb.street;

/**
 *
 * @author kamil
 */
public class CouchdbCredentials 
{
    private String host;
    private String dbname;
    private Long port;
    
    public CouchdbCredentials(String host, Long port, String dbname)
    {
        this.host = host;
        this.port = port;
        this.dbname = dbname;
    }
    
    public String GetHost()
    {
        return this.host;
    }
    
    public String GetDbname()
    {
        return this.dbname;
    }
    
    public Long GetPort()
    {
        return this.port;
    }
    
    @Override
    public String toString()
    {
        return String.format("%s %d %s", this.host, this.port, this.dbname);
    }
}
