/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package server.statics;

import common.Event;
import java.sql.SQLException;
import java.util.ArrayList;
import server.Context;

/**
 *
 * @author cv
 */
public class tests
{
    public static void main(String[] args){
    
    try {
            Context context = new Context();
            ArrayList<Event> result;
            // query for all accounts that have that password
	    Event latestUpdatedEvent =
		context.getEventDao().queryBuilder().orderBy("modifiedAt", false)
		   .queryForFirst();
            context.close();
            System.out.println(latestUpdatedEvent.getId());
	    System.out.println("************************************************");
        } catch (SQLException e) {
            System.err.println(e);
            System.out.println("ERRO");
        }
    
    }
    
}
