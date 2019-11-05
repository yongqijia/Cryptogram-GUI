
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Observable;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class CryptogramGUIView extends Application implements java.util.Observer{
	private CryptogramController controller;
	private HashMap<Character, ArrayList<TextField>> Letter_textField_mapping = null;
	private ArrayList<Control> disable;
	
	/**
	 * Initialize the model, controller, Letter_textField_mapping, and disable
	 */
	public CryptogramGUIView(){
		super();
		initialization();	
	}
	
	public void initialization() {
		CryptogramModel model = new CryptogramModel();
		controller = new CryptogramController(model);
		Letter_textField_mapping = new HashMap<>();
		disable = new ArrayList<>();
		model.addObserver(this);
	}
	
	/**
	 * Override start and draw the board which is what we need.
	 */
	@Override
	public void start(Stage primaryStage) {
		primaryStage.setTitle("Cryptograms");
		BorderPane board = new BorderPane();
		Scene scene = new Scene(board, 900, 400);
		primaryStage.setScene(scene);
		drawGridPane(board);
		
		VBox right = new VBox();
		Button btn_new = new Button("New Puzzle");
		btn_new.setOnAction(actionEvent -> {
			initialization();
			start(primaryStage);
		});
		
		Button btn_hint = new Button("Hint");
		disable.add(btn_hint);
		btn_hint.setOnAction(actionEvent -> {
			controller.hint();
		});
		
		CheckBox freq = new CheckBox("Show Freq");
		disable.add(freq);
		GridPane freqGrid = new GridPane();
		
		// Start with the table hidden and the checkbox unchecked.
		freqGrid.setVisible(false);
		
		HashMap<Character, Integer> freqmap = controller.freq();
		VBox freqright = new VBox();
		VBox freqleft = new VBox();
		freqright.setPadding(new Insets(0,15,0,0));
		int count = 0;
		for(char letter : freqmap.keySet()) {
			Label item = new Label(letter + " " + freqmap.get(letter));
			if(count < 13) {
				freqright.getChildren().add(item);
			}else {
				freqleft.getChildren().add(item);
			}
			count += 1;
		}
		freqGrid.add(freqright, 0, 0);
		freqGrid.add(freqleft, 1, 0);
		
		
		freq.setOnAction(actionEvent -> {
			if(freq.selectedProperty().getValue()) {
				freqGrid.setVisible(true);
			}else {
				freqGrid.setVisible(false);
			}			
		});
		
		right.getChildren().addAll(btn_new, btn_hint, freq, freqGrid);
		board.setRight(right);
		primaryStage.show();
	}
	
	/**
	 * draw the main GridPane following the given format.
	 * 
	 * @param board
	 */
	public void drawGridPane(BorderPane board) {
		GridPane center = new GridPane();
        int row = 0;
        String encrypted_quotes = controller.get_encrypted_quotes();
        ArrayList<Character> guess_map = controller.make_guessmap();
        String temp_guess_str = "";
		for(int i = 0; i < guess_map.size(); i++) {
			temp_guess_str += guess_map.get(i);
		}
		String temp_encrypted_quotes = "";
		for(int i = 0; i < encrypted_quotes.length(); i++) {
			temp_encrypted_quotes += encrypted_quotes.substring(i, i+1);
		}
        while(temp_encrypted_quotes.length() > 30) {
			int check = 30;
			while(temp_encrypted_quotes.charAt(check) != ' ') {
				check -= 1;
			}
			for(int i = 0; i < check; i++) {
				char a = temp_encrypted_quotes.charAt(i);
        		TextField textField = new TextField();
        		textField.setPrefWidth(30);
        		if(!temp_guess_str.substring(i, i+1).equals(" ")) {
        			textField.appendText(temp_guess_str.substring(i, i+1));
        			textField.setDisable(true);
        			textField.setAlignment(Pos.CENTER);
        		}else if(temp_encrypted_quotes.charAt(i) == ' ') {
        			textField.setDisable(true);
        		}
            	String str = Character.toString(temp_encrypted_quotes.charAt(i));
    			Label label = new Label(str);
            	VBox vb = new VBox();
            	vb.setAlignment(Pos.CENTER);
           
            	textField.setOnKeyTyped((KeyEvent keyEvent) -> {
            		controller.replace(a, keyEvent.getCharacter().charAt(0));
            		keyEvent.consume();
            	});
            	disable.add(textField);
            	if (Letter_textField_mapping.get(a) != null) {
            		Letter_textField_mapping.get(a).add(textField);
				} else {
					ArrayList<TextField> list = new ArrayList<>();
					list.add(textField);
					Letter_textField_mapping.put(a, list);
				}
            	
                vb.getChildren().addAll(textField);
                vb.getChildren().addAll(label);
                center.add(vb, i, row);
			}
			temp_guess_str = temp_guess_str.substring(check + 1);
			temp_encrypted_quotes = temp_encrypted_quotes.substring(check + 1);	
			row += 1;
		}
        for(int i = 0; i < temp_encrypted_quotes.length(); i++) {
			char a = temp_encrypted_quotes.charAt(i);
    		TextField textField = new TextField();
    		disable.add(textField);
    		textField.setPrefWidth(30);
    		if(!temp_guess_str.substring(i, i+1).equals(" ")) {
    			textField.appendText(temp_guess_str.substring(i, i+1));
    			textField.setDisable(true);
    			textField.setAlignment(Pos.CENTER);
    		}else if(temp_encrypted_quotes.charAt(i) == ' ') {
    			textField.setDisable(true);
    		}
        	String str = Character.toString(temp_encrypted_quotes.charAt(i));
			Label label = new Label(str);
        	VBox vb = new VBox();
        	vb.setAlignment(Pos.CENTER);
       
        	textField.setOnKeyTyped((KeyEvent keyEvent) -> {
        		System.out.println(keyEvent.getCharacter().charAt(0));
        		controller.replace(a, keyEvent.getCharacter().charAt(0));
        		keyEvent.consume();
        	});
        	if (Letter_textField_mapping.get(a) != null) {
        		Letter_textField_mapping.get(a).add(textField);
			} else {
				ArrayList<TextField> list = new ArrayList<>();
				list.add(textField);
				Letter_textField_mapping.put(a, list);
			}
        	
            vb.getChildren().addAll(textField);
            vb.getChildren().addAll(label);
            center.add(vb, i, row);
		}		
		board.setCenter(center);
		
	}

	/**
	 * Automatically called by Observer and change the 
	 * view according to the information which was stored at model. 
	 * 
	 * And then, check whether the game is over.
	 */
	@Override
	public void update(Observable o, Object arg) {
		@SuppressWarnings("unchecked")
		ArrayList<Character> arglist = (ArrayList<Character>) arg;
		ArrayList<TextField> textlist = Letter_textField_mapping.get(arglist.get(0));
		for (TextField textField : textlist) {
			textField.setText("");
			textField.setText(arglist.get(1).toString().toUpperCase());
		}	
		
		if(controller.isOver()) {
			for(Control item : disable) {
				item.setDisable(true);
			}
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Message");
			alert.setHeaderText("Message");
			alert.setContentText("You won!");
			alert.showAndWait();
		}
	}
}
