/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package common;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Emanuel
 */
public interface RemoteClientProtocol extends Remote{
        // Client Remote Methods    
    public Event create(Event event) throws RemoteException;
    public Event createNoMulticast(Event event) throws RemoteException;
    
    public boolean update(Event event) throws RemoteException;
    public boolean updateNoMulticast(Event event) throws RemoteException;
    
    public boolean delete(int id) throws RemoteException;
    public boolean deleteNoMulticast(int id) throws RemoteException;
    
    public ArrayList<Event> find(Event event) throws RemoteException;
}
