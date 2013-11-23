/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server.statics;

import common.RemoteClientProtocol;
import java.rmi.RemoteException;
import server.EventLog;
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
    public void executeRequest(EventLog log) throws RemoteException;
    
    public void getUpdated(EventLog lastLog) throws RemoteException;
    
    public NodeState getMasterServer() throws RemoteException;
    

    
    
}
