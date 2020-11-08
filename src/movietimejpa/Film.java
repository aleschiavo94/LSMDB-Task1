package movietimejpa;


import java.util.HashSet;
import java.util.Set;

import javax.persistence.*;


@Entity
@Table(name = "film")
public class Film {
	
	@Id
	@Column(name = "id_film")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private final int idFilm;
	
	@Column(name = "title")
	private final String title;
	
	@Column(name = "genre")
	private final String genre;
	
	@Column(name = "plot")
	private final String plot;
	
	@Column(name = "pubblication_year")
	private final int release_year;
	
	@Column(name = "weekly_price")
	private final int weekly_price;
	
	@Column(name = "director")
	private final String director;
	
	
	@ManyToMany(fetch = FetchType.LAZY)
	private Set<Rental> rentalList;
	
	@OneToMany(mappedBy = "film", fetch = FetchType.EAGER)
	private Set<Rating> ratings;
	
	
	public Film() {
		this.idFilm=0;
		this.director="";
		this.genre="";
		this.title = "";
		this.release_year= 0;
		this.weekly_price = 0;
		this.plot = "";
	}
	
	public Film(int idFilm, String title, String genre, String plot, int releaseYear, int weeklyPrice, String director) {

		this.idFilm = idFilm;
        this.title = title;
		this.genre = genre;
		this.plot = plot;
		this.release_year = releaseYear;
		this.weekly_price = weeklyPrice;
		this.director = director;
	}
	
	public Film( String title, String genre, String plot, int releaseYear, int weeklyPrice, String director/*, List<Rental> rental, List<Rating> rating*/) {

		this.idFilm = 0;
        this.title = title;
		this.genre = genre;
		this.plot = plot;
		this.release_year = releaseYear;
		this.weekly_price = weeklyPrice;
		this.director = director;
	}
	
	public Film(int idFilm, String title, String genre, String plot, int releaseYear, int weeklyPrice, String director, Set<Rating> ratings) {

		this.idFilm = idFilm;
        this.title = title;
		this.genre = genre;
		this.plot = plot;
		this.release_year = releaseYear;
		this.weekly_price = weeklyPrice;
		this.director = director;
		this.ratings = new HashSet<Rating>(); 
		this.ratings.addAll(ratings);
	}

	public int getIdFilm(){
		return this.idFilm;
	}
	public String getTitle(){
		return this.title;
	}
	public String getGenre(){
		return this.genre;
	}
	public String getPlot(){
		return this.plot;
	}
	public int getReleaseYear(){
		return this.release_year;
	}
	public int getWeeklyPrice() {
		return this.weekly_price;
	}
	public String getDirector() {
		return this.director;
	}
	
	
	public Set<Rating> getRatingList(){
		return this.ratings;
	}
	
	public Set<Rental> getRentalList(){
		return this.rentalList;
	}
	

	public Film(Film f){
		this.idFilm = f.getIdFilm();
        this.title = f.getTitle();
		this.genre = f.getGenre();
		this.plot = f.getPlot();
		this.release_year = f.getReleaseYear();
		this.weekly_price = f.getWeeklyPrice();
		this.director = f.getDirector();
		
		this.ratings = new HashSet<Rating>();
		ratings.addAll(f.getRatingList());
	}

	
	
}
