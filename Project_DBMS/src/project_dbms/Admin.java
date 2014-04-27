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
public class Admin {
    Connection theConnection;
    Statement theStatement;
    ResultSet theResult;
    ResultSetMetaData theMetaData;
    public void init()
    {
        JFrame frame = new JFrame();
        
        frame.setTitle("Admin control");
        frame.setSize(300,150);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	placeComponents(frame);
	frame.setVisible(true);
    }
    
    private static void placeComponents(final JFrame frame) {
		frame.setLayout(null);

		JButton loginButton = new JButton("Create New User");
		loginButton.setBounds(10, 30, 180, 35);
		frame.add(loginButton);

		JButton registerButton = new JButton("Sign Out");
		registerButton.setBounds(180, 80, 100, 25);
		frame.add(registerButton);
                JButton back = new JButton("Back");
                back.setBounds(30,80,60,25);
                //frame.add(back);
                back.addActionListener(new ActionListener(){

                    @Override
                    public void actionPerformed(ActionEvent e) {
                        frame.dispose();
                        Login log = new Login();
                        try {
                            log.init();
                        } catch (ClassNotFoundException ex) {
                            Logger.getLogger(Admin.class.getName()).log(Level.SEVERE, null, ex);
                        } catch (InstantiationException ex) {
                            Logger.getLogger(Admin.class.getName()).log(Level.SEVERE, null, ex);
                        } catch (IllegalAccessException ex) {
                            Logger.getLogger(Admin.class.getName()).log(Level.SEVERE, null, ex);
                        } catch (UnsupportedLookAndFeelException ex) {
                            Logger.getLogger(Admin.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                    }
                    
                });
		loginButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
                                frame.dispose();
				Register_Operator o = new Register_Operator();
                                o.init();
			}
		});
		
		registerButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				frame.dispose();
                                Login x = new Login();
                            try {
                                x.init();
                            } catch (ClassNotFoundException ex) {
                                Logger.getLogger(Admin.class.getName()).log(Level.SEVERE, null, ex);
                            } catch (InstantiationException ex) {
                                Logger.getLogger(Admin.class.getName()).log(Level.SEVERE, null, ex);
                            } catch (IllegalAccessException ex) {
                                Logger.getLogger(Admin.class.getName()).log(Level.SEVERE, null, ex);
                            } catch (UnsupportedLookAndFeelException ex) {
                                Logger.getLogger(Admin.class.getName()).log(Level.SEVERE, null, ex);
                            }
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
        
        e.printStackTrace();
        if (e instanceof SQLException) {
            while ((e = ((SQLException) e).getNextException()) != null) {
                System.out.println(e);
            }
        }
    }
}
