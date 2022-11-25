package hua.oop2;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

import gr.hua.dit.oop2.musicplayer.Player;
import gr.hua.dit.oop2.musicplayer.PlayerException;
import gr.hua.dit.oop2.musicplayer.PlayerFactory;

public class Mp3Player {
	
	private static Player player = PlayerFactory.getPlayer();
	
	public void play(String file) {
		play(file, null);
	}
	public void play(String file, String method) {
		try {
			System.out.print(file + ": ");
			if(method == null) {
				System.out.println("OneTime Mode.");
			}else System.out.println("Loop Mode.");
			
			do {
				player.play(new FileInputStream(file));
			}while(method != null); //method == loop
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
