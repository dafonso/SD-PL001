/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server.statics;

import common.RemoteClientProtocol;
import java.rmi.RemoteException;
import java.util.Date;
import java.util.List;
import server.EventLog;
import server.pool.NodeProperties;
import server.pool.NodeState;
import server.pool.RemoteNode;

/**
 *
 * @author Emanuel
 */
public interface RemoteBullyPassiveNode extends RemoteNode,RemoteClientProtocol {

    // Bully
    public void election(long id) throws RemoteException;

    public void coordinator(long id) throws RemoteException;
    
    // Passive
    public void executeLog(EventLog log) throws RemoteException;
    
    public List<EventLog> getLogsToUpdate(Date lastLogDate) throws RemoteException;
    
    public NodeProperties getMasterServer() throws RemoteException;
    

    
    
}
