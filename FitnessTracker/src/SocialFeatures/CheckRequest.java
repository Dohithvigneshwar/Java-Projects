package SocialFeatures;

import javax.swing.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class CheckRequest {
    private final Connection con;
    private final Scanner in;
    public CheckRequest(Connection con,Scanner in){
        this.con = con;
        this.in = in;
    }
    public void check(String userName){
        try{
            String query = "select userid from friendsdetails where status='pending' and friendId=?;";
            PreparedStatement pst = con.prepareStatement(query);
            pst.setString(1,userName);
            ResultSet rs = pst.executeQuery();
            boolean flag = true;
            while(rs.next()){
                flag = false;
                String frid = rs.getString(1);
                String msg = "Friend Request From "+frid+" DO YOU WANT ACCEPT ?";
                int yesNo = JOptionPane.showConfirmDialog(null,msg,"conformation",JOptionPane.YES_NO_OPTION);
                if(yesNo == 0){
                    query = "update friendsDetails set status = 'accept' where userId = ? and friendId = ?;";
                    pst = con.prepareStatement(query);
                    pst.setString(1,frid);
                    pst.setString(2,userName);
                    int r = pst.executeUpdate();
                    msg = frid+" Connected";
                    JOptionPane.showMessageDialog(null,msg,"Information",JOptionPane.INFORMATION_MESSAGE);
                    query = "INSERT INTO friendsDetails (userId, friendId , status) VALUES (?, ?, ?);";
                    pst = con.prepareStatement(query);
                    pst.setString(1,userName);
                    pst.setString(2,frid);
                    pst.setString(3,"accept");
                    r = pst.executeUpdate();
                }
                else{
                    JOptionPane.showMessageDialog(null,"Request Rejected","Information",JOptionPane.INFORMATION_MESSAGE);
                }
            }
            if(flag){
                JOptionPane.showMessageDialog(null,"No friend request found","Information5",JOptionPane.WARNING_MESSAGE);
            }

        }
        catch (SQLException e13){
            e13.printStackTrace();
        }
    }
}
