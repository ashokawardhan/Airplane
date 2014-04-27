package project_dbms;
import javax.swing.*;
import java.awt.event.*;
import java.sql.*;
import java.awt.*;
import java.util.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.logging.Level;
import java.util.logging.Logger;
import javazoom.jl.player.advanced.*;

public class Project_DBMS extends JFrame {
    Connection theConnection;
    Statement theStatement;
    ResultSet theResult;
    ResultSetMetaData theMetaData;
    
    String theDataSource;
    String theUser;
    String thePassword;
    
    public static void main(String[] args) throws ClassNotFoundException, InstantiationException, IllegalAccessException, UnsupportedLookAndFeelException {
        SoundJLayer soundToPlay = new SoundJLayer("Swervedriver-Planes Over the Skyline-.mp3");

        soundToPlay.play();
        new Project_DBMS().init();
    }
    public void init() throws ClassNotFoundException, InstantiationException, IllegalAccessException, UnsupportedLookAndFeelException 
    {
        
        openConnection();
       //execSQLCommand("SELECT * FROM ASSIGNMENT2_CUSTOMER");
        final JFrame in = new JFrame();
        //in.setLayout(new FlowLayout());
        
        JButton getin = new JButton(new ImageIcon("YourImage.png"));
        in.add(getin);
        in.setSize(600,700);
        in.setVisible(true);
        in.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        getin.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
                            in.dispose();
                            Login x = new Login();
                            try {
                                x.init();
                            } catch (ClassNotFoundException ex) {
                                Logger.getLogger(Project_DBMS.class.getName()).log(Level.SEVERE, null, ex);
                            } catch (InstantiationException ex) {
                                Logger.getLogger(Project_DBMS.class.getName()).log(Level.SEVERE, null, ex);
                            } catch (IllegalAccessException ex) {
                                Logger.getLogger(Project_DBMS.class.getName()).log(Level.SEVERE, null, ex);
                            } catch (UnsupportedLookAndFeelException ex) {
                                Logger.getLogger(Project_DBMS.class.getName()).log(Level.SEVERE, null, ex);
                            }
                            closeConnection();
                        }
        });
       
    }
    
     public void openConnection()
    {
        theDataSource = "jdbc:oracle:thin:@localhost:1521:XE";
        theUser = "system";
        thePassword = "taurus";
        
        try
        {
            Class.forName("oracle.jdbc.driver.OracleDriver");
            theConnection = DriverManager.getConnection(theDataSource, theUser, thePassword);
        }
        catch(Exception e)
        {
            handleException(e);
        }
    }
    
    public void closeConnection()
    {
        try
        {
            theConnection.close();
        }
        catch (Exception e)
        {
            handleException(e);
        }
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
class SoundJLayer extends PlaybackListener implements Runnable
{
    private String filePath;
    private AdvancedPlayer player;
    private Thread playerThread;    

    public SoundJLayer(String filePath)
    {
        this.filePath = filePath;
    }

    public void play()
    {
        try
        {
            String urlAsString = 
                "file:///" 
                + new java.io.File(".").getCanonicalPath() 
                + "/" 
                + this.filePath;

            this.player = new AdvancedPlayer
            (
                new java.net.URL(urlAsString).openStream(),
                javazoom.jl.player.FactoryRegistry.systemRegistry().createAudioDevice()
            );

            this.player.setPlayBackListener(this);

            this.playerThread = new Thread(this, "AudioPlayerThread");

            this.playerThread.start();
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }

 

    public void run()
    {
        try
        {
            this.player.play();
        }
        catch (javazoom.jl.decoder.JavaLayerException ex)
        {
            ex.printStackTrace();
        }

    }
}