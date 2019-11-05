import java.util.ArrayList;
import java.util.HashMap;

public class CryptogramModel extends java.util.Observable{
	private static String quote;
	private static String encrypted_quotes;
	public static ArrayList<Character> guess_map;
	private static HashMap<Character, Character> correct_mapping;
	
	/**
	 * Initialize the guess_map
	 */
	public CryptogramModel() {
		guess_map = new ArrayList<Character>();
	}
	
	/**
	 * store the quote
	 * 
	 * @param quote
	 */
	public void set_quote(String quote) {	
		this.quote = quote;
	}
	
	/**
	 * get the quote in model.
	 * 
	 * @return String quote
	 */
	public String get_qoute() {
		return quote;
	}
	
	/**
	 * store the guess_map in model.
	 * 
	 * @param guess_map
	 */
	public void set_guess_map(ArrayList<Character> guess_map){
		this.guess_map = guess_map;
	}
	
	/**
	 * get the guess_map
	 * 
	 * @return guess_map
	 */
	public ArrayList<Character> get_guess_map(){
		return guess_map;
	}
	
	/**
	 * store encrypted_quotes in model.
	 * 
	 * @param encrypted_quotes
	 */
	public void set_encrypted_quotes(String encrypted_quotes) {
		this.encrypted_quotes = encrypted_quotes;
	}
	
	/**
	 * get the encrypted_quotes.
	 * 
	 * @return encrypted_quotes
	 */
	public String get_encrypted_quotes() {
		return encrypted_quotes;
	}
	
	/**
	 * store the correct_mapping in model.
	 * 
	 * @param correct_mapping
	 */
	public void set_correct_mapping(HashMap<Character, Character> correct_mapping) {
		this.correct_mapping = correct_mapping;
	}
	
	/**
	 * get the correct_mapping
	 * 
	 * @return correct_mapping
	 */
	public HashMap<Character, Character> get_correct_mapping(){
		return correct_mapping;
	}
	
	/**
	 * update the guess_map and notify the view to change the view.
	 * 
	 * @param replace
	 * @param replacement
	 */
	public void replace(Character replace, Character replacement) {
		for(int i = 0; i < encrypted_quotes.length(); i++) {
	    	if(encrypted_quotes.charAt(i) == replace) {
	    		guess_map.set(i, replacement.toString().toUpperCase().charAt(0));
	    	}
	    }
		ArrayList<Character> list = new ArrayList<>();
		list.add(replace);
		list.add(replacement);
		setChanged();
		notifyObservers(list);	
	}
}
