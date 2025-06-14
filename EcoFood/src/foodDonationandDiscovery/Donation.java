package foodDonationandDiscovery;
import rewardsandRating.Rewards;
import javax.swing.*;
import java.sql.*;
import java.util.Scanner;
public class Donation extends FoodItem {
    Connection con;
    Scanner in;
    int userid;
    public Donation(Connection con, Scanner in,int userid) throws Exception{
        super(con,in,userid);
        this.con = con;
        this.in = in;
        this.userid = userid;
    }
    public void donateFood() throws Exception{
        System.out.print("Enter Food name : ");
        String foodname = in.nextLine();
        System.out.print("Enter Food Quantity [approximate(kg)] : ");
        double quantity = Double.parseDouble(in.nextLine());
        System.out.print("Food category (Bakery/Daity/fruits/Meals/etc...): ");
        String category = in.nextLine();
        System.out.print("Food Status (describe the condition, e.g., Fresh, Good, Near Expiry) : ");
        String status = in.nextLine();
        System.out.print("Enter Expire date (YYYY-MM-DD): ");
        String dateInput = in.nextLine();
        Date expireDate = Date.valueOf(dateInput);
        super.updateFood(foodname,quantity,category,status,expireDate);
        int foodId = super.getFoodid();
        String query = "insert into donation(userid,foodid) values(?,?)";
        PreparedStatement pst = con.prepareStatement(query);
        pst.setInt(1,userid);
        pst.setInt(2,foodId);
        pst.executeUpdate();
        JOptionPane.showMessageDialog(null,"Thanking for Donating Food (A request for food donations has been distributed)....Have a Good Day...Reward Point will be credited");
        Rewards rewards = new Rewards(con,in,userid);
        rewards.updatePoints((int)quantity*10);
    }
    public void fetchHistory() throws Exception{
        String query = "select d.donation_date,f.food_name,f.category,f.quantity from donation d  join fooditem f  on f.foodid = d.foodid where d.userid = ?;";
        PreparedStatement pst = con.prepareStatement(query);
        pst.setInt(1,userid);
        ResultSet rs = pst.executeQuery();
        System.out.println("---------------------------------------------------------------------------------------------");
        System.out.println("Date and time                foodname                 category                   quantity(kg)");
        System.out.println("--------------------------------------------------------------------------------------------");
        boolean flag = false;
        while(rs.next()){
            flag = true;
            Timestamp tm = rs.getTimestamp(1);
            String foodname = rs.getString(2);
            String category = rs.getString(3);
            double quantity = rs.getDouble(4);
            System.out.println(tm+"                  "+foodname+"                  "+category+"                  "+quantity);
        }
        if(!flag){
            System.out.println("No Records are Found");
        }
    }
}
