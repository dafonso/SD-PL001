/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import common.Event;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 *
 * @author Emanuel
 */
public class Context {

    private final Dao<Event, String> eventDao;
    private final Dao<EventLog, String> eventLogDao;
    private final ConnectionSource connectionSource;

    public Context() throws SQLException {
        // this uses h2 by default but change to match your database
        String databaseUrl = "jdbc:sqlite:db/localdata.db";
        // create a connection source to our database
        this.connectionSource = new JdbcConnectionSource(databaseUrl);
        // instantiate the dao
        eventDao = DaoManager.createDao(connectionSource, Event.class);
        // if you need to create the 'event' table make this call
        TableUtils.createTableIfNotExists(connectionSource, Event.class);
        // instantiate the dao
        eventLogDao = DaoManager.createDao(connectionSource, EventLog.class);
        // if you need to create the 'eventLog' table make this call
        TableUtils.createTableIfNotExists(connectionSource, EventLog.class);
    }

    public Dao<Event, String> getEventDao() {
        return eventDao;
    }

    public Dao<EventLog, String> getEventLogDao() {
        return eventLogDao;
    }

    public void close() throws SQLException {
        this.connectionSource.close();
    }
}


/*
 //Once we have configured our database objects, we can use them to persist an Account to the database and query for it from  the database  {
 //    by 
 //}
 //its ID:

 // create an instance of Account
 Date date = new Date();
 Event account = new Event(date, date, "New Event", "New Description");
 //account.setName("Jim Coakley");

 // persist the account object to the database
 accountDao.create(account);

 // retrieve the account from the database by its id field (name)
 for (Event e : accountDao.queryForAll()) {
 System.out.println("Event id " + e.getId() + " : " + e.getTitle() + " ->" + e.getDescription());
 }

 // close the connection source
 connectionSource.close();
 */
