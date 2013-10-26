package server;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Emanuel
 */
public class JDBCSample {

    public JDBCSample() {
    }

    public void runTest() throws ClassNotFoundException, SQLException {
        // load the sqlite-JDBC driver using the current class loader
        Class.forName("org.sqlite.JDBC");

        Connection connection = null;
        try {
            // create a database connection
            //connection = DriverManager.getConnection("jdbc:sqlite:sample.db");
            connection = DriverManager.getConnection("jdbc:sqlite:db/mydatabase.db");
            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);  // set timeout to 30 sec.

            statement.executeUpdate("drop table if exists person");
            statement.executeUpdate("create table person (id integer, name string)");
            statement.executeUpdate("insert into person values(1, 'leo')");
            statement.executeUpdate("insert into person values(2, 'yui')");
            ResultSet rs = statement.executeQuery("select * from person");
            while (rs.next()) {
                // read the result set
                System.out.println("name = " + rs.getString("name"));
                System.out.println("id = " + rs.getInt("id"));
            }
        } catch (SQLException e) {

        }
    }
}
        /*
         JDBCSample sql = new JDBCSample();
         try {
         sql.runTest();
         } catch (ClassNotFoundException | SQLException ex) {
         Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
         }*/
