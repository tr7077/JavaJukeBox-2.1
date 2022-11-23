package hua.oop2;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

public class M3uManager {
	
	//private static final String plsPattern = ".+\\.(pls)$";
	private static final String mp3Pattern = ".+\\.(mp3)$";
	private static final String httpPattern = ".*http://.*";
	private static Mp3Player player = new Mp3Player();
	
	
	public void startPlaylist(String file) {
		startPlaylist(file, "order");
	}
	public void startPlaylist(String file, String method) {
		
		BufferedReader reader = null;
		
		ArrayList<String> shuffledSongs = null;
		
		try {
			
			String line = null;
			
			if(!method.equals("random")) {
				
				if(method.equals("loop")) {
					System.out.println("M3uFile: Loop Mode.");
				}else System.out.println("M3uFile: Order Mode.");
				
				do {
					
					reader = new BufferedReader(new FileReader(file));
					while((line = reader.readLine()) != null) {
						if(line.matches(mp3Pattern)) { // || line.matches(plsPattern)
							
							if(line.matches(httpPattern)) {
								// TODO
							}
							else {
								line = fixPath(line);
								player.play(line);
							}
						}
					}
					reader.close();
					
				}while(method.equals("loop"));
			}
			// random: TODO:
			else {
				shuffledSongs = new ArrayList<String>();
				reader = new BufferedReader(new FileReader(file));
				while((line = reader.readLine()) != null) {
					if(line.matches(mp3Pattern)) { // || line.matches(plsPattern)
						
						if(line.matches(httpPattern)) {
							// TODO
						}
						else {
							line = fixPath(line);
							shuffledSongs.add(line);
						}
					}
				}
				reader.close();
				
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
	
	public void close() {
		player.close();
	}
	
	private String fixPath(String line) {
		String[] array = line.split(" - ");
		if(array.length == 1) return array[0];
		return array[0] + "\\" + array[1];
	}
}
