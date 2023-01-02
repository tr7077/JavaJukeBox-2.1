package hua.oop2;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

import gr.hua.dit.oop2.musicplayer.Player;
import gr.hua.dit.oop2.musicplayer.PlayerException;
import gr.hua.dit.oop2.musicplayer.PlayerFactory;
//import gr.hua.dit.oop2.musicplayer.PlayerListener;

/**
 * @author teo&manos
 * @Description This class was created for playing just an .mp3 file under a specific method-strategy
 */
public class Mp3Player {
	
	private static Player player = PlayerFactory.getPlayer();
	
	
	public void play(String path) {
		if(path == null && player.getStatus() == Player.Status.PAUSED) {
			player.resume();
			return;
		}
		else if(path == null) return;
		
		if(player.getStatus() == Player.Status.PLAYING || player.getStatus() == Player.Status.PAUSED) {
			player.stop();
		}
		try {
			player.startPlaying(new FileInputStream(path));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (PlayerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void pause() {
		if(player.getStatus() == Player.Status.PLAYING) {
			player.pause();
		}
	}
	public void replay(String path) {
		play(path);
	}
	
	/**
	 * @Description closes the player object when called
	 * @return void
	 */
	public void close() {
		player.close();
	}
}
