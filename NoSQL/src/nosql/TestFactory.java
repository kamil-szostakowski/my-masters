/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package nosql;

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
    
    public static ITestableDB GetInstance()
    {
        ITestableDB result = null;
        
        if(TestFactory.DbType.equals("couchdb")) { result = new CouchDB(); }        
        if(TestFactory.DbType.equals("mongodb")) { result = new MongoDB(); }        
        if(TestFactory.DbType.equals("postgresql")) { result = new PostgresDB(); }   
        
        return result;
    }
}
