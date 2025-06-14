package planandPackage;

import SubscriptionManageMent.ManageSubscription;

import javax.swing.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Scanner;
public class ServicesAndPlan extends ManageSubscription {
    private final Connection con;
    private final Scanner in;
    public ServicesAndPlan(Connection con, Scanner in){
        super(con,in);
        this.con = con;
        this.in = in;
    }
    public void viewPlan(String userName) throws Exception{
        while(true) {
            System.out.println("==========================================");
            System.out.println("1 Basic Plan  ");
            System.out.println("2.Family Plan  ");
            System.out.println("3.Gaming Plan  ");
            System.out.println("4.Premium Plan  ");
            System.out.println("5.Standard Plan  ");
            System.out.println("6.All Plan ");
            System.out.println("7.Exit");
            char c = in.nextLine().charAt(0);
            String planName = "null";
            switch (c) {
                case '1':
                    planName = "Basic Plan";
                    break;
                case '2':
                    planName = "Family Plan";
                    break;
                case '3':
                    planName = "Gaming Plan";
                    break;
                case '4':
                    planName = "Premium Plan";
                    break;
                case '5':
                    planName = "Standard Plan";
                    break;
                default:
                    planName = "*";
            }
            if (c == '7') {
                break;
            } else {
                if (c == '6') {
                    fetchPlans(planName,userName);
                } else if (c > '0' && c < '7') {
                    fetchPlans(planName,userName);
                    break;
                } else {
                    System.out.println("Invalid Option try again");
                }
            }
        }
    }
    private void fetchPlans(String planName,String userName) throws Exception{
        String query = "null";
        boolean flag = true;
        if(planName.equals("*")){
            query = "select * from plans";
            flag = false;
        }
        else{
            query = "select * from plans where PlanName = ?;";
        }
        PreparedStatement pst = con.prepareStatement(query);
        if(flag){
            pst.setString(1,planName);
        }
        ResultSet rs = pst.executeQuery();
        while(rs.next()) {
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
            Thread.sleep(3000);
            int input = JOptionPane.showConfirmDialog(null,"Would you like to purchase this plan ?","Question",JOptionPane.YES_NO_OPTION);
            if(input == 0){
                super.buyingPlan(planName,userName,false);
            }
        }
    }
}
