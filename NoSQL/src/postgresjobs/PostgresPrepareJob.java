/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package postgresjobs;

import basejobs.IDatabaseJob;

/**
 *
 * @author test
 */
public class PostgresPrepareJob extends PostgresTestJob
{    
    public IDatabaseJob Clone()
    {
        return new PostgresPrepareJob();
    }
}