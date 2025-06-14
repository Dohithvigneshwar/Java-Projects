package userRegistrationAndAuthentication;
import javax.swing.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Scanner;
public class SignUp {
    private Connection con;
    private Scanner in;
    public SignUp(Connection con,Scanner in){
        this.con = con;
        this.in = in;
    }
    public void createUser() throws Exception{
        String userName = "";
        PreparedStatement pst;
        while(true) {
            System.out.print("Enter the UserName : ");
            userName = in.nextLine();
            if(userName.isEmpty()){
                System.out.println("Invalid Username try again");
                continue;
            }
            pst = con.prepareStatement("select userName from users where userName = ?;");
            pst.setString(1,userName);
            ResultSet rs = pst.executeQuery();
            if(rs.next()){
                JOptionPane.showMessageDialog(null,"user name already exists try another userName","Warning", JOptionPane.WARNING_MESSAGE);
            }
            else{
                break;
            }
        }
        System.out.print("Enter the Password : ");
        String password = in.nextLine();
        System.out.print("Enter Your fullName : ");
        String name = in.nextLine();
        long no;
        while(true) {
            System.out.print("Enter the PhoneNo : ");
            String phoneNo = in.nextLine().trim();
            String regex = "[6789]\\d{9}$";
            if(phoneNo.matches(regex)) {
                no = Long.parseLong(phoneNo);
                break;
            }
            else{
                System.out.println("Invalid Phone No try again");
            }
        }
        System.out.print("Enter the EmailId (optional) : ");
        String email = in.nextLine();
        System.out.print("Enter the City (district): ");
        String city = in.nextLine();
        String role = "";
        while(true) {
            System.out.print("Enter your Role (Donor/Receiver/Volunteer) :");
            char c = in.nextLine().charAt(0);
            if (c == 'd' || c == 'D'){
                role = "Donor";
            }
            else if(c == 'R' || c == 'r'){
                role = "Receiver";
            }
            else if(c == 'V' || c == 'v'){
                role = "Voluenteer";
            }
            else{
                System.out.println("Invalid Option try again !");
                continue;
            }
            break;
        }
        String query = "insert into users(username,password,name,phoneno,email,city,role) values(?,?,?,?,?,?,?);";
        pst = con.prepareStatement(query);
        pst.setString(1,userName);
        pst.setString(2,password);
        pst.setString(3,name);
        pst.setLong(4,no);
        pst.setString(5,email);
        pst.setString(6,city);
        pst.setString(7,role);
        pst.executeUpdate();
        JOptionPane.showMessageDialog(null,"Account created please log in");
    }
}
