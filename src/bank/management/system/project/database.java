package bank.management.system.project;

import java.sql.Connection;
import java.sql.DriverManager;


public class database {
     public static Connection ConnectDB(){
    
        try{
        Class.forName("com.mysql.jdbc.Driver");
        Connection connect = DriverManager.getConnection("jdbc:mysql://localhost/bankDb", "root", "");
        System.out.println("Database connected");
        return connect;
        
        }catch(Exception e){
            e.printStackTrace();
                    System.out.println("Database not connected");
        }
        return null;
    
    }
}
