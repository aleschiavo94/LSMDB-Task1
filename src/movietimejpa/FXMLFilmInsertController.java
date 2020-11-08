package movietimejpa;

import java.io.IOException;
import java.util.List;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class FXMLFilmInsertController {
	@FXML private TextField inserted_title;
	@FXML private TextField inserted_genre;
	@FXML private TextField inserted_pub_year;
	@FXML private TextField inserted_weekly_price;
	@FXML private TextField inserted_director;
	@FXML private TextArea inserted_plot;
	
	private List<Film> film_list;
	
	public void insertNewFilm() {
		
		String title = this.inserted_title.getText();
		String genre = this.inserted_genre.getText();
		String director = this.inserted_director.getText();
		String plot = this.inserted_plot.getText();
		String release_year = this.inserted_pub_year.getText();
		String weekly_price = this.inserted_weekly_price.getText();
		
		int year =0;
		int price = 0;
		
		if(release_year.matches("\\d*")) {
        	year = Integer.parseInt(release_year);
		}else {
			inserted_pub_year.clear();
			inserted_pub_year.setPromptText("Insert a number");
			inserted_pub_year.setStyle("-fx-prompt-text-fill: red;");
        	return;
		}
		
		if(weekly_price.matches("\\d*")) {
        	price = Integer.parseInt(weekly_price);
		}else {
			inserted_weekly_price.clear();
			inserted_weekly_price.setPromptText("Insert a number");
			inserted_weekly_price.setStyle("-fx-prompt-text-fill: red;");
        	return;
		}
		
		if(genre.length() == 0 || title.length() == 0 || director.length() == 0 || plot.length() == 0) {
			inserted_title.clear();
			inserted_title.setPromptText("Fill in all the fields");
			inserted_title.setStyle("-fx-prompt-text-fill: red;");
        	return;
		}
		
		Film f = new Film(title, genre, plot, year, price, director);
		
		try {
			System.out.println("insertFilm leveldb");
			LevelDB.insertFilm(f);
		} catch (IOException e) {
			System.out.println("exception insertFilm leveldb");
			System.out.println(e);
		}
		UserEntityManager.insertFilm(f);
		
		film_list.clear();
		try {
			System.out.println("getFilm leveldb");
			film_list.addAll(LevelDB.getFilm());
		} catch (Exception e) {
			System.out.println("exception getFilm leveldb");
			System.out.println(e);
			film_list.addAll(UserEntityManager.getFilms(0));
		}
		
    	Stage stage = (Stage) inserted_title.getScene().getWindow();
    	stage.close();
		
	}
	
	public void initList(List<Film> list) {
		this.film_list = list;
		
		inserted_title.setFocusTraversable(false);
		inserted_genre.setFocusTraversable(false);
		inserted_pub_year.setFocusTraversable(false);
		inserted_weekly_price.setFocusTraversable(false);
		inserted_director.setFocusTraversable(false);
		inserted_plot.setFocusTraversable(false);
	}
	
}
