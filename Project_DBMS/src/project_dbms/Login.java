

package project_dbms;

import javax.swing.*;
import java.awt.event.*;
import java.sql.*;
import java.awt.*;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Login extends Frame {
    
    TextField Username = new TextField("Username");
    TextField Password = new TextField("Password");
    Button Submit = new Button("Submit");
    Connection theConnection;
    Statement theStatement;
    ResultSet theResult;
    ResultSetMetaData theMetaData;
    JPasswordField passwordText = new JPasswordField(20);
    JTextField userText = new JTextField(20);
    String theDataSource;
    String theUser;
    String thePassword;
    public void init()throws ClassNotFoundException, InstantiationException,IllegalAccessException, UnsupportedLookAndFeelException
    {
        UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
        JFrame frame = new JFrame();
        frame.setTitle("Airline Booking System");
	frame.setSize(300, 150);
        frame.setResizable(false);
	frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
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
	placeComponents(frame);
	frame.setVisible(true);
        
        
    }
    
    private void placeComponents(final JFrame frame)  {

        frame.setLayout(null);

		JLabel userLabel = new JLabel("User");
		userLabel.setBounds(10, 10, 80, 25);
		frame.add(userLabel);

		
		userText.setBounds(100, 10, 180, 25);
		frame.add(userText);

		JLabel passwordLabel = new JLabel("Password");
		passwordLabel.setBounds(10, 40, 80, 25);
		frame.add(passwordLabel);

		
		passwordText.setBounds(100, 40, 180, 25);
		frame.add(passwordText);
                JButton forgot = new JButton("Forgot Password");
                forgot.setBounds(0,80,125,25);
                frame.add(forgot);
                forgot.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
                           JOptionPane.showMessageDialog(null, "Contact Administrator or call our Customer Care Number +918400345757(Non-Toll Free)", "Forgot Password", JOptionPane.INFORMATION_MESSAGE);
                        }
                });
		JButton loginButton = new JButton("Login");
		loginButton.setBounds(130, 80, 60, 25);
		frame.add(loginButton);

		JButton registerButton = new JButton("Register");
		registerButton.setBounds(195, 80, 80, 25);
		frame.add(registerButton);

		loginButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
                                JButton source = (JButton) e.getSource();
                                execSQLCommand("SELECT * FROM PROJECT_LOGIN_DATA WHERE USERNAME = '" + userText.getText() + "'");
                            
                            try {
                                
                                if (theResult.next()) {
                                    //System.out.println(theResult.getString("PASSWORD"));
                                
                                if (passwordText.getText().equals(theResult.getString("PASSWORD"))) {
                                    if (theResult.getString("RIGHTS").equals("Admin")) {
                                        //JOptionPane.showMessageDialog(null, "yoy");
                                        frame.dispose();
                                        Admin o = new Admin();
                                        o.init();
                                    } else if (theResult.getString("RIGHTS").equals("User")) {
                                        frame.dispose();
                                        Client x = new Client();
                                        x.init(userText.getText());
                                    } else if (theResult.getString("RIGHTS").equals("Operator")) {
                                        frame.dispose();
                                        Operator op = new Operator();
                                        op.init(userText.getText());
                                    }
                                } else {
                                    JOptionPane.showMessageDialog(null, "Wrong Username/Password ", "Error Logging In", JOptionPane.ERROR_MESSAGE);
                                }
                                }else {
                                    JOptionPane.showMessageDialog(null, "Wrong Username/Password ", "Error Logging In", JOptionPane.ERROR_MESSAGE);
                                }
                            } catch (SQLException ex) {
                                Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, ex);
                            } catch (ClassNotFoundException ex) {
                                Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, ex);
                            } catch (InstantiationException ex) {
                                Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, ex);
                            } catch (IllegalAccessException ex) {
                                Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, ex);
                            } catch (UnsupportedLookAndFeelException ex) {
                                Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, ex);
                            }
			}
		});
		
		registerButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
                                frame.dispose();
				Register_User r = new Register_User();
                                r.init();
			}
		});
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
        Password.setText("Error: " + e.getMessage());
        e.printStackTrace();
        if (e instanceof SQLException) {
            while ((e = ((SQLException) e).getNextException()) != null) {
                System.out.println(e);
            }
        }
    }
}

class WindowHandler extends WindowAdapter {
    public void windowClosing(WindowEvent event) {
        Object source = event.getSource();
        if (source instanceof Frame) {
            ((Frame) source).setVisible(false);
            System.exit(0);
        }
    }
}
