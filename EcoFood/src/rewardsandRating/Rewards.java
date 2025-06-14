package rewardsandRating;
import javax.swing.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Scanner;
public class Rewards {
    Connection con;
    Scanner in;
    int userid;

    public Rewards(Connection con, Scanner in,int userid){
        this.con = con;
        this.in = in;
        this.userid = userid;
    }
    public void updatePoints(int point) throws Exception {
        String query = "select points from rewards where userid = ?";
        PreparedStatement pst = con.prepareStatement(query);
        pst.setInt(1,userid);
        ResultSet rs = pst.executeQuery();
        if(rs.next()){
            query = "select points from rewards where userid = ?";
            pst = con.prepareStatement(query);
            pst.setInt(1,userid);
            rs = pst.executeQuery();
            rs.next();
            int newpoint = point + rs.getInt(1);
            query = "update rewards set points = ? where userid = ?";
            pst = con.prepareStatement(query);
            pst.setInt(1,newpoint);
            pst.setInt(2,userid);
            pst.executeUpdate();
            checkBadgeUpgrade(newpoint);
        }
        else{
            query = "insert into rewards(userid,badge_name,points) values(?,?,?);";
            pst = con.prepareStatement(query);
            pst.setInt(1,userid);
            pst.setString(2,"Bronze ");
            pst.setInt(3,point);
            pst.executeUpdate();
        }
    }
    private void checkBadgeUpgrade(int points) throws Exception{
        String badge = "";
        if(points>=200 && points<=400){
            badge = "Silver";
        }
        else if(points>400 && points<=600){
            badge = "Gold";
        }
        else if(points>600 && points<=1000){
            badge = "Diamond";
        }
        else{
            badge = "Platinum";
        }
        String query = "select badge_name from rewards where userid = ?";
        PreparedStatement pst = con.prepareStatement(query);
        pst.setInt(1,userid);
        ResultSet rs = pst.executeQuery();
        rs.next();
        String oldBadge = rs.getString(1);
        if(!oldBadge.equals(badge)){
            JOptionPane.showMessageDialog(null,"congratulation! You got new Badge "+badge);
            query = "update rewards set badge_name = ? where userid = ?";
            pst = con.prepareStatement(query);
            pst.setString(1,badge);
            pst.setInt(2,userid);
            pst.executeUpdate();
        }
    }
    public void viewRewardDetails() throws Exception{
        String query = "select * from rewards where userid = ?;";
        PreparedStatement pst = con.prepareStatement(query);
        pst.setInt(1,userid);
        ResultSet rs = pst.executeQuery();
        if(!rs.next()) {
            JOptionPane.showMessageDialog(null, "No rewards are found");
            return;
        }
        System.out.println("----------------------------");
        System.out.println("Points         Badge Name ");
        System.out.println("----------------------------");
        System.out.println(rs.getInt(4)+"            "+rs.getString(3));
        System.out.println("----------------------------");
    }
}
