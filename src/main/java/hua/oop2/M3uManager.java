package hua.oop2;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class M3uManager {
	
	private static final String plsPattern = ".+\\.(pls)$";
	private static final String mp3Pattern = ".+\\.(mp3)$";
	private static final String httpPattern = ".*http://.*";
	
	M3uManager(String file) throws IOException {
		this(file, "order");
	}
	M3uManager(String file, String method) throws IOException {
		BufferedReader reader = null;
		Mp3Player player = new Mp3Player();
		try {
			
			String line = null;
			
			if(!method.equals("random")) {
				
				if(method.equals("loop")) {
					System.out.println("M3uFile: Loop Mode.");
				}else System.out.println("M3uFile: Order Mode.");
				
				do {
					
					reader = new BufferedReader(new FileReader(file));
					while((line = reader.readLine()) != null) {
						if(line.matches(plsPattern) || line.matches(mp3Pattern)) {
							
							if(line.matches(httpPattern)) {
								
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
				System.out.println("M3uFile: Random Mode.");
			}
			
		}
		catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally {
			player.close();
		}
	}
	
	private String fixPath(String line) {
		String[] array = line.split(" - ");
		return array[0] + "\\" + array[1];
	}
}
