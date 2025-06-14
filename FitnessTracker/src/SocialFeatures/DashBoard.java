package SocialFeatures;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;
public class DashBoard {
    Connection con;
    Scanner in;
    String userName;
    int userid;
    public DashBoard(Connection con,Scanner in,String userName,int userId){
        this.con = con;
        this.in = in;
        this.userid = userId;
        this.userName = userName;
        this.viewMenu();
    }
    public void viewMenu(){
        while(true){
            System.out.println("Social Features..... :) !");
            System.out.println("1.Check Friends Request :");
            System.out.println("2.Send Friend Request :");
            System.out.println("3.Share Workout Achievement With Friends :");
            System.out.println("4.Check Overall Leaderboard :");
            System.out.println("5.Exit");
            String option = in.nextLine();
            char c = option.charAt(0);
            if(c == '1'){
                    CheckRequest cr = new CheckRequest(con,in);
                    cr.check(userName);
            }
            else if(c=='2'){
                System.out.println("Enter your friend userName :");
                String friendname  = in.nextLine();
                SendRequest sd = new SendRequest(con,in);
                sd.send(userName,friendname);
            }
            else if(c=='3'){
                try{
                    String query = "select max(distanceCovered) from activityTracker where userid = ?;";
                    PreparedStatement pst = con.prepareStatement(query);
                    pst.setInt(1,userid);
                    ResultSet rs = pst.executeQuery();
                    if(rs.next()) {
                        double max = rs.getDouble(1);
                        String msg = "hey! my maximum distance is "+max;
                        query = "update friendsDetails set message = ? where userid = ?;";
                        pst = con.prepareStatement(query);
                        pst.setString(1,msg);
                        pst.setString(2,userName);
                        int r = pst.executeUpdate();
                        if(r>0){
                            System.out.println("Achievement shared your friends");
                        }
                    }
                    else{
                        System.out.println("No achievement found");
                    }
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
            else if(c=='4'){
                LeaderBoard lb = new LeaderBoard(con);
            }
            else if(c=='5'){
                break;
            }
            else{
                System.out.println("Invalid Option try Again");
            }
        }
    }

}
