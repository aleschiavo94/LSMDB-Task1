package movietimejpa;

import javax.persistence.*;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Utente
 */

@Entity
@Table(name = "rental")
public class Rental {
	
    @Id
    @Column(name="id_rental")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private final int idRental;
   // private final SimpleIntegerProperty idUser;
   // private  List<Film> listFilm;
    @Column(name="start_date")
    private final LocalDate startDate;
    @Column(name="end_date")
    private final LocalDate endDate;
    @Column(name="total_price")
    private final int totalPrice;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_user")
    private User user;
    
    @ManyToMany(fetch = FetchType.EAGER)
    private Set<Film> listFilm;
    
    public Rental( int idN, User u, List<Film> list, LocalDate stDate, LocalDate eDate, int totp){
    	
        this.idRental = idN ;
        this.user = new User(u);
        //this.listFilm = new ArrayList<>();
        listFilm = new HashSet<Film>();
        this.listFilm.addAll(list);
       
        this.startDate = (stDate);
        this.endDate = (eDate);
        this.totalPrice = (totp); 
        
    }
    
    public Rental() {
    	this.idRental = 0;
    	this.startDate = LocalDate.now();
    	this.endDate = LocalDate.now();
    	this.totalPrice = 0;
    	
    }
    
    public Rental(User u, List<Film> list, LocalDate stDate, LocalDate eDate, int totp) {
    	this.idRental = (0);
    	this.user = new User(u);
    	listFilm = new HashSet<Film>(list);
       
        this.startDate = (stDate);
        this.endDate = (eDate);
        this.totalPrice = (totp);
        
        
    }
    
    public Rental(Rental n){
    	
        this.idRental = (n.getIdRental());
        this.user = n.getUser();
       // this.listFilm = new ArrayList<>();
        this.listFilm = new HashSet<Film>();
        this.listFilm.addAll(n.getFilmList());
        this.startDate = (n.getStartDate());
        this.endDate = (n.getEndDate());
        this.totalPrice = (n.getTotalPrice());
        
    }
    

    public int getIdRental(){
        return this.idRental;
    }
    
    
    public Set<Film> getFilmList(){
    	
        return this.listFilm;
    }
    public LocalDate getStartDate(){
        return this.startDate;
    }
    public LocalDate getEndDate(){
        return this.endDate;
    }
    public int getTotalPrice(){
        return this.totalPrice;
    }
    
    
    public User getUser() {
    	return this.user;
    }
    
    public void setUser(User u) {
		this.user = u;
	}
    
}
