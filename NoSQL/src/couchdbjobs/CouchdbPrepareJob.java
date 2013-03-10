package couchdbjobs;

import basejobs.IDatabaseJob;

/**
 *
 * @author test
 */
public class CouchdbPrepareJob extends CouchdbTestJob
{   
    public IDatabaseJob Clone()
    {
        return new CouchdbPrepareJob();
    }
}