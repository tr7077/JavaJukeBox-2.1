package hua.oop2;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

import gr.hua.dit.oop2.musicplayer.Player;
import gr.hua.dit.oop2.musicplayer.PlayerException;
import gr.hua.dit.oop2.musicplayer.PlayerFactory;

/**
 * @author teo&manos
 * @Description This class was created for playing just an .mp3 file under a specific method-strategy
 */
public class Mp3Player {
	
	private static Player player = PlayerFactory.getPlayer();
	
	public void play(String file) {
		play(file, null);
	}
	
	/**
	 * @Description Creates a player object from the external library gr.hua.dit.oop2.musicplayer,
	 *  uses the player object to play the song according to the given method.
	 *  If a FileNotFoundException or a PlayerException is catched then it prints out an appropriate message.
	 * @param file
	 * @param method
	 * @return void
	 */
	public void play(String file, String method) {
		try {
			System.out.print(file + ": ");
			if(method == null) {
				System.out.println("OneTime Mode.");
			}else System.out.println("Loop Mode.");
			
			
			//if method = null then song will play once else it's a loop
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
	/**
	 * @Description closes the player object when called
	 * @return void
	 */
	public void close() {
		player.close();
	}
}
