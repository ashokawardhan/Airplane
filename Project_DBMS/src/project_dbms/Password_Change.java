/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package project_dbms;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;

/**
 *
 * @author thedarkone
 */
class Password_Change {
    Statement theStatement;
    ResultSet theResult;
    Connection theConnection;
    String theDataSource;
    JLabel status = new JLabel("");
    JPasswordField p3 = new JPasswordField(25);
    JPasswordField p2 = new JPasswordField(25);
    JPasswordField p1 = new JPasswordField(25);
    public void change(String user){
        final String y = user;
        final JFrame frame4 = new JFrame("Change Password");
        frame4.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        JLabel label1 = new JLabel("Old Password");
       
        JLabel label2 = new JLabel("New Password");
        
        JLabel label3 = new JLabel("Confirm Password");
        JButton b2 = new JButton("Back");
        JButton b1 = new JButton("Apply Changes");
        
        
            p1.setSize(20,20);
            p2.setSize(20,20);
            p3.setSize(20,20);
        frame4.add(label1);
        frame4.add(p1);
        frame4.add(label2);
        frame4.add(p2);
        frame4.add(label3);
        frame4.add(p3);
        frame4.add(status);
        b1.setPreferredSize(new Dimension(180,40));
        frame4.add(b2,BorderLayout.SOUTH);  
        frame4.add(b1);
        frame4.setLayout(new FlowLayout());
        frame4.setVisible(true);
        frame4.setResizable(false);
        frame4.setSize(320,230);
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
        b2.addActionListener(new ActionListener() {
                                @Override
                                public void actionPerformed(ActionEvent ae) {
                                    frame4.dispose();
                                }
        });
        ActionListener b1ButtonListener = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
                                //System.out.println("e");
				JButton source = (JButton) e.getSource();
				execSQLCommand ("SELECT * FROM PROJECT_LOGIN_DATA WHERE USERNAME = '" + y + "'");
                            
                            try {              
                                
                                if(theResult.next()) {
                                    if (p1.getText().equals(theResult.getString("PASSWORD")) ) {
                                    //System.out.println("e" + passwordText.getText() + confpasswordText.getText());
                                    if (Arrays.equals(p2.getPassword(),p3.getPassword())) {
                                       execSQLCommand ("UPDATE PROJECT_LOGIN_DATA SET PASSWORD = '"+ p2.getText() +"' WHERE USERNAME = '" + y + "'");
                                       // execSQLCommand ("INSERT INTO PROJECT_DATA_ENTRY_OPERATOR(EMPLOYEE_NAME, PHONE, TERMINAL, GENDER) VALUES('"+ nameText.getText() +"','" + phoneText.getText() +"','" + terminalText.getText() + "','" + box.getSelectedItem() + "')");
                                        JOptionPane.showMessageDialog(null, "Password changed", "Password Change", JOptionPane.INFORMATION_MESSAGE);
                                    } else {
                                        JOptionPane.showMessageDialog(null, "Password doesn't match", "Password Change", JOptionPane.ERROR_MESSAGE);
                                    }
                                    } else {
                                    JOptionPane.showMessageDialog(null, "Wrong Password", "Password Change", JOptionPane.ERROR_MESSAGE);
                               }
                               } 
                            } catch (SQLException ex) {
                                Logger.getLogger(Register_User.class.getName()).log(Level.SEVERE, null, ex);
                            }
			}
		};
                
                b1.addActionListener(b1ButtonListener);
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
