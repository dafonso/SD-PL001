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
import java.util.Date;

/**
 *
 * @author Emanuel
 */
@DatabaseTable(tableName = "EventLog")
public class EventLog implements Serializable {

    @DatabaseField(generatedId = true, allowGeneratedIdInsert = true)
    private int id;
    @DatabaseField(canBeNull = false)
    private Operation operationType;
    @DatabaseField(canBeNull = false)
    private int eventId;
    @DatabaseField()
    private Date eventStart;
    @DatabaseField()
    private Date eventEnd;
    @DatabaseField()
    private String eventTitle;
    @DatabaseField()
    private String eventDescription;
    @DatabaseField()
    private Date eventCreatedAt;
    @DatabaseField()
    private Date eventModifiedAt;

    public EventLog(){
	id = 0;
    }

    public enum Operation {        
        createOrUpdate,
        delete                
    }
    
    public Event getEventFromLog(){
	Event result = new Event();
	result.setId(eventId);
	result.setStart(eventStart);
	result.setEnd(eventEnd);
	result.setTitle(eventTitle);
	result.setDescription(eventDescription);
	result.setCreatedAt(eventCreatedAt);
	result.setModifiedAt(eventModifiedAt);
	return result;
    }
    
    public void setEventToLog(Event eventToLog){
	eventId = eventToLog.getId();
	eventStart = eventToLog.getStart();
	eventEnd = eventToLog.getEnd();
	eventTitle = eventToLog.getTitle();
	eventDescription = eventToLog.getDescription();
	eventCreatedAt = eventToLog.getCreatedAt();
	eventModifiedAt = eventToLog.getModifiedAt();
    }

    public Operation getOperationType()
    {
	return operationType;
    }

    public void setOperationType(Operation operationType)
    {
	this.operationType = operationType;
    }

    //needed for delete event operation
    public void setEventId(int eventId)
    {
	this.eventId = eventId;
    }
    
    
    
    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder("eventLog ");
        sb.append(id).append(" ");
        sb.append(operationType);
	sb.append(id).append(" - ");
	sb.append(this.getEventFromLog().toString());
        return sb.toString();
        
    }

}
