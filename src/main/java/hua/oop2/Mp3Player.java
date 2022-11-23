package hua.oop2;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

import gr.hua.dit.oop2.musicplayer.Player;
import gr.hua.dit.oop2.musicplayer.PlayerException;
import gr.hua.dit.oop2.musicplayer.PlayerFactory;

public class Mp3Player {
	
	private static Player player = PlayerFactory.getPlayer();
	private FileInputStream song;
	
	public void play(String file) {
		play(file, null);
	}
	public void play(String file, String method) {
		try {
			song = new FileInputStream(file);
			if(method == null) {
				System.out.println("Song: OneTime Mode.");
				player.play(song);
			}
			else {
				System.out.println("Song: Loop Mode.");
				while(true) {
					player.play(song);
					song = new FileInputStream(file);
				}
			}
		}
		catch (FileNotFoundException e) { 
			System.err.println("File " + file + " not found");
		}
		catch (PlayerException e) {
			System.err.println("Something's wrong with the player: " + e.getMessage());
		}
	}
	public void close() {
		player.close();
	}
}
