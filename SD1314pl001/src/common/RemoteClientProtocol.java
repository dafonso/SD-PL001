/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package common;

import java.util.List;
import server.statics.RemoteBullyPassiveNode;

/**
 *
 * @author Emanuel
 */
public interface RemoteClientProtocol {
        // Client Remote Methods    
    public Event create(Event event);
    
    public boolean update(Event event);
    
    public boolean delete(Event event);
    
    public List<Event> find(Event event);
}
