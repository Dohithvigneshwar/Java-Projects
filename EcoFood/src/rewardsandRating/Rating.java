package rewardsandRating;
import javax.swing.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Scanner;
public class Rating {
    private final Connection con;
    private final Scanner in;
    private int userid;
    public Rating(Connection con, Scanner in, int userid){
        this.con = con;
        this.in = in;
        this.userid = userid;
    }
    public void ratingDonerSite() throws Exception{
        String query = "select donation_id from donation where userid = ?";
        PreparedStatement pst = con.prepareStatement(query);
        pst.setInt(1,userid);
        ResultSet rs = pst.executeQuery();
        if(!rs.next()){
            System.out.println("No Donation are Found");
            return;
        }
        query = "select foodid,food_name from fooditem where foodid in (select foodid from donation where userid = ? ) and isAvailable = 'No';";
        pst = con.prepareStatement(query);
        pst.setInt(1,userid);
        rs = pst.executeQuery();
        boolean flag = true;
        while(rs.next()){
            flag = false;
            System.out.print("Food id : "+rs.getInt(1)+"               ");
            System.out.println("Food Name : "+rs.getString(2));
        }
        if(flag){
            System.out.println("Your donated food has not been ordered by any receiver");
            return;
        }
        System.out.print("Enter food id to continue your feedback : ");
        int id = Integer.parseInt(in.nextLine());
        query = "select orderid from orders where foodid = ?";
        pst = con.prepareStatement(query);
        pst.setInt(1,id);
        rs = pst.executeQuery();
        if(!rs.next()){
            System.out.println("Invalid food id");
            return;
        }
        int orderid = rs.getInt(1);
        System.out.print("Rate the delivery service (1-5):");
        int deliveryRate = in.nextLine().charAt(0)-'0';
        System.out.print("Share your feedback on the delivery: ");
        String deliveryfeed = in.nextLine();
        query = "insert into rating(userid,orderid,userRole,rating_value,feedback) values(?,?,?,?,?);";
        pst = con.prepareStatement(query);
        pst.setInt(1,userid);
        pst.setInt(2,orderid);
        pst.setString(3, "Donar");
        pst.setInt(4, deliveryRate);
        pst.setString(5,deliveryfeed);
        pst.executeUpdate();
        JOptionPane.showMessageDialog(null,"Thank you for your feedback");
    }
    public void ratingReceiver() throws Exception{
        String query = "select orderid from orders where receiver_id = ? and order_status = 'delivered'";
        PreparedStatement pst = con.prepareStatement(query);
        pst.setInt(1,userid);
        ResultSet rs = pst.executeQuery();
        if(!rs.next()){
            System.out.println("No Order delivered");
            return;
        }
        boolean flag = true;
        query = "SELECT orders.orderid, fooditem.food_name FROM orders JOIN fooditem ON orders.foodid = fooditem.foodid WHERE orders.receiver_id = ? AND orders.order_status = 'Delivered';";
        pst = con.prepareStatement(query);
        pst.setInt(1,userid);
        rs = pst.executeQuery();
        while(rs.next()){
            flag = false;
            System.out.print("order id : "+rs.getInt(1)+"               ");
            System.out.println("Food Name : "+rs.getString(2));
        }
        System.out.print("Enter food id to continue your feedback :");
        int orderid = Integer.parseInt(in.nextLine().trim());
        System.out.print("Rate the delivery service (1-5):");
        int deliveryRate = in.nextLine().charAt(0)-'0';
        System.out.print("Share your feedback on the delivery: ");
        String deliveryfeed = in.nextLine();
        System.out.print("Rate the food quality (1-5):");
        int foodrate = in.nextLine().charAt(0)-'0';
        System.out.print("Rate your feedback on the food quality:");
        String foodfeed = in.nextLine();
        query = "insert into rating(userid,orderid,userRole,rating_value,feedback) values(?,?,?,?,?);";
        pst = con.prepareStatement(query);
        pst.setInt(1,userid);
        pst.setInt(2,orderid);
        pst.setString(3,"Receiver");
        pst.setInt(4,foodrate);
        pst.setString(5,foodfeed);
        pst.executeUpdate();
        query = "insert into rating(userid,orderid,userRole,rating_value,feedback) values(?,?,?,?,?);";
        pst = con.prepareStatement(query);
        pst.setInt(1,userid);
        pst.setInt(2,orderid);
        pst.setString(3,"Receiver");
        pst.setInt(4,deliveryRate);
        pst.setString(5,deliveryfeed);
        pst.executeUpdate();
        JOptionPane.showMessageDialog(null,"Thank you for your feedback");
    }
    public void viewVolunteerRating() throws Exception{
        String query = "select r.userid,r.userRole,r.rating_value,r.feedback from rating r join orders o on r.orderid = o.orderid where volunteer_id = ? and receive_user = 'Volunteer'";
        PreparedStatement pst = con.prepareStatement(query);
        pst.setInt(1,userid);
        ResultSet rs = pst.executeQuery();
        boolean flag = true;
        System.out.println("----------------------------------------------------------");
        while(rs.next()){
            flag = false;
            query = "select name from users where userid = ?";
            PreparedStatement pst1 = con.prepareStatement(query);
            pst1.setInt(1,rs.getInt(1));
            ResultSet r1 = pst1.executeQuery();
            r1.next();
            String name = r1.getString(1);
            System.out.println("Feedback From :"+rs.getString(2)+"           Name : "+name+"               Rating :"+rs.getInt(3)+"            Feedback :  "+rs.getString(4));
        }
        System.out.println("----------------------------------------------------------");
        if(flag){
            JOptionPane.showMessageDialog(null,"No feedback found");
        }
    }
    public void viewDonerRating()throws Exception{
        String query = "select r.rating_value, r.feedback, u.name from rating r "+
                "join orders o on r.orderid = o.orderid " +
                "join donation d on d.foodid = o.foodid " +
                "join users u on o.receiver_id = u.userid " +
                "where d.userid = ? and r.receive_user = 'Doner';";
        PreparedStatement pst = con.prepareStatement(query);
        pst.setInt(1,userid);
        ResultSet rs = pst.executeQuery();
        boolean flag = true;
        System.out.println("----------------------------------------------------------");
        while(rs.next()){
            flag = false;
            String name = rs.getString(3);
            System.out.println("Feedback From :"+"Receiver"+"   Name : "+name+"  Rating :"+rs.getInt(1)+"   Feedback :  "+rs.getString(2));
        }
        System.out.println("----------------------------------------------------------");
        if(flag){
            JOptionPane.showMessageDialog(null,"No feedback found");
        }
    }
}
