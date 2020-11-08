package movietimejpa;

import java.time.LocalDate;


import javax.persistence.*;

@Entity
@Table(name = "rating")
public class Rating {
	
    @Id
    @Column(name="id_rating")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private final int idRating;
		
    @Column(name="insert_date")	
	private final LocalDate insertDate;
    
    @Column(name="vote")	
	private final int vote;
		
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_film")
    private Film film;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_user")
    private User user;
    
    
		public Rating(int idR, User u, Film f, LocalDate insDate, int vote) {
			this.idRating = idR;
			this.user = new User(u);
			this.film = new Film(f);
			this.insertDate = insDate;
			this.vote = vote;
		}
	
		public Rating(User u, Film f, LocalDate insDate, int vote) {	
			this.idRating = 0; // null value, used when inserting a new vote
			this.user = new User(u);
			this.film = new Film(f);
			this.insertDate = insDate;
			this.vote = vote;
		}
		
		public Rating(User u, LocalDate insDate, int vote) {	
			this.idRating = 0; // null value, used when inserting a new vote
			this.user = new User(u);
			this.film = null;
			this.insertDate = insDate;
			this.vote = vote;
		}

		public int getIdRating() {
			return this.idRating;
		}
		public LocalDate getDate() {
			return this.insertDate;
		}
		public Film getFilm() {
			return this.film;
		}
		public User getUser() {
			return this.user;
		}
		public int getVote() {
			return this.vote;
		}
		
		public Rating() {
			this.idRating = 0;
			this.insertDate = null;
			this.vote = 0;
		}
		
		public void setUser(User u) {
			this.user = u;
		}
		public void setFilm(Film f) {
			this.film = f;
		}
		
}
