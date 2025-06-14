package main;
import accountManagement.ManageAccount;
import foodDonationandDiscovery.Donation;
import rewardsandRating.Rating;
import rewardsandRating.Rewards;
import java.util.Scanner;
import java.sql.Connection;
public class DonorDashboard extends Main{
    private final Scanner in;
    private final Connection con;
    private final int userId;
    public DonorDashboard(Connection con, Scanner in,int userId){
        this.con = con;
        this.userId = userId;
        this.in = in;
    }
    public void dashBoard() throws Exception{
        boolean check = true;
        Donation donation = new Donation(con,in,userId);
        Rating rating = new Rating(con,in,userId);
        while(check){
            System.out.println("***************************************");
            System.out.println("1.Food Donation ");
            System.out.println("2.Donation History");
            System.out.println("3.Manage Profile ");
            System.out.println("4.Rewards and Achievements ");
            System.out.println("5.Send Ratings and Feedback");
            System.out.println("6.View Rating and Feedback");
            System.out.println("7.Exit");
            System.out.print("Enter your choice : ");
            char choice = in.nextLine().charAt(0);
            switch (choice){
                case '1':
                    donation.donateFood();
                    break;
                case '2':
                    donation.fetchHistory();
                    break;
                case '3':
                    ManageAccount manageAccount = new ManageAccount(con,in,userId);
                    manageAccount.dashBoard();
                    break;
                case '4':
                    Rewards rewards = new Rewards(con,in,userId);
                    rewards.viewRewardDetails();
                    break;
                case '5':
                    rating.ratingDonerSite();
                    break;
                case '6':
                    rating.viewDonerRating();
                    break;
                case '7':
                    check = false;
                    break;
                default:
                    System.out.println("Invalid Option try again");
            }
        }
    }
}
