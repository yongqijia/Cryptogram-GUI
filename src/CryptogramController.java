import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.util.regex.Pattern;

public class CryptogramController {
	private static CryptogramModel model;
	
	/**
	 * Initialize the controller and update the information that stored at model.
	 * 
	 * @param model
	 */
	CryptogramController(CryptogramModel model){
		this.model = model;
		model.set_quote(getquote());
		model.set_correct_mapping(random_key());
		model.set_encrypted_quotes(encryption(model.get_correct_mapping(), model.get_qoute()));
		model.set_guess_map(make_guessmap());
	}
	
	/**
	 * update the user guesses list which was located at model.
	 * 
	 * @param replace
	 * @param replacement
	 */
	public void replace(char replace, char replacement) {
		model.replace(replace, replacement);
	}
	
	/**
	 * get model.get_encrypted_quotes()
	 * 
	 * @return model.get_encrypted_quotes()
	 */
	public String get_encrypted_quotes() {
		return model.get_encrypted_quotes();
	}
	
	/**
	 * get model.get_guess_map()
	 * 
	 * @return model.get_guess_map()
	 */
	public ArrayList<Character> get_guess_map(){
		return model.get_guess_map();
	}
	
	/**
	 * Initialize the guessmap ArrayList.
	 * 
	 * @return guess_str
	 */
	public ArrayList<Character> make_guessmap() {
		ArrayList<Character> guess_str = new ArrayList<Character>();
		for(int i = 0; i < model.get_encrypted_quotes().length(); i++) {
			if(Pattern.matches("\\p{Punct}", Character.toString(model.get_encrypted_quotes().charAt(i)))) {
				guess_str.add(i, model.get_encrypted_quotes().charAt(i));
			}else {
				guess_str.add(i, ' ');
			}
		}
		return guess_str;
	}
	
	
	/**
	 * Gets the quote.
	 * Get the quote from the input file.
	 *
	 * @return the quote
	 */
	public static String getquote() {
		Scanner input = null;
		try {
            input = new Scanner(new File("quotes.txt"));
		}catch(FileNotFoundException ex) {
            System.out.println("ERROR: File not found.");
            System.exit(1);
		}
        ArrayList<String> quotes_array = new ArrayList<>();
        while (input.hasNextLine()) {
            String quotes = input.nextLine().trim();
            quotes_array.add(quotes);
        }
        Random rand = new Random();
        if(quotes_array.size() == 0) {
        	System.out.println("Error: File is empty!");
        	System.exit(1);
        }
        int n = rand.nextInt(quotes_array.size());
        String target = new String();
        target = quotes_array.get(n).toUpperCase();
        input.close();
		return target;
	}
	
	/**
	 * Encrypt the quotes that randomly chosen.
	 * 
	 * Create a new string to store encrypted quotes. Keep the same punctuation and only encrypt letter.
	 * 
	 * @param cryptogram the ArrayMap the contain the correct mapping.
	 * 
	 * @param target the quotes that randomly chosen.
	 * 
	 * @return the encrypted string 
	 */
	public static String encryption(HashMap<Character, Character> cryptogram, String target) {
		String encrypted = new String();
		for(int i = 0; i < target.length(); i++) {
			if(target.charAt(i) == ' ') {
				encrypted += ' ';
			}else if(Pattern.matches("\\p{Punct}", Character.toString(target.charAt(i)))){
				encrypted += Character.toString(target.charAt(i));
			}else {
				encrypted += cryptogram.get(target.charAt(i));
			}
		}
		return encrypted;
	}
	
	/**
	 * Create the random encryption key and store in a ArrayMap.
	 * 
	 * First create a ArrayMap that contain alphabetical order.
	 * Using shuffle to create mappings.
	 * 
	 * @return a ArrayMap contain correct mapping
	 */
	private static HashMap<Character, Character> random_key() {
		List<Character> alpha = new ArrayList<Character>(); 
		List<Character> temp = new ArrayList<Character>();
		for(int i = 0; i < 26; i++){
			temp.add((char)(65 + i));
			alpha.add((char)(65 + i));
		}
		HashMap<Character, Character> substitution_cypher = new HashMap<Character, Character>();
		int count = 0;
		while(count < 26) {
			count = 0;
			Collections.shuffle(alpha);
			for(int i = 0; i < 26; i++) {
				if(alpha.get(i) != temp.get(i)) {
					count += 1;
				}
			}
		}
		for(int i = 0; i < temp.size(); i++) {
			substitution_cypher.put(temp.get(i), alpha.get(i));
		}	
		return substitution_cypher;	
	}
	
	/**
	 * hint function is to display one correct mapping that has not yet been guessed.
	 * 
	 * First find one character that has not been guessed, and then, using cryptogram to get the correct
	 * mapping and display. Finally, check whether user is get the whole mapping correct.
	 * 
	 * Finally, call model.replace() to update the user guess list which was located at model.
	 */
	public void hint() {
		char need_replace = ' ';
		String encrypted_quotes = model.get_encrypted_quotes();
		ArrayList<Character> guess_str = model.get_guess_map();
		HashMap<Character, Character> cryptogram = model.get_correct_mapping();
		for(int i = 0; i < encrypted_quotes.length(); i++) {
			if('A' <= encrypted_quotes.charAt(i) && 'Z' >= encrypted_quotes.charAt(i)) {
				if(guess_str.get(i) == ' ') {
					need_replace = encrypted_quotes.charAt(i);
					break;
				}
			}
		}
		char replacement = ' ';
		for(char letter : cryptogram.keySet()) {
			if(cryptogram.get(letter) == need_replace) {
				replacement = letter;
			}
		}
		model.replace(need_replace, replacement);
	}
	
	/**
	 * freq method is to create a hashmap and mapping the letters and their number of occurrences.
	 * 
	 * @return HashMap<Character, Integer> freq
	 */
	public HashMap<Character, Integer> freq() {
		List<Character> temp = new ArrayList<Character>();
		HashMap<Character, Integer> freq = new HashMap<Character, Integer>();
		for(int i = 0; i < 26; i++){
			temp.add((char)(65 + i));
		}
		for(int i = 0; i < temp.size(); i++) {
			freq.put(temp.get(i), 0);
		}
		for(char letter : freq.keySet()) {
			for(int j= 0; j < model.get_encrypted_quotes().length(); j++) {
				if(!Pattern.matches("\\p{Punct}", model.get_encrypted_quotes().substring(j, j+1))) {
					if(Character.toString(letter).equals(model.get_encrypted_quotes().substring(j, j+1))) {
						freq.put(letter, freq.get(letter)+1);
					}
				}
			}
		}
		return freq;
	}
	
	/**
	 * isOver method is to check whether user has finished the decryption.
	 * 
	 * @return boolean
	 */
	public Boolean isOver() {
		String temp = "";
	    for(int i = 0; i < model.get_guess_map().size(); i++) {
	    	temp += model.get_guess_map().get(i);
	    }	 

	    // check whether user complete the decryption.
	    if(temp.equals(model.get_qoute())) {
	    	return true;
	    }else {
	    	return false;
	    }	
	}
	
}
