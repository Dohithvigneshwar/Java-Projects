package Sign_IN_UP;

import java.sql.*;
import java.util.*;
public class Registration{
	private Connection con;
	private Scanner in;
	public Registration(Connection con,Scanner in) {
		this.con = con;
		this.in = in;
	}
	public boolean registerUser(){
		try {
			System.out.println("Welcome to OurApplication");
			String username = "";
			while(true) {
				System.out.println("UserName : ");
				username = in.nextLine();
				String query = "select username from users where username = ?;";
				PreparedStatement pst = con.prepareStatement(query);
				pst.setString(1,username);
				ResultSet rs = pst.executeQuery();
				if(rs.next()) {
					System.out.println("User already Exits....try another user name.....");
				}
				else {
					break;
				}
			}
			System.out.println("Password :");
			String password = in.nextLine();
			System.out.println("Email :");
			String email = in.nextLine();
			System.out.println("Age :");
			int age = in.nextInt();
			System.out.print("Phone no :");
			long no = in.nextLong();
			System.out.println("height :");
			double height = in.nextDouble();
			System.out.println("Weight :");
			double weight = in.nextDouble();
			in.nextLine();
			System.out.println("Fitness Goal :");
			String goal = in.nextLine();
			
			String query = "insert into users(userName,passWord,email,age,phoneno,height,weight,fitnessGoal) values(?,?,?,?,?,?,?,?);";
			PreparedStatement pst = con.prepareStatement(query);
			pst.setString(1,username);
			pst.setString(2,password);
			pst.setString(3,email);
			pst.setInt(4,age);
			pst.setLong(5,no);
			pst.setDouble(6,height);
			pst.setDouble(7,weight);
			pst.setString(8,goal);
			pst.executeUpdate();
			pst.close();
		}
		catch(Exception e) {
			return false;
		}
		return true;
	}
}
