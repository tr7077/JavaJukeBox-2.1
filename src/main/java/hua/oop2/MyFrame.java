package hua.oop2;


import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.FileInputStream;
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
import javax.swing.ListSelectionModel;
import javax.swing.filechooser.FileNameExtensionFilter;

@SuppressWarnings("serial")
public class MyFrame extends JFrame implements ActionListener {
	
	public static final int WIDTH = 1000;
	public static final int HEIGHT = 800;
	private String strategy;
	
	private JButton m3uButton;
	private JButton dirButton;
	private JRadioButton orderButton;
	private JRadioButton loopButton;
	private JRadioButton randButton;
	private ButtonGroup strategyGroup;
	private JPanel strategyPanel;
	private JList<String> songs;
	private JPanel songsPanel;
	private JLabel selectedSong;
	private JButton next, prev, play, pause, replay;
	private int currentSong;
	private String path;
	private static Mp3Player player = new Mp3Player();
	private ArrayList<String> absoluteMp3Paths;
	private boolean firstPlay;

	
	public MyFrame() {
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
		
		orderButton = new JRadioButton("Order");
		loopButton = new JRadioButton("Loop");
		randButton = new JRadioButton("Random");
		orderButton.addActionListener(this);
		loopButton.addActionListener(this);
		randButton.addActionListener(this);
		
		strategyGroup = new ButtonGroup();
		strategyGroup.add(orderButton);
		strategyGroup.add(loopButton);
		strategyGroup.add(randButton);
		
		strategyPanel = new JPanel();
		strategyPanel.add(orderButton);
		strategyPanel.add(loopButton);
		strategyPanel.add(randButton);
		strategyPanel.setBounds(10, 500, 200, 40);
		strategyPanel.setBackground(Color.gray);
		strategyPanel.setForeground(Color.gray);
		
		songsPanel = new JPanel();
		songsPanel.setBounds(400, 10, 400, 300);
		
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
		
		this.add(m3uButton);
		this.add(dirButton);
		this.add(strategyPanel);
		this.add(songsPanel);
		this.add(selectedSong);
		this.add(prev);
		this.add(next);
		this.add(play);
		this.add(pause);
		this.add(replay);
		
		this.getContentPane().setBackground(Color.gray);
		this.setVisible(true);
	}
	
	private String chooseM3u() {
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
		fileChooser.setFileFilter(new FileNameExtensionFilter("M3U File", "m3u"));
		int result = fileChooser.showOpenDialog(this);
		if (result == JFileChooser.APPROVE_OPTION) {
		    File selectedFile = fileChooser.getSelectedFile();
		    return selectedFile.getAbsolutePath();
		}
		return null;
	}
	private String chooseDir() {
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
		fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		int result = fileChooser.showOpenDialog(this);
		if (result == JFileChooser.APPROVE_OPTION) {
		    File selectedFile = fileChooser.getSelectedFile();
		    return selectedFile.getAbsolutePath();
		}
		return null;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == m3uButton) {
			path = chooseM3u();
		}
		else if(e.getSource() == dirButton) {
			path = chooseDir();
			absoluteMp3Paths = getMp3sAbsoluteFromDir(path);
			firstPlay = true;
			currentSong = 0;
			ArrayList<String> mp3s = getMp3sNamesFromDir(path);
			if(mp3s == null) return;
			for(String song: mp3s) {
				System.out.println(song);
			}
			String[] temp = new String[mp3s.size()];
			int i = 0;
			for(String s: mp3s) {
				temp[i++] = s;
			}
			songs = new JList<>(temp);
			songs.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			songs.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					if (e.getClickCount() == 1) {
						int index = songs.locationToIndex(e.getPoint());
						currentSong = index;
						String item = songs.getModel().getElementAt(index);
						selectedSong.setText("Selected song: " + item);
						songs.setCellRenderer(new CustomCellRenderer(currentSong));				
						player.play(absoluteMp3Paths.get(currentSong));
						firstPlay = false;
					}
				}
			});
			songs.setBounds(400, 10, 400, 300);
			songsPanel.removeAll();
			songsPanel.add(songs);
			songsPanel.revalidate();
			songsPanel.repaint();
			
		}
		if(absoluteMp3Paths == null) return;
		if(absoluteMp3Paths.size() == 0) return;
		if(e.getSource() == prev) {
			currentSong = currentSong == 0 ? absoluteMp3Paths.size()-1 : currentSong-1;
			selectedSong.setText("Selected song: " + songs.getModel().getElementAt(currentSong));
			songs.setCellRenderer(new CustomCellRenderer(currentSong));
			player.play(absoluteMp3Paths.get(currentSong));
			firstPlay = false;
		}
		else if(e.getSource() == next) {
			currentSong = (currentSong + 1) % absoluteMp3Paths.size();
			selectedSong.setText("Selected song: " + songs.getModel().getElementAt(currentSong));
			songs.setCellRenderer(new CustomCellRenderer(currentSong));
			player.play(absoluteMp3Paths.get(currentSong));
			firstPlay = false;
		}
		else if(e.getSource() == play) {
			if(firstPlay) {
				selectedSong.setText("Selected song: " + songs.getModel().getElementAt(currentSong));
				player.play(absoluteMp3Paths.get(currentSong));
				firstPlay = false;
			}
			else player.play(null);
		}
		else if(e.getSource() == pause) {
			player.pause();
		}
		else if(e.getSource() == replay) {
			player.replay(absoluteMp3Paths.get(currentSong));
		}
		
		if(e.getSource() == orderButton) {
			strategy = "order";
		}
		else if(e.getSource() == loopButton) {
			strategy = "loop";
		}
		else if(e.getSource() == randButton) {
			strategy = "random";
		}
		
	}
	
	private ArrayList<String> getMp3sAbsoluteFromDir(String dir) {
		if(dir == null) return null;
		
		ArrayList<String> mp3s = new ArrayList<>();
		String mp3Pattern = ".+\\.(mp3)$";
		
		File directory = new File(dir);
	    // Get all the files in the directory
	    File[] files = directory.listFiles();
	    // Iterate through the files and print their names
	    for (File file : files) {
	    	if(file.getAbsolutePath().matches(mp3Pattern)) {
	    		mp3s.add(file.getAbsolutePath());
	    	}
	    }
	 
	    return mp3s;
	}
	private ArrayList<String> getMp3sNamesFromDir(String dir) {
		if(dir == null) return null;
		
		ArrayList<String> mp3s = new ArrayList<>();
		String mp3Pattern = ".+\\.(mp3)$";
		
		File directory = new File(dir);
	    // Get all the files in the directory
	    File[] files = directory.listFiles();
	    // Iterate through the files and print their names
	    for (File file : files) {
	    	if(file.getAbsolutePath().matches(mp3Pattern)) {
	    		mp3s.add(file.getName().split(".mp3")[0]);
	    	}
	    }
	 
	    return mp3s;
	}
	
	private class CustomCellRenderer extends DefaultListCellRenderer {
		private final int index;

		public CustomCellRenderer(int index) {
			this.index = index;
		}

		@Override
		public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected,
				boolean cellHasFocus) {
			Component component;
			if (index == this.index) {
				component = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
				component.setBackground(Color.yellow);
			} else {
				component = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
				component.setBackground(list.getBackground());
			}
			return component;
		}
	}

	
}
