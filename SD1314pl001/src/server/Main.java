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
import common.properties.CommonProps;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Emanuel
 */
public class Main {

    public static void main(String args[]) throws SQLException {
        /*
         Server server = new Server(CommonProps.getServerHost(), CommonProps.getServerPort());
         try {
         server.startServerSocket();
         } catch (IOException | ClassNotFoundException ex) {
         Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
         }*/
        
        // this uses h2 by default but change to match your database
        String databaseUrl = "jdbc:sqlite:db/localdata.db";
        // create a connection source to our database
        ConnectionSource connectionSource
                = new JdbcConnectionSource(databaseUrl);

        // instantiate the dao
        Dao<Event, String> accountDao
                = DaoManager.createDao(connectionSource, Event.class);

        // if you need to create the 'accounts' table make this call
        TableUtils.createTableIfNotExists(connectionSource, Event.class);
        //Once we have configured our database objects, we can use them to persist an Account to the database and query for it from  the database  {
        //    by 
        //}
        //its ID:

        // create an instance of Account
        Date date = new Date();
        Event account = new Event(date,date, "New Event", "New Description");
        //account.setName("Jim Coakley");

        // persist the account object to the database
        accountDao.create(account);

        // retrieve the account from the database by its id field (name)
        for(Event e : accountDao.queryForAll()){               
            System.out.println("Event id " + e.getId()+" : "+e.getTitle()+" ->"+e.getDescription());      
        }

        // close the connection source
        connectionSource.close();
    }
}
