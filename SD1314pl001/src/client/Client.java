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
import java.util.Date;

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
        Message message = new Message(MessageType.CSAdd, e);
        return message;
    }

    public Message updateEvent(int id) {
        Message message = new Message(MessageType.CSUpdate, e);
        return message;
    }
    public Message deleteEvent(Event e){
        Message message = new Message(MessageType.CSDelete, e);
        return message;
    }
    public Message findEvent(Event e){
        Message message = new Message(MessageType.CSFind, e);
        return message;
    }
    public void processRececeivedMsg(Message msg){
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
    private Agenda getAgenda(){
        return this.agenda;
    }
    private void setAgenda(Agenda a){
        this.agenda = a;
    }
    public String showAgenda(){
        StringBuilder str = new StringBuilder();
        for(Event e : agenda.getEvents()){
            str.append(e.toString());
            str.append("\n");   
        }
        return str.toString();
    }
}
