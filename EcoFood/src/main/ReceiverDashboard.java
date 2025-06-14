package main;
import accountManagement.ManageAccount;
import foodDonationandDiscovery.FoodItem;
import rewardsandRating.Rating;
import java.util.Scanner;
import java.sql.Connection;
public class ReceiverDashboard extends Main {
    private final Connection con;
    private final Scanner in;
    private int userid;
    public ReceiverDashboard(Connection con,Scanner in,int userid){
        this.con = con;
        this.in = in;
        this.userid = userid;
    }
    public void dashBoard() throws Exception{
        boolean check = true;
        FoodItem foodItem = new FoodItem(con,in,userid);
        while(check){
            System.out.println("-----------------------------------------------------");
            System.out.println("1.View Available Food Items ");
            System.out.println("2.Request Food");
            System.out.println("3.Track Request Status & View History ");
            System.out.println("4.Manage Profile ");
            System.out.println("5.Rating and Feedback ");
            System.out.println("6.Exit");
            System.out.print("Enter your choice : ");
            char choice = in.nextLine().charAt(0);
            switch (choice){
                case '1':
                    foodItem.viewAvailableFoods();
                    break;
                case '2':
                    foodItem.requestFood();
                    break;
                case '3':
                    foodItem.trackRequest();
                    break;
                case '4':
                    ManageAccount manageAccount = new ManageAccount(con,in,userid);
                    manageAccount.dashBoard();
                    break;
                case '5':
                    Rating rating = new Rating(con,in,userid);
                    rating.ratingReceiver();
                    break;
                case '6':
                    check = false;
                    break;
                default:
                    System.out.println("Invalid Option try again");
            }
        }
    }
}
