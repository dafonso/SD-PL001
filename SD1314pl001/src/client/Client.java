/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package client;

import common.Agenda;
import common.Event;
import common.properties.CommonProps;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import server.statics.RemoteBullyPassiveNode;


/**
 *
 * @author João
 */
public class Client {

    //final private Socket socket;
    //final private ObjectOutputStream out;
    //final private ObjectInputStream in;
    private Agenda agenda;
    //private String host;
    //private int port;
    private String[][] serverPool;
    private RemoteBullyPassiveNode clientStub;

    public Client()  {
        this.serverPool = CommonProps.getServerPool();
        //this.host = hostname;
        //this.port = portNumber;
//        this.socket = new Socket(hostname, portNumber);
//        this.out = new ObjectOutputStream(this.socket.getOutputStream());
        //      this.in = new ObjectInputStream(this.socket.getInputStream());
        this.agenda = new Agenda();
        System.out.println("A iniciar o Cliente...");// Vamos tentar aceder ao Servidor de Registos para recolher a interface
        try {
            connect2MasterServer();
        } catch (NoSuchServerOn e) {
            System.out.println("Falhou o arranque do Cliente");
            System.out.println("Não existe nenhum servidor ligado!!");
            System.exit(0);
        }
    }

    private void connect2MasterServer() throws NoSuchServerOn {
        int i = 0;
        while (i < serverPool.length) {
            String getMasterName;
            Registry registry;
            try {
                registry = LocateRegistry.getRegistry(serverPool[i][0]);
                RemoteBullyPassiveNode stub = (RemoteBullyPassiveNode) registry.lookup(serverPool[i][3]);
                getMasterName = stub.getMasterServer().getKey();
                this.clientStub = (RemoteBullyPassiveNode) registry.lookup(getMasterName);
            } catch (RemoteException | NotBoundException ex) {
                System.out.println("Não é possível aceder ao servidor");
                i++;
            }
        }
        throw new NoSuchServerOn();
    }

    public void addEvent(Event e) throws RemoteException {
        clientStub.create(e);
    }

    public void updateEvent(Event e) throws RemoteException {
       if(clientStub.update(e))
           System.out.println("O evento foi actualizado com sucesso!");
        else
           System.out.println("Ocorreu um erro e o evento não foi actualizado!"); 
    }

    public void deleteEvent(int id) throws RemoteException {
        if(clientStub.delete(id))
           System.out.println("O evento foi apagado com sucesso!");
        else
           System.out.println("Ocorreu um erro e o evento não foi apagado!"); 
    }

    public List<Event> findEvent(Event e) throws RemoteException {
        return clientStub.find(e);
    }

    public Event getEvent(int id) {
        Event event = null;
        for (Event e : agenda.getEvents()) {
            if (e.getId() == id) {
                event = e;
                break;
            }
        }
        return event;
    }

    public void setAgenda() throws RemoteException {
        agenda.setEvents(new HashSet((ArrayList<Event>) findEvent(null)));
    }

    public String showAgenda() {
        StringBuilder str = new StringBuilder();
        for (Event e : agenda.getEvents()) {
            str.append(e.toString());
            str.append("\n");
        }
        return str.toString();
    }
}
