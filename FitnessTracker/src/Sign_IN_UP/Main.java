package Sign_IN_UP;
import ActivityTracker.Workout;
import CustomPlan.Custom;
import GoalSetting.Goals;
import SocialFeatures.DashBoard;
import javax.swing.*;
import java.sql.*;
import java.util.Scanner;
public class Main {
    public static void main(String args[]) throws Exception {
        Scanner in = new Scanner(System.in);

        JOptionPane.showMessageDialog(null,"Welcome to our Fitness Application","Welcome",JOptionPane.PLAIN_MESSAGE);

        String server = "jdbc:mysql://localhost:3306/fitness";
        String username = "root";
        String password = "Dohi@1409";
        Connection con = null;
        Statement st = null;


        try {
            con = DriverManager.getConnection(server,username,password);
//            st = con.createStatement();

        }
        catch(SQLException e) {
            e.printStackTrace();
        }
        boolean signIn = false;
        boolean signUp = false;
        String user = "";
        String pass = "";
        int choice = 1;

        while(!signIn) {
            if (!signUp) {
                System.out.println("Sign In : 1\nSign Up : 2");
                choice = in.nextInt();
                in.nextLine();
            }
            if (choice == 1){
                LogIn login = new LogIn(con, in);
                System.out.println("Enter the username ?");
                user = in.nextLine();
                System.out.println("Enter the password ?");
                pass = in.nextLine();
                signIn = login.validUser(user, pass);
            }
            else if (choice == 2) {
                Registration res = new Registration(con, in);
                signUp = res.registerUser();

                if (signUp) {
                    System.out.println("Registration successful! Please sign in.");

                    choice = 1;
                } else {
                    System.out.println("Registration failed... Try again with valid inputs...");
                    choice = 2;
                }
            } else {
                System.out.println("Invalid options Try again");
            }
        }
        String query = "select userId from users where username = ? and password = ?;";
        PreparedStatement pst = con.prepareStatement(query);
        pst.setString(1,user);
        pst.setString(2,pass);
        ResultSet rs = pst.executeQuery();
        rs.next();
        int userId = rs.getInt(1);
        JOptionPane.showMessageDialog(null,"Log in Success","verified",JOptionPane.INFORMATION_MESSAGE);

        while(true) {
            System.out.println("*****************SELECT ONE FROM BELOW***************");
            System.out.println("1.WorkOut or CheckActivityDetails :");
            System.out.println("2.Custom WorkOut Plan : ");
            System.out.println("3.Set Goals : ");
            System.out.println("4.Social Features : ");
            System.out.println("5.Exit");
            String input = in.nextLine();
            char c = input.charAt(0);
            if (c == '1') {
                Workout workout = new Workout(con, in, userId);
                workout.start();
            } else if (c == '2') {
                Custom custom = new Custom(con,in,userId,username);

            } else if (c == '3') {
                Goals goals = new Goals(con,in,username,userId);
            } else if (c == '4') {
                DashBoard db = new DashBoard(con,in,user,userId);
            } else if (c == '5') {
                JOptionPane.showMessageDialog(null,"Thank You","",JOptionPane.PLAIN_MESSAGE);
                break;
            } else {
                System.out.println("Invalid Options");
            }
        }
    }
}