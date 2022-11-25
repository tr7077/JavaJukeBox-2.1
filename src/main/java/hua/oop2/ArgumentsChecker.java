package hua.oop2;

public class ArgumentsChecker {
	
	private String[] args;
	private boolean isMp3;
	
	ArgumentsChecker(String[] args){
		this.args = args;
	}
		
	public void checkArgs() {
		if(args.length != 1 && args.length != 2) {
			System.err.println("You must enter 1 - 2 arguments.");
			System.exit(1);
		}
		isMp3 = args[0].matches(".+\\.(mp3)$");
		
		if(!args[0].matches(".+\\.(mp3|m3u)$")) {
			System.err.println("Argument 1 must be .mp3 or .m3u file.");
			System.exit(1);
		}
		
		if(args.length == 2) {
			if(!(args[1].equals("loop")||args[1].equals("random")||args[1].equals("order"))) {
				System.err.println("Argument 2 must be loop, random or order.");
				System.exit(1);
			}
			if(isMp3 && (args[1].equals("random") || args[1].equals("order"))) {
				System.err.println(".mp3 file only playable with loop or without 2nd argument.");
				System.exit(1);
			}
		}
	}
	
	public boolean isMp3() {
		return isMp3;
	}

}
