/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server.pool;

import java.rmi.Remote;

/**
 *
 * @author Emanuel
 */
public interface RemoteNode extends Remote {

    public void alive();
}
