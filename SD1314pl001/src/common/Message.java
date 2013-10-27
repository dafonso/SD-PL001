/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package common;

import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author Emanuel
 */
public class Message implements Serializable {

    private MessageType type;
    private final Date timestamp;
    private Serializable data;

    public Message(MessageType type, Serializable data) {
        this.type = type;
        this.data = data;
        this.timestamp = new Date();
    }

    public Message() {
        this.data = null;
        this.type = null;
        this.timestamp = new Date();
    }

    public MessageType getType() {
        return type;
    }

    public Serializable getData() {
        return data;
    }

    public void setType(MessageType type) {
        this.type = type;
    }

    public void setData(Serializable data) {
        this.data = data;
    }
}
