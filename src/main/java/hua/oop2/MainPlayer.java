package hua.oop2;

public class MainPlayer {

	public static void main(String[] args) {
		
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
			M3uManager m3uPlayer = new M3uManager();
			
			if(args.length == 1)
				m3uPlayer.startPlaylist(args[0]);
			else
				m3uPlayer.startPlaylist(args[0], args[1]);
			
			m3uPlayer.close();
		}
		
		System.out.println("GoodBye!");
	}
}