package SocialFeatures;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class LeaderBoard {
    Connection con;
    public LeaderBoard(Connection con) {
        this.con = con;
        fetchLeaderBoard();
    }
    public void fetchLeaderBoard() {
        String query = "select distinct users.userName,activityTracker.distanceCovered,activityTracker.type from activityTracker join users on users.userId = activityTracker.userId order by distanceCovered desc;";
        try {
            PreparedStatement pst = con.prepareStatement(query);
            ResultSet rs = pst.executeQuery();
            int i = 1;
            while(rs.next()){
                System.out.print ("Position ---->  "+i);
                i++;
                String Name = rs.getString(1);
                double dis = rs.getDouble(2);
                String mode = rs.getString(3);
                System.out.println("   Name : "+Name+" distance : "+dis+" Mode : "+mode);
            }
        }catch (Exception e0){
            e0.printStackTrace();
        }

    }
}
