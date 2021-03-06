/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package common;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import java.io.Serializable;
import java.util.Date;

/**
 *
 *
 * @author Emanuel
 */
@DatabaseTable(tableName = "Event")
public class Event implements Serializable{

    @DatabaseField(generatedId = true,allowGeneratedIdInsert = true)
    private int id;
    @DatabaseField(canBeNull = false)
    private Date start;
    @DatabaseField(canBeNull = false)
    private Date end;
    @DatabaseField(canBeNull = false)
    private String title;
    @DatabaseField(canBeNull = false)
    private String description;
    @DatabaseField(canBeNull = false)
    private Date createdAt;
    @DatabaseField(canBeNull = false)
    private Date modifiedAt;

    public Event(){
        
    }
    
    public Event(Date start, Date end, String title, String description) {
        this.start = start;
        this.end = end;
        this.title = title;
        this.description = description;
        createdAt = new Date();
        modifiedAt = new Date();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getStart() {
        return start;
    }

    public void setStart(Date start) {
        this.start = start;
    }

    public Date getEnd() {
        return end;
    }

    public void setEnd(Date end) {
        this.end = end;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getModifiedAt() {
        return modifiedAt;
    }

    public void setModifiedAt(Date modifiedAt) {
        this.modifiedAt = modifiedAt;
    }
    
    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder("event ");
        sb.append(id).append(" - ");
        sb.append(title);                
        return sb.toString();
        
    }
}
