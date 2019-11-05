/**
 * @author Yongqi Jia
 */
import javafx.application.Application;

public class Cryptogram{
	public static void main(String[] args) {
		if(args.length > 0 && args[0].equals("-text")) {
			CryptogramTextView textView = new CryptogramTextView();
		}
		Application.launch(CryptogramGUIView.class, args);		
	}
}
