package hua.oop2;

public class ArgumentsChecker {
	
	private String[] args;
	private boolean isMp3;
	
	ArgumentsChecker(String[] args){
		this.args = args;
	}
		
	public boolean checkArgs() {
		if(args.length != 1 && args.length != 2) {
			return false;
		}
		isMp3 = args[0].matches(".+\\.(mp3)$");
		
		if(!args[0].matches(".+\\.(mp3|m3u)$")) {
			return false;
		}
		
		if(args.length == 2) {
			if(!(args[1].equals("loop")||args[1].equals("random")||args[1].equals("order"))) {
				return false;
			}
			if(isMp3 && (args[1].equals("random") || args[1].equals("order"))) {
				return false;
			}
		}

		return true;
	}
	
	public boolean isMp3() {
		return isMp3;
	}

}
