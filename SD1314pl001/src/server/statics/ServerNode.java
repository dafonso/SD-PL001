/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server.statics;

import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.Where;
import common.Event;
import java.io.Serializable;
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
public class ServerNode implements RemoteBullyPassiveNode, Serializable {

    private ServerState state;
    private NodeProperties self;
    private NodeState master;
    private List<NodeState> pool;
    //timer to check if master is alive
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

    //TODO: isto é para manter public?
    @Override
    public void coordinator(long id) throws RemoteException {
        for (NodeState server : pool) {
            if (server.getId() == id) {
                master = server;
                this.setMasterCheckup();
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
        //no other server has a higher ID -> this server will be the new master
        if (count == 0) {
            for (NodeState server : pool) {
                try {
                    Registry registry = LocateRegistry.getRegistry(server.getHostname(), server.getPortNumber());
                    RemoteBullyPassiveNode stub = (RemoteBullyPassiveNode) registry.lookup(server.getKey());
                    stub.coordinator(self.getId());
                } catch (RemoteException | NotBoundException e) {
                    System.err.println(e);
                }
            }
            if (mcTimer != null) {
                mcTimer.cancel();
            }
            mcTimer = null;
            master = null;
            System.out.println(self.getKey() + " says: Im the new master");
        }
        state = ServerState.upAndRunning;
    }
    // executar log
    @Override
    public void executeLog(EventLog log) throws RemoteException {
        switch (log.getOperationType()){
	    case create:
		this.create(log.getEventFromLog());
		break;
	    case update:
		this.update(log.getEventFromLog());
		break;
	    case delete:
		this.delete(log.getEventFromLog().getId());
		break;
	}
	
    }

    // master devolve lista de logs
    @Override
    public List<EventLog> getLogsToUpdate(Date lastLogDate) throws RemoteException {
	List<EventLog> result = new ArrayList<>();        
	try {
            Context context = new Context();
            QueryBuilder<EventLog, String> queryBuilder = context.getEventLogDao().queryBuilder();
	    Where<EventLog, String> where = queryBuilder.where();
	    result = where.gt("logCreatedAt", lastLogDate).query();
            
        } catch (SQLException e) {
            System.err.println(e);
        }
	return result;
    }
        /**
     * Method that updates the database, based on the logs and the master's logs
     */
    public void updateDataBase() {

	String masterHostName = null;
	int masterPortNumber = 0;
	String masterKey = null;
        for (NodeState server : pool) {
            try {
                Registry registry = LocateRegistry.getRegistry(server.getHostname(), server.getPortNumber());
                RemoteBullyPassiveNode stub = (RemoteBullyPassiveNode) registry.lookup(server.getKey());
                
		NodeProperties masterProp = stub.getMasterServer();
		masterKey = masterProp.getKey();
		masterHostName = masterProp.getHostname();
		masterPortNumber = masterProp.getPortNumber();
                break;
            } catch (RemoteException | NotBoundException e) {
                System.err.println(e);
            }
        }
	
	if (masterHostName==null || masterPortNumber==0 || masterKey==null){
            masterHostName = self.getHostname();
	    masterPortNumber = self.getPortNumber();
	    masterKey = self.getKey();
	}
        try {
	    Date latestLogDate = this.getLatestLogDate();
            Registry registry = LocateRegistry.getRegistry(masterHostName, masterPortNumber);
            RemoteBullyPassiveNode stub = (RemoteBullyPassiveNode) registry.lookup(masterKey);
            List<EventLog> logsList = stub.getLogsToUpdate(latestLogDate);
	    
	    for (EventLog eventLog : logsList)
	    {
		executeLog(eventLog);
	    }
	    
        } catch (NullPointerException npe) {
            System.err.println("Nenhum servidor ligado");
	    // não precisa de fazer nenhum update
        } catch (RemoteException | NotBoundException e) {
            System.err.println(e);
        }

        
        // block master database from accepting changes
        // get new logs
        //Date latestLogDate = this.getLatestLogDate();
        //master.getObject();
        // update db from new logs
        // unblock master database
    }

    @Override
    public NodeProperties getMasterServer() throws RemoteException {
        if (this.master == null) {
            return this.self;
        } else {
            return this.master;
        }
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

            EventLog eventLog = new EventLog();
            eventLog.setEventToLog(event);
            eventLog.setOperationType(EventLog.Operation.create);
            context.getEventLogDao().create(eventLog);

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
            int resultEvent = context.getEventDao().update(event);

            EventLog eventLog = new EventLog();
            eventLog.setEventToLog(event);
            eventLog.setOperationType(EventLog.Operation.update);
            int resultLog = context.getEventLogDao().create(eventLog);

            context.close();

            return (resultEvent + resultLog) == 2;
        } catch (SQLException e) {
            System.err.println(e);
            return false;
        }
    }

    @Override
    public boolean delete(int id) throws RemoteException {
        try {
            Context context = new Context();
            int resultEvent = context.getEventDao().deleteById(Integer.toString(id));

            EventLog eventLog = new EventLog();
            eventLog.setEventId(id);
            eventLog.setOperationType(EventLog.Operation.delete);
            int resultLog = context.getEventLogDao().create(eventLog);

            context.close();
            return (resultEvent + resultLog) == 2;
        } catch (SQLException e) {
            System.err.println(e);
            return false;
        }
    }

    @Override
    public ArrayList<Event> find(Event event) throws RemoteException {
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
        RemoteBullyPassiveNode stub = null;
        try {
            Registry registry = LocateRegistry.getRegistry(self.getPortNumber());
            stub = (RemoteBullyPassiveNode) UnicastRemoteObject.exportObject(this, 0);
            registry.bind(self.getKey(), stub);
            System.err.println("Server Ready");
        } catch (AlreadyBoundException e) {
            try {
                Registry registry = LocateRegistry.getRegistry(self.getPortNumber());
                registry.rebind(self.getKey(), stub);
                System.err.println("Server Ready");
            } catch (RemoteException ex) {
                System.err.println("Server exception : " + ex);
            }
        } catch (RemoteException e) {
            System.err.println("Server exception : " + e);
        }
    }

    public void setMasterCheckup() {
        if (mcTimer == null) {
            mcTimer = new Timer();
            mcTimer.schedule(new MasterCheckup(), 10 * 1000, 10 * 1000);
        }
    }

    private Date getLatestLogDate() {
        try {
            Context context = new Context();
            EventLog latestLog = context.getEventLogDao().queryBuilder().
                    orderBy("logCreatedAt", false).queryForFirst();
            return latestLog != null ? latestLog.getLogCreatedAt() : new Date(0);
        } catch (SQLException e) {
            System.err.println(e);
            return null;
        }

    }

    private class MasterCheckup extends TimerTask {

        private MasterCheckup() {
        }

        //task that checks it master is alive. if not, it holds an election
        @Override
        public void run() {
            try {
                System.out.println("Master Checkup : " + master.getKey());
                Registry registry = LocateRegistry.getRegistry(master.getHostname(), master.getPortNumber());
                RemoteBullyPassiveNode stub = (RemoteBullyPassiveNode) registry.lookup(master.getKey());
                stub.alive();
            } catch (NotBoundException | RemoteException ex) {
                holdElection();
            }
        }
    }

}
