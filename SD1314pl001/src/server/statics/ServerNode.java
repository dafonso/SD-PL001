/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server.statics;

import common.Agenda;
import common.Event;
import java.net.ServerSocket;
import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimerTask;
import server.Context;
import server.EventLog;
import server.pool.NodeProperties;
import server.pool.NodeState;

/**
 *
 * @author Emanuel
 */
public class ServerNode implements RemoteBullyPassiveNode {

    private ServerState state;
    private NodeProperties self;
    private NodeState master;
    private List<NodeState> pool;
    /*private final String host;
     private int port;
     private ServerSocket serverSocket;
     private final Agenda agenda;*/

    public ServerNode() {
        state = ServerState.outOfDate;
        pool = new ArrayList<>();
    }

    public NodeProperties getSelf() {
        return self;
    }

    public void setSelf(NodeProperties self) {
        this.self = self;
    }

    public List<NodeState> getPool() {
        return pool;
    }

    public void setPool(List<NodeState> pool) {
        this.pool = pool;
    }

    public NodeState getMaster() {
        return master;
    }

    public void setMaster(NodeState master) {
        this.master = master;
    }

    @Override
    public void election(long id) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void coordinator(long id) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void alive() {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void holdElection() {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void executeRequest(EventLog log) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void getUpdated(EventLog lastLog) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public NodeState getMasterServer() {
        return this.master;
    }

    @Override
    public Event create(Event event) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean update(Event event) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean delete(Event event) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<Event> find(Event event) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private Event addEvent(Event event) throws SQLException {
        Context context = new Context();
        Date now = new Date();
        event.setCreatedAt(now);
        event.setModifiedAt(now);
        event.setId(0);
        context.getEventDao().create(event);
        context.close();
        return event;
    }

    private Event updateEvent(Event event) throws SQLException {
        Context context = new Context();
        event.setModifiedAt(new Date());
        context.getEventDao().update(event);
        context.close();
        return event;
    }

    private boolean deleteEvent(Event event) throws SQLException {
        Context context = new Context();
        int result = context.getEventDao().delete(event);
        context.close();
        return result == 1;
    }

    private ArrayList<Event> findEvents(Event event) throws SQLException {
        Context context = new Context();
        ArrayList<Event> result = null;
        if (event == null) {
            result = new ArrayList(context.getEventDao().queryForAll());
        } else {
            result = new ArrayList(context.getEventDao().queryForMatching(event));
        }
        context.close();
        return result;
    }

    public void registerRMI() {
        try { //Fazer o registo para o porto desejado 
            LocateRegistry.createRegistry(self.getPortNumber());
            System.out.println("RMI registry ready.");
        } catch (RemoteException e) {
            System.out.println("Exception starting RMI registry: " + e);
        }
        try {
            RemoteBullyPassiveNode stub = (RemoteBullyPassiveNode) UnicastRemoteObject.exportObject(this, 0);
            Registry registry = LocateRegistry.getRegistry(self.getPortNumber());
            registry.bind(self.getKey(), stub);
            System.err.println("Server Ready");
        } catch (AlreadyBoundException | RemoteException e) {
            System.err.println("Server exception : " + e);
        }
    }

    private class MasterCheckup extends TimerTask {

        @Override
        public void run() {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

    }
}
