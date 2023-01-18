package hua.oop2;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

public class MyFileChooser {
	
	private MyFrame frame;
	
	public MyFileChooser(MyFrame frame) {
		this.frame = frame;
	}
	
	public ArrayList<File> chooseDir() {
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
		fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		int result = fileChooser.showOpenDialog(frame);
		if (result == JFileChooser.APPROVE_OPTION) {
			File selectedFile = fileChooser.getSelectedFile();
		    // TODO: make chooseDir return the songs from the path rather than just returning the path, so that it checks if the path contains any songs to then return null
			File[] songs = selectedFile.listFiles();
			ArrayList<File> mp3Files = new ArrayList<>();
			for(File f: songs) {
				if(f.getAbsolutePath().matches(".+\\.(mp3)$")) {
					mp3Files.add(f);
				}
			}
			return mp3Files.size() == 0 ? null : mp3Files;
		}
		return null;
	}
	
	public ArrayList<File> chooseM3u() {
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
		fileChooser.setFileFilter(new FileNameExtensionFilter("M3U File", "m3u"));
		int result = fileChooser.showOpenDialog(frame);
		if (result == JFileChooser.APPROVE_OPTION) {
		    File selectedFile = fileChooser.getSelectedFile();
		    ArrayList<File> mp3Files = new ArrayList<>();
		    //////////////
		    try(BufferedReader reader = new BufferedReader(new FileReader(selectedFile))){
				String line;
				while((line = reader.readLine()) != null) {
					if(line.matches("^#")) continue;
					if(line.matches(".+\\.(mp3)$")) {
						File tempFile = new File(line);
						if(tempFile.exists()) {
							mp3Files.add(tempFile);
							continue;
						}
					}
				}
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		    //////////////
		    return mp3Files.size() == 0 ? null : mp3Files;
		}
		return null;
	}
}
