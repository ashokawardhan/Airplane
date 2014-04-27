/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package project_dbms;
import javax.swing.*;
import java.awt.event.*;
import java.sql.*;
import java.awt.*;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
public class Register_User extends Frame {
    JTextField userText = new JTextField(20);
    JPasswordField passwordText = new JPasswordField(20);
    JPasswordField confpasswordText = new JPasswordField(20);
    Statement theStatement;
    ResultSet theResult;
    Connection theConnection;
    String theDataSource;
    JLabel status = new JLabel("");
    void init() {
        JFrame reg = new JFrame("Customer registration");
        reg.setSize(400,200);
        theDataSource = "jdbc:oracle:thin:@localhost:1521:XE";
        try
        {
            Class.forName("oracle.jdbc.driver.OracleDriver");
            theConnection = DriverManager.getConnection(theDataSource, "system", "taurus");
        }
        catch(Exception e)
        {
            handleException(e);
        }
        reg.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        placeComponents(reg);
	reg.setVisible(true);
    }
    
    void placeComponents (final JFrame frame) {
        frame.setLayout(null);
        
        JLabel userLabel = new JLabel("Username");
		userLabel.setBounds(10, 10, 80, 25);
		frame.add(userLabel);

		
		userText.setBounds(120, 10, 160, 25);
		frame.add(userText);

		JLabel passwordLabel = new JLabel("Password");
		passwordLabel.setBounds(10, 40, 80, 25);
		frame.add(passwordLabel);

		
		passwordText.setBounds(120, 40, 160, 25);
		frame.add(passwordText);
                
                JLabel confpasswordLabel = new JLabel("Confirm Password");
		confpasswordLabel.setBounds(10, 70, 140, 25);
		frame.add(confpasswordLabel);

		
		confpasswordText.setBounds(120, 70, 160, 25);
		frame.add(confpasswordText);
                
                
                status.setBounds(10, 110, 150, 25);
                frame.add(status);
                JButton back = new JButton("Back");
		back.setBounds(10, 110, 80, 25);
		frame.add(back);
                JButton loginButton = new JButton("Register");
		loginButton.setBounds(200, 110, 80, 25);
		frame.add(loginButton);
                back.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
              Login o = new Login();
                try {
                    o.init();
//throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                } catch (ClassNotFoundException ex) {
                    Logger.getLogger(Register_User.class.getName()).log(Level.SEVERE, null, ex);
                } catch (InstantiationException ex) {
                    Logger.getLogger(Register_User.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IllegalAccessException ex) {
                    Logger.getLogger(Register_User.class.getName()).log(Level.SEVERE, null, ex);
                } catch (UnsupportedLookAndFeelException ex) {
                    Logger.getLogger(Register_User.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
                });
                ActionListener loginButtonListener = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
                                //System.out.println("e");
				JButton source = (JButton) e.getSource();
				execSQLCommand ("SELECT * FROM PROJECT_LOGIN_DATA WHERE USERNAME = '"+ userText.getText()+"'");
                            try {                                
                                if(!theResult.next()) {
                                    //System.out.println("e" + passwordText.getText() + confpasswordText.getText());
                                    if (Arrays.equals(passwordText.getPassword(),confpasswordText.getPassword())) {
                                        execSQLCommand ("INSERT INTO PROJECT_LOGIN_DATA VALUES('"+ userText.getText()+"','" + passwordText.getText()+ "','User')");
                                        JOptionPane.showMessageDialog(null, "Registered successfully", "Registration", JOptionPane.INFORMATION_MESSAGE);
                                    } else {
                                        JOptionPane.showMessageDialog(null, "Passwords don't match", "Registration", JOptionPane.ERROR_MESSAGE);
                                    }
                               } else {
                                    JOptionPane.showMessageDialog(null, "Username already exists", "Registration", JOptionPane.ERROR_MESSAGE);
                                }
                            } catch (SQLException ex) {
                                Logger.getLogger(Register_User.class.getName()).log(Level.SEVERE, null, ex);
                            }
			}
		};
                
                loginButton.addActionListener(loginButtonListener);
    }
    
    public void execSQLCommand(String command)
    {
        try
        {
            theStatement = theConnection.createStatement();
            theResult = theStatement.executeQuery(command);
        }
        catch (Exception e)
        {
            handleException(e);
        }
    }
    public void handleException(Exception e)
    {
        e.printStackTrace();
        if (e instanceof SQLException) {
            while ((e = ((SQLException) e).getNextException()) != null) {
                System.out.println(e);
            }
        }
    }
}