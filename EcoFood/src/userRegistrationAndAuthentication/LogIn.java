package userRegistrationAndAuthentication;
import javax.swing.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
public class LogIn {
    private Connection con;
    public LogIn(Connection con) {
        this.con = con;
    }
    public boolean isValidUser(String userName,String password) throws Exception{
        String query = "select userId from users where username = ? and password = ?;";
        PreparedStatement pst = con.prepareStatement(query);
        pst.setString(1,userName);
        pst.setString(2,password);
        ResultSet rs = pst.executeQuery();
        if(rs.next()) {
            JOptionPane.showMessageDialog(null, "LogIn Success", "Information", JOptionPane.INFORMATION_MESSAGE);
            return true;
        }
        JOptionPane.showMessageDialog(null,"Invalid UserId and Password","Warning",JOptionPane.WARNING_MESSAGE);
        return false;
    }
}
