package main;

import javax.swing.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Scanner;

public class Admin extends Main{
    private final Connection con;
    private final Scanner in;
    public Admin(Connection con, Scanner in){
        this.con = con;
        this.in = in;
    }
    public void dashBoard() throws Exception{
        boolean check = true;
        while(check) {
            System.out.println("=======================================================");
            System.out.println("1.Analytics & impact reports ");
            System.out.println("2.carbon footprint reduction ");
            System.out.println("3.Exit");
            System.out.print("Enter Your Option : ");
            char c = in.nextLine().charAt(0);
            System.out.println("=======================================================");
            if(c == '1'){
                printReport();
            }
            else if (c == '2') {
                carbonfoodPrint();
            }
            else if(c == '3'){
                check = false;
            }
            else{
                System.out.println("Invalid Option try again.....");
            }
        }
    }
    private void printReport() throws Exception{
        System.out.println("=== EcoFood Analytics & Impact Report ===");
        String query = "select category,sum(quantity) from fooditem join orders on fooditem.foodid = orders.foodid group by category;";
        PreparedStatement pst = con.prepareStatement(query);
        ResultSet rs = pst.executeQuery();
        boolean flag = false;
        System.out.println("--------------------------------------------------------");
        System.out.println("Category                        Total(kg)                        ");
        System.out.println("---------------------------------------------------------");
        while(rs.next()){
            flag = true;
            System.out.println(rs.getString(1)+"                "+rs.getDouble(2));
        }
        System.out.println("---------------------------------------------------------");
        if(!flag){
            JOptionPane.showMessageDialog(null,"No records are found");
            return;
        }
        query = "select count(distinct orderid) from orders where order_status = 'delivered';";
        pst = con.prepareStatement(query);
        rs = pst.executeQuery();
        if(rs.next()) {
            int count = rs.getInt(1);
            System.out.println("Number of People Helped: " + count);
        }
    }
    private void carbonfoodPrint() throws Exception{
        String query = "select distinct monthname(donation_date) from donation";
        PreparedStatement pst = con.prepareStatement(query);
        boolean check = false;
        ResultSet rs = pst.executeQuery();
        System.out.println("---------------------------------------------------------------");
        System.out.println("Month            no of food donation             no of food ordered           carbon reduction");
        System.out.println("----------------------------------------------------------------");
        while(rs.next()){
            check = true;
            System.out.print(rs.getString(1)+"                 ");
            query = "select  count(case when o.order_status = 'delivered' then 1 end)," +
                    "count(d.foodid) - count(case when o.order_status = 'delivered' then 1 end) " +
                    "from donation d " +
                    "left join orders o on d.foodid = o.foodid " +
                    "where monthname(d.donation_date) = ?;";
            PreparedStatement pst1 = con.prepareStatement(query);
            pst1.setString(1,rs.getString(1));
            ResultSet rs1 = pst1.executeQuery();
            rs1.next();
            System.out.print(rs1.getInt(1)+"                         "+rs1.getInt(2));
            double weate = (double) (rs1.getInt(2)/rs1.getInt(1)) * 100;
//            System.out.println(weate);
            System.out.println("                           "+weate+"%");
        }
        System.out.println("----------------------------------------------------------------");
        if(!check){
            JOptionPane.showMessageDialog(null,"No records are found");
            return;
        }
    }
}
