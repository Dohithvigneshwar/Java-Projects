package main;
import accountManagement.ManageAccount;
import orderManagement.OrderManagement;
import rewardsandRating.Rating;
import rewardsandRating.Rewards;
import java.sql.Connection;
import java.util.Scanner;
public class VolunteerDashboard extends Main {
    private final Scanner in;
    private final Connection con;
    private final int userId;
    public VolunteerDashboard(Connection con, Scanner in, int userId){
        this.con = con;
        this.userId = userId;
        this.in = in;
    }
    public void dashBoard() throws Exception{
        OrderManagement orderManagement = new OrderManagement(con,in,userId);
        boolean check = true;
        while(check){
            System.out.println("***************************************");
            System.out.println("1. View Order Request");
            System.out.println("2. Pick Up Food");
            System.out.println("3. Manage Profile");
            System.out.println("4. Rewards and Achievements");
            System.out.println("5. View Rating and feedback");
            System.out.println("6. Exit");
            System.out.print("Enter your choice : ");
            char choice = in.nextLine().charAt(0);
            switch (choice){
                case '1':
                    orderManagement.viewOrderRequest();
                    break;
                case '2':
                    orderManagement.pickUp();
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
                    Rating rating = new Rating(con,in,userId);
                    rating.viewVolunteerRating();
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
