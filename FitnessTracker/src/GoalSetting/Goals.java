package GoalSetting;

import javax.swing.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class Goals {
    Connection con;
    Scanner in;
    String userName;
    int userId;
    public Goals(Connection con, Scanner in,String userName,int userId){
        this.con = con;
        this.in = in;
        this.userName = userName;
        this.userId = userId;
        setGoals();
    }
    public void setGoals(){
        remainder();
        while(true) {
            System.out.println("1.Set Goals");
            System.out.println("2.Check Progress");
            System.out.println("3.Exit");
            String choice = in.nextLine();
            char c = choice.charAt(0);
            if(c=='1'){
                setPlan();
            }
            else if(c=='2'){
                checkProgress();
            }
            else if(c=='3'){
                break;
            }
            else{
                System.out.println("Invalid Options Try again");
            }
        }
    }
    public void setPlan(){
        System.out.println("Enter The Target Calories : ");
        String cal = in.nextLine();
        System.out.println("Enter The Target Distance : ");
        String dis = in.nextLine();
        System.out.println("Enter The WorkOut Mode(1.Walking \n2.Running\n3.cycling\n4.swimming");
        String mode = in.nextLine();
        char m = mode.charAt(0);
        if(m=='1'){
            mode = "Walking";
        }
        if(m=='2'){
            mode = "Running";
        }
        if(m=='3'){
            mode = "Cycling";
        }
        if(m=='4'){
            mode = "Swimming";
        }
        if(m<'0' || m>'4'){
            System.out.println("Invalid input");
        }
        try{
            String query = "insert into goals(userId,userName,targetCalories,targetDistance,type) values (?,?,?,?,?);";
            PreparedStatement pst = con.prepareStatement(query);
            pst.setInt(1,userId);
            pst.setString(2,userName);
            double calories = Double.parseDouble(cal);
            pst.setDouble(3,calories);
            double distance = Double.parseDouble(dis);
            pst.setDouble(4,distance);
            pst.setString(5,mode);
            int r = pst.executeUpdate();
            if(r>0){
                System.out.println("goal set into db");
            }

        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public void checkProgress(){
        String query = "select * from goals where userName = ?";
        try {
            PreparedStatement pst = con.prepareStatement(query);
            pst.setString(1,userName);
            ResultSet rs = pst.executeQuery();
            boolean flag = true;
            while(rs.next()){
                flag = false;
                System.out.println("-----------------------------------------------");
                System.out.println("Target Calories : "+rs.getDouble(3));
                System.out.println("Target Distance : "+rs.getDouble(4));
                System.out.println("WorkOut Mode    : "+rs.getString(5));
                System.out.println("-----------------------------------------------");
            }
            if(flag){
                JOptionPane.showMessageDialog(null,"No Custom Goals are found","Information",JOptionPane.INFORMATION_MESSAGE);
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
    public void remainder(){
        try {
            String query = "select count(*) from goals where userName = ?";
            PreparedStatement pst = con.prepareStatement(query);
            pst.setString(1,userName);
            ResultSet rs = pst.executeQuery();
            while(rs.next()){
                String msg = rs.getInt(1)+" Goals are pending";
                JOptionPane.showMessageDialog(null,msg,"Information",JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
