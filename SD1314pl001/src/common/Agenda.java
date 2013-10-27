/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package common;

import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author Emanuel
 */
public class Agenda {
    private Set<Event> events;

    public Agenda(Set<Event> events) {
        this.events = events;
    }

    public Agenda() {
        this.events = new HashSet<>();
    }

    /**
     * 
     * @return 
     */
    public Set<Event> getEvents() {
        return events;
    }

    /**
     * 
     * @param events 
     */
    public void setEvents(Set<Event> events) {
        this.events = events;
    }       
}
