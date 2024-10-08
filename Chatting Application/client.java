import javax.swing.*;
import java.awt.*;
import javax.swing.border.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.awt.event.*;
import java.net.*;
import java.io.*;
class test extends JFrame implements ActionListener
{
	JTextArea txta;
	DataOutputStream dos;
	DataInputStream dis ;
	JTextField txt;
	JButton btn;
	Socket s;
	public test() 
	{
		setSize(500,500);
		setLayout(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setResizable(false);
		setLocationRelativeTo(null);
		setTitle("CLIENT");
		setVisible(true);
		
		txta = new JTextArea();
		txta.setBounds(15,15,450,350);
		txta.setFont(new Font("MV Boli",Font.PLAIN,25));
		txta.setForeground(Color.blue);
		txta.setBorder(BorderFactory.createLineBorder(new Color(162,0,206),2));
		
		txt = new JTextField();
		txt.setBounds(15,390,400,40);
		txt.setFont(new Font("Serif",Font.PLAIN,20));
		txt.setBorder(BorderFactory.createLineBorder(new Color(162,0,206),2));
		
		btn = new JButton("Send");
		btn.setBounds(415,390,65,40);
		
		add(txta);
		add(txt);
		add(btn);
		
		Border b = BorderFactory.createLineBorder(Color.green,3);
		getRootPane().setBorder(b);
		
		
		try{
		s = new Socket("localhost",4312);	

		dis= new DataInputStream(s.getInputStream());
		dos = new DataOutputStream(s.getOutputStream());
		
		btn.addActionListener(this);
		while(true)
		{
		SimpleDateFormat sd1 = new SimpleDateFormat("hh:mm a");
		String time1 = sd1.format(Calendar.getInstance().getTime());
		String datain = dis.readUTF();
		txta.setText(txta.getText().trim()+"\nSERVER :"+datain+" "+time1);
		}
		}
			catch(Exception exc)
		{
			JOptionPane.showMessageDialog(null,"Server Disconnected","Waring",JOptionPane.WARNING_MESSAGE);
		}
	
	}
	public void actionPerformed(ActionEvent e)
	{
		if(e.getSource()==btn)
		{
			try
			{
					SimpleDateFormat sd = new SimpleDateFormat("hh:mm a");
					String time = sd.format(Calendar.getInstance().getTime());
					String dataout = txt.getText();
					if(dataout.isEmpty()){
						JOptionPane.showMessageDialog(null,"Empty Message Not Permitted To Send","Waring",JOptionPane.WARNING_MESSAGE);
					}
					else{
						txta.setText(txta.getText().trim()+"\n\t ME:"+dataout+" "+time);
						if(dataout.equals("bye")||dataout.equals("end"))
						{
							System.exit(0);
						}
						dos.writeUTF(dataout);
						dos.flush();
						txt.setText(null);
					}
			}
			catch(Exception ex)
			{
				JOptionPane.showMessageDialog(null,"SomeThing Went Wrong","Waring",JOptionPane.INFORMATION_MESSAGE);
			}
		}
	}
}
class client
{
	public static void main(String args[]) throws Exception
	{
		new test();
	}
}
