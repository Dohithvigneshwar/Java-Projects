package foodDonationandDiscovery;

import javax.swing.*;
import java.sql.*;
import java.util.HashSet;
import java.util.Scanner;
public class FoodItem implements foods{
    Connection con;
    Scanner in;
    int userid;
    private int foodid;
    private HashSet<Integer> set = new HashSet<>();
    public FoodItem(Connection con, Scanner in,int userid) throws Exception{
        this.con = con;
        this.in = in;
        this.userid = userid;
        updateFoodExpired();
    }
    @Override
    public void updateFood(String foodname, double quantity, String category, String status, Date expireDate) throws Exception {
        String query = "insert into fooditem(food_name,category,quantity,food_status,ExpireDate) values(?,?,?,?,?);";
        PreparedStatement pst = con.prepareStatement(query);
        pst.setString(1,foodname);
        pst.setString(2,category);
        pst.setDouble(3,quantity);
        pst.setString(4,status);
        pst.setDate(5,expireDate);
        pst.executeUpdate();
        query = "select foodid from fooditem where food_name = ? and category = ? and quantity = ?;";
        pst = con.prepareStatement(query);
        pst.setString(1,foodname);
        pst.setString(2,category);
        pst.setDouble(3,quantity);
        ResultSet rs = pst.executeQuery();
        if(rs.next()){
            foodid = rs.getInt(1);
        }

    }
    public int getFoodid(){
        return foodid;
    }
    @Override
    public void viewAvailableFoods() throws Exception{
        String query = "select city from users where userid = ?";
        PreparedStatement pst = con.prepareStatement(query);
        pst.setInt(1,userid);
        ResultSet rs = pst.executeQuery();
        rs.next();
        String receiverCity = rs.getString(1);
        query = "select * from fooditem where foodid in (select foodid from donation where userid in (select userid from users where city = ? )) having isAvailable = 'YES';";
        pst = con.prepareStatement(query);
        pst.setString(1,receiverCity);
        rs = pst.executeQuery();
        boolean flag = true;
        System.out.println("Food Items Available:");
        System.out.println("-------------------------------------------------------------------------------------------------------");
        System.out.println("Food ID    Food Name           Category           Quantity(kg)    Food Status          ExpireDate");
        System.out.println("--------------------------------------------------------------------------------------------------------");
        while(rs.next()){
            flag = false;
            int foodId = rs.getInt(1);
            set.add(foodId);
            String foodName = rs.getString(2);
            String category = rs.getString(3);
            double quantity = rs.getDouble(4);
            String foodStatus = rs.getString(5);
            Timestamp tm = rs.getTimestamp(6);
            System.out.println(foodId + "          " + foodName + "                 " + category + "                 " + quantity + "            " + foodStatus+"                   "+tm);
        }
        if(flag){
            JOptionPane.showMessageDialog(null,"Sorry currently no available food");
            return;
        }
        JOptionPane.showMessageDialog(null,"Please make sure to note down the Food ID, as it is required to book the food item.","Information",JOptionPane.INFORMATION_MESSAGE);
    }
    @Override
    public void requestFood() throws Exception {
        if (set.isEmpty()) {
            System.out.println("Please check the available food");
            return;
        }
        System.out.print("Enter the Food Id to place your Order : ");
        int id = in.nextLine().charAt(0) - '0';
        if (set.contains(id)) {
            int input = JOptionPane.showConfirmDialog(null, "Are You Sure to Order ? ", "Confirmation", JOptionPane.YES_NO_OPTION);
            if (input == 0) {
                String query = "insert into orders(foodid,receiver_id) values (?,?)";
                PreparedStatement pst = con.prepareStatement(query);
                pst.setInt(1, id);
                pst.setInt(2, userid);
                pst.executeUpdate();
                JOptionPane.showMessageDialog(null, "Order placed! Please wait a moment, the delivery person will pick up your order and deliver it soon");
                query = "update fooditem set isAvailable = 'No' where foodid = ?";
                pst = con.prepareStatement(query);
                pst.setInt(1, id);
                pst.executeUpdate();
            } else {
                return;
            }
        } else {
            System.out.println("No Food item found....");
        }
    }
    @Override
    public void trackRequest() throws Exception {
        String query = "select foodid,order_status from orders where receiver_id = ? order by foodid desc";
        PreparedStatement pst = con.prepareStatement(query);
        pst.setInt(1, userid);
        ResultSet rs = pst.executeQuery();
        boolean flag = false;
        while (rs.next()) {
            flag = true;
            foodid = rs.getInt(1);
            String status = rs.getString(2);
            query = "select orderid from orders where foodid = ?";
            PreparedStatement pst1 = con.prepareStatement(query);
            pst1.setInt(1,foodid);
            ResultSet rs1 = pst1.executeQuery();
            rs1.next();
            int orderid = rs1.getInt(1);
            query = "select food_name from fooditem where foodid = ?";
            PreparedStatement pst2 = con.prepareStatement(query);
            pst2.setInt(1,foodid);
            ResultSet rs2 = pst2.executeQuery();
            rs2.next();
            String foodname = rs2.getString(1);
            if (status.equalsIgnoreCase("Pending")) {
                System.out.println("Order id : " + orderid +" Food Name: "+foodname+".........Order pending! Please wait patiently, the delivery person will pick it up soon");
            } else {
                if (status.equalsIgnoreCase("picked")) {
                    System.out.println("Order id : " + orderid +" Food Name: "+foodname+ ".......Your order has been picked up by the delivery person and will reach you soon");
                }
                else{
                    System.out.println("Order id : " + orderid +" Food Name: "+foodname+ ".......Your order has been successfully delivered to your address.");
                }
            }
        }
        if(!flag){
            System.out.println("no order details found");
        }
    }
    public void updateFoodExpired() throws Exception{
        String query = "update fooditem set food_status = 'Expired' , isAvailable = 'No'  where ExpireDate<curdate();";
        PreparedStatement pst = con.prepareStatement(query);
        pst.executeUpdate();
    }
}
