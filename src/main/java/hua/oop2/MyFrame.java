package hua.oop2;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;

import gr.hua.dit.oop2.musicplayer.Player;
import gr.hua.dit.oop2.musicplayer.Player.Status;
import gr.hua.dit.oop2.musicplayer.PlayerEvent;
import gr.hua.dit.oop2.musicplayer.PlayerException;
import gr.hua.dit.oop2.musicplayer.PlayerFactory;
import gr.hua.dit.oop2.musicplayer.PlayerListener;
import gr.hua.dit.oop2.musicplayer.ProgressEvent;
import gr.hua.dit.oop2.musicplayer.ProgressListener;

@SuppressWarnings("serial")
public class MyFrame extends JFrame implements ActionListener, PlayerListener, ProgressListener {
	
	// constants for frame dimensions
	public static final int WIDTH = 1000;
	public static final int HEIGHT = 800;
	
	// buttons
	private JButton m3uButton, dirButton; 
	private JButton orderButton, loopButton, randButton;
	private JButton next, play, pause, stop;
	
	// songs names and paths
	private JList<String> songs;
	private ArrayList<String> songPaths;
	private ArrayList<String> songNames;
	
	// useful fields for the logic of the player
	private JPanel songsPanel;
	private JLabel selectedSong, statusLabel, durationLabel;
	private int currentSong;
	private boolean[] songsPlayed;
	private boolean firstPlay;
	private static final Player player = PlayerFactory.getPlayer();
	private String strategy;
	private Status prevStatus;
	private MyFileChooser fileChooser;

	public MyFrame() {
		// setup player
		player.addPlayerListener(this);
		player.addProgressListener(this);
		prevStatus = null;
		
		// setup frame
		this.setSize(WIDTH, HEIGHT);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setTitle("JukeBox2.1");
		this.setLocationRelativeTo(null);
		this.setResizable(false);
		this.setLayout(null);
		
		// setup file selection buttons
		m3uButton = new JButton("Choose an M3u file");
		dirButton = new JButton("Choose a directory with music");
		m3uButton.setFocusable(false);
		dirButton.setFocusable(false);
		m3uButton.setFont(new Font("Orbitron", Font.BOLD, 12));
		dirButton.setFont(new Font("Orbitron", Font.BOLD, 12));
		m3uButton.setBackground(Color.gray);
		dirButton.setBackground(Color.gray);
		m3uButton.setForeground(Color.white);
		dirButton.setForeground(Color.white);
		m3uButton.setBounds(10, 50, 200, 50);
		dirButton.setBounds(10, 150, 250, 50);
		m3uButton.addActionListener(this);
		dirButton.addActionListener(this);
		
		// setup strategy selection buttons
		orderButton = new JButton("Order");
		loopButton = new JButton("Loop");
		randButton = new JButton("Random");
		orderButton.addActionListener(this);
		loopButton.addActionListener(this);
		randButton.addActionListener(this);
		orderButton.setBounds(10, 450, 100, 45);
		loopButton.setBounds(110, 450, 100, 45);
		randButton.setBounds(210, 450, 100, 45);
		orderButton.setFocusable(false);
		loopButton.setFocusable(false);
		randButton.setFocusable(false);
		orderButton.setBackground(Color.cyan);
		loopButton.setBackground(Color.gray);
		randButton.setBackground(Color.gray);
		
		// setup the panel that holds the Jlist of songs
		songsPanel = new JPanel();
		songsPanel.setBounds(350, 10, 400, 300);
		songsPanel.setBackground(Color.gray);
		
		// setup label of current selected song
		selectedSong = new JLabel();
		selectedSong.setBounds(400, 250, 500, 300);
		selectedSong.setFont(new Font("Cascadia Code", Font.BOLD, 18));
		selectedSong.setForeground(Color.blue);
		selectedSong.setBackground(Color.cyan);
		
		// setup label of current player status
		statusLabel = new JLabel("Status: Waiting for music selection");
		statusLabel.setBounds(10, 550, 500, 300);
		statusLabel.setFont(new Font("Cascadia Code", Font.BOLD, 20));
		statusLabel.setForeground(Color.blue);
		statusLabel.setBackground(Color.cyan);
		System.out.println("Status: Waiting for music selection");
		
		// setup label of song time playing
		durationLabel = new JLabel("Duration: 0.0s");
		durationLabel.setBounds(400, 270, 500, 100);
		durationLabel.setFont(new Font("Cascadia Code", Font.BOLD, 18));
		durationLabel.setForeground(Color.blue);
		durationLabel.setBackground(Color.cyan);
		
		// setup basic player buttons
		next = new JButton("Next");
		play = new JButton("Play");
		pause = new JButton("Pause");
		stop = new JButton("Stop");
		next.addActionListener(this);
		play.addActionListener(this);
		pause.addActionListener(this);
		stop.addActionListener(this);
		next.setBounds(450, 450, 100, 45);
		play.setBounds(550, 450, 100, 45);
		pause.setBounds(650, 450, 100, 45);
		stop.setBounds(750, 450, 100, 45);
		next.setFocusable(false);
		play.setFocusable(false);
		pause.setFocusable(false);
		stop.setFocusable(false);
		
		// initialize important fields to starting values
		strategy = "order";
		firstPlay = true;
		currentSong = 0;
		songPaths = new ArrayList<>();
		songNames = new ArrayList<>();
		fileChooser = new MyFileChooser(this);
		
		// adding components in the frame
		this.add(m3uButton);
		this.add(dirButton);
		this.add(songsPanel);
		this.add(selectedSong);
		this.add(next);
		this.add(play);
		this.add(pause);
		this.add(stop);
		this.add(orderButton);
		this.add(loopButton);
		this.add(randButton);
		this.add(statusLabel);
		this.add(durationLabel);
		
		// color and visibility
		this.getContentPane().setBackground(Color.gray);
		this.setVisible(true);
	}
	
	private void initListOfSongs(ArrayList<File> temp) {
		// if the arraylist returned is null then just return
		if(temp == null) return;
		// clear previous songs info
		songPaths.clear(); songNames.clear();
		// save paths and names of the newly selected songs
		for(File f: temp) {
			songPaths.add(f.getAbsolutePath());
			songNames.add(f.getName().split(".mp3")[0]);
		}
		// reset songsPlayed array to indicate that no song has been played yet
		songsPlayed = new boolean[songNames.size()];
		// reset firstPlay and currentSong
		firstPlay = true;
		currentSong = 0;
		// update the Jlist with the new song info
		newListSongs();
		// update the status to default
		statusLabel.setText("Status: IDLE");
		System.out.println("Status: IDLE");
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		// if a strategy button is pressed update strategy and highlight the selected button
		if(e.getSource() == orderButton) { 
			strategy = "order";
			orderButton.setBackground(Color.cyan);
			loopButton.setBackground(Color.gray);
			randButton.setBackground(Color.gray);
		}
		else if(e.getSource() == loopButton) {
			strategy = "loop";
			loopButton.setBackground(Color.cyan);
			orderButton.setBackground(Color.gray);
			randButton.setBackground(Color.gray);
		}
		else if(e.getSource() == randButton) {
			strategy = "random";
			randButton.setBackground(Color.cyan);
			orderButton.setBackground(Color.gray);
			loopButton.setBackground(Color.gray);
		}
		
		// if file selection button is pressed call the corresponding method and update the list of songs inside the panel
		if(e.getSource() == m3uButton) {
			initListOfSongs(fileChooser.chooseM3u()); // initListOfSongs accepts arraylist of files and chooseM3u return an arraylist of files from an m3u
		}
		else if(e.getSource() == dirButton) {
			initListOfSongs(fileChooser.chooseDir()); // initListOfSongs accepts arraylist of files and chooseDir return an arraylist of files from a dir
		}
		
		// if no music is selected then do not continue to avoid exceptions
		if(songPaths == null) return;
		if(songPaths.size() == 0) return;
		
		// if next is pressed update the currentSong based on the strategy and then play()
		if(e.getSource() == next) {
			chooseNextSongBasedOnStrategyAndPlay();
		}
		else if(e.getSource() == play) {
			// if it's the firstplay or the player is idle then the play button should start playing the selectedSong from the beginning
			if(firstPlay || player.getStatus() == Player.Status.IDLE) play();
			// else it should just resume the currentSong that will probably have been paused
			else player.resume();
		}
		else if(e.getSource() == pause) {
			player.pause();
		}
		else if(e.getSource() == stop) {
			player.stop();
		}

	}
	
	private void chooseNextSongBasedOnStrategyAndPlay(){
		if(strategy == "order") 
			currentSong = (currentSong + 1) % songPaths.size();
		else if(strategy == "random")
			chooseRandomSong();
		play();
	}

	@Override
	public void statusUpdated(PlayerEvent arg0) {
		
		// update the status label
		statusLabel.setText("Status: " + arg0.getStatus());
		System.out.println("Status: " + arg0.getStatus());
		
		// automatically play the next song if the player just finished playing an other one
		if((arg0.getStatus() == Player.Status.IDLE && prevStatus == Player.Status.PLAYING)) {
			chooseNextSongBasedOnStrategyAndPlay();
		}
		// update the previous status for the next play
		prevStatus = player.getStatus();
	}
	@Override
	public void progress(ProgressEvent arg0) {
		// calculate the seconds passed since the start of the song playing
		float seconds = arg0.getMicroseconds() / 1000000f;
		// update the text of the duration label
		durationLabel.setText("Duration: " + Math.round(seconds*10)/10f + "s");
	}
	
	private void play() {
		// update the text of the selectedSong label
		selectedSong.setText("Selected song: " + songs.getModel().getElementAt(currentSong));
		// highlight the current song in the Jlist
		songs.setCellRenderer(new CustomCellRenderer(currentSong));
		
		// stop the player before playing an other song if it's currently playing or have paused an other song
		if(player.getStatus() == Player.Status.PLAYING || player.getStatus() == Player.Status.PAUSED) {
			player.stop();
		}
		try {
			// set the currentSong as played
			songsPlayed[currentSong] = true;
			// play the currentSong
			player.startPlaying(new FileInputStream(songPaths.get(currentSong)));
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (PlayerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// after playing one song then it's no longer the firstPlay
		firstPlay = false;
	}
	
	private void newListSongs() {	
		
		// create a string array with song names
		String[] temp = new String[songNames.size()];
		int i = 0;
		for(String s: songNames) {
			temp[i++] = s;
		}
		
		// initialize the Jlist with the newly created string array
		songs = new JList<>(temp);
		songs.setFont(new Font("Cascadia Code", Font.BOLD, 14));
		// update the text of selectedSong label
		selectedSong.setText("Selected song: " + songs.getModel().getElementAt(currentSong));
		// highlight the currentSong
		songs.setCellRenderer(new CustomCellRenderer(currentSong));
		// stop the player because when a new list of songs is selected whatever was playing before should stop
		player.stop();
		// only select one song at a time
		songs.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		// add a mouse listener and override in an anonymous class
		songs.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 1) { // when a song is clicked, update the currentSong and play it
					currentSong = songs.locationToIndex(e.getPoint());
					play();
				}
			}
		});
		
		// reset the panel and add the new songs with a scroller
		songsPanel.removeAll();
		songsPanel.add(new JScrollPane(songs));
		songsPanel.revalidate();
		songsPanel.repaint();
	}
	
	// this method just updates the currentSong in a random manner
	private void chooseRandomSong() {
		// check if there are songs that haven't been played yet
		boolean reset = true;
		for(int i=0; i<songsPlayed.length; i++) {
			if(!songsPlayed[i]) {
				reset = false;
				break;
			}
		}
		// if there aren't any, reset the songsPlayed array
		if(reset) {
			for(int i=0; i<songsPlayed.length; i++) {
				songsPlayed[i] = false;
			}
		}
		
		// choose a random song that hasn't been played yet
		while(true) {
			currentSong = new Random().nextInt(songsPlayed.length);
			if(!songsPlayed[currentSong]) break;
		}
	}
	
}
