/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server.statics;

import common.Event;
import java.rmi.AlreadyBoundException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Timer;
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
    private Timer mcTimer;

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
    public void election(long id) throws RemoteException {
        if (state == ServerState.upAndRunning) {
            this.holdElection();
        }
    }

    @Override
    public void coordinator(long id) throws RemoteException {
        for (NodeState server : pool) {
            if (server.getId() == id) {
                master = server;
                System.out.println("new master is " + server.getKey());
            }
        }
    }

    @Override
    public void alive() throws RemoteException {
        /*if (state != ServerState.upAndRunning || state != ServerState.inElection) {
         throw new RemoteException("Server out of date!");
         }*/
    }

    public void holdElection() {
        state = ServerState.inElection;
        int count = 0;
        for (NodeState server : pool) {
            if (server.getId() > self.getId()) {
                try {
                    count++;
                    Registry registry = LocateRegistry.getRegistry(server.getHostname(), server.getPortNumber());
                    RemoteBullyPassiveNode stub = (RemoteBullyPassiveNode) registry.lookup(server.getKey());
                    stub.election(self.getId());
                } catch (RemoteException | NotBoundException e) {
                    count--;
                    System.err.println(e);
                }
            }
        }
        if (count == 0) {
            for (NodeState server : pool) {
                try {
                    Registry registry = LocateRegistry.getRegistry(server.getHostname(), server.getPortNumber());
                    RemoteBullyPassiveNode stub = (RemoteBullyPassiveNode) registry.lookup(server.getKey());
                    stub.coordinator(self.getId());
                    if (mcTimer != null) {
                        mcTimer.cancel();
                    }
                    mcTimer = null;
                } catch (RemoteException | NotBoundException e) {
                    System.err.println(e);
                }
            }
        }
        state = ServerState.upAndRunning;
    }

    @Override
    public void executeRequest(EventLog log) throws RemoteException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void getUpdated(EventLog lastLog) throws RemoteException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public NodeState getMasterServer() throws RemoteException {
        return this.master;
    }

    @Override
    public Event create(Event event) throws RemoteException {
        try {
            Context context = new Context();
            Date now = new Date();
            event.setCreatedAt(now);
            event.setModifiedAt(now);
            event.setId(0);
            context.getEventDao().create(event);
            context.close();
            return event;
        } catch (SQLException e) {
            System.err.println(e);
            return null;
        }
    }

    @Override
    public boolean update(Event event) throws RemoteException {
        try {
            Context context = new Context();
            event.setModifiedAt(new Date());
            int result = context.getEventDao().update(event);
            context.close();
            return result == 1;
        } catch (SQLException e) {
            System.err.println(e);
            return false;
        }
    }

    @Override
    public boolean delete(Event event) throws RemoteException {
        try {
            Context context = new Context();
            int result = context.getEventDao().delete(event);
            context.close();
            return result == 1;
        } catch (SQLException e) {
            System.err.println(e);
            return false;
        }
    }

    @Override
    public List<Event> find(Event event) throws RemoteException {
        try {
            Context context = new Context();
            ArrayList<Event> result;
            if (event == null) {
                result = new ArrayList(context.getEventDao().queryForAll());
            } else {
                result = new ArrayList(context.getEventDao().queryForMatching(event));
            }
            context.close();
            return result;
        } catch (SQLException e) {
            System.err.println(e);
            return null;
        }
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

    public void setMasterCheckup() {
        mcTimer = new Timer();
        mcTimer.schedule(new MasterCheckup(), 10 * 1000, 10 * 1000);
    }

    private class MasterCheckup extends TimerTask {

        private MasterCheckup() {
        }

        @Override
        public void run() {
            if (master != null) {
                try {
                    System.out.println("Master Checkup : " + master.getKey());
                    Registry registry = LocateRegistry.getRegistry(master.getHostname(), master.getPortNumber());
                    RemoteBullyPassiveNode stub = (RemoteBullyPassiveNode) registry.lookup(master.getKey());
                    stub.alive();
                } catch (Exception ex) {
                    holdElection();
                }
            }
        }
    }
}