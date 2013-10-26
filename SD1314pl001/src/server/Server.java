/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import common.Agenda;
import common.properties.CommonProps;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 *
 * @author Emanuel
 */
public class Server {

    private String host;
    private int port;
    private ServerSocket serverSocket;
    private Agenda agenda;

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

    public void startServerSocket() throws UnknownHostException, IOException, ClassNotFoundException {
        // Create server socket.
        serverSocket = new ServerSocket(port);
        port = serverSocket.getLocalPort();
        while (true) {
            try {
                Socket connectionSocket = serverSocket.accept();
                InputStream is = connectionSocket.getInputStream();
                ObjectInputStream ois = new ObjectInputStream(is);
                
                //Test connection
                System.out.print(ois.readObject());
                
                //close this connection
                ois.close();
                is.close();
            } catch (UnknownHostException e) {
            } catch (IOException e) {
            }
        }
    }

}
