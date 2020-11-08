/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package movietimejpa;

import java.io.IOException;


import java.net.URL;

import java.time.LocalDate;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import movietimejpa.User;

/**
 *
 * @author Federica
 */
public class FXMLDocumentController implements Initializable {
	//textfield for login
    @FXML private TextField username_field;
    @FXML private PasswordField password_field;
    
    @FXML private User user;
    
    @FXML
    //login function which controls 
    public void loginUser(MouseEvent event) throws IOException { 
    	//getting informations from username and password fields
        String username_string = username_field.getText(); 
        String password_string = password_field.getText();
        
        password_string = HashClass.convertToSha(password_string);
        
        //verifying user's informations
        user =  UserEntityManager.login(username_string, password_string);      
       
        if(user != null) {//if the user is registrated
        	//creating a new window 
        	LevelDB.setupLevelDB();
            Stage dialogStage = new Stage();
            Scene scene;
            Node source = (Node) event.getSource();
                dialogStage = (Stage) source.getScene().getWindow();
                dialogStage.close();
                String resource;
                Parent root;
            if(user.getUsername().equals("admin")) {
            	resource = "FXMLAdmin.fxml";
            	FXMLLoader loader = new FXMLLoader();
                loader.setLocation(getClass().getResource(resource));
                //passing user's informations to the new controller FXMLUserController
                root = (Parent) loader.load();
               		
               FXMLAdminController controller = loader.getController();
               controller.initUser(user);
            }else {
            	resource = "FXMLUser.fxml";
            	FXMLLoader loader = new FXMLLoader();
                loader.setLocation(getClass().getResource(resource));
                
                //passing user's informations to the new controller FXMLUserController
                root = (Parent) loader.load();
                		
                FXMLUserController controller = loader.getController();
                controller.initUser(user);
            }
            
            
            scene = new Scene(root);
            dialogStage.setTitle(user.getUsername());
            dialogStage.setScene(scene);
            dialogStage.show(); 
         }else { //if the user is not registrated visualize message error
            username_field.clear();
            password_field.clear();
            username_field.setStyle("-fx-prompt-text-fill: red;");
            password_field.setStyle("-fx-prompt-text-fill: red;");
            username_field.setPromptText("Error");
            password_field.setPromptText("Error");
         }
    }
    
   

   
    
    //performing the signup
    public void signin(){
    	//opening a new window
    	Parent root;
        try {
        	String resource = "FXMLRegistration.fxml";
        	FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource(resource));
            root = (Parent) loader.load();
            
            Stage secondStage = new Stage();
            secondStage.setTitle("Registration");
            secondStage.setScene(new Scene(root));
            secondStage.initModality(Modality.WINDOW_MODAL);
            
            FXMLRegistrationController controller = loader.getController();
            
            secondStage.show();
        }catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    
    public void initialize(URL url, ResourceBundle rb) {
    	
    }   
    
}
