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
import server.EventLog;

/**
 *
 * @author cv
 */
public class tests
{
    public static void main(String[] args){
    
    try {
            Context context = new Context();
	    EventLog latestLog = context.getEventLogDao().queryBuilder().orderBy("id", false).queryForFirst();
	    Event latestEvent = latestLog.getEventFromLog();
            context.close();
	    System.out.println("************************************************");
        } catch (SQLException e) {
            System.err.println(e);
            System.out.println("ERRO");
        }
    
    }
    
}
