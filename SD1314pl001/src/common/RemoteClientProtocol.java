/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package common;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

/**
 *
 * @author Emanuel
 */
public interface RemoteClientProtocol extends Remote{
        // Client Remote Methods    
    public Event create(Event event) throws RemoteException;
    
    public boolean update(Event event) throws RemoteException;
    
    public boolean delete(Event event) throws RemoteException;
    
    public List<Event> find(Event event) throws RemoteException;
}
