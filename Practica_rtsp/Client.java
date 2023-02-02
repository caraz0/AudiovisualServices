import java.io.*;
import java.net.*;
import java.util.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.SwingUtilities;

import uk.co.caprica.vlcj.component.EmbeddedMediaPlayerComponent;
import uk.co.caprica.vlcj.discovery.NativeDiscovery;
import uk.co.caprica.vlcj.player.MediaPlayer;
import uk.co.caprica.vlcj.player.MediaPlayerEventAdapter;

public class Client{
    
    private String url_str;
    private int puerto_int;
    private String ip_url;
    private RTSP conf;
    private String IpAddr;

    private final JFrame frame;
    
    private final EmbeddedMediaPlayerComponent mediaPlayerComponent;
    
    public static void main(final String[] args) {
        new NativeDiscovery().discover();
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new Client(args);
            }
        });
    }
    
    public Client(String[] args) {
        
        IpAddr = args[0];
        frame = new JFrame("Media Player");
        
        //TO DO! choose the correct arguments for the methods below. Add more method calls as necessary
        frame.setLocation(50, 50);
        frame.setSize(500,300);
        //...
        
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                mediaPlayerComponent.release();
                System.exit(0);
            }
        });
        
        JPanel contentPane = new JPanel();
        contentPane.setLayout(new BorderLayout());
        
        mediaPlayerComponent = new EmbeddedMediaPlayerComponent();
        contentPane.add(mediaPlayerComponent, BorderLayout.CENTER);
        
        JTextField url = new JTextField(150);
        contentPane.add(url, BorderLayout.NORTH);
        JPanel controlsPane = new JPanel();

        //Boton SETUP
        JButton setupButton = new JButton("Setup");
        controlsPane.add(setupButton);
        contentPane.add(controlsPane, BorderLayout.SOUTH);
        setupButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //TO DO!! configure the playback of the video received via RTP, or resume a paused playback.
                int puerto = 5004;
                String url_video = url.getText();
                url_video=url_video.replaceAll("://", "-");
                url_video=url_video.replaceAll("/", "-");
                url_video=url_video.replaceAll(":", "-");
                String linea[] = url_video.split("-");
                ip_url=linea[1];
                String aux = linea[2];
                puerto_int=Integer.parseInt(aux);
                url_str=linea[3];
                conf= new RTSP(ip_url, puerto_int, puerto, url_str);
                conf.send_request("SETUP");
                
               
                //...
            }
        });
        //Definition of PLAY button
        
        //----------------------
        JButton playButton = new JButton("Play");
        controlsPane.add(playButton);
        contentPane.add(controlsPane, BorderLayout.SOUTH);
        
        //Handler for PLAY button
        //-----------------------
        playButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //TO DO!! configure the playback of the video received via RTP, or resume a paused playback.
                mediaPlayerComponent.getMediaPlayer().playMedia("rtp://"+ IpAddr +":5004");
                conf.send_request("PLAY");
                //...
            }
        });
        
        //TO DO! implement a PAUSE button to pause video playback.
        JButton pauseButton = new JButton("Pause");
        controlsPane.add(pauseButton);
        pauseButton.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                mediaPlayerComponent.getMediaPlayer().pause();
                conf.send_request("PAUSE");
            }

        });
        
        
        //TO DO! implement a STOP button to stop video playback and exit the application.
             JButton stopButton = new JButton("Stop");
        controlsPane.add(stopButton);
        stopButton.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                mediaPlayerComponent.getMediaPlayer().stop();
                conf.send_request("TEARDOWN");
            }

        });
        
        
        //Makes visible the window
        frame.setContentPane(contentPane);
        frame.setVisible(true);
        
        
    }
    
}

