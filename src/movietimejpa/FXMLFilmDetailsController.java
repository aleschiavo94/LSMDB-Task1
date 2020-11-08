package movietimejpa;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class FXMLFilmDetailsController implements Initializable{
	//objects for retrieving logged user and searched film informations
	@FXML private Film current_film;
	@FXML private User current_user;
	
	private List<Film> list;
		
	//objects for visualizing film's informations
	@FXML private Label title_label;
	@FXML private Label genre_label;
	@FXML private Label year_label;
	@FXML private Label director_label;
	@FXML private TextArea plot_area;
	@FXML private Label rating_label;
	@FXML private TextField vote_field;
	@FXML private Label vote_label;
	
	//objects for rating the film
	@FXML private ToggleGroup group;
	@FXML private RadioButton radiobutton1;
	@FXML private RadioButton radiobutton2;
	@FXML private RadioButton radiobutton3;
	@FXML private RadioButton radiobutton4;
	@FXML private RadioButton radiobutton5;
	@FXML private Button submit_button;
	
	@FXML private Label list_label;
	@FXML private ListView<String> rate_list;
		  private ObservableList<String> vote;
	

	//inizializing with the current film's informations
	public void setInfo(Film f, List<Film> list, User u) {
		
		this.current_user = new User(u); 
		
		this.current_film = new Film(f.getIdFilm(), f.getTitle(), f.getGenre(), f.getPlot(), f.getReleaseYear(), f.getWeeklyPrice(), f.getDirector());
		this.list = list;
		
		
		title_label.setText(f.getTitle());
		genre_label.setText(f.getGenre());
		year_label.setText(Integer.toString(f.getReleaseYear()));
		director_label.setText(f.getDirector());
		plot_area.setText(f.getPlot());
		
		group = new ToggleGroup();
		
		//getting the mean rate for the current film
		Integer rating_value;
		try {
			System.out.println("getAverageRate leveldb");
			rating_value = LevelDB.getAverageRatingByFilm(f);
		} catch (IOException e1) {
			System.out.println("exception getAverageRate leveldb");
			System.out.println(e1);
			rating_value = getRating(f);
		}
		rating_label.setText(rating_value.toString());
		
		//searching for all the rating for the current film
		Rating r;
		List<Rating> rating_film = new ArrayList<>();
		try {
			System.out.println("getRatingByFilm leveldb");
			rating_film.addAll(LevelDB.getRatingByFilm(f));
		} catch (IOException e) {
			System.out.println("exception getRatingByFilm leveldb");
			System.out.println(e);
			rating_film.addAll(f.getRatingList());
		}
		
		if(!current_user.getUsername().equals("admin")) {
			rate_list.setVisible(false);
			list_label.setVisible(false);
			
			radiobutton1.setToggleGroup(group);
			radiobutton2.setToggleGroup(group);
			radiobutton3.setToggleGroup(group);
			radiobutton4.setToggleGroup(group);
			radiobutton5.setToggleGroup(group);
			
			if(rating_film.size() > 0) { //if the current film has at least one rate
			
				//getting all the rates for the current film
				for (Iterator<Rating> it = rating_film.iterator(); it.hasNext(); ) {
					r = it.next();
					//if the current user has already rated the current film, the button is disabled
					if(r.getUser().getUsername().equals(current_user.getUsername())) {
						submit_button.setDisable(true);
					}
				}
			}
		}else {
			radiobutton1.setVisible(false);
			radiobutton2.setVisible(false);
			radiobutton3.setVisible(false);
			radiobutton4.setVisible(false);
			radiobutton5.setVisible(false);
			
			submit_button.setVisible(false);
			
			vote_label.setVisible(false);
			
			rate_list.setVisible(true);
			list_label.setVisible(true);
			
			ObservableList<String> vote = FXCollections.observableArrayList();
			List<Rating> rating_list = new ArrayList<Rating>();
			try {
				System.out.println("getRatingByFilm leveldb");
				rating_list.addAll(LevelDB.getRatingByFilm(f));
			} catch (IOException e) {
				System.out.println("exception getRatingByFilm leveldb");
				System.out.println(e);
				rating_list.addAll(f.getRatingList());
			}
			for (Iterator<Rating> it = rating_list.iterator(); it.hasNext(); ) {
	        	Rating rate = it.next();
	        	
	        	String s = rate.getUser().getUsername()+", "+rate.getVote();
	        	vote.add(s);
			}
			rate_list.setItems(vote);
		}
	}
	
	
	//getting the rate from the radiobutton
	public void getRadioButtonVote() {
		Rating r;
		
		//getting the value from the selected radiobutton 
		RadioButton selected = (RadioButton)group.getSelectedToggle();
		Integer value = Integer.parseInt(selected.getText());
		
		//adding the new rate		
		r = new Rating(current_user, LocalDate.now(), value);
	
		try {
			System.out.println("Insert Rating levelDB");
			LevelDB.insertRating(r, current_film);
		} catch(Exception e){
			System.out.println("exception Insert Rating levelDB");
			System.out.println(e);
		} finally{
			UserEntityManager.insertRating(r);
		}
		
		//updating film's informations
		try {
			System.out.println("Refresh Film levelDB");
			current_film = LevelDB.refreshFilm(current_film);
		} catch (Exception e1) {
			System.out.println("exception refresh Film levelDB");
			System.out.println(e1);
			current_film = UserEntityManager.refreshFilm(current_film);
		}
		
		//calculating the new mean rate and changing the value in the label
		Integer mean = 0;
		try {
			System.out.println("Get Average Rating levelDB");
			mean = LevelDB.getAverageRatingByFilm(current_film);
		} catch (IOException e2) {
			System.out.println("exception get average rating levelDB");
			System.out.println(e2);
			mean = getRating(current_film);
		}
		rating_label.setText(mean.toString());
		
		//disabling the button to not rate again
		submit_button.setDisable(true);
		list.clear();
		try {
			System.out.println("get film levelDB");
			list.addAll(LevelDB.getFilm());
		} catch (Exception e3) {
			System.out.println("exception get film levelDB");
			System.out.println(e3);
			list.addAll(UserEntityManager.getFilms(0));
		}
	}
	
	public void initialize(URL url, ResourceBundle rb) {
		
    }
	
	//calculating and retrieving the rating given a film
	public Integer getRating(Film f) {
		Integer avg=0; //mean
		Integer sum=0; //sum of rates
		Integer n=0; //number of rates
	   
		try {
			for (Iterator<Rating> it = f.getRatingList().iterator(); it.hasNext(); ) {
	        	Rating r = it.next();
	            sum+=r.getVote();
	            n+=1;
	        }
			if(n !=0) {
				avg=sum/n;
			}else {
				avg=0;
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			System.out.println("A problem occurred in retrieving the rate!");
		} 
	   return avg;
	}
	
}
