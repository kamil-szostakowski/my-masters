/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package nosql;

import couchdbjobs.CouchdbPrepareJob;
import couchdbjobs.CouchdbSchemaJob;
import basejobs.IDatabaseJob;
import postgresjobs.PostgresTestJob;
import couchdbjobs.CouchdbTestJob;
import mongojobs.MongodbPrepareJob;
import mongojobs.MongodbSchemaJob;
import mongojobs.MongodbTestJob;
import postgresjobs.PostgresPrepareJob;
import postgresjobs.PostgresSchemaJob;

/**
 *
 * @author test
 */
public class TestFactory 
{
    private static String DbType;
    
    public static void SetDbType(String db)
    {
        TestFactory.DbType = db;
    }
    
    public static IDatabaseJob GetTestJob()
    {
        IDatabaseJob result = null;
        
        if(TestFactory.DbType.equals("couchdb")) { result = new CouchdbTestJob(); }        
        if(TestFactory.DbType.equals("mongodb")) { result = new MongodbTestJob(); }        
        if(TestFactory.DbType.equals("postgresql")) { result = new PostgresTestJob(); }   
        
        return result;
    }
    
    public static IDatabaseJob GetPrepareJob()
    {
        IDatabaseJob result = null;
        
        if(TestFactory.DbType.equals("couchdb")) { result = new CouchdbPrepareJob(); }        
        if(TestFactory.DbType.equals("mongodb")) { result = new MongodbPrepareJob(); }        
        if(TestFactory.DbType.equals("postgresql")) { result = new PostgresPrepareJob(); }   
        
        return result;
    }    
    
    public static IDatabaseJob GetSchemaJob()
    {
        IDatabaseJob result = null;
        
        if(TestFactory.DbType.equals("couchdb")) { result = new CouchdbSchemaJob(); }        
        if(TestFactory.DbType.equals("mongodb")) { result = new MongodbSchemaJob(); }        
        if(TestFactory.DbType.equals("postgresql")) { result = new PostgresSchemaJob(); }   
        
        return result;
    }       
}