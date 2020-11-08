package movietimejpa;
import javax.persistence.*;

import java.util.HashSet;
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
@Table(name = "user")
public class User {
	
	@Id
    @Column(name="id_user")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    private final int idUser;
    @Column(name="username")
	private final String username;
    @Column(name="password")
    private String password;
    @Column(name="name")
    private String name;
    @Column(name="surname")
    private String surname;
    @Column(name="email")
    private String email;
    @Column(name="credit")
    private int credit;
    
    
    @OneToMany(mappedBy = "user",  fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
    private Set<Rental> rentals;
    
    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
    private Set<Rating> ratings;
    
    public User(int idUser, String username, String psw, String name, String surn, String email, int credit) {
    	
    	this.idUser = idUser;
    	this.username = username;
    	this.password = psw;
    	this.name = name;
    	this.surname = surn;
    	this.email = email;
    	this.credit = credit;
    }
    
    
    public User() {
    	this.idUser = 0;
    	this.username = "";
    }
    
    //used to gather data when user want to register
    public User(String username, String psw, String name, String surn, String email, int credit) {
    	
    	this.idUser = 0; // dummy value, not used
    	this.username = username;
    	this.password = psw;
    	this.name = name;
    	this.surname = surn;
    	this.email = email;
    	this.credit = credit;
    	
    }
  
    public User(User u) {
    	this.idUser = u.getIdUser();
    	this.username = (u.getUsername());
    	this.password = (u.getPassword());
    	this.name = (u.getName());
    	this.surname = (u.getSurname());
    	this.email = (u.getEmail());
    	this.credit = (u.getCredit());
    	this.rentals = new HashSet<Rental>();
    	rentals = u.getRentals();
    }
    
    public int getIdUser(){
        return this.idUser;
    }
    
    public String getUsername(){
        return this.username;
    }
    public String getPassword(){
        return this.password;
    }
    
    public String getName() {
    	return this.name;
    }
    public String getSurname() {
    	return this.surname;
    }
    public String getEmail() {
    	return this.email;
    }
    public int getCredit() {
    	return this.credit;
    }
    public void setPassword(String psw){
        this.password = psw;
    }
    public void setEmail(String email) {
    	this.email = (email);
    }
    public void setCredit(int credit) {
    	this.credit= (credit);
    }
    
    
    public Set<Rental> getRentals(){
    	return this.rentals;
    }
    
}
