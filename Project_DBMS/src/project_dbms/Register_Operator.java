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
public class Register_Operator extends Frame {
    JTextField userText = new JTextField(20);
    JTextField nameText = new JTextField(20);
    JTextField phoneText = new JTextField(20);
    JTextField terminalText = new JTextField(20);
    DefaultComboBoxModel genderText = new DefaultComboBoxModel();
    
    JPasswordField passwordText = new JPasswordField(20);
    JPasswordField confpasswordText = new JPasswordField(20);
    Statement theStatement;
    ResultSet theResult;
    Connection theConnection;
    String theDataSource;
    JLabel status = new JLabel("");
    void init() {
        JFrame frame = new JFrame();
        //JFrame reg = new JFrame("Customer registration");
        frame.setSize(800,600);
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
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        placeComponents(frame);
	frame.setVisible(true);
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
                
                JLabel nameLabel = new JLabel("Name:");
                nameLabel.setBounds(10,100,140,25);
                frame.add(nameLabel);
                nameText.setBounds(120,100,160,25);
                frame.add(nameText);
                JLabel phoneLabel = new JLabel("Phone No:");
                phoneLabel.setBounds(10,130,140,25);
                frame.add(phoneLabel);
                phoneText.setBounds(120,130,160,25);
                frame.add(phoneText);
                JLabel terminalLabel = new JLabel("Terminal:");
                terminalLabel.setBounds(10,160,140,25);
                frame.add(terminalLabel);
                terminalText.setBounds(120,160,160,25);
                frame.add(terminalText);
                
                JLabel genderLabel = new JLabel("Gender:");
                genderLabel.setBounds(10,190,140,25);
                frame.add(genderLabel);
                genderText.addElement("Male");
                genderText.addElement("Female");
                final JComboBox box = new JComboBox(genderText);
                box.setBounds(120,190,140,25);
                frame.add(box);
                
                status.setBounds(10, 230, 150, 25);
                frame.add(status);
                JButton loginButton = new JButton("Create");
		loginButton.setBounds(300, 90, 80, 25);
                JButton back = new JButton("Back");
		JButton so = new JButton("Sign Out");
                back.setBounds(400,20,80, 25);
                so.setBounds(500,20,80,25);
                
                frame.add(loginButton);
                frame.add(back);
                //frame.add(so);
                back.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
              Admin o = new Admin();
                o.init();
//throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }
                    
                });
                so.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
                Login m = new Login();
                try {
                    m.init();
                    //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                } catch (ClassNotFoundException ex) {
                    Logger.getLogger(Register_Operator.class.getName()).log(Level.SEVERE, null, ex);
                } catch (InstantiationException ex) {
                    Logger.getLogger(Register_Operator.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IllegalAccessException ex) {
                    Logger.getLogger(Register_Operator.class.getName()).log(Level.SEVERE, null, ex);
                } catch (UnsupportedLookAndFeelException ex) {
                    Logger.getLogger(Register_Operator.class.getName()).log(Level.SEVERE, null, ex);
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
                                        execSQLCommand ("INSERT INTO PROJECT_LOGIN_DATA VALUES('"+ userText.getText()+"','" + passwordText.getText()+ "','Operator')");
                                        execSQLCommand ("INSERT INTO PROJECT_DATA_ENTRY_OPERATOR(EMPLOYEE_NAME, PHONE, TERMINAL, GENDER) VALUES('"+ nameText.getText() +"','" + phoneText.getText() +"','" + terminalText.getText() + "','" + box.getSelectedItem() + "')");
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