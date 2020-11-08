package movietimejpa;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.persistence.*;


public class UserEntityManager {
	
	private static EntityManagerFactory factory;
	private static EntityManager entityManager;
	
   // public static void setup() {
    static {	
    	factory = Persistence.createEntityManagerFactory("movie_rental2");
   }

   public static void exit() {
   		factory.close();
   }
   
   //finding a user given the email
   public static boolean findIfNotExist(User u) {
       try {
    	   entityManager = factory.createEntityManager();
	        
	        TypedQuery<User> typedQuery = entityManager.createQuery("Select u from User u Where u.email = :email", User.class);
	        typedQuery.setParameter("email", u.getEmail());
	        List<User> listuser = typedQuery.getResultList();
	        if(!listuser.isEmpty()) {
	        	System.out.println("already present");
	        	return false;
	        }
	        
		} catch (Exception ex) {
			System.out.println("A problem occurred in finding the user!");
			ex.printStackTrace();
			

		} finally {
			entityManager.close();
		}       
       return true;
    
   }
   
   
   //inserting the new user
   public static void insertUser(User u) {
	   try {
		   entityManager = factory.createEntityManager();
		   
	        entityManager.getTransaction().begin();
	       
	        entityManager.persist(u);
	        entityManager.getTransaction().commit();
	        
	    } catch (Exception ex) {
			ex.printStackTrace();
			System.out.println("A problem occurred in inserting the user!");

		} finally {
			entityManager.close();
		}       
   }
   
   //retrieving a user given username and password
   public static User login(String username, String password) {
	   User user = null;
   	   
	   try {
    	   entityManager = factory.createEntityManager();
	       
	        TypedQuery<User> typedQuery = entityManager.createQuery("Select u from User u Where u.username = :username AND u.password = :password" , User.class);
	        typedQuery.setParameter("username", username);
	        typedQuery.setParameter("password", password);
	        user = typedQuery.getSingleResult();
		} catch (Exception ex) {
			ex.printStackTrace();
			System.out.println("A problem occurred in retrieving the user!");

		} finally {
			entityManager.close();
		}       
	   
	   return user;
   }
   
   //retrieving a list of all films in the database 
   public static List<Film> getFilms(int value){
	   List<Film> filmList = new ArrayList<>();
	  
	   try {
    	   entityManager = factory.createEntityManager();
	        
	        TypedQuery<Film> typedQuery = entityManager.createQuery("Select f from Film f", Film.class);
	       
	        filmList = typedQuery.getResultList();
	        
	        if(value == 1) {
		        for(Film f : filmList) {
		        	LevelDB.insertFilm(f);
		        }
	        }
	       
		} catch (Exception ex) {
			ex.printStackTrace();
			System.out.println("A problem occurred in retrieving the films!");

		} finally {
			entityManager.close();
		}       
	   return filmList;
   }
   
   
   //retrieving a list of all users in the database 
   public static List<User> getUsers(){
	   List<User> userList = new ArrayList<>();
	  
	   try {
    	   entityManager = factory.createEntityManager();
	        
	        TypedQuery<User> typedQuery = entityManager.createQuery("Select u from User u where u.username <> 'admin'", User.class);
	       
	        userList = typedQuery.getResultList();
	        
	       
		} catch (Exception ex) {
			ex.printStackTrace();
			System.out.println("A problem occurred in retrieving the users!");

		} finally {
			entityManager.close();
		}       
	   return userList;
   }
   
   
   //inserting a new rental
   public static void insertRental(Rental r) {
	   try {
		   entityManager = factory.createEntityManager();
		  
	        entityManager.getTransaction().begin();
	       
	        entityManager.persist(r);
	        entityManager.getTransaction().commit();
	        
	       
		} catch (Exception ex) {
			ex.printStackTrace();
			System.out.println("A problem occurred in inserting the new rent!");

		} finally {
			entityManager.close();
		}       
   }
   
   //retrieving a list of rentals given the user
   public static List<Rental> searchRentalByUser(User u){
	   List<Rental> list = new ArrayList<>();
	   try {
    	   entityManager = factory.createEntityManager();
	       
	        TypedQuery<Rental> typedQuery = entityManager.createQuery("Select r from Rental r Where r.user = :user " , Rental.class);
	        typedQuery.setParameter("user", u);
	        
	        list = typedQuery.getResultList();
	        
	        
		} catch (Exception ex) {
			ex.printStackTrace();
			System.out.println("A problem occurred in retrieving the rentals!");

		} finally {
			entityManager.close();
		}       
	   return list;
   }
   
   //updating user's informations
   public static void updateUserInfo(User u) {
	   try {
		   entityManager = factory.createEntityManager();
		  
	        entityManager.getTransaction().begin();
	       
	        entityManager.merge(u);
	        entityManager.getTransaction().commit();
	        
	       
		} catch (Exception ex) {
			ex.printStackTrace();
			System.out.println("A problem occurred in updating user info!");

		} finally {
			entityManager.close();
		}       
   }
   
   //removing the given user and all his rentals
   public static void removeUser(User u, List<Film> films) {
	   
	   try {
		   entityManager = factory.createEntityManager();
		  
	       entityManager.getTransaction().begin();
	       
	       for (Iterator<Rental> it = u.getRentals().iterator(); it.hasNext(); ) {
	        	Rental r = it.next();
	            entityManager.remove(entityManager.contains(r) ? r : entityManager.merge(r));
	        }
	       
	        for(Film f : films) {
		        for (Iterator<Rating> it = f.getRatingList().iterator(); it.hasNext(); ) {
		        	
		            Rating r = it.next();
		            if(r.getUser().equals(u)) {
		            	r.setUser(null);
		            	r.setFilm(null);
		            	entityManager.remove(entityManager.contains(r) ? r : entityManager.merge(r));
		            }
		        }
	       }
	 	   
	 	   	
	       entityManager.remove(entityManager.contains(u) ? u : entityManager.merge(u));
	       entityManager.getTransaction().commit();
	       
	       
		} catch (Exception ex) {
			ex.printStackTrace();
			System.out.println("A problem occurred in removing a user!");

		} finally {
			entityManager.close();
		} 
	   
   }
   
   //retrieving a list of all users in the database 
   public static List<Film> getUsersFilms(User u){
	   List<Film> filmList = new ArrayList<>();
	  
	   try {
    	   entityManager = factory.createEntityManager();
	        
	        TypedQuery<Film> typedQuery = entityManager.createQuery("Select r.film from Rating r where r.user == ':user'", Film.class);
	        typedQuery.setParameter("user", u);
	        filmList = typedQuery.getResultList();
	        
	       
		} catch (Exception ex) {
			ex.printStackTrace();
			System.out.println("A problem occurred in retrieving the films!");

		} finally {
			entityManager.close();
		}       
	   return filmList;
   }
   
   //inserting a new rating
   public static void insertRating(Rating r) {
	   try {
		   entityManager = factory.createEntityManager();
		  
	       entityManager.getTransaction().begin();
	       
	       entityManager.persist(r);
	       entityManager.getTransaction().commit();
	       
		} catch (Exception ex) {
			ex.printStackTrace();
			System.out.println("A problem occurred in inserting the new rate!");

		} finally {
			entityManager.close();
		}       
   }
   
   //retrieving the updated film
   public static Film refreshFilm(Film f) {
	   Film film = null;
	   try {
		   entityManager = factory.createEntityManager();
		 
	       film = entityManager.find(Film.class, f.getIdFilm());
  
		} catch (Exception ex) {
			ex.printStackTrace();
			System.out.println("A problem occurred in updating a film!");

		} finally {
			entityManager.close();
		} 
	   	return film;
   }
   
   public static User refreshUser(User u) {
	   User user = null;
	   try {
		   entityManager = factory.createEntityManager();
		  
	       user = entityManager.find(User.class, u.getIdUser());

	       
		} catch (Exception ex) {
			ex.printStackTrace();
			System.out.println("A problem occurred in updating a film!");

		} finally {
			entityManager.close();
		} 
	   	return user;
   }
   
   public static void insertFilm(Film f) {
	   try {
		   entityManager = factory.createEntityManager();
		  
	       entityManager.getTransaction().begin();
	       
	       entityManager.persist(f);
	       entityManager.getTransaction().commit();
	       
		} catch (Exception ex) {
			ex.printStackTrace();
			System.out.println("A problem occurred in inserting the new film!");

		} finally {
			entityManager.close();
		}   
   }
   
   public static List<Rental> getRentals(){
	   List<Rental> rentalList = new ArrayList<>();
	  
	   try {
    	   entityManager = factory.createEntityManager();
	        
	        TypedQuery<Rental> typedQuery = entityManager.createQuery("Select r from Rental r", Rental.class);
	       
	        rentalList = typedQuery.getResultList();
	        
	       
		} catch (Exception ex) {
			ex.printStackTrace();
			System.out.println("A problem occurred in retrieving the rentals!");

		} finally {
			entityManager.close();
		}       
	   return rentalList;
   }   

}
