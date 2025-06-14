import AccountManagent.ManageAccountDetails;
import BillingAndPayment.Payment;
import FeedbackAndRating.FeedBack;
import SubscriptionManageMent.ManageSubscription;
import planandPackage.ServicesAndPlan;
import userRegistrationAndAuthentication.LogIn;
import userRegistrationAndAuthentication.Register;
import javax.swing.*;
import java.sql.*;
import java.util.Scanner;
public class Main {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        String url = "jdbc:mysql://localhost:3306/broadbandservice";
        String username = "root";
        String password = "Dohi@1409";
        Connection con = null;
        JOptionPane.showMessageDialog(null,"Welcome","",JOptionPane.INFORMATION_MESSAGE);
        try {
            con = DriverManager.getConnection(url, username, password);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            boolean close = false;
            while (true) {
                boolean flag = true;
                System.out.println("==== Broadband Service App ====");
                System.out.println("1. Register");
                System.out.println("2. Login");
                System.out.println("3. Exit");
                char c = in.next().charAt(0);
                in.nextLine();
                if (c == '1') {
                    Register register = new Register(con, in);
                    register.accountRegister();
                } else if (c == '2') {
                    LogIn login = new LogIn(con);
                    System.out.println("Enter the UserName :");
                    String userName = in.nextLine();
                    username = userName;
                    System.out.println("Enter the Password :");
                    String pass = in.nextLine();
                    if (login.validUser(userName, pass)) {
                        break;
                    } else {
                        System.out.println("Invalid username and password try again......");
                    }
                } else if (c == '3') {
                    close = true;
                    break;
                } else {
                    System.out.println("Invalid Option Try again........");
                }
            }
            if (close) return;

        } catch (Exception e) {
            e.printStackTrace();
        }
        int userId = 0;
        JOptionPane.showMessageDialog(null,"Login Success","Information",JOptionPane.PLAIN_MESSAGE);
        try {
            String query = "select planAmount, paymentStatus from subscription where userName = ? ;";
            PreparedStatement pst = con.prepareStatement(query);
            pst.setString(1,username);
            ResultSet rs = pst.executeQuery();
            if(rs.next()){
                if(rs.getString(2).equals("pending")){
                    JOptionPane.showMessageDialog(null,"$ "+rs.getInt(1)+" The amount is pending. Kindly complete your payment to proceed.","Pending", JOptionPane.WARNING_MESSAGE);
                    Payment obj = new Payment();
                    Thread.sleep(1500);
                    obj.payAmount(rs.getInt(1),in);
                }
            }
            query = "select custId from customers where userName = ?";
            pst = con.prepareStatement(query);
            pst.setString(1,username);
            rs = pst.executeQuery();
            rs.next();
            userId = rs.getInt(1);
            while (true) {
                System.out.println("***************************************");
                System.out.println("1.Service Plan and Package ");
                System.out.println("2.Subscription Management ");
                System.out.println("3.Billing and Payment History");
                System.out.println("4.Account Management");
                System.out.println("5.Feedback");
                System.out.println("6.Exit");
                boolean flag = false;
                char choice = in.nextLine().charAt(0);
                switch (choice) {
                    case '1':
                        ServicesAndPlan sp = new ServicesAndPlan(con,in);
                        sp.viewPlan(username);
                        break;
                    case '2':
                        ManageSubscription manageSubscription = new ManageSubscription(con,in,username);
                        manageSubscription.menu();
                        break;
                    case '3':
                        Payment payment = new Payment();
                        payment.details(username,con);
                        break;
                    case '4':
                        ManageAccountDetails manageAccountDetails = new ManageAccountDetails(con,in,userId);
                        manageAccountDetails.dashBoard();
                        break;
                    case '5':
                        FeedBack feedback = new FeedBack(con,in);
                        feedback.sendFeedBack(userId,username);
                        break;
                    case '6':
                        flag = true;
                        break;
                    default:
                        System.out.println("Invalid Option try again");
                }
                if (flag) {
                    break;
                }
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
        JOptionPane.showMessageDialog(null,"Thank You!","",JOptionPane.PLAIN_MESSAGE);
    }
}