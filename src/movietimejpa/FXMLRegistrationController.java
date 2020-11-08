package movietimejpa;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class FXMLRegistrationController implements Initializable{
	//objects for registration
    @FXML private Button submit_button;
    @FXML private TextField name_field;
    @FXML private TextField surname_field;
    @FXML private TextField email_field;
    @FXML private PasswordField pwd_field;
    @FXML private TextField user_field;
    @FXML private TextField credit_field;
    
    @FXML
    //getting user's credentials
    public void getCredentials(){
    	//getting informations from the fields
        String name_string = name_field.getText();
        String surname_string = surname_field.getText();
        String email_string = email_field.getText();
        String pwd_string = pwd_field.getText();
        String user_string = user_field.getText();
        String inserted_credit = credit_field.getText();
        
        Integer credit_int = 0;
        
        //verifying the format of credit field
        if(inserted_credit.matches("\\d*")) {
        	credit_int = Integer.parseInt(inserted_credit);
		}else {
			credit_field.clear();
        	credit_field.setPromptText("Insert a number");
        	credit_field.setStyle("-fx-prompt-text-fill: red;");
        	return;
		}

        //verifying full fields
        if(name_string.length() == 0 || surname_string.length() == 0 || email_string.length() == 0 || pwd_string.length() == 0 || user_string.length() == 0 || credit_int < 0 ) {
        	user_field.clear();
        	user_field.setPromptText("Compilare tutti i campi");
        	user_field.setStyle("-fx-prompt-text-fill: red;");
        	return;
            
        }
        
        pwd_string = HashClass.convertToSha(pwd_string);
        
        //creating a new user with the new informations
        User u = new User(user_string, pwd_string, name_string, surname_string, email_string, credit_int);
               
        //verifying that the new user is not already registered
        boolean result;
        result = UserEntityManager.findIfNotExist(u);
        if(result == false) {
        	user_field.clear();
        	email_field.clear();
        	email_field.setPromptText("Account già in uso");
        	user_field.setPromptText("Account già in uso");
        	user_field.setStyle("-fx-prompt-text-fill: red;");
        	email_field.setStyle("-fx-prompt-text-fill: red;");
        	return;
        }
        
      
        UserEntityManager.insertUser(u);
        
        
    	//closing the window
    	Stage stage = (Stage) submit_button.getScene().getWindow();
    	stage.close();
    }
    
    
	public void initialize(URL url, ResourceBundle rb) {
		name_field.setFocusTraversable(false);
	    surname_field.setFocusTraversable(false);
	    email_field.setFocusTraversable(false);
	    pwd_field.setFocusTraversable(false);
	    user_field.setFocusTraversable(false);
	    credit_field.setFocusTraversable(false);
    }
	
}
