package Sign_IN_UP;
import javax.swing.*;
import java.util.*;
import java.sql.*;
public class LogIn {
	Connection con;
	Scanner in;
	public LogIn(Connection con,Scanner in) {
		this.con = con;
		this.in = in;
	}
	boolean validUser(String user,String pass) throws Exception{
		String query = "select username,password from users where username = ? and password = ?;";
		PreparedStatement pst = con.prepareStatement(query);
		pst.setString(1,user);
		pst.setString(2,pass);
		ResultSet rs = pst.executeQuery();
		if(rs.next()==false) {
			JOptionPane.showMessageDialog(null,"Account doesn't exist","Warning",JOptionPane.WARNING_MESSAGE);
			return false;
		}
		return true;
	}
}
