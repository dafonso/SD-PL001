/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server.pool;

/**
 *
 * @author Emanuel
 * @param <T>
 */
public class NodeState<T extends RemoteNode> extends NodeProperties {

    private T object;

    public NodeState(long id, String hostname, int portNumber, String key) {
        super(id, hostname, portNumber, key);
    }

    public boolean reconnect(){
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
