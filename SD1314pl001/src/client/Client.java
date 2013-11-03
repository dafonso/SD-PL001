/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package client;

import common.Agenda;
import common.Event;
import common.Message;
import common.MessageType;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;


/**
 *
 * @author João
 */
public class Client {

    final private Socket socket;
    final private ObjectOutputStream out;
    final private ObjectInputStream in;
    private Agenda agenda;

    public Client(String hostname, int portNumber) throws java.net.UnknownHostException, IOException {
        this.socket = new Socket(hostname, portNumber);
        this.out = new ObjectOutputStream(this.socket.getOutputStream());
        this.in = new ObjectInputStream(this.socket.getInputStream());
        this.agenda = new Agenda();
    }

    public Message addEvent(Event e) {
        return new Message(MessageType.CSAdd, e);
    }

    public Message updateEvent(Event e) {
        return new Message(MessageType.CSUpdate, e);
    }

    public Message deleteEvent(int id) {
        return new Message(MessageType.CSDelete, getEvent(id));
    }

    public Message findEvent(Event e) {
        return new Message(MessageType.CSFind, e);
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

    public void processRececeivedMsg(Message msg) {
        switch (msg.getType()) {
            case SCAdd:

                break;
            case SCUpdate:

                break;
            case SCDelete:

                break;
            case SCFind:

                break;

        }
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
