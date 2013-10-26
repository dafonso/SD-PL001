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

    private final MessageType type;
    private final Date timestamp;
    private final Serializable data;

    public Message(MessageType type, Serializable data) {
        this.type = type;
        this.data = data;
        this.timestamp = new Date();
    }

    public MessageType getType() {
        return type;
    }

    public Serializable getData() {
        return data;
    }
}
