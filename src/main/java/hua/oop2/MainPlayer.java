package hua.oop2;

import java.io.IOException;

public class MainPlayer {

	public static void main(String[] args) throws IOException {
		
		ArgumentsChecker checker = new ArgumentsChecker(args);
		
		if(!checker.checkArgs()) {
			throw new IllegalArgumentException("Invalid arguments!");
		}
	
		if(checker.isMp3()) {
			Mp3Player player = new Mp3Player();
			
			if(args.length == 1)
				player.play(args[0]);
			else
				player.play(args[0], args[1]);
			
			player.close();
		}
		else {
			
			if(args.length == 1)
				new M3uManager(args[0]);
			else
				new M3uManager(args[0], args[1]);
		}
	}

}