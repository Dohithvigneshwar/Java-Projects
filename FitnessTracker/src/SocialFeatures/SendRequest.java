package SocialFeatures;

import javax.swing.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Scanner;

public class SendRequest {
    Connection con;
    Scanner in;
    public SendRequest(Connection con,Scanner in){
        this.con = con;
        this.in = in;
    }
    public void send(String userName,String friend){
        try{

            String query = "select userName from users where userName = ?;";
            PreparedStatement pst = con.prepareStatement(query);
            pst.setString(1,friend);
            ResultSet rs = pst.executeQuery();
            if(rs.next()){
                query = "INSERT INTO friendsDetails (userId, friendId , status) VALUES (?, ?, ?);";
                pst = con.prepareStatement(query);
                pst.setString(1,userName);
                pst.setString(2,friend);
                pst.setString(3,"pending");
                int r = pst.executeUpdate();
                JOptionPane.showMessageDialog(null,"Request Send to Your Friend "+friend,"Information",JOptionPane.INFORMATION_MESSAGE);
            }
            else{
                JOptionPane.showMessageDialog(null,"User not found","Welcome",JOptionPane.ERROR_MESSAGE);

            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
}
