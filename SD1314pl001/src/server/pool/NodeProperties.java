/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server.pool;

import java.io.Serializable;

/**
 *
 * @author Emanuel
 */
public class NodeProperties implements Serializable{

    private long id;
    private String hostname;
    private int portNumber;
    private String key;

    public NodeProperties(long id, String hostname, int portNumber, String key) {
        this.id = id;
        this.hostname = hostname;
        this.portNumber = portNumber;
        this.key = key;
    }

    public long getId() {
        return id;
    }

    public String getHostname() {
        return hostname;
    }

    public int getPortNumber() {
        return portNumber;
    }

    public String getKey() {
        return key;
    }
}
