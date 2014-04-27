/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package project_dbms;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.util.List;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JScrollBar;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.*;
import javax.swing.plaf.basic.BasicComboBoxEditor;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;
public class Operator {
    Connection theConnection;
    Statement theStatement;
    ResultSet theResult;
    ResultSetMetaData theMetaData;
    
    String theDataSource;
    String theUser;
    String thePassword;
    void init(final String x) {
        final JFrame frame = new JFrame("Operator");
        frame.setSize(300, 150);
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
        

        JButton chfl = new JButton(" Change Flight Data ");
        //chfl.setSize(20, 30);
        frame.add(chfl);
        
        JButton addfl = new JButton("     Add Flight Data     ");
        frame.add(addfl);
        JButton passenger = new JButton("Get Passenger Data");
        frame.add(passenger);
        JButton chpass = new JButton(" Change Password  ");
        frame.add(chpass);
        JButton but4 = new JButton("          Sign Out          ");
        frame.add(but4);
        
        frame.setLayout(new FlowLayout(5,60,20));
        
        frame.setSize(250, 300);
        frame.setResizable(false);
        frame.setVisible(true);
        but4.addActionListener(new ActionListener(){
        
            @Override
            public void actionPerformed(ActionEvent ae) {
                frame.dispose();
                Login lo = new Login();
                try {
                    lo.init();
                } catch (ClassNotFoundException ex) {
                    Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
                } catch (InstantiationException ex) {
                    Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IllegalAccessException ex) {
                    Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
                } catch (UnsupportedLookAndFeelException ex) {
                    Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        chpass.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
                            
                            Password_Change pc = new Password_Change();
                            pc.change(x);
                        }
        });
        chfl.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
                                JButton source = (JButton) e.getSource();
                                frame.dispose();
                                final JFrame frame2 = new JFrame("Change Flight Data");
                                frame2.setSize(250,250);
                                frame2.setLayout(new FlowLayout());
                                frame2.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                                JLabel flno = new JLabel("Flight No");
                                final JTextField jtflno = new JTextField(20);
              
                                frame2.add(flno);
                                frame2.add(jtflno);
                                JButton back = new JButton("Back");
                                frame2.add(back);
                                back.addActionListener(new ActionListener(){
                                                                            
                                    @Override
                                    public void actionPerformed(ActionEvent e) {
                                        frame2.dispose();
                                                     
                                        Operator op = new Operator();
                                        op.init(x);
                                    }
                                                                           
                               });
                                JButton chdata = new JButton ("Change");
                                frame2.add(chdata);
                                frame2.setResizable(false);
                                frame2.setVisible(true);
                                chdata.addActionListener(new ActionListener() {
                                    @Override
                                    public void actionPerformed(ActionEvent e) {
                                        execSQLCommand("SELECT * FROM PROJECT_FLIGHT WHERE FLIGHT_NO = '" + jtflno.getText() + "'");
                                        try {
                                            if (theResult.next()) {
                                                
                                                final JFrame frame3 = new JFrame();
                                                frame3.setSize(800,600);
                                                frame3.setLayout(new GridLayout(10,2));
                                                frame3.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                                                JLabel board = new JLabel("Board Point");
                                                final JTextField jtboard = new JTextField(20);
                                                frame3.add(board);
                                                frame3.add(jtboard);
                                                JLabel end = new JLabel("End Point");
                                                frame3.add(end);
                                                final JTextField jtend = new JTextField(20);
                                                frame3.add(jtend);
                                                JLabel seats = new JLabel("Maximum Seats");
                                                frame3.add(seats);
                                                final JTextField jtseats = new JTextField(20);
                                                frame3.add(jtseats);
                                                JLabel dist = new JLabel("Distance");
                                                frame3.add(dist);
                                                final JTextField jtdist = new JTextField(20);
                                                frame3.add(jtdist);
                                                JLabel way = new JLabel("Way Of Travel");
                                                frame3.add(way);
                                                final JTextField jtway = new JTextField(20);
                                                frame3.add(jtway);
                                                JLabel eco = new JLabel("Charges(Economic)");
                                                frame3.add(eco);
                                                final JTextField jteco = new JTextField(20);
                                                frame3.add(jteco);
                                                JLabel exec = new JLabel("Charges(Executive)");
                                                frame3.add(exec);
                                                final JTextField jtexec = new JTextField(20);
                                                frame3.add(jtexec);
                                                JLabel time = new JLabel("Time");
                                                frame3.add(time);
                                                final JTextField jttime = new JTextField(20);
                                                frame3.add(jttime);
                                                JButton back = new JButton("Back");
                                                frame3.add(back);
                                                JButton jbsub = new JButton("Apply changes");
                                                frame3.add(jbsub);
                                                back.addActionListener(new ActionListener(){
                                                                            
                                                    @Override
                                                    public void actionPerformed(ActionEvent e) {
                                                        frame3.dispose();
                                                    }                                                                            
                                                });
                                                //jbsub.setPreferredSize(new Dimension(20,20));
                                                //frame2.add(chdata);
                                                frame3.setResizable(false);
                                                frame3.setVisible(true);
                                                jbsub.addActionListener(new ActionListener() {
                                                    @Override
                                                    public void actionPerformed(ActionEvent e) {
                                                        execSQLCommand("UPDATE PROJECT_FLIGHT SET BOARD_POINT = '" + jtboard.getText() + "', END_POINT = '" + jtend.getText() + "', MAXIMUM_SEATS = '" + jtseats.getText() + "', DISTANCE = '" + jtdist.getText() + "', WAY_OF_TRAVEL = '" + jtway.getText() + "', CHARGES_ECONOMIC = '" + jteco.getText() + "', CHARGES_EXECUTIVE = '" + jtexec.getText() + "', TIME = '" + jttime.getText() + "' WHERE FLIGHT_NO = '" + jtflno.getText() + "'");
                                                        JOptionPane.showMessageDialog(null, "Flight Data for "+ jtflno.getText() + " has been updated", "Data Change", JOptionPane.INFORMATION_MESSAGE);
                                                    }
                                                });
                                            } else {
                                                JOptionPane.showMessageDialog(null, "Wrong Flight No", "Error", JOptionPane.ERROR_MESSAGE);
                                            }
                                        } catch (SQLException ex) {
                                            Logger.getLogger(Operator.class.getName()).log(Level.SEVERE, null, ex);
                                        }
                                    }
                                });
                                    
                        }
        });
        addfl.addActionListener(new ActionListener() {
                                    @Override
                                    public void actionPerformed(ActionEvent e) {
                                                frame.dispose();
                                                final JFrame frame3 = new JFrame();
                                                frame3.setSize(800,560);
                                                frame3.setLayout(new GridLayout(12,2));
                                                frame3.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                                                JLabel airline = new JLabel("Airline Name");
                                                frame3.add(airline);
                                                final JTextField jtairline = new JTextField(20);
                                                frame3.add(jtairline);
                                                JLabel flno = new JLabel("Flight No");
                                                frame3.add(flno);
                                                final JTextField jtflno = new JTextField(20);
                                                frame3.add(jtflno);
                                                JLabel board = new JLabel("Board Point");
                                                final JTextField jtboard = new JTextField(20);
                                                frame3.add(board);
                                                frame3.add(jtboard);
                                                JLabel end = new JLabel("End Point");
                                                frame3.add(end);
                                                final JTextField jtend = new JTextField(20);
                                                frame3.add(jtend);
                                                JLabel seats = new JLabel("Maximum Seats");
                                                frame3.add(seats);
                                                final JTextField jtseats = new JTextField(20);
                                                frame3.add(jtseats);
                                                JLabel dist = new JLabel("Distance");
                                                frame3.add(dist);
                                                final JTextField jtdist = new JTextField(20);
                                                frame3.add(jtdist);
                                                JLabel way = new JLabel("Way Of Travel");
                                                frame3.add(way);
                                                final JTextField jtway = new JTextField(20);
                                                frame3.add(jtway);
                                                JLabel eco = new JLabel("Charges(Economic)");
                                                frame3.add(eco);
                                                final JTextField jteco = new JTextField(20);
                                                frame3.add(jteco);
                                                JLabel exec = new JLabel("Charges(Executive)");
                                                frame3.add(exec);
                                                final JTextField jtexec = new JTextField(20);
                                                frame3.add(jtexec);
                                                JLabel time = new JLabel("Time");
                                                frame3.add(time);
                                                final JTextField jttime = new JTextField(20);
                                                frame3.add(jttime);
                                                JButton back = new JButton("Back");
                                                frame3.add(back);
                                                JButton jbsub = new JButton("Add Flight");
                                                frame3.add(jbsub);
                                                back.addActionListener(new ActionListener(){
                                                                            
                                                                            @Override
                                                                            public void actionPerformed(ActionEvent e) {
                                                                                frame3.dispose();
                                                                                    
                                                                                Operator op = new Operator();
                                                                                op.init(x);
                                                                            }
                                                                            
                                                                        });
                                                //jbsub.setPreferredSize(new Dimension(20,20));
                                                //frame2.add(chdata);
                                                frame3.setResizable(false);
                                                frame3.setVisible(true);
                                                jbsub.addActionListener(new ActionListener() {
                                                    @Override
                                                    public void actionPerformed(ActionEvent e) {
                                                        execSQLCommand("SELECT * FROM PROJECT_FLIGHT WHERE FLIGHT_NO = '" + jtflno.getText() + "'");
                                                        try {
                                                            if (!theResult.next()) {
                                                                execSQLCommand("INSERT INTO PROJECT_FLIGHT VALUES ('" + jtairline.getText() + "','" + jtflno.getText() + "','" + jtboard.getText() + "','" + jtend.getText() + "','" + jtseats.getText() + "','" + jtdist.getText() + "','" + jtway.getText() + "','" + jtexec.getText() + "','" + jteco.getText() + "','" + jttime.getText() + "')");
                                                                JOptionPane.showMessageDialog(null, "Flight Data has been added", "New Flight", JOptionPane.INFORMATION_MESSAGE);
                                                            } else {
                                                                JOptionPane.showMessageDialog(null, "Flight already exists in system", "No change", JOptionPane.ERROR_MESSAGE);
                                                            }
                                                        } catch (SQLException ex) {
                                                            Logger.getLogger(Operator.class.getName()).log(Level.SEVERE, null, ex);
                                                        }
                                                        
                                                    }
                                                });
                                    }
        });
        passenger.addActionListener(new ActionListener() {
                                    @Override
                                    public void actionPerformed(ActionEvent e) {
                                                frame.dispose();
                                                final JFrame frame4 = new JFrame();
                                                frame4.setSize(300,250);
                                                frame4.setLayout(new GridLayout(3,2));
                                                frame4.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                                                JLabel jluid = new JLabel("Unique ID");
                                                frame4.add(jluid);
                                                final JTextField uid = new JTextField(7);
                                                frame4.add(uid);
                                                
                                                JButton back = new JButton("Back");
                                                frame4.add(back);
                                                JButton change = new JButton("Change");
                                                frame4.add(change);
                                                frame4.setResizable(false);
                                                frame4.setVisible(true);
                                                back.addActionListener(new ActionListener() {
                                                    @Override
                                                    public void actionPerformed(ActionEvent e) {
                                                         frame4.dispose();
                                                         Operator op = new Operator();
                                                         op.init(x);
                                                    }
                                                });
                                                change.addActionListener(new ActionListener() {
                                                    @Override
                                                    public void actionPerformed(ActionEvent e) {
                                                         final String unique_id = uid.getText();
                                                         
                                                         execSQLCommand("SELECT * FROM PROJECT_CLIENT WHERE UNIQUE_ID ='" + unique_id +"'");
                                                                try {
                                                                    if (theResult.next()) {
                                                                        System.out.println("asda");
                                                                        frame4.dispose();
                                                                        final JFrame frame5 = new JFrame();
                                                                        frame5.setSize(800,600);
                                                             
                                                                        frame5.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                                                                        JPanel jp = new JPanel();
                                                                        jp.setLayout(new GridLayout(11,1));
                                                                        
                                                                        JLabel ladd = new JLabel("Address");
                                                                        jp.add(ladd);
                                                                        final JTextField add = new JTextField(20);
                                                                        add.setPreferredSize(new Dimension(40,10));
                                                                        jp.add(add);
                                                                        JLabel lcity = new JLabel("City");
                                                                        jp.add(lcity);
                                                                        final JTextField city = new JTextField(20);
                                                                        city.setPreferredSize(new Dimension(40,10));
                                                                        jp.add(city);
                                                                        JLabel lstate = new JLabel("State");
                                                                        jp.add(lstate);
                                                                        final JTextField state = new JTextField(20);
                                                                        state.setPreferredSize(new Dimension(40,10));
                                                                        jp.add(state);
                                                                        JLabel lid = new JLabel("ID Proof");
                                                                        jp.add(lid);
                                                                        final JTextField id = new JTextField(20);
                                                                        id.setPreferredSize(new Dimension(40,10));
                                                                        jp.add(id);
                                                                        JLabel lfood = new JLabel("Food Preference");
                                                                        jp.add(lfood);
                                                                        DefaultComboBoxModel foodText = new DefaultComboBoxModel();
                                                                        foodText.addElement("Veg");
                                                                        foodText.addElement("Non-Veg");
                                                                        final JComboBox fbox = new JComboBox(foodText);
                                                                        fbox.setPreferredSize(new Dimension(40,10));
                                                                        jp.add(fbox);
                                                                        
                                                                        JLabel lphone = new JLabel("Phone");
                                                                        jp.add(lphone);
                                                                        final JTextField phone = new JTextField(20);
                                                                        phone.setPreferredSize(new Dimension(40,10));
                                                                        jp.add(phone);
                                                                        JLabel leid = new JLabel("E-mail ID");
                                                                        jp.add(leid);
                                                                        final JTextField eid = new JTextField(20);
                                                                        eid.setPreferredSize(new Dimension(40,10));
                                                                        jp.add(eid);
                                                                        JButton back  = new JButton("Back");
                                                                        jp.add(back);
                                                                        back.setPreferredSize(new Dimension(20,20));
                                                                            
                                                                        JButton book = new JButton("Change Ticket");
                                                                        jp.add(book);
                                                                        frame5.add(jp);
                                                                        frame5.setResizable(false);
                                                                        frame5.setVisible(true);
                                                                        back.addActionListener(new ActionListener(){
                                                                            
                                                                            @Override
                                                                            public void actionPerformed(ActionEvent e) {
                                                                                frame5.dispose();
                                                                                    
                                                                                Operator op = new Operator();
                                                                                op.init(x);
                                                                            }
                                                                            
                                                                        });
                                                                        book.addActionListener(new ActionListener()
                                                                        {
                                                                            @Override
                                                                            public void actionPerformed(ActionEvent ae) {
                                                                                execSQLCommand("UPDATE PROJECT_CLIENT SET STREET_ADDRESS = '" + add.getText() + "', CITY = '" + city.getText() + "', STATE = '" + state.getText() + "', PHONE = '" + phone.getText() + "', E_MAIL_ID = '" + eid.getText() + "', ID_PROOF = '" + id.getText() + "', FOOD_CHOICE = '" + fbox.getSelectedItem() + "' WHERE UNIQUE_ID = '" + unique_id + "'");
                                                                                JOptionPane.showMessageDialog(null, "Details Changed", "Change", JOptionPane.INFORMATION_MESSAGE);

                                                                            }
                                                                        });
                                                                    } else {
                                                                        JOptionPane.showMessageDialog(null, "Wrong Unique ID", "Error", JOptionPane.ERROR_MESSAGE);
                                                                    }
                                                                } catch (SQLException ex) {
                                                                    Logger.getLogger(Operator.class.getName()).log(Level.SEVERE, null, ex);
                                                                }
                                                    }
                            });
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
