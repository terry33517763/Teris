/*
 *
 *  * WorldMISF - cms of mc-serverworld
 *  * Copyright (C) 2019-2020 mc-serverworld
 *  *
 *  * This program is free software: you can redistribute it and/or modify
 *  * it under the terms of the GNU General Public License as published by
 *  * the Free Software Foundation, either version 3 of the License, or
 *  * (at your option) any later version.
 *  *
 *  * This program is distributed in the hope that it will be useful,
 *  * but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 *  * GNU General Public License for more details.
 *  *
 *  * You should have received a copy of the GNU General Public License
 *  * along with this program. If not, see <http://www.gnu.org/licenses/>.
 *  
 */

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class Sqlcon {

    public static Connection connection;
    private static String host, database, username, password;
    private static int port;

    public Sqlcon(){

    }

    public void MYSQLlogin(String ihost,String idatabase, String iusername, String ipassword,int iport){
        host = ihost;
        port = iport;
        database = idatabase;
        username = iusername;
        password = ipassword;
        try {
            MYSQLopenConnection();
            Statement statement = connection.createStatement();
            //create database
            //useraccountdata
            statement.executeUpdate("CREATE TABLE IF NOT EXISTS `ur` (ID int NOT NULL AUTO_INCREMENT,score int,PRIMARY KEY(ID));");
            //userphoenixdata

        }catch (Exception e){
            e.printStackTrace();
        }
        try {
            MYSQLopenConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void MYSQLopenConnection() throws SQLException, ClassNotFoundException {
        if (connection != null && !connection.isClosed()) {
            return;
        }

        synchronized (this) {
            if (connection != null && !connection.isClosed()) {
                return;
            }
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://" + this.host + ":" + this.port + "/" + this.database+"?autoReconnect=true&characterEncoding=utf-8", this.username, this.password);
        }
    }

    public static Connection getConnection() throws SQLException, ClassNotFoundException {
        return connection;
    }
}