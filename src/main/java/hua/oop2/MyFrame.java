package hua.oop2;

import java.awt.Color;
import java.awt.Component;
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

import javax.swing.DefaultListCellRenderer;
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
	
	public static final int WIDTH = 1000;
	public static final int HEIGHT = 800;
	private String strategy;
	
	private JButton m3uButton;
	private JButton dirButton;
	private JButton orderButton;
	private JButton loopButton;
	private JButton randButton;
	private JList<String> songs;
	private JPanel songsPanel;
	private JLabel selectedSong, statusLabel, durationLabel;
	private JButton next, play, pause, stop;
	private int currentSong;
	private ArrayList<String> songPaths;
	private ArrayList<String> songNames;
	private boolean[] songsPlayed;
	private boolean firstPlay;
	private static final Player player = PlayerFactory.getPlayer();
	private Status prevStatus;
	private MyFileChooser fileChooser;

	public MyFrame() {
		player.addPlayerListener(this);
		player.addProgressListener(this);
		prevStatus = null;
		
		this.setSize(WIDTH, HEIGHT);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setTitle("JukeBox2.1");
		this.setLocationRelativeTo(null);
		this.setResizable(false);
		this.setLayout(null);
		
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
		
		songsPanel = new JPanel();
		songsPanel.setBounds(350, 10, 400, 300);
		songsPanel.setBackground(Color.gray);
	
		selectedSong = new JLabel();
		selectedSong.setBounds(400, 250, 500, 300);
		selectedSong.setFont(new Font("Cascadia Code", Font.BOLD, 18));
		selectedSong.setForeground(Color.blue);
		selectedSong.setBackground(Color.cyan);
		
		statusLabel = new JLabel("Status: Waiting for music selection");
		statusLabel.setBounds(10, 550, 500, 300);
		statusLabel.setFont(new Font("Cascadia Code", Font.BOLD, 20));
		statusLabel.setForeground(Color.blue);
		statusLabel.setBackground(Color.cyan);
		System.out.println("Status: Waiting for music selection");
		
		durationLabel = new JLabel("Duration: 0.0s");
		durationLabel.setBounds(400, 270, 500, 100);
		durationLabel.setFont(new Font("Cascadia Code", Font.BOLD, 18));
		durationLabel.setForeground(Color.blue);
		durationLabel.setBackground(Color.cyan);
		
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
		
		strategy = "order";
		firstPlay = true;
		currentSong = 0;
		songPaths = new ArrayList<>();
		songNames = new ArrayList<>();
		fileChooser = new MyFileChooser(this);
		
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
		
		this.getContentPane().setBackground(Color.gray);
		this.setVisible(true);
	}
	
	private void initListOfSongs(ArrayList<File> temp) {
		if(temp == null) return;
		
		songPaths.clear(); songNames.clear();
		for(File f: temp) {
			songPaths.add(f.getAbsolutePath());
			songNames.add(f.getName().split(".mp3")[0]);
		}
		songsPlayed = new boolean[songNames.size()];
		firstPlay = true;
		currentSong = 0;
		newListSongs();
		statusLabel.setText("Status: Idle");
		System.out.println("Status: Idle");
	}
	@Override
	public void actionPerformed(ActionEvent e) {
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
		
		if(e.getSource() == m3uButton) {
			initListOfSongs(fileChooser.chooseM3u());
		}
		else if(e.getSource() == dirButton) {
			initListOfSongs(fileChooser.chooseDir());
		}
		
		if(songPaths == null) return;
		if(songPaths.size() == 0) return;
		
		if(e.getSource() == next) {
			if(strategy == "order") {
				currentSong = (currentSong + 1) % songPaths.size();
				play();
			}
			else if(strategy == "loop") {
				play();
			}
			else {
				chooseRandomSong();
				play();
			}
		}
		else if(e.getSource() == play) {
			if(firstPlay) play();
			else if(player.getStatus() == Player.Status.IDLE) play();
			else player.resume();
		}
		else if(e.getSource() == pause) {
			player.pause();
		}
		else if(e.getSource() == stop) {
			player.stop();
		}

	}
	
	private class CustomCellRenderer extends DefaultListCellRenderer {
		private final int index;

		public CustomCellRenderer(int index) {
			this.index = index;
		}

		@Override
		public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
			Component component;
			if (index == this.index) {
				component = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
				component.setBackground(Color.yellow);
			}
			else {
				component = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
				component.setBackground(Color.gray);
			}
			return component;
		}
	}

	@Override
	public void statusUpdated(PlayerEvent arg0) {
		
		statusLabel.setText("Status: " + arg0.getStatus());
		System.out.println("Status: " + arg0.getStatus());
		
		if((arg0.getStatus() == Player.Status.IDLE && prevStatus == Player.Status.PLAYING)) {
			
			if(strategy == "order") {
				currentSong = (currentSong + 1) % songPaths.size();
				play();
			}
			else if(strategy == "loop") {
				play();
			}
			else if(strategy == "random") {
				chooseRandomSong();
				play();
			}
		}
		prevStatus = player.getStatus();
	}
	@Override
	public void progress(ProgressEvent arg0) {
		float seconds = arg0.getMicroseconds() / 1000000f;
		durationLabel.setText("Duration: " + Math.round(seconds*10)/10f + "s");
	}
	
	private void play() {
		selectedSong.setText("Selected song: " + songs.getModel().getElementAt(currentSong));
		songs.setCellRenderer(new CustomCellRenderer(currentSong));
		String pathSong = songPaths.get(currentSong);
		
		if(player.getStatus() == Player.Status.PLAYING || player.getStatus() == Player.Status.PAUSED) {
			player.stop();
		}
		try {
			songsPlayed[currentSong] = true;
			player.startPlaying(new FileInputStream(pathSong));
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (PlayerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		firstPlay = false;
	}
	
	private void newListSongs() {	
		if(songNames == null) return;
		if(songNames.size() == 0) return;
		
		String[] temp = new String[songNames.size()];
		int i = 0;
		for(String s: songNames) {
			temp[i++] = s;
		}
		
		songs = new JList<>(temp);
		songs.setFont(new Font("Cascadia Code", Font.BOLD, 14));
		selectedSong.setText("Selected song: " + songs.getModel().getElementAt(currentSong));
		songs.setCellRenderer(new CustomCellRenderer(currentSong));
		player.pause();
		songs.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		
		songs.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 1) {
					currentSong = songs.locationToIndex(e.getPoint());
					play();
				}
			}
		});

		songsPanel.removeAll();
		songsPanel.add(new JScrollPane(songs));
		songsPanel.revalidate();
		songsPanel.repaint();
	}
	
	private void chooseRandomSong() {
		
		boolean reset = true;
		for(int i=0; i<songsPlayed.length; i++) {
			if(!songsPlayed[i]) {
				reset = false;
				break;
			}
		}
		if(reset) {
			for(int i=0; i<songsPlayed.length; i++) {
				songsPlayed[i] = false;
			}
		}
		
		while(true) {
			currentSong = new Random().nextInt(songsPlayed.length);
			if(!songsPlayed[currentSong]) break;
		}
	}
	
}
