package hua.oop2;

import java.io.IOException;

public class MainPlayer {

	public static void main(String[] args) throws IOException {
		
		ArgumentsChecker checker = new ArgumentsChecker(args);
		
		checker.checkArgs();

		if(checker.isMp3()) {
			Mp3Player player = new Mp3Player();
			
			if(args.length == 1)
				player.play(args[0]);
			else
				player.play(args[0], args[1]);
			
			player.close();
		}
		else {
			M3uManager pplayer = new M3uManager();
			
			if(args.length == 1)
				pplayer.startPlaylist(args[0]);
			else
				pplayer.startPlaylist(args[0], args[1]);
			
			pplayer.close();
		}
	}

}