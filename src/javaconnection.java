/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

import java.awt.Component;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.swing.JOptionPane;
public class javaconnection {
    Connection conn;
    ResultSet rs;
    PreparedStatement pst;
    
    public javaconnection(){
        
    }
    
    public static Connection connectDb(){
        try{
            Class.forName("com.mysql.jdbc.Driver");
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/renthub", "root", "1234");
            return conn;
        } catch(Exception var1){
            JOptionPane.showMessageDialog((Component)null, var1);
            return null;
        }
    }
}
