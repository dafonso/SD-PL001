/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server.statics;

import common.Message;
import server.EventLog;
import server.pool.RemoteNode;

/**
 *
 * @author Emanuel
 */
public interface RemoteBullyPassiveNode extends RemoteNode {

    // Bully
    public void election(long id);

    public void coordinator(long id);
    
    // Passive
    public void executeRequest(EventLog log);
    
    public void getUpdated(EventLog lastLog);
    
}
