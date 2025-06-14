package FeedbackAndRating;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.Scanner;

public class FeedBack {
    Connection con;
    Scanner in;
    public FeedBack(Connection con,Scanner in){
        this.con = con;
        this.in = in;
    }
    public void sendFeedBack(int userId,String userName){
        System.out.println("==================================");
        System.out.println("Describe your feedback : ");
        String feed = in.nextLine();
        System.out.println("Can you provide rating (1-10) :");
        String rating = in.nextLine();
        try{
            String query = "insert into feedbacks(custId,userName,feedback,rating) values(?,?,?,?)";
            PreparedStatement pst = con.prepareStatement(query);
            pst.setInt(1,userId);
            pst.setString(2,userName);
            pst.setString(3,feed);
            pst.setInt(4,Integer.parseInt(rating));
            pst.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
