package CustomPlan;

import javax.swing.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Scanner;
public class Custom {
    Connection con;
    Scanner in;
    int userId;
    String userName;
    public Custom(Connection con,Scanner in,int userId,String userName){
        this.con = con;
        this.in = in;
        this.userId = userId;
        this.userName = userName;
        menu();
    }
    public void menu(){
        while(true){
            System.out.println("1.Set Custom WorkOut :");
            System.out.println("2.Check Custom WorkOut Plan :");
            System.out.println("3.Exit");
            String option = in.nextLine();
            char c = option.charAt(0);
            if(c=='1'){
                setCustom();
            }
            else if(c=='2'){
                displayCustom();
            }
            else if(c=='3'){
                break;
            }
            else{
                System.out.println("Invalid Option Try Again......");
            }
        }
    }
    public void setCustom(){
        try{
            System.out.println("Enter Target Time(Hours) : ");
            String time = in.nextLine();
            System.out.println("Enter Target Distance : ");
            String d = in.nextLine();
            double distance = Double.parseDouble(d);
            System.out.println("Enter Target Calories : ");
            String c = in.nextLine();
            double calories = Double.parseDouble(c);
            System.out.println("Enter Workout Type : ");
            String mode = in.nextLine();

            String query = "insert into customwork values(?,?,?,?,?);";
            PreparedStatement pst = con.prepareStatement(query);
            pst.setInt(1,userId);
            pst.setString(2,time);
            pst.setDouble(3,distance);
            pst.setDouble(4,calories);
            pst.setString(5,mode);
            pst.executeUpdate();
            System.out.println("Custom WorkOut Updated");
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
    public void displayCustom(){
        String query = "select * from customwork where userId = ?";
        try{
            PreparedStatement pst = con.prepareStatement(query);
            pst.setInt(1,userId);
            ResultSet rs = pst.executeQuery();
            boolean flag = true;
            while(rs.next()){
                flag = false;
                System.out.println("----------------------------------------------------------");
                System.out.println("Custom Plan");
                System.out.println("Target WorkOut Time : "+rs.getString(2));
                System.out.println("Target Distance : "+rs.getDouble(3));
                System.out.println("Target Calories : "+rs.getDouble(4));
                System.out.println("Target Mode : "+rs.getString(5));
                System.out.println("----------------------------------------------------------");
            }
            if(flag){
                JOptionPane.showMessageDialog(null,"No Custom Plan Found","Warning",JOptionPane.INFORMATION_MESSAGE);
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
}
