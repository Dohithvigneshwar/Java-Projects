package AccountManagent;
import javax.swing.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Scanner;
public class ManageAccountDetails {
    Scanner in;
    Connection con;
    int userId;

    public ManageAccountDetails(Connection con, Scanner in, int userId) {
        this.con = con;
        this.in = in;
        this.userId = userId;
    }

    public void dashBoard() throws Exception {
        while (true) {
            System.out.println("=======================================");
            System.out.println("1.View Profile");
            System.out.println("2.Update Profile");
            System.out.println("3.Exit");
            char c = in.nextLine().charAt(0);
            if (c == '1') {
                viewProfile();
            } else if (c == '2') {
                update();
            } else if (c == '3') {
                break;
            } else {
                System.out.println("Invalid Option Try again.......");
            }
        }
    }

    public void viewProfile() throws SQLException {
        System.out.println("========================================");
        String query = "select * from customers where custId = ? ";
        PreparedStatement pst = con.prepareStatement(query);
        pst.setInt(1, userId);
        ResultSet rs = pst.executeQuery();
        rs.next();
        System.out.println("YourId : " + rs.getInt(1));
        System.out.println("UserName : " + rs.getString(2));
        System.out.println("Phone No : " + rs.getLong(4));
        System.out.println("Email Id : " + rs.getString(5));
        System.out.println("City : " + rs.getString(6));
        query = "select billingAddress,PreferredCommunication from subscription where custId = ?";
        pst = con.prepareStatement(query);
        pst.setInt(1, userId);
        rs = pst.executeQuery();
        if (rs.next()) {
            System.out.println("Address : " + rs.getString(1));
            System.out.println("PreferredCommunication : " + rs.getString(2));
        }
    }

    public void update() throws Exception {
        System.out.println("========================================================");
        String str[] = {
                "1.Change Password  ",
                "2.Phone No ",
                "3.Email Id ",
                "4.PreferredCommunication ",
                "5.Address ",
                "6.Exit "
        };
        System.out.println(Arrays.toString(str));
        char c = in.nextLine().charAt(0);
        if (c < '1' || c > '5') {
            System.out.println("Invalid Option");
            return;
        }
        PreparedStatement pst = null;
        ResultSet rs = null;
        String query = "";
        if (c == '1') {
            System.out.println("Enter the Current Password : ");
            String curpassword = in.nextLine();
            query = "select password from customers where custId = ? ";
            pst = con.prepareStatement(query);
            pst.setInt(1, userId);
            rs = pst.executeQuery();
            rs.next();
            String pass = rs.getString(1);
            if (pass.equals(curpassword)) {
                System.out.println("");
                String newpassword = in.nextLine();
                query = "update subs set password = ? where custId = ? ";
                pst = con.prepareStatement(query);
                pst.setString(1, newpassword);
                pst.setInt(2, userId);
                pst.executeUpdate();
                JOptionPane.showMessageDialog(null, "Password Changed !", "Information", JOptionPane.PLAIN_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(null, "password doesn't match", "Information", JOptionPane.ERROR_MESSAGE);
            }
        }
        else if(c=='2'){
            System.out.println("Enter New Phone No : ");
            String phone = in.nextLine();
            query = "update customers set phoneNo = ? where custId = ?;";
            pst = con.prepareStatement(query);
            pst.setLong(1,Long.parseLong(phone));
            pst.setInt(2,userId);
            pst.executeUpdate();
            JOptionPane.showMessageDialog(null,"phone number changed","information",JOptionPane.PLAIN_MESSAGE);
        }
        else if(c=='3'){
            System.out.println("Enter New Email : ");
            String email = in.nextLine();
            query = "update customers set emailId = ? where custId = ?;";
            pst = con.prepareStatement(query);
            pst.setString(1,email);
            pst.setInt(2,userId);
            pst.executeUpdate();
            JOptionPane.showMessageDialog(null,"email id changed","information",JOptionPane.PLAIN_MESSAGE);
        }
        else if (c == '4') {
            System.out.println("Enter preferredCommunication (sms/whatsapp/email) : ");
            String pre = in.nextLine();
            query = "update subscription set preferredCommunication = ? where custId = ?;";
            pst = con.prepareStatement(query);
            pst.setString(1,pre);
            pst.setInt(2,userId);
            pst.executeUpdate();
            JOptionPane.showMessageDialog(null,"phone number changed","information",JOptionPane.PLAIN_MESSAGE);
        }
        else if(c=='5'){
            System.out.println("Enter new billing Address : ");
            String address = "";
            while (true) {
                String s = in.nextLine();
                if (s.isEmpty()) {
                    break;
                }
                address = address + "," + s;
            }
            query = "update subscription set billingAddress = ? where custId = ?;";
            pst = con.prepareStatement(query);
            pst.setString(1,address);
            pst.setInt(2,userId);
            pst.executeUpdate();
            JOptionPane.showMessageDialog(null,"address changed","information",JOptionPane.PLAIN_MESSAGE);
        }
        else{
            return;
        }
    }
}
