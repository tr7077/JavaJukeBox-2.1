package hua.oop2;


import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import javax.swing.ButtonGroup;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.filechooser.FileNameExtensionFilter;

import gr.hua.dit.oop2.musicplayer.Player;
import gr.hua.dit.oop2.musicplayer.Player.Status;
import gr.hua.dit.oop2.musicplayer.PlayerEvent;
import gr.hua.dit.oop2.musicplayer.PlayerException;
import gr.hua.dit.oop2.musicplayer.PlayerFactory;
import gr.hua.dit.oop2.musicplayer.PlayerListener;

@SuppressWarnings("serial")
public class MyFrame extends JFrame implements ActionListener, PlayerListener {
	
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
	private JLabel selectedSong;
	private JButton next, prev, play, pause, replay;
	private int currentSong;
	private ArrayList<String> songPaths;
	private ArrayList<String> songNames;
	private boolean firstPlay;
	private static final Player player = PlayerFactory.getPlayer();
	private Status prevStatus;
	private MyFileChooser fileChooser;
	private boolean cancelled;

	
	public MyFrame() {
		player.addPlayerListener(this);
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
		songsPanel.setBounds(400, 10, 400, 300);
		songsPanel.setBackground(Color.cyan);
		
		selectedSong = new JLabel();
		selectedSong.setBounds(400, 250, 500, 300);
		selectedSong.setFont(new Font("Cascadia Code", Font.BOLD, 18));
		selectedSong.setForeground(Color.blue);
		selectedSong.setBackground(Color.cyan);
		
		prev = new JButton("Previous");
		next = new JButton("Next");
		play = new JButton("Play");
		pause = new JButton("Pause");
		replay = new JButton("Replay");
		prev.addActionListener(this);
		next.addActionListener(this);
		play.addActionListener(this);
		pause.addActionListener(this);
		replay.addActionListener(this);
		prev.setBounds(400, 450, 100, 45);
		next.setBounds(500, 450, 100, 45);
		play.setBounds(600, 450, 100, 45);
		pause.setBounds(700, 450, 100, 45);
		replay.setBounds(800, 450, 100, 45);
		prev.setFocusable(false);
		next.setFocusable(false);
		play.setFocusable(false);
		pause.setFocusable(false);
		replay.setFocusable(false);
		
		strategy = "order";
		firstPlay = true;
		currentSong = 0;
		songPaths = new ArrayList<>();
		songNames = new ArrayList<>();
		fileChooser = new MyFileChooser(this);
		cancelled = false;
		
		this.add(m3uButton);
		this.add(dirButton);
		this.add(songsPanel);
		this.add(selectedSong);
		this.add(prev);
		this.add(next);
		this.add(play);
		this.add(pause);
		this.add(replay);
		this.add(orderButton);
		this.add(loopButton);
		this.add(randButton);
		
		this.getContentPane().setBackground(Color.gray);
		this.setVisible(true);
	}
	
	private void initListOfSongs(ArrayList<File> temp) {
		if(temp == null) {
			cancelled = true;
			return;
		}
		songPaths.clear(); songNames.clear();
		for(File f: temp) {
			songPaths.add(f.getAbsolutePath());
			songNames.add(f.getName().split(".mp3")[0]);
		}
		firstPlay = true;
		currentSong = 0;
		newListSongs();
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
		
		if(e.getSource() == prev) {
			currentSong = currentSong == 0 ? songPaths.size()-1 : currentSong-1;
			play();
		}
		else if(e.getSource() == next) {
			currentSong = (currentSong + 1) % songPaths.size();
			play();
		}
		else if(e.getSource() == play) {
			if(firstPlay) play();
			else player.resume();
		}
		else if(e.getSource() == pause) {
			player.pause();
		}
		else if(e.getSource() == replay) {
			play();
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
		System.out.println("current: " + arg0.getStatus() + " prev: " + prevStatus);
//		if(arg0.getStatus() == Player.Status.IDLE && prevStatus == Player.Status.PLAYING) {
//			//System.out.println("Play next song");
//			if(strategy == "order") {
//				currentSong++;
//				if(currentSong == songPaths.size()) return;
//				play();
//			}
//			else if(strategy == "loop") {
//				System.out.println("loop playing");
//				play();
//			}
//			else if(strategy == "random") {
//				
//			}
//		}
		prevStatus = arg0.getStatus();
	}
	
	private void play() {
		selectedSong.setText("Selected song: " + songs.getModel().getElementAt(currentSong));
		songs.setCellRenderer(new CustomCellRenderer(currentSong));
		String pathSong = songPaths.get(currentSong);
	
		if(player.getStatus() == Player.Status.PLAYING || player.getStatus() == Player.Status.PAUSED) {
			player.stop();
		}
		try {
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
		if(cancelled) {
			cancelled = false;
			return;
		}
		if(songNames == null) return;
		if(songNames.size() == 0) return;
		for(String song: songNames) {
			System.out.println(song);
		}
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
		songs.setBounds(400, 10, 400, 300);
		songsPanel.removeAll();
		songsPanel.add(songs);
		songsPanel.revalidate();
		songsPanel.repaint();
	}
	
}
