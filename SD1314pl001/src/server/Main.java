/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import common.properties.CommonProps;
import java.io.IOException;
import java.net.UnknownHostException;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Emanuel
 */
public class Main {

    public static void main(String args[]) throws UnknownHostException, SQLException {
        Server server = new Server(CommonProps.getServerHost(), CommonProps.getServerPort());
        try {
            server.startServerSocket();
        } catch (IOException | ClassNotFoundException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
}
