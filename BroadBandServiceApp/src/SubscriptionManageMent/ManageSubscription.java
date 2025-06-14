package SubscriptionManageMent;

import BillingAndPayment.Payment;
import javax.swing.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class ManageSubscription extends Payment implements Subscription{
    Connection con;
    Scanner in;
    private String user;
    public ManageSubscription(Connection con,Scanner in){
        this.con = con;
        this.in = in;
    }
    public ManageSubscription(Connection con, Scanner in,String userName){
        this.con = con;
        this.in = in;
        this.user = userName;
    }
    public void menu(){
        while(true) {
            System.out.println("=============================================");
            System.out.println("1. View Current Plan ");
            System.out.println("2. Plan Upgrade ");
            System.out.println("3. Plan Downgrades ");
            System.out.println("4. Cancel Current Plan ");
            System.out.println("5. Exit ");
            char c = in.nextLine().charAt(0);
            if(c=='1'){
                displayCurrentPlan();
            }
            else if(c=='2'){
                upgrade();
            }
            else if(c=='3'){
                downgrade();
            }
            else if(c=='4'){
                cencellation();
            }
            else if(c=='5'){
                break;
            }
            else{
                System.out.println("Invalid Option Try Again");
            }
        }
    }
    private void displayCurrentPlan(){
        try{
            String query = "select * from subscription where userName = ?";
            PreparedStatement pst = con.prepareStatement(query);
            pst.setString(1,user);
            ResultSet rs = pst.executeQuery();
            if(!rs.next()){
                JOptionPane.showMessageDialog(null,"No active plan found","Error",JOptionPane.ERROR_MESSAGE);
                return;
            }
            System.out.println("*******************************************");
            System.out.println("Current Plan Name : "+rs.getString(4));
            System.out.println("Plan Amount : "+rs.getInt(5));
            System.out.println("Purchase Date : "+rs.getDate(6));
            System.out.println("Expired on : "+rs.getDate(7));
            System.out.println("Address : "+rs.getString(8));
            System.out.println("Preferred Communication : "+rs.getString(9));
            System.out.println("Payment Status : "+rs.getString(10));
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
    private boolean isPlanAvailable(){
        try {
            String query = "select userName from subscription where userName = ? ";
            PreparedStatement pst = con.prepareStatement(query);
            pst.setString(1, user);
            ResultSet rs = pst.executeQuery();
            if (!rs.next()) {
                JOptionPane.showMessageDialog(null, "No active plan found", "Error", JOptionPane.ERROR_MESSAGE);
                return false;
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return true;
    }
    @Override
    public void cencellation(){
        try {
            if(!isPlanAvailable()){
                return;
            }
            int y = JOptionPane.showConfirmDialog(null, "Do you really want to cancel your current plan? (Note: If you cancel within 10 days of purchase, only 50% of the amount will be refunded to your account)", "Cencel", JOptionPane.YES_NO_OPTION);
            if (y == 0) {
                System.out.println("Plan Cancelled Refund amount will credit within 24 hours");
                String query = "delete from subscription where userName = ?";
                PreparedStatement pst = con.prepareStatement(query);
                pst.setString(1,user);
                pst.executeUpdate();
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
    @Override
    public void upgrade(){
        try{
            if(!isPlanAvailable()){
                return;
            }
            String query = "Select planAmount from subscription where userName = ?;";
            PreparedStatement pst = con.prepareStatement(query);
            pst.setString(1,user);
            ResultSet rs = pst.executeQuery();
            rs.next();
            int currentAmount = rs.getInt(1);
            query = "select * from plans where price > ?";
            pst = con.prepareStatement(query);
            pst.setInt(1,currentAmount);
            rs = pst.executeQuery();
            boolean flag = true;
            while(rs.next()){
                flag = false;
                System.out.println("****************************************");
                System.out.println("Plan Name : " + rs.getString(1));
                System.out.println("Data Speed : " + rs.getString(2));
                System.out.println("Date Limit : " + rs.getString(3));
                System.out.println("Price : $ " + rs.getInt(4)+"/month");
                System.out.println("Details : ");
                String details = rs.getString(5).replace(". ", "<>");
                String a[] = details.split("<>");
                for (String e : a) {
                    System.out.println("--> " + e);
                }
            }
            if(flag){
                System.out.println("No upgrade Plans are Available");
                return;
            }
            System.out.println("Do you want to Upgrade (yes/no) :");
            String input = in.nextLine();
            if(input.charAt(0)=='y'){
                System.out.println("Enter the upgrade plan name:");
                String plan = in.nextLine();
                this.buyingPlan(plan,user,true);
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }
    @Override
    public void downgrade(){
        try{
            if(!isPlanAvailable()){
                return;
            }
            String query = "Select planAmount from subscription where userName = ?;";
            PreparedStatement pst = con.prepareStatement(query);
            pst.setString(1,user);
            ResultSet rs = pst.executeQuery();
            rs.next();
            int currentAmount = rs.getInt(1);
            query = "select * from plans where price < ?";
            pst = con.prepareStatement(query);
            pst.setInt(1,currentAmount);
            rs = pst.executeQuery();
            boolean flag = true;
            while(rs.next()){
                flag = false;
                System.out.println("****************************************");
                System.out.println("Plan Name : " + rs.getString(1));
                System.out.println("Data Speed : " + rs.getString(2));
                System.out.println("Date Limit : " + rs.getString(3));
                System.out.println("Price : $ " + rs.getInt(4)+"/month");
                System.out.println("Details : ");
                String details = rs.getString(5).replace(". ", "<>");
                String a[] = details.split("<>");
                for (String e : a) {
                    System.out.println("--> " + e);
                }
            }
            if(flag){
                System.out.println("No downgrades Plans are Available");
                return;
            }
            System.out.println("Do you want to Downgrades (yes/no) :");
            String input = in.nextLine();
            if(input.charAt(0)=='y'){
                System.out.println("Enter the downgrades plan name:");
                String plan = in.nextLine();
                this.buyingPlan(plan,user,true);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    @Override
    public void buyingPlan(String planName,String userName,boolean UpDown){
        System.out.println("===========================================");
        System.out.println("Customer Name : "+userName);
        System.out.println("Plan Name : "+planName);
        try{
            Thread.sleep(2000);
        }
        catch (Exception e){
            e.printStackTrace();
        }
        int input = JOptionPane.showConfirmDialog(null,"Are sure to purchase ? ","conformation",JOptionPane.YES_NO_OPTION);
        if(input != 0){
            return;
        }
        String query = "select custId from subscription where userName = ?;";
        ResultSet rs = null;
        PreparedStatement pst;
        String address = "null";
        int custId = 0;
        try {
            if(UpDown){
                query = "select billingAddress from subscription where userName = ?";
                pst = con.prepareStatement(query);
                pst.setString(1,userName);
                rs = pst.executeQuery();
                rs.next();
                address = rs.getString(1);
                query = "delete from subscription where userName = ?;";
                pst = con.prepareStatement(query);
                pst.setString(1,userName);
                pst.executeUpdate();

            }else {
                pst = con.prepareStatement(query);
                pst.setString(1, userName);
                rs = pst.executeQuery();
                if (rs.next()) {
                    JOptionPane.showMessageDialog(null, "Currently, there is one active plan. If you want to purchase a new plan, you can select either the upgrade or downgrade option (Subscription ManageMent)", "Warning", JOptionPane.WARNING_MESSAGE);
                    return;
                }
            }
            query = "select custId from customers where userName = ?;";
            pst = con.prepareStatement(query);
            pst.setString(1,userName);
            rs = pst.executeQuery();
            rs.next();
            custId = rs.getInt(1);
            if(!UpDown) {
                System.out.println("Enter the billing Address : ");
                address = "";
                while (true) {
                    String s = in.nextLine();
                    if (s.isEmpty()) {
                        break;
                    }
                    address = address + "," + s;
                }
                address = address.substring(1, address.length() - 1);
            }
            System.out.println("how we will contact you (sms/whatsapp/email):");
            String contact = in.nextLine();
            query = "insert into subscription(custId,userName,planName,planAmount,billingAddress,PreferredCommunication,paymentStatus) values(?,?,?,?,?,?,?);";
            pst = con.prepareStatement(query);
            pst.setInt(1,custId);
            pst.setString(2,userName);
            pst.setString(3,planName);
            String amountquery = "select price from plans where planName = ?";
            PreparedStatement pst2 = con.prepareStatement(amountquery);
            pst2.setString(1,planName);
            ResultSet rs1 = pst2.executeQuery();
            rs1.next();
            String str = rs1.getString(1);
            int amount = Integer.parseInt(str.replaceAll("[^\\d+]",""));
            pst.setInt(4,amount);
            pst.setString(5,address);
            pst.setString(6,contact);
            System.out.println("Would you like to pay now ? (yes/no)");
            String payment = in.nextLine();
            if(payment.charAt(0)=='y' || payment.charAt(0)=='Y'){
                boolean flag = super.payAmount(amount,in);
                if(!flag){
                    payment = "pending";
                    JOptionPane.showMessageDialog(null,"payment failed","Warning",JOptionPane.WARNING_MESSAGE);
                }
                else{
                    payment = "paid";
                    JOptionPane.showMessageDialog(null,"payment success","Success",JOptionPane.INFORMATION_MESSAGE);
                }
            }
            else{
                payment = "pending";
            }
            pst.setString(7,payment);
            int r = pst.executeUpdate();
            query = "select purchase_date from subscription where userName = ?;";
            pst = con.prepareStatement(query);
            pst.setString(1,userName);
            rs = pst.executeQuery();
            rs.next();
            java.sql.Date purchaseDate = rs.getDate(1);
            String query1 = "update subscription set expired_on = DATE_ADD(?, INTERVAL 30 DAY) where userName = ?";
            pst2 = con.prepareStatement(query1);
            pst2.setString(2,userName);
            pst2.setDate(1,purchaseDate);
            pst2.executeUpdate();
            query = "select userName from billDetails where userName = ?";
            pst = con.prepareStatement(query);
            pst.setString(1,userName);
            rs = pst.executeQuery();
            if(rs.next()){
                query = "update billDetails set planstatus = 'inactive' where userName = ?";
                pst = con.prepareStatement(query);
                pst.setString(1,userName);
                pst.executeUpdate();
            }
            JOptionPane.showMessageDialog(null,"Your Plan is Successfully purchased we will contact soon!","Information",JOptionPane.INFORMATION_MESSAGE);
            query = "insert into billDetails(custId,userName,planName,planAmount,paymentMethod) values(?,?,?,?,?);";
            pst = con.prepareStatement(query);
            pst.setInt(1,custId);
            pst.setString(2,userName);
            pst.setString(3,planName);
            pst.setInt(4,amount);
            pst.setString(5,"net banking");
            pst.executeUpdate();


            query = "select purchaseDate from billDetails where userName = ?";
            pst = con.prepareStatement(query);
            pst.setString(1,userName);
            rs = pst.executeQuery();
            rs.next();
            purchaseDate = rs.getDate(1);
            query = "update billDetails set expired_on = DATE_ADD(?, INTERVAL 30 DAY) where userName = ?";
            pst = con.prepareStatement(query);
            pst.setDate(1,purchaseDate);
            pst.setString(2,userName);
            pst.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

