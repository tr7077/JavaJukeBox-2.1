package hua.oop2;


import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
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
		
		this.add(m3uButton);
		this.add(dirButton);
		this.add(strategyPanel);
		this.add(songsPanel);
		
		this.getContentPane().setBackground(Color.cyan);
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
		String path = null;
		if(e.getSource() == m3uButton) {
			path = chooseM3u();
		}
		else if(e.getSource() == dirButton) {
			path = chooseDir();
			ArrayList<String> mp3s = getMp3sNamesFromDir(path);
			for(String song: mp3s) {
				System.out.println(song);
			}
			String[] temp = new String[mp3s.size()];
			int i = 0;
			for(String s: mp3s) {
				temp[i++] = s;
			}
			songs = new JList<>(temp);
			songs.setBounds(400, 10, 400, 300);
			songsPanel.removeAll();
			songsPanel.add(songs);
			songsPanel.revalidate();
			songsPanel.repaint();
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
}
