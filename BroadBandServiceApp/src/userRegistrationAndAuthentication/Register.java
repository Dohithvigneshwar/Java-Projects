package userRegistrationAndAuthentication;
import javax.swing.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Scanner;
import java.util.regex.Pattern;

public class Register {
    private final Connection con;
    private final Scanner in;
    private String query;
    private PreparedStatement pst;
    public Register(Connection con, Scanner in){
        this.con =  con;
        this.in = in;
    }
    public void accountRegister() throws Exception{
        String userName = "";
        while(true) {
            System.out.println("Enter the UserName : ");
            userName = in.nextLine();
            pst = con.prepareStatement("select userName from customers where userName = ?;");
            pst.setString(1,userName);
            ResultSet rs = pst.executeQuery();
            if(rs.next()){
                JOptionPane.showMessageDialog(null,"user name already exists try another userName","Warning", JOptionPane.WARNING_MESSAGE);
            }
            else{
                break;
            }
        }
        System.out.println("Enter the Password : ");
        String password = in.nextLine();
        long no;
        while(true) {
            System.out.println("Enter the PhoneNo : ");
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
        System.out.println("Enter the EmailId (optional) : ");
        String email = in.nextLine();
        System.out.println("Enter the City : ");
        String city = in.nextLine();
        query = "insert into customers(userName,password,phoneNo,emailId,city) values(?,?,?,?,?);";
        pst = con.prepareStatement(query);
        pst.setString(1,userName);
        pst.setString(2,password);
        pst.setLong(3,no);
        pst.setString(4,email);
        pst.setString(5,city);
        int r = pst.executeUpdate();
        if(r>0){
            System.out.println("Account Created");
        }
        else{
            System.out.println("Something went wrong in query");
        }
    }
}
