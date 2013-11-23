/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import common.Event;
import java.io.Serializable;

/**
 *
 * @author Emanuel
 */
@DatabaseTable(tableName = "EventLog")
public class EventLog implements Serializable {

    @DatabaseField(generatedId = true, allowGeneratedIdInsert = true)
    private int id;
    @DatabaseField()
    private Operation operationType;
    @DatabaseField(foreign = true)
    private Event event;

    public enum Operation {        
        createOrUpdate,
        delete                
    }
}
