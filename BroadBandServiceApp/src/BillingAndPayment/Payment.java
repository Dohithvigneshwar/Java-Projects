package BillingAndPayment;

import javax.swing.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Scanner;

public class Payment {
    Scanner in;
    public boolean payAmount(int amount, Scanner in){
        this.in = in;
        JOptionPane.showMessageDialog(null,"Choose Payment Method","Information",JOptionPane.INFORMATION_MESSAGE);
        while(true) {
            System.out.println("============================================");
            System.out.println("1.UPI");
            System.out.println("2.Debit Card");
            System.out.println("3.Credit Card");
            System.out.println("4.Exit");
            char c = in.nextLine().charAt(0);
            if(c=='1'){
                return upi(amount);
            }
            else if(c=='2'){
                return card(amount);
            }
            else if(c=='3'){
                return card(amount);
            }
            else if(c=='4'){
                return false;
            }
            else{
                System.out.println("Invalid Option try again.....");
            }
        }
    }
    private boolean upi(int amount) {
        System.out.println("$ " + amount);
        System.out.println("Enter the PhoneNo : ");
        String phoneNo = in.nextLine();
        System.out.println("Enter the Upi Id : ");
        String paymentId = in.nextLine();
        while (true) {
            int random = (int) (Math.random() * 9000) + 1000;
            System.out.println("Your OTP : " + random);
            System.out.println("Enter Otp : ");
            int otp = Integer.parseInt(in.nextLine());
            if (otp == random) {
                return true;
            } else {
                System.out.println("Invalid OTP try again........");
            }
        }
    }
    private boolean card(int amount) {
        System.out.println("$ " + amount);
        System.out.println("Enter the PhoneNo : ");
        String phoneNo = in.nextLine();
        System.out.println("Enter the card number(XXXX XXXX XXXX) : ");
        String paymentId = in.nextLine();
        while (true) {
            int random = (int) (Math.random() * 9000) + 1000;
            System.out.println("Your OTP : " + random);
            System.out.println("Enter Otp : ");
            int otp = Integer.parseInt(in.nextLine());
            if (otp == random) {
                return true;
            } else {
                System.out.println("Invalid OTP try again........");
            }
        }
    }
    public void details(String userName, Connection con){
        try{
            String query = "select * from billDetails where userName = ?";
            PreparedStatement pst = con.prepareStatement(query);
            pst.setString(1,userName);
            ResultSet rs = pst.executeQuery();
            boolean flag = true;
            while(rs.next()){
                flag = false;
                System.out.println("================================================");
                System.out.println("Plan Name : "+rs.getString(4));
                System.out.println("Purchase Date : "+rs.getDate(5));
                System.out.println("Expired on : "+rs.getDate(6));
                System.out.println("Plan Status : "+rs.getString(7));
                System.out.println("Plan Amount : "+rs.getInt(8));
                System.out.println("Payment Method : "+rs.getString(9));

            }
            if(flag){
                JOptionPane.showMessageDialog(null,"No Records Found","information",JOptionPane.ERROR_MESSAGE);
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
}
