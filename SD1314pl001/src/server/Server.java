/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import common.Agenda;
import common.Event;
import common.Message;
import common.MessageType;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;

/**
 *
 * @author Emanuel
 */
public class Server {

    private final String host;
    private int port;
    private ServerSocket serverSocket;
    private final Agenda agenda;

    public Server(String serverHost, int serverPort) {
        this.host = serverHost;
        this.port = serverPort;
        this.agenda = new Agenda();
    }

    public int getPort() {
        return this.port;
    }

    public String getHost() {
        return host;
    }

    public void stopServer() {
        try {
            serverSocket.close();
        } catch (IOException e) {
        }
    }

    @SuppressWarnings("ConvertToTryWithResources")
    public void startServerSocket() throws UnknownHostException, IOException, ClassNotFoundException, SQLException {
        // Create server socket.
        serverSocket = new ServerSocket(port);
        port = serverSocket.getLocalPort();
        while (true) {
            try {
                Socket connectionSocket = serverSocket.accept();
                InputStream is = connectionSocket.getInputStream();
                ObjectInputStream ois = new ObjectInputStream(is);

                // Process message
                Message response = processMessage((Message) ois.readObject());

                // Send response
                OutputStream os = connectionSocket.getOutputStream();
                ObjectOutputStream oos = new ObjectOutputStream(os);
                oos.writeObject(response);

                //close this connection
                oos.close();
                os.close();
                ois.close();
                is.close();
            } catch (UnknownHostException e) {
            } catch (IOException e) {
            }
        }
    }

    private Message processMessage(Message msg) throws SQLException {
        Message result = new Message();
        switch (msg.getType()) {
            case CSAdd:
                result.setType(MessageType.SCAdd);
                result.setData(addEvent((Event) msg.getData()));
                break;
            case CSUpdate:
                result.setType(MessageType.SCUpdate);
                result.setData(updateEvent((Event) msg.getData()));
                break;
            case CSDelete:
                result.setType(MessageType.SCDelete);
                result.setData(deleteEvent((Event) msg.getData()));
                break;
            case CSFind:
                result.setType(MessageType.SCFind);
                result.setData(findEvents((Event) msg.getData()));
                break;

        }
        return result;
    }

    private Event addEvent(Event event) throws SQLException {
        Context context = new Context();
        Date now = new Date();
        event.setCreatedAt(now);
        event.setModifiedAt(now);
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
        ArrayList<Event> result = new ArrayList(context.getEventDao().queryForMatching(event));
        context.close();
        return result;
    }

}
