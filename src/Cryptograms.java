/**
 * @author Qingzhou Pan
 * Cryptograms.java
 * 
 * Description. This java program is build a word puzzle based upon a simple substitution cypher..
 */

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class Cryptograms {
	public static void main(String[] args) {
		ArrayList<Character> theQuote = getOneQuotes("quotes.txt");
		ArrayMap<Character, Character> encryptMap = CreateEncryptionKeyMap();
		ArrayList<Character> encrypted = Encrypt(theQuote, encryptMap);
		Decrypt(theQuote, encrypted, encryptMap);
	}
	
	/**
	 * Read the file and return the arraylist that contains one quote randomly.
	 * 
     * @param file_path the file path that can find the file.
     * @return the arraylist of characters that contains the selected quote.
     */
	public static ArrayList<Character> getOneQuotes(String file_path) {
		Scanner fileInput = null;
        try {
            fileInput = new Scanner(new File(file_path));
        } catch (FileNotFoundException ex) {
            System.out.println("ERROR: File not found.");
            System.exit(1);
        }
        
        // check if empty file
        if(!fileInput.hasNextLine()) {
        	System.out.println("ERROR: Empty file.");
        	System.exit(1);
        }
        
        // get all quotes from the given file
        ArrayList<String> quotes = new ArrayList<String>();
        while (fileInput.hasNextLine()) {
        	quotes.add(fileInput.nextLine().toUpperCase());
        }
        // randomly select one quote from quotes
        Random rand = new Random();
        String oneQuote = quotes.get(rand.nextInt(quotes.size()));
        ArrayList<Character> result = new ArrayList<Character>();
        for (char c : oneQuote.toCharArray()) {
        	result.add(c);
        }
        
        fileInput.close();
		return result;
	}

	/**
	 * Create a ArrayMap that matched 26 letters to 26 shuffled letters.
	 * 
	 * Make two arraylist that one contains 26 letters in alphabetical order
	 * 	and one contains 26 shuffled letters. Shuffle again if some letters 
	 * 	match to themselves. Put pairs of letters in the ArrayMap.
	 * 
	 * @return	the encrypted map.
	 */
	public static ArrayMap<Character, Character> CreateEncryptionKeyMap(){
		List<Character> letters 		 = new ArrayList<Character>();
		List<Character> shuffled_letters = new ArrayList<Character>();
		for (int i = 0; i < 26; i++) {
			letters.add((char)(65 + i));
			shuffled_letters.add((char)(65 + i));
		}
		
		// shuffle again if some letters match to themselves.
		int matchedToSelf = 1;
		while (matchedToSelf > 0) {
			matchedToSelf = 0;
			Collections.shuffle(shuffled_letters);
			for (int i = 0; i < 26; i++) {
				if (letters.get(i) == shuffled_letters.get(i)) {
					matchedToSelf += 1;
				}
			}
		}
		
		// make the ArrayMap for letters.
		ArrayMap<Character, Character> encryption_key = new ArrayMap<Character, Character>();
		for (int i = 0; i < 26; i++) {
			encryption_key.put(letters.get(i), shuffled_letters.get(i));
		}
		return encryption_key;
	}

	/**
	 * Encrypt the quote.
	 * 
	 * Encrypt the original quote into encrypted quote according to the encryption map.
	 * 
	 * @param origin the original quote.
	 * @param encryption_map the ArrayMap that contains encryption mapping between letters.
	 * @return the encrypted quote.
	 */
	public static ArrayList<Character> Encrypt(ArrayList<Character> origin, ArrayMap<Character, Character> encryption_map){
		ArrayList<Character> encrypted = new ArrayList<Character>(origin.size());
		for (int i = 0; i < origin.size(); i++) {
			encrypted.add(origin.get(i));
		}
		
		for (int i = 0; i < origin.size(); i++) {
			if ('A' <= origin.get(i) && origin.get(i) <= 'Z') {
				encrypted.set(i, encryption_map.get(origin.get(i)));
			} else {
				encrypted.set(i, origin.get(i));
			}
		}
		return encrypted;
	}
	
	/**
	 * Promote user to decrypt the encrypted quote.
	 * 
	 * Promote user to type in commands that can change letters, see letters frequency,
	 *  give a hint, exit game or get help. Function ends naturally when user finish the game.
	 * 
	 * @param origin the original quote.
	 * @param encrypted the encrypted quote.
	 * @param encryptMap the ArrayMap that contains encryption mapping between letters.
	 */
	public static void Decrypt(ArrayList<Character> origin, ArrayList<Character> encrypted, ArrayMap<Character, Character> encryptMap) {
		ArrayMap<Character, Character> user_map = new ArrayMap<Character, Character>();
		ArrayList<Character> user_guess = new ArrayList<Character>();
		user_guess = complieUserGuess(encrypted, user_map);
		printTwo(user_guess, encrypted);
		
    	Boolean not_equal = true;
	    while (not_equal) {
	    	Scanner promote = new Scanner(System.in);
	    	System.out.print("Enter a command (help to see commands): ");
	    	String command_line = promote.nextLine().toUpperCase();
	    	String[] command = command_line.trim().toUpperCase().split(" ");
	    	
	    	// letter change command
	    	if (command.length == 4 && command[0].equals("REPLACE") && command[2].equals("BY")) {
	    		user_map = replace(encrypted, user_map, command[1], command[3]);
	    	} 
	    	
	    	// letter change shortcut command
	    	else if (command.length == 3 && command[1].equals("=")) {
	    		user_map = replace(encrypted, user_map, command[0], command[2]);
	    	} 
	    	
	    	// frequency command
	    	else if (command.length == 1 && command[0].equals("FREQ")) {
	    		frequency(encrypted);
	    	} 
	    	
	    	// hint command
	    	else if (command.length == 1 && command[0].equals("HINT")) {
	    		user_map = hint(encrypted, user_map, encryptMap);
	    	} 
	    	
	    	// exit command
	    	else if (command.length == 1 && command[0].equals("EXIT")) {
	    		System.exit(1);
	    	} 
	    	
	    	// help command
	    	else if (command.length == 1 && command[0].equals("HELP")) {
	    		help();
	    	} 
	    	
	    	// unrecognized command
	    	else {
	    		System.out.println("Unrecognized Command. Please type \"HELP\" for help or another command.");
	    		System.out.println("");
	    	}
		    
	    	user_guess = complieUserGuess(encrypted, user_map);
	    	// check if finished
		    if (origin.equals(user_guess)) {
		    	not_equal = false;
		    	System.out.println("You got it!");
		    }
		    user_guess.clear();
	    }
	}
	
	/**
	 * Replace a by b and update the user map.
	 * Give error messages if multiple letters or wrong letters are given.
	 * 
	 * @param encrypted the encrypted quote.
	 * @param user_map the mapping that user guessed.
	 * @param a letter a.
	 * @param b letter b.
	 * @return updated user mapping of letters.
	 */
	public static ArrayMap<Character, Character> replace(ArrayList<Character> encrypted, ArrayMap<Character, Character> user_map, String a, String b) {
		ArrayList<Character> user_guess = new ArrayList<Character>();
		if (a.length() > 1 || b.length() > 1) {
			System.out.println("Error: should be single letter.");
			return user_map;
		}
		char which = a.charAt(0);
		char replacement = b.charAt(0);
		if ('A' <= which && which <= 'Z' && 'A' <= replacement && replacement <= 'Z') {
			user_map.put(which, replacement);
			user_guess = complieUserGuess(encrypted, user_map);
    		printTwo(user_guess, encrypted);
		} else {
			System.out.println("Error: should be letters A - Z.");
		}
		return user_map;
	}
	
	/**
	 * Print out frequencies of letters.
	 * 
	 * @param encrypted the encrypted quote.
	 */
	public static void frequency(ArrayList<Character> encrypted) {
		ArrayMap<Character, Integer> prep = new ArrayMap<Character, Integer>();
		for (int i = 0; i < 26; i++) {
			prep.put((char) (65+i), 0);
		}
		for (int i = 0; i< encrypted.size(); i++) {
			char currChar = encrypted.get(i);
			if ('A' <= currChar && currChar <= 'Z') {
				prep.put(currChar, prep.get(currChar)+1);
			}
		}
		
		int nth = 0;
		for (char c : prep.keySet()) {
			nth += 1;
			System.out.print(c + ": " + prep.get(c) + " ");
			if (nth % 7 == 0) {
				System.out.println();
			}
		}
		System.out.println("\n");
	}
	
	/**
	 * 
	 * Give user a hint match that not been guessed.
	 * 
	 * @param encrypted the encrypted quote.
	 * @param user_map the mapping that user guessed.
	 * @param encryptMap the mapping for letters.
	 * @return the updated user mapping of letters.
	 */
	public static ArrayMap<Character, Character> hint(ArrayList<Character> encrypted, ArrayMap<Character, Character> user_map, ArrayMap<Character, Character> encryptMap) {
		boolean full = true;
		char temp;
		ArrayList<Character> user_guess = new ArrayList<Character>();
		
		ArrayMap<Character, Character> hintMap = new ArrayMap<Character, Character>();
		for (char key: encryptMap.keySet()) {
			hintMap.put(encryptMap.get(key), key);
		}
		
		for (int i = 0; i < encrypted.size(); i++) {
			temp = encrypted.get(i);
			if ('A' <= temp && temp <= 'Z') {
				if (!user_map.containsKey(temp)) {
					user_map.put(temp, hintMap.get(temp));
					full = false;
					break;
				}
			}
			
		}
		if (full) {
			for (int i = 0; i < encrypted.size(); i++) {
    			temp = encrypted.get(i);
    			if ('A' <= temp && temp <= 'Z') {
    				char userGuessedOne = user_map.get(temp);
        			char correctOne = hintMap.get(temp);
        			if (userGuessedOne != correctOne) {
        				user_map.put(temp, hintMap.get(temp));
        				break;
        			}
    			}
    			
    		}
		}
		user_guess = complieUserGuess(encrypted, user_map);
		printTwo(user_guess, encrypted);
		
		return user_map;
	}
	
	/**
	 * Print help information
	 */
	public static void help() {
		System.out.println("a. Replace X by Y – replace letter X by letter Y in our attempted solution");
		System.out.println("   X = Y – a shortcut for this same command");
		System.out.println("b. freq – Display the letter frequencies in the encrypted quotation (i.e., how many of letter X appear) like:");
		System.out.println("   A: 3 B: 8 C:4  D: 0 E: 12 F: 4 G: 6");
		System.out.println("   (and so on, 7 per line for 4 lines)");
		System.out.println("c. hint – display one correct mapping that has not yet been guessed");
		System.out.println("d. exit – Ends the game early");
		System.out.println("e. help – List these commands");
		System.out.println("");
	}
	
	/**
	 * Compile the user guess quote.
	 * 
	 * Compile the user guess quote according to the mapping of letters by user.
	 * 
	 * @param encrypted the encrypted quote.
	 * @param user_map the mapping that user guessed.
	 * @return user guessed quote.
	 */
	public static ArrayList<Character> complieUserGuess(ArrayList<Character> encrypted, ArrayMap<Character, Character> user_map) {
		ArrayList<Character> user_guess = new ArrayList<Character>();
		for (int i = 0; i < encrypted.size(); i++) {
	    	if ('A' <= encrypted.get(i) && encrypted.get(i) <= 'Z') {
	    		if (user_map.containsKey(encrypted.get(i))) {
	    			user_guess.add(user_map.get(encrypted.get(i)));
	    		} else {
	    			user_guess.add(' ');
	    		}
	    	} else {
	    		user_guess.add(encrypted.get(i));
	    	}
	    }
		return user_guess;
	}
	
	/**
	 * Print out user guess quote and encrypted quote.
	 * 
	 * Print out quotes and break each at whitespace or punctuation
	 *  to make sure each line is less than 80 letters.
	 * 
	 * @param user_guessIn the user guess quote.
	 * @param encryptedIn the encrypted quote.
	 */
	public static void printTwo(ArrayList<Character> user_guessIn, ArrayList<Character> encryptedIn) {
		List<Character> user_guess = new ArrayList<Character>();
		List<Character> encrypted = new ArrayList<Character>();
		for (int i = 0; i < encryptedIn.size(); i++) {
			user_guess.add(user_guessIn.get(i));
			encrypted.add(encryptedIn.get(i));
		}
		
		List<Character> up = new ArrayList<Character>();
		List<Character> down = new ArrayList<Character>();
		
		int index;
		while (encrypted.size() > 80) {
			index = 80;
			while (encrypted.get(index) != ' ') {
				index -= 1;
			}
			up = user_guess.subList(0, index + 1);
			down = encrypted.subList(0, index + 1);
			if (encrypted.size() > 0) {
				user_guess = user_guess.subList(index + 1, user_guess.size());
				encrypted = encrypted.subList(index + 1, encrypted.size());
			}
			System.out.println(list2string(up));
			System.out.println(list2string(down));
			System.out.println();
		}
		
		if (encrypted.size() > 0) {
			up = user_guess.subList(0, user_guess.size());
			down = encrypted.subList(0, encrypted.size());
			user_guess = user_guess.subList(0, user_guess.size());
			encrypted = encrypted.subList(0, encrypted.size());
		}
		System.out.println(list2string(up));
		System.out.println(list2string(down));
		System.out.println();
	}
	
	
	/**
	 * Convert the list into string.
	 * 
	 * @param target the target list.
	 * @return the string converted from the target list.
	 */
	public static String list2string(List<Character> target) {
		String result = "";
		for (int i = 0; i < target.size(); i++) {
			result += target.get(i);
		}
		return result;
	}
	
}
