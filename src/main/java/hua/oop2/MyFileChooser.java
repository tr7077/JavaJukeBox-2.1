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
	
	// takes a MyFrame object in order open a window for file selection in that frame
	public MyFileChooser(MyFrame frame) {
		this.frame = frame;
	}
	
	public ArrayList<File> chooseDir() {
		// setup the Jfilechooser object for directory selection only
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
		fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		// open the dialog window
		int result = fileChooser.showOpenDialog(frame);
		// if selection was valid
		if (result == JFileChooser.APPROVE_OPTION) {
			// save the selected dir
			File selectedFile = fileChooser.getSelectedFile();
			// save the files of the dir to an array
			File[] songs = selectedFile.listFiles();
			// save only the .mp3 file to an arraylist
			ArrayList<File> mp3Files = new ArrayList<>();
			for(File f: songs) {
				if(f.getAbsolutePath().matches(".+\\.(mp3)$")) {
					mp3Files.add(f);
				}
			}
			// if arraylist is empty return null
			return mp3Files.size() == 0 ? null : mp3Files;
		}
		// if selection was invalid return null
		return null;
	}
	
	public ArrayList<File> chooseM3u() {
		// setup the Jfilechooser object for .m3u selection only
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
		fileChooser.setFileFilter(new FileNameExtensionFilter("M3U File", "m3u"));
		// open the dialog window
		int result = fileChooser.showOpenDialog(frame);
		// if selection was valid
		if (result == JFileChooser.APPROVE_OPTION) {
			// save the .m3u file
		    File selectedFile = fileChooser.getSelectedFile();
		    // arraylist for .mp3 paths inside of .m3u
		    ArrayList<File> mp3Files = new ArrayList<>();		
		    // read line by line and if the path exists and it's a .mp3 file save it
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
		    // if arraylist is empty return null
		    return mp3Files.size() == 0 ? null : mp3Files;
		}
		// if selection was invalid return null
		return null;
	}
}
