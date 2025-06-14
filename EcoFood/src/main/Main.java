package main;
import userRegistrationAndAuthentication.LogIn;
import userRegistrationAndAuthentication.SignUp;
import javax.swing.*;
import java.sql.*;
import java.util.Scanner;
public class Main {
    public static void main(String[] args) {
        JOptionPane.showMessageDialog(null,"WELCOME - ECOFOOD (Food Waste Management & Redistribution App)");
        String user = "root";
        String pass = "Dohi@1409";
        String url = "jdbc:mysql://localhost:3306/EcoFood";
        Scanner in = new Scanner(System.in);
        Connection con = null;
        try{
            con = DriverManager.getConnection(url,user,pass);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try{
            String username = "";
            boolean check = true;
            while(check) {
                System.out.println("====================== ECO - FOOD ======================");
                System.out.println("1.Register");
                System.out.println("2.Log in");
                System.out.println("3.Admin");
                System.out.println("4.Exit");
                System.out.print("Enter your choice : ");
                char c = in.nextLine().charAt(0);
                if (c == '2' || c == '3') {
                    LogIn login = new LogIn(con);
                    System.out.print("Enter your userName : ");
                    String userName = in.nextLine();
                    System.out.print("Enter your Password : ");
                    String password = in.nextLine();
                    if (login.isValidUser(userName, password)) {
                        username = userName;
                        if(c == '3'){
                            System.out.print("Enter the Admin Entry Code : ");
                            String code = in.nextLine();
                            if(code.equals("14@09")){
                                check = false;
                                System.out.println("Access Granted");
                            }
                            else{
                                System.out.println("Access Decline");
                            }
                        }
                        else {
                            check = false;
                        }
                    }
                } else if (c == '1') {
                    SignUp signup = new SignUp(con, in);
                    signup.createUser();
                } else if (c == '4') {
                    check = false;
                    return;
                } else {
                    System.out.println("Invalid Option try again !");
                }
            }
            String query = "select userid from users where userName = ?";
            PreparedStatement pst = con.prepareStatement(query);
            pst.setString(1,username);
            ResultSet rs = pst.executeQuery();
            rs.next();
            int userId = rs.getInt(1);
            query = "select role from users where userid = ?;";
            pst = con.prepareStatement(query);
            pst.setInt(1,userId);
            rs = pst.executeQuery();
            rs.next();
            String role = rs.getString(1);
            if(role.equals("Donor")){
                DonorDashboard donorDashboard = new DonorDashboard(con,in,userId);
                donorDashboard.dashBoard();
            }
            else if(role.equals("Receiver")){
                ReceiverDashboard receiverDashboard = new ReceiverDashboard(con,in,userId);
                receiverDashboard.dashBoard();
            }
            else if(role.equals("ADMIN")){
                Admin admin = new Admin(con,in);
                admin.dashBoard();
            }
            else{
                VolunteerDashboard volunteerDashboard = new VolunteerDashboard(con,in,userId);
                volunteerDashboard.dashBoard();
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }
}