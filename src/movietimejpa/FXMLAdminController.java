package movietimejpa;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.DatePicker;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class FXMLAdminController implements Initializable{
			private User current_user;
	//objects for visualizing all the film in the table
		@FXML private TableView<Film> film_table;
		@FXML private TableColumn<Film, String> title_column;
		@FXML private TableColumn<Film, String> genre_column;
		@FXML private TableColumn<Film, Integer> price_column;
			  private ObservableList<Film> film_in_db;
			  
		//objects for visualizing all the rentals
		@FXML private TableView<Rental> rental_table;
		@FXML private TableColumn<Rental, Integer> rental_price_column;
		@FXML private TableColumn<Rental, LocalDate> rental_startDate_column;
		@FXML private TableColumn<Rental, LocalDate> rental_endDate_column;
			  private ObservableList<Rental> rentals;	
			  
		//objects for visualizing the top3 movies
		@FXML private Label top_label;
		@FXML private ListView<String> top_list;
		@FXML private ObservableList<String> list_in_db;
		
		//objects for visualizing the rentals
		@FXML private TextField search_usern_rental;
		@FXML private DatePicker search_start_period;
		@FXML private DatePicker search_end_period;
		
		@FXML private Label count_text_label;
		@FXML private Label count_value_label;
		@FXML private Label total_price_text_label;
		@FXML private Label total_price_count_label;
		
		//textfield for live search
		@FXML private TextField search_field;
		@FXML private TextField search_user_field;
		
		//object for visualizing all the user
		@FXML private User selected_user;
		@FXML private TableView<User> user_table;
		@FXML private TableColumn<User, String> user_column;
			private ObservableList<User> user_in_db;
			
		//label for user's informations
		@FXML private Label name_label;
		@FXML private Label surname_label;
		@FXML private Label email_label;
		@FXML private Label credit_label;
		@FXML private Label name_field;
		@FXML private Label surname_field;
		@FXML private Label email_field;
		@FXML private Label credit_field;
		
		
	//live search for films by title
	public void setFilmSearched() {
			
		String s = search_field.getText();
		if(s.length() > 0 )
			getFilmSearched(s);
		else
			film_table.setItems(film_in_db);
	}
	
	//live search for users by username
	public void setUserSearched() {
		
		String s = search_user_field.getText();
		if(s.length() > 0 )
			getUserSearched(s);
		else
			user_table.setItems(user_in_db);
	}
	
	//loading current user's informations
	public void initUser(User u) {
		this.current_user = new User(u);
	}
	
	//searching for films beginning with the string got from the field
	public void getFilmSearched(String s) {
		ObservableList<Film> list = FXCollections.observableArrayList();
		s = s.toLowerCase();
		String tmp;
		for(Film f : this.film_in_db) {
			tmp = f.getTitle().toLowerCase();
			if(tmp.startsWith(s))
				list.add(f);
			}
		film_table.setItems(list);
	}
	
	//searching for users beginning with the string got from the field
	public void getUserSearched(String s) {
		ObservableList<User> list = FXCollections.observableArrayList();
		s = s.toLowerCase();
		String tmp;
		for(User u : this.user_in_db) {
			tmp = u.getUsername().toLowerCase();
			if(tmp.startsWith(s))
				list.add(u);
			}
		user_table.setItems(list);
	}
	
	
	
	//showing film's details
	public void showDetails(MouseEvent event) throws IOException{
		Film film = film_table.getSelectionModel().getSelectedItem();
	    if (event.getClickCount() == 2 && film != null){
	        getFilmDetails(film);
	    }
	}
		
	//getting the details for the selected film
	public void getFilmDetails( Film f )throws IOException {
		//opening a new window with a new controller
        Stage dialogStage = new Stage();
        Scene scene;
        
        String resource = "FXMLFilmDetails.fxml";
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource(resource));
        
        Parent root = (Parent) loader.load();
        		
        FXMLFilmDetailsController controller = loader.getController();
        
        controller.setInfo(f, film_in_db, current_user);
        
       
        scene = new Scene(root);
        dialogStage.setTitle(f.getTitle());
        dialogStage.setScene(scene);
        dialogStage.show(); 
	}
	
	public void openFilmForm() throws IOException{
		
        try {
        	 Stage dialogStage = new Stage();
             Scene scene;
             
             String resource = "FXMLFilmInsert.fxml";
             FXMLLoader loader = new FXMLLoader();
             loader.setLocation(getClass().getResource(resource));
             
             Parent root = (Parent) loader.load();
             		
             FXMLFilmInsertController controller = loader.getController();
             
            controller.initList(film_in_db);
             
            
             scene = new Scene(root);
             dialogStage.setTitle("Insert new Film");
             dialogStage.setScene(scene);
             dialogStage.show(); 
        }catch (IOException e) {
            e.printStackTrace();
        }
	}
		
	public void refreshFilm() {
		film_in_db.clear();
		try {
			System.out.println("getFilm levelDB");
			film_in_db.addAll(LevelDB.getFilm());
		} catch (Exception e) {
			System.out.println("exception getFilm levelDB");
			film_in_db.addAll(UserEntityManager.getFilms(0));
		}
	}
		
	public void initUser() {
		user_in_db.clear();
		user_in_db.addAll(UserEntityManager.getUsers());
		user_table.setItems(user_in_db);
		
		search_user_field.clear();
		
		name_label.setVisible(false);
		surname_label.setVisible(false);
		email_label.setVisible(false);
		credit_label.setVisible(false);
		
		name_field.setVisible(false);
		surname_field.setVisible(false);
		email_field.setVisible(false);
		credit_field.setVisible(false);
	}
	
	public void initRentals() {
		this.rentals.clear();
		this.search_start_period.getEditor().clear();
		this.search_end_period.getEditor().clear();	
		
		try {
			System.out.println("getRental leveldb");
			this.rentals.addAll(LevelDB.getRentals());
		}
		catch(Exception e){
			System.out.println("exception getRental leveldb");
			System.out.println(e);
			this.rentals.addAll(UserEntityManager.getRentals());
		}
		
		this.rental_table.setItems(rentals);
		this.count_value_label.setText(Integer.toString(rentals.size()));
		int sum = 0;
		for(int i = 0; i < rentals.size(); i++) {
			sum += rentals.get(i).getTotalPrice();
		}
		this.total_price_count_label.setText(Integer.toString(sum));
		this.search_usern_rental.clear();
		
		search_usern_rental.setFocusTraversable(false);
		
		top_label.setVisible(false);
		top_list.setVisible(false);
	}
	
	//showing user's informations
	public void showInformations(MouseEvent event) throws IOException{
		selected_user = user_table.getSelectionModel().getSelectedItem();
		if (event.getClickCount() == 2 && selected_user != null){
			name_label.setVisible(true);
			surname_label.setVisible(true);
			email_label.setVisible(true);
			credit_label.setVisible(true);
			
			name_field.setVisible(true);
			surname_field.setVisible(true);
			email_field.setVisible(true);
			credit_field.setVisible(true);
			
			name_field.setText(selected_user.getName());
			surname_field.setText(selected_user.getSurname());
			email_field.setText(selected_user.getEmail());
			Integer i = selected_user.getCredit();
			credit_field.setText(i.toString());
	    }
	}
	
	public void deleteAccount() throws IOException {
		if(selected_user == null) {
			Alert windowAlert = new Alert(AlertType.INFORMATION);
			windowAlert.setHeaderText("None selected user!");
			windowAlert.setContentText("You have to select a user to delete the account");
			windowAlert.setTitle("Warning");
			windowAlert.showAndWait();
		}else {
			//deleting the account and its rentals and ratings
			UserEntityManager.removeUser(selected_user, film_in_db);
			LevelDB.deleteRatingByUser(selected_user);
			LevelDB.deleteRentalByUser(selected_user);
			
			//updating users list
			user_in_db.clear();
			user_in_db.addAll(UserEntityManager.getUsers());
			user_table.setItems(user_in_db);
			
			closeInformations();
		}
		
	}
		
	
	public void closeInformations() {
		name_label.setVisible(false);
		surname_label.setVisible(false);
		email_label.setVisible(false);
		credit_label.setVisible(false);
		
		name_field.setVisible(false);
		surname_field.setVisible(false);
		email_field.setVisible(false);
		credit_field.setVisible(false);
	}
	

	
	public void initialize(URL url, ResourceBundle rb) {
		//initializing of the table 
		title_column.setCellValueFactory(new PropertyValueFactory<Film, String>("title"));
		genre_column.setCellValueFactory(new PropertyValueFactory<Film, String>("genre"));
		price_column.setCellValueFactory(new PropertyValueFactory<Film, Integer>("weeklyPrice"));
				
		//retrieving all the films from the database and putting them into the table
		film_in_db = FXCollections.observableArrayList();
		try {
			System.out.println("getFilm leveldb");
			film_in_db.addAll(LevelDB.getFilm());
			if(film_in_db.isEmpty()) {
				UserEntityManager.getFilms(1);
				System.out.println("(empty) getFilm leveldb");
				film_in_db.addAll(LevelDB.getFilm());
			}
		} catch(Exception e) {
			System.out.println("exception getFilm leveldb");
			System.out.println(e);
			film_in_db.addAll(UserEntityManager.getFilms(0));
		}
		film_table.setItems(film_in_db);
		
		//initializing the rent table
		rental_startDate_column.setCellValueFactory(new PropertyValueFactory<Rental, LocalDate>("startDate"));
		rental_endDate_column.setCellValueFactory(new PropertyValueFactory<Rental, LocalDate>("endDate"));
		rental_price_column.setCellValueFactory(new PropertyValueFactory<Rental, Integer>("totalPrice"));
		rentals = FXCollections.observableArrayList();
		
		//initializing the user table
		user_column.setCellValueFactory(new PropertyValueFactory<User, String>("username"));
		user_in_db = FXCollections.observableArrayList();
		
    } 
	
	
	public void searchRentals() {
		String username_searched = this.search_usern_rental.getText();
		LocalDate start_period = this.search_start_period.getValue();
		LocalDate end_period = this.search_end_period.getValue();
		
		ObservableList<Rental> list = FXCollections.observableArrayList();
		
		if(start_period == null && end_period == null  && username_searched.length() == 0 ) {
			initRentals();
			return;
		}
			
		if((start_period == null && end_period != null) || (start_period != null && end_period == null)) {
			Alert windowAlert = new Alert(AlertType.INFORMATION);
			windowAlert.setHeaderText("End Date missing!");
			windowAlert.setContentText("Insert both start date and end date");
			windowAlert.setTitle("Warning");
			windowAlert.showAndWait();
			return;
		}
		

		int sum = 0;
		int count = 0;
		if(username_searched.length() > 0) {
			if(start_period == null && end_period == null) {
				for(Rental r : this.rentals) {
					if(r.getUser().getUsername().equals(username_searched)) {
						list.add(r);
						sum += r.getTotalPrice();
						count +=1;
					}
					
				}
			}else {
				for(Rental r : this.rentals) {
					if(r.getUser().getUsername().equals(username_searched) && r.getStartDate().isAfter(start_period) && r.getEndDate().isBefore(end_period)) {
						list.add(r);
						sum += r.getTotalPrice();
						count +=1;
					}
				}
			}
		}else {
			for(Rental r : this.rentals) {
				if(r.getStartDate().isAfter(start_period) && r.getEndDate().isBefore(end_period)) {
					list.add(r);
					sum += r.getTotalPrice();
					count +=1;
				}
			}
			//show the top3 movies bought in that period
			top_label.setVisible(true);
			top_list.setVisible(true);
			
			list_in_db = FXCollections.observableArrayList();
			list_in_db.clear();
			list_in_db.addAll(getTop3Movies(list));
			top_list.setItems(list_in_db);
		}
		this.count_value_label.setText(Integer.toString(count));
		this.total_price_count_label.setText(Integer.toString(sum));
		this.rental_table.setItems(list);
		
	}
	
	public ObservableList<String> getTop3Movies(ObservableList<Rental> list){
		ObservableList<Film> lista_film = FXCollections.observableArrayList();
		ObservableList<String> lista_finale = FXCollections.observableArrayList();
		
		//inserting all the films in one list
		for(Rental r: list) {
			Set<Film> set = null;
			try {
				System.out.println("getRentSelected leveldb");
				set = LevelDB.getFilmByRent(r);
			} catch (Exception e) {
				System.out.println("exception getRentSelected leveldb");
				System.out.println(e);
				set = r.getFilmList();
			}
			for(Film film_set: set) {
				lista_film.add(film_set);
			}
			
		}
		
		//counting the occurrences
		ArrayList<String> arr = new ArrayList();
		for(Film f: lista_film) {
			arr.add(f.getTitle());
		}
		lista_finale = countFrequencies(arr);
		
		return lista_finale;
	}
	
	
	public static ObservableList<String> countFrequencies(ArrayList<String> list) { 
		ObservableList<String> top_lista = FXCollections.observableArrayList();
        // hash set is created and elements of 
        // arraylist are inserted into it 
        Set<String> st = new HashSet<String>(list); 
        int lenght = 0;
        ArrayList a = new ArrayList();
        for (String s : st) {
        	String string = Collections.frequency(list, s)+": "+s;
            a.add(string); 
        }
        
        //sorting in descending order
        Collections.sort(a, Collections.reverseOrder());
        if(a.size()==0) {
        	Alert windowAlert = new Alert(AlertType.INFORMATION);
			windowAlert.setHeaderText("No rentals found in this period!");
			windowAlert.setTitle("Warning");
			windowAlert.showAndWait();
        	return top_lista;
        }
        if(a.size() < 3) {
        	lenght = a.size();
        }
        else lenght = 3;
        
        for(int i=0; i < lenght; i++) {
        	top_lista.add(i, a.get(i).toString());
        }
        
        return top_lista;
    } 
	
	public void exit(MouseEvent event) throws IOException {
		Stage dialogStage = new Stage();
        Scene scene;
        Node source = (Node) event.getSource();
		 dialogStage = (Stage) source.getScene().getWindow();
         dialogStage.close();
         String resource;
         Parent root;
         
         resource = "FXMLDocument.fxml";
     	FXMLLoader loader = new FXMLLoader();
         loader.setLocation(getClass().getResource(resource));
       //passing user's informations to the new controller FXMLUserController
         root = (Parent) loader.load();
        		
        FXMLDocumentController controller = loader.getController();
        
        scene = new Scene(root);
        dialogStage.setTitle("MovieTime");
        dialogStage.setScene(scene);
        dialogStage.show(); 
	}

}
