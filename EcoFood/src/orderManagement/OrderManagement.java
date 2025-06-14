package orderManagement;
import rewardsandRating.Rewards;
import javax.swing.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Scanner;
public class OrderManagement {
    private final Scanner in;
    private final Connection con;
    private final int userId;
    private HashSet<Integer> set = new HashSet<>();
    public OrderManagement(Connection con, Scanner in, int userId){
        this.con = con;
        this.userId = userId;
        this.in = in;
    }
    public void viewOrderRequest() throws Exception{
        String query = "select city from users where userid = ?";
        PreparedStatement pst = con.prepareStatement(query);
        pst.setInt(1,userId);
        ResultSet rs = pst.executeQuery();
        rs.next();
        String volunteerCity = rs.getString(1);
        query = "select * from orders where receiver_id in (select userid from users where city = ? ) having order_status = 'Pending';";
        pst = con.prepareStatement(query);
        pst.setString(1,volunteerCity);
        rs = pst.executeQuery();
        boolean flag = false;
        System.out.println("Order id               food id               receiver id                 orderDate");
        while(rs.next()){
            flag = true;
            Timestamp tm = rs.getTimestamp(6);
            set.add(rs.getInt(1));
            System.out.println(rs.getInt(1)+"                         "+rs.getInt(2)+"                         "+rs.getInt(3)+"                     "+tm);
        }
        if(!flag) {
            JOptionPane.showMessageDialog(null, "No order found");
            return;
        }
    }
    public void pickUp() throws Exception{
        if(set.isEmpty()){
            System.out.println("Please check the available order");
            return;
        }
        System.out.print("Enter the order Id to pickup your Order : ");
        int id = in.nextLine().charAt(0)-'0';
        if(set.contains(id)){
            String query = "update orders set order_status = 'picked' where orderid = ?";
            PreparedStatement pst = con.prepareStatement(query);
            pst.setInt(1,id);
            pst.executeUpdate();
            query = "update orders set volunteer_id = ? where orderid = ?";
            pst = con.prepareStatement(query);
            pst.setInt(1,userId);
            pst.setInt(2,id);
            pst.executeUpdate();
            boolean flag = true;
            for(int i=0;i<10;i++){
                System.out.print(". ");
                Thread.sleep(1200);
            }
            System.out.println();
            JOptionPane.showMessageDialog(null,"Order Delivered!");
            query = "update orders set order_status = 'delivered' where orderid = ?";
            pst = con.prepareStatement(query);
            pst.setInt(1,id);
            int r = pst.executeUpdate();
            Rewards rewards = new Rewards(con,in,userId);
            rewards.updatePoints(150);
        }
        else{
            System.out.println("No order id found....");
        }
    }
}
