# JavaJukeBox-2.0

1.) public class ArgumentsChecker{} :
	<br>
	- This class was created for validation of cmd line arguments
	- An instance of this class is created in main method once in the beginning, args[] array is passed to the constructor

	- The class contains:
		- public void checkArgs() method which checks the format of args's elements and the number of them
		- If something is wrong it prints out an appropriate message and the program terminates with code 1
		- This method is called once at the beginning
	
		- public boolean isMp3() method returns true if args[0] is .mp3 or false otherwise
		- this method was created in order to inform the main method of the type of file in args[0]

2.) public class Mp3Player{} :
	<br>
	- This class was created for playing just an .mp3 file under a specific method-strategy
	- Instances of this class are created in other classes
	
	- The class contains:
		- a private static gr.hua.dit.oop2.musicplayer.Player player for playing the songs, it's static because the same Player can be used for playing all different songs

		- public void play(String file, String method) method which creates a player object from the external library gr.hua.dit.oop2.musicplayer
		- then our play method uses the player object to play the song according to the given method
		- Also if a FileNotFoundException or a PlayerException is catched then it prints out an appropriate message

		- public void close() method which closes the player object when called

3.) public class M3uManager{} :
	<br>
	- This class was created for handling and playing .m3u playlists which contain songs .mp3, under a specific method-strategy
	- An instance of this class is created in main method once if  ArgumentsChecker.isMp3() return false

	- The class contains:
		- a private static Mp3Player player for playing the songs, it's static because the same Mp3Player can be used for playing all different songs

		- public void startPlaylist(String file, String method) which takes an .m3u file, and according to the method reads it and plays the songs with the help of Mp3Player class
		- Also if a FileNotFoundException or a IOException is catched then it prints out an appropriate message
		
		- private String fixPath(String line) method which takes a string that contains info about the path of a .mp3 file and returns the path fixed
			ex: input: D:\Java_Eclipse\Maven\JavaJukeBox-2.0\src\main\resources - sample3s.mp3
			      output: D:\Java_Eclipse\Maven\JavaJukeBox-2.0\src\main\resources\sample3s.mp3 

		- public void close() method which closes the Mp3Player player object when called

4.) HOW TO EXECUTE THE PROJECT :
<br>
	-  Make sure you have git and maven installed
<br>
	-  open a terminal
<br> 
	-  execute this: git clone https://github.com/tr7077/JavaJukeBox-2.0.git
<br>
	-  execute this: cd ./JavaJukeBox-2.0/target
<br>
	-  execute this: java -jar .\JavaJukeBox-2.0-1.0-SNAPSHOT.jar <.mp3 or .m3u file> <[optional] "order","loop","random">
<br>

 	-  Warning: when .mp3 file is given, only loop or (no 2nd argument == just plays the song and terminates) is accepted
<br>
	-  .m3u file + no 2nd argument == .m3u file + "order"