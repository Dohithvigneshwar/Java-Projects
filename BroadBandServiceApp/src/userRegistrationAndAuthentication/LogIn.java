package userRegistrationAndAuthentication;
import javax.swing.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class LogIn {
    Connection con;
    public LogIn(Connection con){
        this.con = con;
    }
    public boolean validUser(String userName,String password) throws Exception{
        String query = "select userName from customers where userName = ? and password = ?";
        PreparedStatement pst = con.prepareStatement(query);
        pst.setString(1,userName);
        pst.setString(2,password);
        ResultSet rs = pst.executeQuery();
        if(rs.next()){
            return true;
        }
        return false;
    }
}
