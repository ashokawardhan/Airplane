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
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
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

class Java2sAutoTextField extends JTextField {
  class AutoDocument extends PlainDocument {

    public void replace(int i, int j, String s, AttributeSet attributeset)
        throws BadLocationException {
      super.remove(i, j);
      insertString(i, s, attributeset);
    }

    public void insertString(int i, String s, AttributeSet attributeset)
        throws BadLocationException {
      if (s == null || "".equals(s))
        return;
      String s1 = getText(0, i);
      String s2 = getMatch(s1 + s);
      int j = (i + s.length()) - 1;
      if (isStrict && s2 == null) {
        s2 = getMatch(s1);
        j--;
      } else if (!isStrict && s2 == null) {
        super.insertString(i, s, attributeset);
        return;
      }
      if (autoComboBox != null && s2 != null)
        autoComboBox.setSelectedValue(s2);
      super.remove(0, getLength());
      super.insertString(0, s2, attributeset);
      setSelectionStart(j + 1);
      setSelectionEnd(getLength());
    }

    public void remove(int i, int j) throws BadLocationException {
      int k = getSelectionStart();
      if (k > 0)
        k--;
      String s = getMatch(getText(0, k));
      if (!isStrict && s == null) {
        super.remove(i, j);
      } else {
        super.remove(0, getLength());
        super.insertString(0, s, null);
      }
      if (autoComboBox != null && s != null)
        autoComboBox.setSelectedValue(s);
      try {
        setSelectionStart(k);
        setSelectionEnd(getLength());
      } catch (Exception exception) {
      }
    }

  }

  public Java2sAutoTextField(List list) {
    isCaseSensitive = false;
    isStrict = true;
    autoComboBox = null;
    if (list == null) {
      throw new IllegalArgumentException("values can not be null");
    } else {
      dataList = list;
      init();
      return;
    }
  }

  Java2sAutoTextField(List list, Java2sAutoComboBox b) {
    isCaseSensitive = false;
    isStrict = true;
    autoComboBox = null;
    if (list == null) {
      throw new IllegalArgumentException("values can not be null");
    } else {
      dataList = list;
      autoComboBox = b;
      init();
      return;
    }
  }

  private void init() {
    setDocument(new AutoDocument());
    if (isStrict && dataList.size() > 0)
      setText(dataList.get(0).toString());
  }

  private String getMatch(String s) {
    for (int i = 0; i < dataList.size(); i++) {
      String s1 = dataList.get(i).toString();
      if (s1 != null) {
        if (!isCaseSensitive
            && s1.toLowerCase().startsWith(s.toLowerCase()))
          return s1;
        if (isCaseSensitive && s1.startsWith(s))
          return s1;
      }
    }

    return null;
  }

  public void replaceSelection(String s) {
    AutoDocument _lb = (AutoDocument) getDocument();
    if (_lb != null)
      try {
        int i = Math.min(getCaret().getDot(), getCaret().getMark());
        int j = Math.max(getCaret().getDot(), getCaret().getMark());
        _lb.replace(i, j - i, s, null);
      } catch (Exception exception) {
      }
  }

  public boolean isCaseSensitive() {
    return isCaseSensitive;
  }

  public void setCaseSensitive(boolean flag) {
    isCaseSensitive = flag;
  }

  public boolean isStrict() {
    return isStrict;
  }

  public void setStrict(boolean flag) {
    isStrict = flag;
  }

  public List getDataList() {
    return dataList;
  }

  public void setDataList(List list) {
    if (list == null) {
      throw new IllegalArgumentException("values can not be null");
    } else {
      dataList = list;
      return;
    }
  }

  private List dataList;

  private boolean isCaseSensitive;

  private boolean isStrict;

  private Java2sAutoComboBox autoComboBox;
}

class Java2sAutoComboBox extends JComboBox {
  private class AutoTextFieldEditor extends BasicComboBoxEditor {

    private Java2sAutoTextField getAutoTextFieldEditor() {
      return (Java2sAutoTextField) editor;
    }

    AutoTextFieldEditor(java.util.List list) {
      editor = new Java2sAutoTextField(list, Java2sAutoComboBox.this);
    }
  }

  public Java2sAutoComboBox(java.util.List list) {
    isFired = false;
    autoTextFieldEditor = new AutoTextFieldEditor(list);
    setEditable(true);
    setModel(new DefaultComboBoxModel(list.toArray()) {

      protected void fireContentsChanged(Object obj, int i, int j) {
        if (!isFired)
          super.fireContentsChanged(obj, i, j);
      }

    });
    setEditor(autoTextFieldEditor);
  }

  public boolean isCaseSensitive() {
    return autoTextFieldEditor.getAutoTextFieldEditor().isCaseSensitive();
  }

  public void setCaseSensitive(boolean flag) {
    autoTextFieldEditor.getAutoTextFieldEditor().setCaseSensitive(flag);
  }

  public boolean isStrict() {
    return autoTextFieldEditor.getAutoTextFieldEditor().isStrict();
  }

  public void setStrict(boolean flag) {
    autoTextFieldEditor.getAutoTextFieldEditor().setStrict(flag);
  }

  public java.util.List getDataList() {
    return autoTextFieldEditor.getAutoTextFieldEditor().getDataList();
  }

  public void setDataList(java.util.List list) {
    autoTextFieldEditor.getAutoTextFieldEditor().setDataList(list);
    setModel(new DefaultComboBoxModel(list.toArray()));
  }

  void setSelectedValue(Object obj) {
    if (isFired) {
      return;
    } else {
      isFired = true;
      setSelectedItem(obj);
      fireItemStateChanged(new ItemEvent(this, 701, selectedItemReminder,
          1));
      isFired = false;
      return;
    }
  }

  protected void fireActionEvent() {
    if (!isFired)
      super.fireActionEvent();
  }

  private AutoTextFieldEditor autoTextFieldEditor;

  private boolean isFired;

}
public class Client
{
    
    JTextField da = new JTextField("dd-mm-yyyy");
    Statement theStatement;
    ResultSet theResult;
    Connection theConnection;
    String theDataSource;
    JButton view = new JButton("View Results");
    ArrayList<String> arr = new ArrayList<String>();
                ArrayList<String> arr1 = new ArrayList<String>();
    Java2sAutoComboBox tx = new Java2sAutoComboBox(arr);
    Java2sAutoComboBox to = new Java2sAutoComboBox(arr1);
    DefaultComboBoxModel ti2 = new DefaultComboBoxModel();
    JComboBox t2 = new JComboBox(ti2);
    
    DefaultComboBoxModel ti1 = new DefaultComboBoxModel();
    JComboBox t1 = new JComboBox(ti1);
    DefaultComboBoxModel ti3 = new DefaultComboBoxModel();
    JComboBox t3 = new JComboBox(ti3);
    public static long daysDiff(Date from, Date to) {
    return daysDiff(from.getTime(), to.getTime());
}

public static long daysDiff(long from, long to) {
    return Math.round( (to - from) / 86400000D ); // 1000 * 60 * 60 * 24
}
    public void init(final String x) throws ClassNotFoundException, InstantiationException, IllegalAccessException, UnsupportedLookAndFeelException {
       UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
        //UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        //UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
        final JFrame frame1 = new JFrame("Account");
        frame1.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //JLabel label1 = new JLabel("Account");
        
        JButton but1 = new JButton("   Plan Your Travel  ");
        JButton but2 = new JButton("   Booked History    ");
        JButton but3 = new JButton("Change Password ");
        JButton but4 = new JButton("          Sign Out          ");
        
        frame1.add(but1);
        
        frame1.add(but2);
        frame1.add(but3);
        frame1.add(but4);
        
        frame1.setLayout(new FlowLayout(5,60,20));
        
        frame1.setSize(300, 300);
        frame1.setResizable(false);
        frame1.setVisible(true);
        
        but4.addActionListener(new ActionListener(){
        
            @Override
            public void actionPerformed(ActionEvent ae) {
                frame1.dispose();
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
        but1.addActionListener(new ActionListener(){
        
            @Override
            public void actionPerformed(ActionEvent ae) {
                //JOptionPane.showMessageDialog(null, "njn");
                frame1.dispose();
                final JFrame frame2 = new JFrame("Plan Your Travel");
                frame2.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                JLabel lab1 = new JLabel("City From");
                
                
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
                execSQLCommand("SELECT DISTINCT BOARD_POINT FROM PROJECT_FLIGHT");
                try {
                    while (theResult.next()) {
                        arr.add(theResult.getString("BOARD_POINT"));
                    }
                } catch (SQLException ex) {
                    Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
                }
                execSQLCommand("SELECT DISTINCT END_POINT FROM PROJECT_FLIGHT");
                try {
                    while (theResult.next()) {
                        arr1.add(theResult.getString("END_POINT"));
                    }
                } catch (SQLException ex) {
                    Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
                }
                tx.setEditable(true);
                tx.getEditor().selectAll();
                tx.setDataList(arr);
                SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                
                tx.getEditor().setItem(0);
                tx.getEditor().selectAll();
            }
        });
                frame2.add(lab1);
                lab1.setAlignmentX(50);
                frame2.add(tx);
                
                JLabel lab2 = new JLabel("City To");
                
                to.setEditable(true);
                to.getEditor().selectAll();
                to.setDataList(arr1);
                SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                
                to.getEditor().setItem(0);
                to.getEditor().selectAll();
            }
        });
                frame2.setLayout(new GridLayout(2,1));
                frame2.add(lab2);
                //frame2.setResizable(false);
                frame2.add(to);
                JLabel lab4 = new JLabel("Time From");
                
                ti1.addElement("00:00");
                ti1.addElement("01:00");
                ti1.addElement("02:00");
                ti1.addElement("03:00");
                ti1.addElement("04:00");
                ti1.addElement("05:00");
                ti1.addElement("06:00");
                ti1.addElement("07:00");
                ti1.addElement("08:00");
                ti1.addElement("09:00");
                ti1.addElement("10:00");
                ti1.addElement("11:00");
                ti1.addElement("12:00");
                ti1.addElement("13:00");
                ti1.addElement("14:00");
                ti1.addElement("15:00");
                ti1.addElement("16:00");
                ti1.addElement("17:00");
                ti1.addElement("18:00");
                ti1.addElement("19:00");
                ti1.addElement("20:00");
                ti1.addElement("21:00");
                ti1.addElement("22:00");
                ti1.addElement("23:00");
                ti1.addElement("24:00");
                frame2.add(lab4);
                JComboBox t1 = new JComboBox(ti1);
                frame2.add(t1);
                JLabel lab5 = new JLabel("Time To");
                
                ti2.addElement("00:00");
                ti2.addElement("01:00");
                ti2.addElement("02:00");
                ti2.addElement("03:00");
                ti2.addElement("04:00");
                ti2.addElement("05:00");
                ti2.addElement("06:00");
                ti2.addElement("07:00");
                ti2.addElement("08:00");
                ti2.addElement("09:00");
                ti2.addElement("10:00");
                ti2.addElement("11:00");
                ti2.addElement("12:00");
                ti2.addElement("13:00");
                ti2.addElement("14:00");
                ti2.addElement("15:00");
                ti2.addElement("16:00");
                ti2.addElement("17:00");
                ti2.addElement("18:00");
                ti2.addElement("19:00");
                ti2.addElement("20:00");
                ti2.addElement("21:00");
                ti2.addElement("22:00");
                ti2.addElement("23:00");
                ti2.addElement("24:00");
                
                frame2.add(lab5);
                frame2.setResizable(false);
                frame2.add(t2);
                JLabel lab6 = new JLabel("Class");
                frame2.add(lab6);
                ti3.addElement("Economic");
                ti3.addElement("Executive");
                frame2.add(t3);
                JLabel lab3 = new JLabel("Date");
              
                frame2.add(lab3);
                
                //da.setBounds(300, 300, 20, 20);
                //da.setFont("Arial");
                frame2.add(da);
                //frame2.setLayout(new GridLayout(6,1));
                JButton bac = new JButton("Back");
                frame2.add(bac);
                
                frame2.add(view,FlowLayout.RIGHT);
                //view.setBounds( 20, 20);
                frame2.add(view,BorderLayout.LINE_END);
                frame2.setLayout(new GridLayout(8,1));
                
                frame2.setSize(350,350);
                frame2.setVisible(true);
                bac.addActionListener(new ActionListener(){

                    @Override
                    public void actionPerformed(ActionEvent e) {
                        frame2.dispose();
                        Client lg = new Client();
                        try {                        
                            lg.init(x);
                            //Client cl = new Client();
                            
                            // throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
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
                view.addActionListener(new ActionListener() {
             @Override
            public void actionPerformed(ActionEvent ae) {
                DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                        Calendar cal = Calendar.getInstance();
                        Calendar cal2 = Calendar.getInstance();
                 try {
                     Date date = new SimpleDateFormat("dd-MM-yyyy").parse(da.getText());
                     cal2.setTimeInMillis(date.getTime());
                     //System.out.println(daysDiff(cal.getTimeInMillis(), cal2.getTimeInMillis()));
                     if (date.compareTo(cal.getTime()) > 0 && daysDiff(cal.getTimeInMillis(), cal2.getTimeInMillis()) <= 60){
                         frame2.dispose();
                String fromx = (String) tx.getSelectedItem();
                final JFrame frame6 = new JFrame();
                Container cp = new Container();
                cp.setLayout(new BorderLayout());
                frame6.setSize(730,550);
                frame6.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JPanel js = new JPanel();
        JPanel js2 = new JPanel();
        final JRadioButton jr[] = new JRadioButton[1000];
        JPanel jpradio = new JPanel();
        JLabel jlradio = new JLabel(" ");
        jlradio.setPreferredSize(new Dimension(5,20));
        jpradio.add(jlradio);
        JPanel jpairline = new JPanel();
        JLabel jlairline = new JLabel ("Airline");
        jlairline.setPreferredSize(new Dimension(80,20));
        jpairline.add(jlairline);
        JPanel jpflno = new JPanel();
        JLabel jlflno = new JLabel ("Flight No");
        jlflno.setPreferredSize(new Dimension(110,20));
        jpflno.add(jlflno);
        JPanel jpdist = new JPanel();
        JLabel jldist = new JLabel ("Distance");
        jldist.setPreferredSize(new Dimension(90,20));
        jpdist.add(jldist);
        JPanel jpway = new JPanel();
        JLabel jlway = new JLabel ("Way Of Travel");
        jlway.setPreferredSize(new Dimension(120,20));
        jpway.add(jlway);
        JPanel jpcharges = new JPanel();
        JLabel jlcharges = new JLabel ("Charges");
        jlcharges.setPreferredSize(new Dimension(100,20));
        jpcharges.add(jlcharges);
        JPanel jptime = new JPanel();
        JLabel jltime = new JLabel ("Time");
        jltime.setPreferredSize(new Dimension(100,20));
        jptime.add(jltime);
        
        ButtonGroup group = new ButtonGroup();
        js2.add(jpradio);
        js2.add(jpairline);
        js2.add(jpflno);
        js2.add(jpdist);
        js2.add(jpway);
        js2.add(jpcharges);
        js2.add(jptime);
        JLabel jp[][] = new JLabel[7][1000];
        int i = 0;
            //System.out.println(fromx);
           execSQLCommand("SELECT * FROM PROJECT_FLIGHT WHERE BOARD_POINT = '" + fromx + "' AND END_POINT = '" + (String)to.getSelectedItem() + "' AND TIME BETWEEN '" + (String)ti1.getSelectedItem() + "' AND '" + (String)ti2.getSelectedItem()+"'" ) ;
                 try {
                     while(theResult.next()) {
                         //System.out.println(theResult.getString("AIRLINE_NAME"));
                         jr[i] = new JRadioButton();
                         js.add(jr[i]);
                         group.add(jr[i]);
                         //jp[i] = new JLabel();
                         if ("Economic".equals((String)ti3.getSelectedItem())) {
                                 jp[i][0] = new JLabel();
                                 jp[i][0].setPreferredSize(new Dimension(110,20));
                                 jp[i][0].setText(theResult.getString("AIRLINE_NAME"));
                                 jp[i][1] = new JLabel();
                                 jp[i][1].setPreferredSize(new Dimension(120,20));
                                 jp[i][1].setText(theResult.getString("FLIGHT_NO"));
                                 jp[i][2] = new JLabel();
                                 jp[i][2].setPreferredSize(new Dimension(80,20));
                                 jp[i][2].setText(theResult.getString("DISTANCE"));
                                 jp[i][3] = new JLabel();
                                 jp[i][3].setPreferredSize(new Dimension(140,20));
                                 jp[i][3].setText(theResult.getString("WAY_OF_TRAVEL"));
                                 jp[i][4] = new JLabel();
                                 jp[i][4].setPreferredSize(new Dimension(100,20));
                                 jp[i][4].setText(theResult.getString("CHARGES_ECONOMIC") +"   ");
                                 jp[i][5] = new JLabel();
                                 jp[i][5].setPreferredSize(new Dimension(100,20));
                                 jp[i][5].setText(theResult.getString("TIME") + "   ");
                            
                         }
                            //jp[i].setText( + "  " +  + "  " + + "  " + + "  " + + "  " + );
                         else {
                                 jp[i][0] = new JLabel();
                                 jp[i][0].setPreferredSize(new Dimension(110,20));
                                 jp[i][0].setText(theResult.getString("AIRLINE_NAME"));
                                 jp[i][1] = new JLabel();
                                 jp[i][1].setPreferredSize(new Dimension(120,20));
                                 jp[i][1].setText(theResult.getString("FLIGHT_NO"));
                                 jp[i][2] = new JLabel();
                                 jp[i][2].setPreferredSize(new Dimension(80,20));
                                 jp[i][2].setText(theResult.getString("DISTANCE"));
                                 jp[i][3] = new JLabel();
                                 jp[i][3].setPreferredSize(new Dimension(140,20));
                                 jp[i][3].setText(theResult.getString("WAY_OF_TRAVEL"));
                                 jp[i][4] = new JLabel();
                                 jp[i][4].setPreferredSize(new Dimension(100,20));
                                 jp[i][4].setText(theResult.getString("CHARGES_EXECUTIVE") +"   ");
                                 jp[i][5] = new JLabel();
                                 jp[i][5].setPreferredSize(new Dimension(100,20));
                                 jp[i][5].setText(theResult.getString("TIME") + "   ");
                            }
                             //jp[i].setText(theResult.getString("AIRLINE_NAME") + "  " + theResult.getString("FLIGHT_NO") + "  " +theResult.getString("DISTANCE") + "  " +theResult.getString("WAY_OF_TRAVEL") + "  " +theResult.getString("CHARGES_EXECUTIVE") + "  " + theResult.getString("TIME"));
                         for (int j = 0; j < 6; j++)
                            js.add(jp[i][j]);
                         i++;
                     }    
                 } catch (SQLException ex) {
                     Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
                 }
                 cp.add(js2, BorderLayout.PAGE_START);
                 cp.add(js);
                JButton back = new JButton("Back");
                //back.setBounds(10,300,20,20);
                frame6.add(back,BorderLayout.PAGE_START);
                JButton get = new JButton("Select");
                back.setPreferredSize(new Dimension(20,20));
                  get.setBounds(300, 300, 40, 40);
                //get.setSize(50, 50);
                cp.add(get, BorderLayout.SOUTH);
                //frame2.setLayout(new FlowLayout());
                frame6.add(cp);
                frame6.setVisible(true);
                back.addActionListener(new ActionListener(){

                    @Override
                    public void actionPerformed(ActionEvent e) {
                        frame1.dispose();
                        frame6.dispose();
                        Login lg = new Login();
                        try {
                            lg.init();
//throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
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
                get.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent ae) {
                        
                        frame6.dispose();
                        final JFrame frame3 = new JFrame();
                        frame3.setSize(800,600);
                        frame3.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                        int i = 0;
            //System.out.println(fromx);
           execSQLCommand("SELECT * FROM PROJECT_FLIGHT WHERE BOARD_POINT = '" + (String)tx.getSelectedItem() + "' AND END_POINT = '" + (String)to.getSelectedItem() + "' AND TIME BETWEEN '" + (String)ti1.getSelectedItem() + "' AND '" + (String)ti2.getSelectedItem()+"'" ) ;
                 try {
                     while(theResult.next()) {
                         
                         if (jr[i].isSelected() == true) {
                             JPanel jp1 = new JPanel();
                             
                             JLabel flno = new JLabel();
                             final String flnos = theResult.getString("FLIGHT_NO");
                             flno.setText("Flight No: " + flnos + "  ");
                             jp1.add(flno);
                             JLabel date = new JLabel();
                             
                             date.setText("Date: " + da.getText() + "  ");
                             jp1.add(date);
                             JLabel bo = new JLabel();
                             bo.setText("Boarding Point: " + (String)tx.getSelectedItem() + "  ");
                             jp1.add(bo);
                             JLabel end = new JLabel();
                             end.setText("End Point: " + (String)to.getSelectedItem() + "  ");
                             jp1.add(end);
                             JLabel time = new JLabel();
                             final String times = theResult.getString("TIME");
                             time.setText("Time Of Flight: " + times + "  ");
                             jp1.add(time);
                             final int charges;
                             if ("Economic".equals((String)ti3.getSelectedItem()))
                                charges = theResult.getInt("CHARGES_ECONOMIC");
                             else
                                 charges = theResult.getInt("CHARGES_EXECUTIVE");
                             JPanel jp = new JPanel();
                             jp.setLayout(new GridLayout(11,1));
                             jp.setSize(40, 80);
                             jp.add(jp1);
                             JPanel jp2 = new JPanel();
                             jp.add(jp2);
                             JLabel lname = new JLabel("Name");
                             jp.add(lname);
                             final JTextField name = new JTextField(20);
                             name.setPreferredSize(new Dimension(40,10));
                             jp.add(name);
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
                             JLabel lgender = new JLabel("Gender");
                             jp.add(lgender);
                             DefaultComboBoxModel genderText = new DefaultComboBoxModel();
                              genderText.addElement("Male");
                              genderText.addElement("Female");
                             final JComboBox box = new JComboBox(genderText);
                             box.setPreferredSize(new Dimension(40,10));
                             jp.add(box);
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
                             
                             JButton book = new JButton("Book Ticket");
                             jp.add(book);
                             book.setForeground(Color.red);
                             book.setBounds(i, i, i, charges);
                             
                             back.addActionListener(new ActionListener(){

                                 @Override
                                 public void actionPerformed(ActionEvent e) {
                                     frame1.dispose();
                                     frame6.dispose();
                                     frame3.dispose();
                                     frame3.setVisible(false);
                                     Login l = new Login();
                                     
                                     
                                     try {
                                         l.init();
                                         //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
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
                             book.addActionListener(new ActionListener()
                             {
                                @Override
                                public void actionPerformed(ActionEvent ae) {
                                    execSQLCommand("SELECT PROJECT_FLIGHT.MAXIMUM_SEATS AS MAXIMUM_SEATS, PROJECT_FLIGHT_DATE.SEATS_OCCUPIED AS SEATS_OCCUPIED FROM PROJECT_FLIGHT, PROJECT_FLIGHT_DATE WHERE PROJECT_FLIGHT_DATE.FLIGHT_NO = '" + flnos + "' AND PROJECT_FLIGHT_DATE.FLIGHT_DATE = '" + da.getText() + "'");
                                    try {
                                        if (theResult.next()) {
                                            int a = theResult.getInt("SEATS_OCCUPIED"); 
                                            if (a < theResult.getInt("MAXIMUM_SEATS")) {
                                                execSQLCommand("INSERT INTO PROJECT_CLIENT (NAME, GENDER, STREET_ADDRESS, CITY, STATE, PHONE, E_MAIL_ID, ID_PROOF, FLIGHT_NO, DATE_OF_FLIGHT, FOOD_CHOICE, TIME, PRICE) VALUES ('" + name.getText() + "','" + box.getSelectedItem() + "','" + add.getText() + "','" + city.getText() + "','" + state.getText() + "','" + phone.getText() + "','" + eid.getText() + "','" + id.getText() + "','" + flnos + "','" + da.getText() + "','" + fbox.getSelectedItem() + "','" + times +"','" + charges + "')");
                                                execSQLCommand("SELECT MAX(UNIQUE_ID) FROM PROJECT_CLIENT");
                                                String abc = "";
                                                try {
                                                    if(theResult.next()) {
                                                        abc = theResult.getString(1);
                                                        JOptionPane.showMessageDialog(null, "Your unique ID for future reference is " + abc + " ", "Ticket Booked", JOptionPane.INFORMATION_MESSAGE);
                                                    }
                                                } catch (SQLException ex) {
                                                    Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
                                                }
                                                execSQLCommand("INSERT INTO PROJECT_CLIENT_LOGIN (USERNAME, UNIQUE_ID) VALUES('" + x + "','" + abc +"')");
                                                a = a + 1;
                                                execSQLCommand("UPDATE PROJECT_FLIGHT_DATE SET SEATS_OCCUPIED = '" + a +"' WHERE FLIGHT_NO = '" + flnos + "' AND FLIGHT_DATE = '" + da.getText() + "'");
                                            } 
                                        } else {
                                            execSQLCommand("INSERT INTO PROJECT_FLIGHT_DATE VALUES('" + flnos + "','" + da.getText() + "','1')");
                                            execSQLCommand("INSERT INTO PROJECT_CLIENT (NAME, GENDER, STREET_ADDRESS, CITY, STATE, PHONE, E_MAIL_ID, ID_PROOF, FLIGHT_NO, DATE_OF_FLIGHT, FOOD_CHOICE, TIME, PRICE) VALUES ('" + name.getText() + "','" + box.getSelectedItem() + "','" + add.getText() + "','" + city.getText() + "','" + state.getText() + "','" + phone.getText() + "','" + eid.getText() + "','" + id.getText() + "','" + flnos + "','" + da.getText() + "','" + fbox.getSelectedItem() + "','" + times +"','" + charges + "')");
                                            execSQLCommand("SELECT MAX(UNIQUE_ID) FROM PROJECT_CLIENT");
                                                String abc = "";
                                                try {
                                                    if(theResult.next()) {
                                                        abc = theResult.getString(1);
                                                        JOptionPane.showMessageDialog(null, "Your unique ID for future reference is " + abc + " ", "Ticket Booked", JOptionPane.INFORMATION_MESSAGE);
                                                    }
                                                } catch (SQLException ex) {
                                                    Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
                                                }
                                                execSQLCommand("INSERT INTO PROJECT_CLIENT_LOGIN (USERNAME, UNIQUE_ID) VALUES('" + x + "','" + abc +"')");
                                      //System.out.println("asd");
                                        }
                                    } catch (SQLException ex) {
                                        Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
                                    }
                              }                      
                            });
                            
                            frame3.add(jp);
                         }
                         
                         i++;
                     }    
                 } catch (SQLException ex) {
                     Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
                 }
                        frame3.setVisible(true);
                    }
                });
                     } else {
                         JOptionPane.showMessageDialog(null, "Select correct date", "Date Error", JOptionPane.ERROR_MESSAGE);
                     }
                 } catch (ParseException ex) {
                    JOptionPane.showMessageDialog(null, "Not correct date format", "Date Error", JOptionPane.ERROR_MESSAGE);

                     //Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
                 }
                 
                
            }
        });
//throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }
            
            
        });
        
        
        but3.addActionListener(new ActionListener()
        {

            @Override
            public void actionPerformed(ActionEvent ae) {
              
                
                Password_Change che = new Password_Change();
                che.change(x);
             
            }                      
        });
        but2.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent ae) {
               frame1.dispose();
               final JFrame frame5 = new JFrame("Results");
               Container cp = new Container();
                cp.setLayout(new BorderLayout());
                frame5.setSize(800,600);
                frame5.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                JPanel js = new JPanel();
                JPanel js2 = new JPanel();
                final JRadioButton jr[] = new JRadioButton[1000];
                JPanel jpradio = new JPanel();
                JLabel jlradio = new JLabel(" ");
                jpradio.add(jlradio);
                JPanel jpid = new JPanel();
                JLabel jlid = new JLabel("Unique ID");
                jpid.add(jlid);
                JPanel jpairline = new JPanel();
                JLabel jlairline = new JLabel ("Airline");
                jpairline.add(jlairline);
                JPanel jpflno = new JPanel();
                JLabel jlflno = new JLabel ("Flight No");
                jpflno.add(jlflno);
                JPanel jpdist = new JPanel();
                JLabel jldist = new JLabel ("Distance");
                jpdist.add(jldist);
                JPanel jpway = new JPanel();
                JLabel jlway = new JLabel ("Way Of Travel");
                jpway.add(jlway);
                JPanel jpcharges = new JPanel();
                JLabel jlcharges = new JLabel ("Charges");
                jpcharges.add(jlcharges);
                JPanel jpdate = new JPanel();
                JLabel jldate = new JLabel ("    Date      ");
                jpdate.add(jldate);
                JPanel jptime = new JPanel();
                JLabel jltime = new JLabel ("     Time");
                jptime.add(jltime);
                
                ButtonGroup group = new ButtonGroup();
                js2.setLayout(new FlowLayout(1,27,10) );
                js2.add(jpradio);
                js2.add(jpid);
                js2.add(jpairline);
                js2.add(jpflno);
                js2.add(jpdist);
                js2.add(jpway);
                js2.add(jpcharges);
                js2.add(jpdate);
                js2.add(jptime);
                js.setLayout(new FlowLayout(1,27,10) );
                JLabel jp[][] = new JLabel[8][1000];
                int i = 0;
                //System.out.println(x);
                theDataSource = "jdbc:oracle:thin:@localhost:1521:XE";
                try {
                     Class.forName("oracle.jdbc.driver.OracleDriver");
                     theConnection = DriverManager.getConnection(theDataSource, "system", "taurus");
                     //theStatus.setText("Status: OK");
                }
                catch(Exception e)
                {
                     handleException(e);
                 }
                execSQLCommand("SELECT * FROM PROJECT_FLIGHT,(SELECT FLIGHT_NO AS CFL, UCLLOG, PRICE, DATE_OF_FLIGHT FROM PROJECT_CLIENT, (SELECT UNIQUE_ID AS UCLLOG FROM PROJECT_CLIENT_LOGIN WHERE USERNAME = '" + x + "') WHERE UCLLOG = PROJECT_CLIENT.UNIQUE_ID) WHERE CFL = PROJECT_FLIGHT.FLIGHT_NO");
                try {
                     while(theResult.next()) {
                         //System.out.println(theResult.getString("AIRLINE_NAME"));
                         jr[i] = new JRadioButton();
                         js.add(jr[i]);
                         group.add(jr[i]);
                         //jp[i] = new JLabel();
                         
                         jp[i][0] = new JLabel();
                         jp[i][0].setPreferredSize(new Dimension(60,20));
                         jp[i][0].setText(theResult.getString("UCLLOG"));
                         jp[i][1] = new JLabel();
                         jp[i][1].setPreferredSize(new Dimension(60,20));
                         jp[i][1].setText(theResult.getString("AIRLINE_NAME"));
                         jp[i][2] = new JLabel();
                         jp[i][2].setPreferredSize(new Dimension(60,20));
                         jp[i][2].setText(theResult.getString("CFL"));
                         jp[i][3] = new JLabel();
                         jp[i][3].setPreferredSize(new Dimension(60,20));
                         jp[i][3].setText(theResult.getString("DISTANCE"));
                         jp[i][4] = new JLabel();
                         jp[i][4].setPreferredSize(new Dimension(100,20));
                         jp[i][4].setText(theResult.getString("WAY_OF_TRAVEL"));
                         jp[i][5] = new JLabel();
                         jp[i][5].setPreferredSize(new Dimension(40,20));
                         jp[i][5].setText(theResult.getString("PRICE"));
                         jp[i][6] = new JLabel();
                         jp[i][6].setPreferredSize(new Dimension(80,20));
                         jp[i][6].setText(theResult.getString("DATE_OF_FLIGHT"));
                         jp[i][7] = new JLabel();
                         jp[i][7].setText(theResult.getString("TIME") + "       ");
                           
                         
                             //jp[i].setText(theResult.getString("AIRLINE_NAME") + "  " + theResult.getString("FLIGHT_NO") + "  " +theResult.getString("DISTANCE") + "  " +theResult.getString("WAY_OF_TRAVEL") + "  " +theResult.getString("CHARGES_EXECUTIVE") + "  " + theResult.getString("TIME"));
                         for (int j = 0; j < 8; j++)
                            js.add(jp[i][j]);
                         i++;
                         
                     }    
                 } catch (SQLException ex) {
                     Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
                 }
                 JButton back = new JButton("Back");
                 JButton change = new JButton("Delete");
                 
                 cp.add(js2, BorderLayout.PAGE_START);
                 cp.add(js);
                 JPanel js3 = new JPanel();
                 js3.add(back);
                 js3.add(change);
                 cp.add(js3, BorderLayout.PAGE_END);
                 frame5.add(cp);
                 frame5.setVisible(true);
                 back.addActionListener(new ActionListener() {

                        @Override
                        public void actionPerformed(ActionEvent ae) {
                            frame5.dispose();
                            frame5.setVisible(false);
                            Client cl = new Client();
                            try {
                                cl.init(x);
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
                 change.addActionListener(new ActionListener() {

                        @Override
                        public void actionPerformed(ActionEvent ae) {
                            
                           
                            execSQLCommand("SELECT * FROM PROJECT_FLIGHT,(SELECT FLIGHT_NO AS CFL, UCLLOG, PRICE, DATE_OF_FLIGHT FROM PROJECT_CLIENT, (SELECT UNIQUE_ID AS UCLLOG FROM PROJECT_CLIENT_LOGIN WHERE USERNAME = '" + x + "') WHERE UCLLOG = PROJECT_CLIENT.UNIQUE_ID) WHERE CFL = PROJECT_FLIGHT.FLIGHT_NO" ) ;
                            int i = 0;
                            try {                            
                                while(theResult.next()) {
                                    
                                    if (jr[i].isSelected() == true) {
                                        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                                        Calendar cal = Calendar.getInstance();
                                        
                                        String abc = theResult.getString("UCLLOG");
                                        String date = theResult.getString("DATE_OF_FLIGHT");
                                        String flno = theResult.getString("FLIGHT_NO");
                                        //System.out.println(cal.get(Calendar.DATE) + " " + date.substring(0, 2) +  " " + (cal.get(Calendar.DATE) < Integer.parseInt(date.substring(0, 2))) + " " + date + " " + (cal.get(Calendar.YEAR) < Integer.parseInt(date.substring(6, 10))) + " " + ((cal.get(Calendar.YEAR) == Integer.parseInt(date.substring(6, 10))) && (cal.get(Calendar.MONTH) < Integer.parseInt(date.substring(3, 5)))) + " " + (cal.get(Calendar.DATE) < Integer.parseInt(date.substring(0, 2))));
                                        if ((cal.get(Calendar.YEAR) < Integer.parseInt(date.substring(6, 10))) || ((cal.get(Calendar.YEAR) == Integer.parseInt(date.substring(6, 10))) && ((cal.get(Calendar.MONTH) + 1) < Integer.parseInt(date.substring(3, 5)))) || ((cal.get(Calendar.YEAR) == Integer.parseInt(date.substring(6, 10))) && ((cal.get(Calendar.MONTH) + 1) == Integer.parseInt(date.substring(3, 5))) && (cal.get(Calendar.DATE) < Integer.parseInt(date.substring(0, 2))))) {
                                            execSQLCommand("DELETE FROM PROJECT_CLIENT_LOGIN WHERE UNIQUE_ID = '" + abc + "'");
                                            execSQLCommand("DELETE FROM PROJECT_CLIENT WHERE UNIQUE_ID = '" + abc + "'");
                                            execSQLCommand ("UPDATE PROJECT_FLIGHT_DATE SET SEATS_OCCUPIED = SEATS_OCCUPIED - 1 WHERE FLIGHT_NO = '" + flno + "' AND FLIGHT_DATE = '" + date +"'");
                                            JOptionPane.showMessageDialog(null, "Your ticket with unique ID " + abc + " has been cancelled", "Ticket Cancellation", JOptionPane.INFORMATION_MESSAGE);
                                        } else {
                                            JOptionPane.showMessageDialog(null, "Your ticket can't be cancelled", "Ticket Cancellation", JOptionPane.ERROR_MESSAGE);
                                        }
                                        break;
                                    }
                                    i++;
                                }
                                
                            } catch (SQLException ex) {
                                Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
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