package hua.oop2;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

/**
 * @author teo&manos
 * @Description This class was created for handling and playing .m3u playlists which contain songs .mp3, under a specific method-strategy
 */
public class M3uManager {
	
	//private static final String plsPattern = ".+\\.(pls)$";
	private static final String mp3Pattern = ".+\\.(mp3)$";
	private static final String httpPattern = ".*http://.*";
	private static Mp3Player player = new Mp3Player();
	
	
	public void startPlaylist(String file) {
		startPlaylist(file, "order");
	}
	/**
	 * @Description takes an .m3u file, and according to the method reads it and plays the songs with the help of Mp3Player class.
	 *  If a FileNotFoundException or a IOException is catched then it prints out an appropriate message
	 * @param file
	 * @param method
	 * @return void
	 */
	public void startPlaylist(String file, String method) {
		
		try {
				
			System.out.print(file + ": ");
			if(method.equals("loop")) {
				System.out.println("Loop Mode.");
			}else if(method.equals("random")) System.out.println("Random Mode.");
			else System.out.println("Order Mode.");
			
			String line;
			BufferedReader reader;
			ArrayList<String> shuffledSongs = new ArrayList<String>();
			
			// if method == loop then the .m3u will be played in loop
			// if method == order code in do while will be executed only once
			// if method == random code in do while will be executed only once and paths will be saved in an arraylist, shuffled and then played
			do {
				reader = new BufferedReader(new FileReader(file));
				while((line = reader.readLine()) != null) {
					if(line.matches(mp3Pattern)) { // || line.matches(plsPattern)
						
						if(line.matches(httpPattern)) {
							// TODO
						}
						else {
							line = fixPath(line);
							if(method.equals("random")) {
								shuffledSongs.add(line);
							}
							else player.play(line);
						}
					}
				}
				reader.close();
				
			}while(method.equals("loop"));
			
			if(method.equals("random")) {
				Collections.shuffle(shuffledSongs);
				for(String song: shuffledSongs) {
					player.play(song);
				}
			}
		}
		catch (FileNotFoundException e) {
			System.err.println("File " + file + " not found.");
		}
		catch (IOException e) {
			System.err.println("Something went wrong while reading file " + file);
		}
	}
	
	/**
	 * @Description closes the Mp3Player object when called
	 * @return void
	 */
	public void close() {
		player.close();
	}
	
	/**
	 * @Description takes a string that contains info about the path of a .mp3 file and returns the path fixed
	 * @param line
	 * @return String
	 */
	private String fixPath(String line) {
		String[] array = line.split(" - ");
		if(array.length == 1) return array[0];
		return array[0] + "\\" + array[1];
	}
}
