package ActivityTracker;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;
public class Workout implements ActionListener{
	private final Connection con;
	private final Scanner in;
	private final int userId;
	private String mode;
	public Workout(Connection con,Scanner in,int userId) {
		this.con = con;
		this.in = in;
		this.userId = userId;
	}
	public void start() throws SQLException {
		boolean choice = true;
		while(true) {
			System.out.println("1.Want to workout : ");
			System.out.println("2.Want to check Activity : ");
			System.out.println("3.Exit :");
			char c = in.nextLine().charAt(0);
			if (c == '2') {
				displayDetails();
			}
			else if (c == '1') {
				System.out.println("1.Walking \n2.Running\n3.cycling\n4.swimming");
				char type = in.nextLine().charAt(0);
				switch (type) {
					case '1':
						mode = "Walking";
						startWorkOut(type, "Walking");
						break;
					case '2':
						mode = "Running";
						startWorkOut(type, "Running");
						break;
					case '3':
						mode = "Cycling";
						startWorkOut(type, "Cycling");
						break;
					case '4':
						mode = "Swimming";
						startWorkOut(type, "Swimming");
						break;
					default:
						System.out.println("Invalid Options");
				}
			}
			else if(c == '3'){
				break;
			}
			else{
				System.out.println("Invalid option Try Again");
			}
		}
	}
	JButton btn1;
	JButton btn2;
	JLabel label,label2;
	private final void startWorkOut(int choice,String mode) {
		JFrame frame = new JFrame();
		frame.setTitle(mode);
		frame.setSize(500,300);
		frame.setResizable(false);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.setLayout(null);
		String.valueOf(btn1 = new JButton("Start"));
		btn2 = new JButton("Stop");
		btn1.setBounds(100, 200, 100, 40);
		btn1.addActionListener(this);
		btn2.setBounds(250, 200, 100, 40);
		frame.add(btn2);
		frame.add(btn1);
		btn2.addActionListener(this);
		
		label = new JLabel("00");
		label2 = new JLabel("sec");
		label.setBounds(200, 20, 200, 100);
	    label.setFont(new Font("Arial", Font.BOLD, 50));
		frame.add(label);
		
		frame.setVisible(true);
	}
	int count = 0;
	boolean running = false;
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == btn1) {
            if (!running) {
                running = true;
                Thread timerThread = new Thread(() -> {
                    while (running) {
                        try {
                            Thread.sleep(1000);
                            count++;
                            label.setText(String.valueOf(count));
                        } catch (InterruptedException e1) {
                            e1.printStackTrace();
                        }
                    }
                });
                timerThread.start();
            }
        }
		if(e.getSource() == btn2) {
				running = false;
				int seconds = Integer.parseInt(label.getText());
				label.setText("00");
				count = 0;
				saveWorkOut(seconds);
		}
	}
	private void saveWorkOut(int sec){
		int hour = sec/60;
		int min = sec%60;
		String query = "select userId from activityTracker where type = ? and userid = ?";
		try {
			PreparedStatement pst = con.prepareStatement(query);
			pst.setString(1, mode);
			pst.setInt(2, userId);
			ResultSet rs = pst.executeQuery();
			if (rs.next()) {
				query = "select workouttime from activityTracker where userId = ? and type = ?;";
				pst = con.prepareStatement(query);
				pst.setInt(1, userId);
				pst.setString(2, mode);
				ResultSet resultSet = pst.executeQuery();
				resultSet.next();
				String hours = resultSet.getString(1);
				int oldhour = Integer.parseInt((hours.substring(0, hours.indexOf(":"))).replaceAll("[^\\d+]", ""));
				int oldmin = Integer.parseInt((hours.substring(hours.indexOf(":"))).replaceAll("[^\\d+]", ""));
				hour += oldhour;
				min += oldmin;
				String hour1 = "" + hour + "hr : " + min + " min";
				query = "update activityTracker set workOutTime = ? where userid = ? and type = ?;";
				pst = con.prepareStatement(query);
				pst.setString(1, hour1);
				pst.setInt(2, userId);
				pst.setString(3, mode);
				pst.executeUpdate();
			} else {
				query = "insert into activityTracker (userId, workOutTime, distanceCovered, heartrate, caloriesBurned,type) values(?,?,?,?,?,?);";
				pst = con.prepareStatement(query);
				pst.setInt(1, userId);
				String hour1 = "" + hour + "hr : " + min + " min";
				pst.setString(2, hour1);
				int ran = (int) (Math.random() * 10);
				pst.setInt(3, ran);
				double hr = 60 + (Math.random() * (60 - 100));
				pst.setDouble(4, hr);
				double cal = Math.random() * 50;
				pst.setDouble(5, cal);
				pst.setString(6, mode);
				int r = pst.executeUpdate();
			}
		}
		catch(SQLException e) {
			e.printStackTrace();
		}
	}
	public void displayDetails() throws SQLException {
		System.out.println("-----------------------------------------------------");
		String query = "select * from activityTracker where userId = ?;";
		PreparedStatement pst = con.prepareStatement(query);
		pst.setInt(1,userId);
		ResultSet rs = pst.executeQuery();
		while(rs.next()) {
			System.out.println("Your Id : "+rs.getInt(1)+" ");
			System.out.println("Total WorkOutTime (today) : "+rs.getString(2)+" ");
			System.out.println("Distance Covered : "+rs.getInt(3)+" km");
			System.out.println("HeartRate : "+rs.getDouble(4)+" ");
			System.out.println("CaloriesBurned : "+rs.getDouble(5)+" ");
			System.out.println("WorkoutType : "+rs.getString(6)+" ");
			System.out.println("-----------------------------------------------------");
		}
	}
}
