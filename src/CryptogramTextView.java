import java.util.ArrayList;
import java.util.HashMap;
import java.util.Observable;
import java.util.Scanner;
import java.util.regex.Pattern;

public class CryptogramTextView implements java.util.Observer{
	private CryptogramController controller;
	private CryptogramModel model;
	
	
	public CryptogramTextView(){
		model = new CryptogramModel();
		controller = new CryptogramController(model);
		model.addObserver(this);
		decryption();
		
	}

	/**
	 * decryption is using additional ArrayMap to store the user guesses and mapping to their ArrayMap
	 * and show them their progress by using the ArrayMap to replace the encrypted characters with their
	 * guess ones.
	 * 
	 * @param encrypted_quotes the string that only contain punctuation
	 * 
	 * @param cryptogram the ArrayMap that contain the correct the mapping.
	 * 
	 * @param target the quotes that has already been encrypted.
	 */
	public void decryption() {
		Boolean check = true;
		format_print(model.get_guess_map(), model.get_encrypted_quotes());
		while(check) {
			Scanner input = new Scanner(System.in);
		    System.out.print("Enter a command (help to see commands): ");
		    String[] command = input.nextLine().toUpperCase().trim().split(" ");
		    
		    if(command[0].equals("REPLACE") && command[2].equals("BY") && command.length == 4) {
		    	check_valid(command[1], command[3]);
		    	controller.replace(command[1].charAt(0), command[3].charAt(0));
		    	
		    }else if(command.length == 3) {
		    	check_valid(command[0], command[2]);
		    	controller.replace(command[0].charAt(0), command[2].charAt(0));
		    }else if(command.length == 1 && command[0].equals("FREQ")) {
		    	HashMap<Character, Integer> map = controller.freq();
		    	print_freq(map);
		    	System.out.println();
		    	format_print(model.get_guess_map(), model.get_encrypted_quotes());;
		    	continue;
		    }else if(command.length == 1 && command[0].equals("HINT")) {
		    	System.out.println();
		    	controller.hint();		    	
		    	continue;
		    }else if(command.length == 1 && command[0].equals("EXIT")) {
		    	System.out.println("\nGame is over.\n");
		    	System.exit(1);
		    }else if(command.length == 1 && command[0].equals("HELP")) {
		    	help();
		    	
		    	continue;
		    }else {
		    	System.out.println("\nError: This is not an options. Please enter \"help\" for all vaild commands.\n");
		    	continue;
		    }
		}
	}

	/**
	 * print all valid command.
	 */
	public static void help() {
		System.out.print("\n1. replace X by Y – replace letter X by letter Y in our attempted solution\n");
    	System.out.print("   X = Y          – a shortcut for this same command\n");
    	System.out.print("2. freq           – Display the letter frequencies in the encrypted quotation\n");
    	System.out.print("3. hint           – display one correct mapping that has not yet been guessed\n");
    	System.out.print("4. exit           – Ends the game early\n");
    	System.out.print("5. help           – List these commands\n");
    	System.out.println();	
	}

	
	/**
	 * format_print function is to support arbitrarily long quotes. 
	 * If the quotes or guess are more than 80 characters, first print the 80 characters per line splits by
	 * whitespace or punctuation. And then, print the rest.
	 * 
	 * @param guess_str the list store the user guess
	 * 
	 * @param encrypted_quotes the string that only contain punctuation
	 */
	private static void format_print(ArrayList<Character> guess_str, String encrypted_quotes) {
		
		String temp_guess_str = "";
		for(int i = 0; i < guess_str.size(); i++) {
			temp_guess_str += guess_str.get(i);
		}
		String temp_encrypted_quotes = encrypted_quotes;	
		while(temp_encrypted_quotes.length() > 30) {
			int check = 30;
			while(temp_encrypted_quotes.charAt(check) != ' ') {
				check -= 1;
			}
			
			System.out.println(temp_guess_str.substring(0, check + 1));
			System.out.println(temp_encrypted_quotes.substring(0, check + 1) + "\n");
			temp_guess_str = temp_guess_str.substring(check + 1);
			temp_encrypted_quotes = temp_encrypted_quotes.substring(check + 1);	
		}
		System.out.println(temp_guess_str);
		System.out.println(temp_encrypted_quotes);	
	}
	
	/**
	 * print the frequency of the letters as given format.
	 * 
	 * @param map
	 */
	public static void print_freq(HashMap<Character, Integer> map) {
		int count = 0;
		for(char letter : map.keySet()) {
			if(count % 7 == 0) {
				System.out.println();
			}
			System.out.print(letter + ": " + map.get(letter) + " ");
			count += 1;
		}
	}

	/**
	 * update the view and check whether the game is over.
	 * 
	 * @param replace
	 * 
	 * @param replacement
	 */
	@Override
	public void update(Observable o, Object arg) {
		format_print(model.get_guess_map(),model.get_encrypted_quotes());
		
		if(controller.isOver()) {
			System.out.println();
			System.out.println("You Win!");
			System.out.println("Game is Over.");
			System.exit(1);
		}
	} 
	
	/**
	 * check_valid function is to check user's input whether or not is valid.
	 * 
	 * @param which	replace letter
	 * 
	 * @param replacement replacement letter
	 */
	public void check_valid(String which, String replacement) {
		if(which.length() != 1 || replacement.length() != 1) {
	    	System.out.println("\nError: Can't replce multi letters at on time. Only one letter is allowed.\n");	
	    }
	    if(Pattern.matches("\\p{Punct}", replacement) || Pattern.matches("\\p{Punct}", which)) {
	    	System.out.println("\nError: Punctuation is not allowed.\n");
    	}	
	}
}
