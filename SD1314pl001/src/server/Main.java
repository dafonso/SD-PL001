/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import common.properties.CommonProps;
import java.net.MalformedURLException;
import java.net.UnknownHostException;
import java.rmi.RemoteException;
import java.sql.SQLException;
import server.pool.NodeProperties;
import server.pool.NodeState;
import server.statics.RemoteBullyPassiveNode;
import server.statics.ServerNode;

/**
 *
 * @author Emanuel
 */
public class Main {

    public static void main(String args[]) throws UnknownHostException, SQLException, MalformedURLException, RemoteException {

        String[][] pool = CommonProps.getServerPool();
        ServerNode obj = new ServerNode();
        for (String[] serverInfo : pool) {
            if (args[0].equals(serverInfo[2])) {
                NodeProperties prop = new NodeProperties(Long.parseLong(serverInfo[2]), serverInfo[0],
                        Integer.parseInt(serverInfo[1]), serverInfo[3]);
                obj.setSelf(prop);
            } else {
                NodeState state = new NodeState(Long.parseLong(serverInfo[2]), serverInfo[0],
                        Integer.parseInt(serverInfo[1]), serverInfo[3]);
                RemoteBullyPassiveNode server = new ServerNode();
                state.setObject(server);
                obj.getPool().add(state);
            }
        }
        obj.registerRMI();
        obj.holdElection();      
        String master = obj.getMaster()==null?"me":obj.getMaster().getKey();
        System.out.println("Server "+obj.getSelf().getKey()+" => "+master);
    }
}
