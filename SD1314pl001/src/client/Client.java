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
import java.util.Set;
import server.statics.RemoteBullyPassiveNode;

/**
 *
 * @author João
 */
public class Client {

    private String[][] serverPool;
    private RemoteBullyPassiveNode clientStub;
    private Agenda agenda;

    public Client() {
        this.serverPool = CommonProps.getServerPool();
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
        boolean masterFound = false;
        while (!masterFound && i<serverPool.length) {
            String getMasterName;
            Registry registry;
            try {
                registry = LocateRegistry.getRegistry(serverPool[i][0], Integer.parseInt(serverPool[i][1]));
                RemoteBullyPassiveNode stub = (RemoteBullyPassiveNode) registry.lookup(serverPool[i][3]);
                getMasterName = stub.getMasterServer().getKey();
                this.clientStub = (RemoteBullyPassiveNode) registry.lookup(getMasterName);
                masterFound = true;
            } catch (RemoteException | NotBoundException ex) {
                System.out.println("Não é possível aceder ao servidor: " + ex);
                i++;
            }
        }
        //throw new NoSuchServerOn();
    }

    public void addEvent(Event e) {
        try {
            clientStub.create(e);
        } catch (RemoteException ex) {
            try {
                connect2MasterServer();
            } catch (NoSuchServerOn n) {
                System.out.println("Não existe nenhum servidor ligado!!");
            }
        }
    }

    public void updateEvent(Event e) {
        try {
            if (clientStub.update(e)) {
                System.out.println("O evento foi actualizado com sucesso!");
            } else {
                System.out.println("Ocorreu um erro e o evento não foi actualizado!");
            }
        } catch (RemoteException ex) {
            try {
                connect2MasterServer();
            } catch (NoSuchServerOn n) {
                System.out.println("Não existe nenhum servidor ligado!!");
            }
        }
    }

    public void deleteEvent(int id) {
        try {
            if (clientStub.delete(id)) {
                System.out.println("O evento foi apagado com sucesso!");
            } else {
                System.out.println("Ocorreu um erro e o evento não foi apagado!");
            }
        } catch (RemoteException ex) {
            try {
                connect2MasterServer();
            } catch (NoSuchServerOn n) {
                System.out.println("Não existe nenhum servidor ligado!!");
            }
        }
    }

    public ArrayList<Event> findEvent(Event e) {
        ArrayList<Event> eventsList = new ArrayList<>();
        try {
            eventsList = clientStub.find(e);
        } catch (RemoteException ex) {
            try {
                connect2MasterServer();
            } catch (NoSuchServerOn n) {
                System.out.println("Não existe nenhum servidor ligado!!");
            }
        } finally {
            return eventsList;
        }
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

    public void setAgenda() {
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
