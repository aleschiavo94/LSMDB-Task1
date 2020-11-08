package movietimejpa;

import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.iq80.leveldb.*;
import static org.iq80.leveldb.impl.Iq80DBFactory.*;
import org.iq80.leveldb.Options;
import org.iq80.leveldb.DBIterator;

public class LevelDB {
		  
	public static DB db;
	public static int r_count = 0;
	public static int f_count = 0;
	
	static JSONObject film = new JSONObject();
	static JSONObject films = new JSONObject();
	static JSONObject userRental = new JSONObject();
	static JSONObject rating = new JSONObject();
	static JSONObject ratings = new JSONObject();
	
	static LocalDate startDate = null;
	static LocalDate endDate = null;
	static int totalPrice = 0;
	static JSONObject listFilm = null;
	static User u = null;
	static int idRental = 0;
	
	static String title = null;
	static String genre = null;
	static String director = null;
	static String plot = null;
	static int releaseYear = 0;
	static int weeklyPrice = 0;
	static JSONObject listRating = null;
	static int idFilm = 0;
	
	

	//Function used to open Level DB
	public static void setupLevelDB() {
		Options options = new Options().createIfMissing(true);
		try {
			System.out.println("open LevelDB");
			db = factory.open(new File("levelDBStore"), options);
		} catch (Exception e) {
			System.out.println(e);
		}
	}
	
	public static void closeDb() throws IOException {
		db.close();
	}

	/*
	 * RENTAL FUNCTION
	 */
	
	//take the last key saved in LevelDB for Rental
	public static int getLastRentalKey() throws IOException {
		int counter = 0;
		
		DBIterator iterator = db.iterator();
		
		try {
			for (iterator.seekToFirst(); iterator.hasNext(); iterator.next()) {
				String key = asString(iterator.peekNext().getKey());
				String[] parts = key.split(":");
				if(parts[0].contentEquals("Rental")) {
					counter = Integer.parseInt(parts[1]);
				}
			}
		} finally {
			iterator.close();
		}
		return counter;
	}

	public static void insertRental(Rental r) throws IOException {
		List<Film> listFilm = new ArrayList<Film>();
		String i = "0";
		User u = r.getUser();
		userRental.put("idUser", u.getIdUser());
		userRental.put("name", u.getName());
		userRental.put("surname", u.getSurname());
		userRental.put("email", u.getEmail());
		userRental.put("username", u.getUsername());
		userRental.put("password", u.getPassword());
		userRental.put("credit", u.getCredit()-r.getTotalPrice());
		
		if(r_count == 0)
			r_count = getLastRentalKey();
		
		r_count += 1;
		
        listFilm.addAll(r.getFilmList());
        film = new JSONObject();
    	films = new JSONObject();
        
        for(Film f : listFilm) {
        	//create a json to save a rented movie
        	film.put("title", f.getTitle());
        	film.put("director", f.getDirector());
        	film.put("releaseYear", f.getReleaseYear());
        	film.put("genre", f.getGenre());
        	film.put("plot", f.getPlot());
        	film.put("weeklyPrice", f.getWeeklyPrice());
        	
        	//put each rented movie in another json
        	films.put(i, film);
        	
        	int index = Integer.parseInt(i);
        	index += 1;
        	i = String.valueOf(index);
        	
        	film = new JSONObject();
        }
                
        //write in LevelDB the new rental
		WriteBatch batch = db.createWriteBatch();
		try {
			batch.put(bytes("Rental:"+r_count+":"+u.getIdUser()+":startDate"), bytes(r.getStartDate().toString()));
			batch.put(bytes("Rental:"+r_count+":"+u.getIdUser()+":endDate"), bytes(r.getEndDate().toString()));
			batch.put(bytes("Rental:"+r_count+":"+u.getIdUser()+":tot"),bytes(String.valueOf(r.getTotalPrice())));
			batch.put(bytes("Rental:"+r_count+":"+u.getIdUser()+":listFilm"),bytes(films.toString()));
			batch.put(bytes("Rental:"+r_count+":"+u.getIdUser()+":user"),bytes(userRental.toString()));
			db.write(batch);
		} catch(Exception e){
			System.out.println(e);
		} finally {
		  batch.close();
		}
	}
	
	//function used to search User rental
	public static List<Rental> getRentalByUser(User user) throws IOException {
		List<Film> filmList = new ArrayList<>();
		List<Rental> rentalList = new ArrayList<>();
	
		int idUser = user.getIdUser();
		int index = 0;
				
		DBIterator iterator = db.iterator();
		try {
			for (iterator.seekToFirst(); iterator.hasNext(); iterator.next(), index++) {
				String key = asString(iterator.peekNext().getKey());
				String[] parts = key.split(":");
				if (parts[0].contentEquals("Rental") && Integer.parseInt(parts[2]) == idUser) {
					String value = asString(iterator.peekNext().getValue());
					switch(parts[3]) {
						case "startDate":
							startDate = LocalDate.parse(value);
							break;
						case "endDate":
							endDate = LocalDate.parse(value);
							break;
						case "tot":
							totalPrice = Integer.parseInt(value);
							break;
						case "listFilm":
							listFilm = new JSONObject(value);
							filmList.addAll(listFilm(listFilm));
							break;
						case "user":
							userRental = new JSONObject(value);
							u = new User(userRental.getInt("idUser"),
									userRental.getString("username"),
									userRental.getString("password"),
									userRental.getString("name"),
									userRental.getString("surname"),
									userRental.getString("email"),
									userRental.getInt("credit"));
							break;
					}
				}
				if(parts[0].contentEquals("Rental") && Integer.parseInt(parts[2]) == idUser && index == 4) {
					idRental = Integer.parseInt(parts[1]);
					index = -1;						
					rentalList.add(new Rental(idRental, user, filmList, startDate, endDate, totalPrice));
				}
				if(parts[0].contentEquals("Rental") && index == 4) {
					index = -1;
				}
				if(parts[0].contentEquals("Film") && index == 6) {
					index = -1;
				}
			}
		} finally {
			iterator.close();
		}
		return rentalList;
	}
	
	//function used to get all rental with list film
	public static List<Rental> getRentals() throws IOException {
		List<Film> filmList = new ArrayList<>();
		List<Rental> rentalList = new ArrayList<>();
		int index = 0;
		
		DBIterator iterator = db.iterator();
		try {
			
			for (iterator.seekToFirst(); iterator.hasNext(); iterator.next(), index++) {
				String key = asString(iterator.peekNext().getKey());
				String[] parts = key.split(":");
				if(parts[0].contentEquals("Rental")) {
					String value = asString(iterator.peekNext().getValue());
					switch(parts[3]) {
						case "startDate":
							startDate = LocalDate.parse(value);
							break;
						case "endDate":
							endDate = LocalDate.parse(value);
							break;
						case "tot":
							totalPrice = Integer.parseInt(value);
							break;
						case "listFilm":
							listFilm = new JSONObject(value);
							filmList.addAll(listFilm(listFilm));
							break;
						case "user":
							userRental = new JSONObject(value);
							u = new User(userRental.getInt("idUser"),
									userRental.getString("username"),
									userRental.getString("password"),
									userRental.getString("name"),
									userRental.getString("surname"),
									userRental.getString("email"),
									userRental.getInt("credit"));
							break;
					}
				}
				if(parts[0].contentEquals("Rental") && index == 4) {
					idRental = Integer.parseInt(parts[1]);
					index = -1;					
					rentalList.add(new Rental(idRental, u, filmList, startDate, endDate, totalPrice));
				}
				if (parts[0].contentEquals("Film")) {
					index = -1;				
				}
			}
		} finally {
			iterator.close();
		}
		return rentalList;
	}
	
	//function that return the movies of a specific rent
	public static List<Film> getRentSelected(Rental r) throws IOException {
		List<Film> filmList = new ArrayList<>();
		
		if(r != null) {			
			DBIterator iterator = db.iterator();
			try {
				for (iterator.seekToFirst(); iterator.hasNext(); iterator.next()) {
					String key = asString(iterator.peekNext().getKey());
					String[] parts = key.split(":");
					if (parts[0].contentEquals("Rental") && Integer.parseInt(parts[1]) == r.getIdRental()) {
						String value = asString(iterator.peekNext().getValue());
						if(parts[3].contentEquals("listFilm")) {
							listFilm = new JSONObject(value);
							filmList.addAll(listFilm(listFilm));
						}
					}
				}
			} finally {
				iterator.close();
			}
		}
		return filmList;
	}
	
	public static Set<Film> getFilmByRent(Rental r) throws IOException {
		Set<Film> filmList = new HashSet<>();
		
		if(r != null) {			
			DBIterator iterator = db.iterator();
			try {
				for (iterator.seekToFirst(); iterator.hasNext(); iterator.next()) {
					String key = asString(iterator.peekNext().getKey());
					String[] parts = key.split(":");
					if (parts[0].contentEquals("Rental") && Integer.parseInt(parts[1]) == r.getIdRental() && parts[3].contentEquals("listFilm")) {
						String value = asString(iterator.peekNext().getValue());
						System.out.println(key+" : "+value);
						listFilm = new JSONObject(value);
						Iterator<String> keys = listFilm.keys();

						while(keys.hasNext()) {
						    String key_value = keys.next();
						    if (listFilm.get(key_value) instanceof JSONObject) {
						    	filmList.add(new Film(((JSONObject) listFilm.get(key_value)).getString("title"),
						    				((JSONObject) listFilm.get(key_value)).getString("genre"),
						    				((JSONObject) listFilm.get(key_value)).getString("plot"),
											((JSONObject) listFilm.get(key_value)).getInt("releaseYear"),
											((JSONObject) listFilm.get(key_value)).getInt("weeklyPrice"),
											((JSONObject) listFilm.get(key_value)).getString("director")));
						    }
						}
					}
				}
			} finally {
				iterator.close();
			}
		}
		return filmList;
	}
	
	//utility function used to parse a json film list in List<Film>
	private static List<Film> listFilm(JSONObject listFilm) {
		List<Film> filmList = new ArrayList<>();
		
		Iterator<String> keys = listFilm.keys();

		while(keys.hasNext()) {
		    String key_value = keys.next();
		    if (listFilm.get(key_value) instanceof JSONObject) {
		    	filmList.add(new Film(((JSONObject) listFilm.get(key_value)).getString("title"),
		    				((JSONObject) listFilm.get(key_value)).getString("genre"),
		    				((JSONObject) listFilm.get(key_value)).getString("plot"),
							((JSONObject) listFilm.get(key_value)).getInt("releaseYear"),
							((JSONObject) listFilm.get(key_value)).getInt("weeklyPrice"),
							((JSONObject) listFilm.get(key_value)).getString("director")));
		    }
		}
		
		return filmList;
	}
	
	//function used to delete a rental
	public static void deleteRentalByUser(User u) throws IOException {
		int idUser = u.getIdUser();
		System.out.println(idUser);
		DBIterator iterator = db.iterator();
		
		try {
			
			for (iterator.seekToFirst(); iterator.hasNext(); iterator.next()) {
				String key = asString(iterator.peekNext().getKey());
				String[] parts = key.split(":");
				if(parts[0].contentEquals("Rental") && Integer.parseInt(parts[2]) == idUser) {
					System.out.println(key);
					db.delete(bytes(key));
				}
			}
		} finally {
			iterator.close();
		}
	}
	
	/*
	 * FILM FUNCTION
	 */
	
	//take the last key saved in LevelDB for Rental
	public static int getLastFilmKey() throws IOException {
		int counter = 0;
		
		DBIterator iterator = db.iterator();
		
		try {
			for (iterator.seekToFirst(); iterator.hasNext(); iterator.next()) {
				String key = asString(iterator.peekNext().getKey());
				String[] parts = key.split(":");
				if(parts[0].contentEquals("Film")) {
					counter = Integer.parseInt(parts[1]);
				}
			}
		} finally {
			iterator.close();
		}
		return counter;
	}
	
	public static void insertFilm(Film f) throws IOException {
		rating = new JSONObject();
		ratings = new JSONObject();
		List<Rating> ratingList = new ArrayList<Rating>();
		String i = "0";
		
		f_count = f.getIdFilm();
		if(f_count == 0) {
			f_count = getLastFilmKey();
			f_count++;
		}
		
		if(!ratingList.isEmpty()) {
			ratingList.addAll(f.getRatingList());
			for(Rating r : ratingList) {
				User u = r.getUser();
				userRental.put("idUser", u.getIdUser());
				userRental.put("name", u.getName());
				userRental.put("surname", u.getSurname());
				userRental.put("email", u.getEmail());
				userRental.put("username", u.getUsername());
				userRental.put("password", u.getPassword());
				userRental.put("credit", u.getCredit());
	        	//create a json to save a rating
	        	rating.put("insertDate", r.getDate());
	        	rating.put("vote", r.getVote());
	        	rating.put("user", userRental);
	        	//put each rating in another json
	        	ratings.put(i, rating);
	        	
	        	int index = Integer.parseInt(i);
	        	index += 1;
	        	i = String.valueOf(index);
	        	
	        	rating = new JSONObject();
	        }
		}	
		
        //write in LevelDB the new rental
		WriteBatch batch = db.createWriteBatch();
		try {
			batch.put(bytes("Film:"+f_count+":title"), bytes(f.getTitle()));
			batch.put(bytes("Film:"+f_count+":genre"), bytes(f.getGenre()));
			batch.put(bytes("Film:"+f_count+":director"),bytes(f.getDirector()));
			batch.put(bytes("Film:"+f_count+":plot"),bytes(f.getPlot()));
			batch.put(bytes("Film:"+f_count+":releaseYear"),bytes(String.valueOf(f.getReleaseYear())));
			batch.put(bytes("Film:"+f_count+":weeklyPrice"),bytes(String.valueOf(f.getWeeklyPrice())));
			batch.put(bytes("Film:"+f_count+":ratingList"),bytes(ratings.toString()));
			db.write(batch);
		} catch(Exception e){
			System.out.println(e);
		} finally {
		  batch.close();
		}
	}
	
	//function used to get all rental with list film
	public static List<Film> getFilm() throws IOException {
		List<Film> filmList = new ArrayList<>();
		Set<Rating> ratingList = new HashSet<Rating>();
		int index = 0;
		
		DBIterator iterator = db.iterator();
		try {
			for (iterator.seekToFirst(); iterator.hasNext(); iterator.next(), index++) {
				String key = asString(iterator.peekNext().getKey());
				String[] parts = key.split(":");
				if(parts[0].contentEquals("Film")) {
					String value = asString(iterator.peekNext().getValue());
					switch(parts[2]) {
						case "title":
							title = value;
							break;
						case "genre":
							genre = value;
							break;
						case "director":
							director = value;
							break;
						case "plot":
							plot = value;
							break;
						case "releaseYear":
							releaseYear = Integer.parseInt(value);
							break;
						case "weeklyPrice":
							weeklyPrice = Integer.parseInt(value);
							break;
						case "ratingList":
							listRating = new JSONObject(value);
							Iterator<String> keys = listRating.keys();
							while(keys.hasNext()) {
							    String key_value = keys.next();
							    if (listRating.get(key_value) instanceof JSONObject) {
							    	u = new User(((JSONObject) ((JSONObject) listRating.get(key_value)).get("user")).getInt("idUser"),
							    			((JSONObject) ((JSONObject) listRating.get(key_value)).get("user")).getString("username"),
											((JSONObject) ((JSONObject) listRating.get(key_value)).get("user")).getString("password"),
											((JSONObject) ((JSONObject) listRating.get(key_value)).get("user")).getString("name"),
											((JSONObject) ((JSONObject) listRating.get(key_value)).get("user")).getString("surname"),
											((JSONObject) ((JSONObject) listRating.get(key_value)).get("user")).getString("email"),
											((JSONObject) ((JSONObject) listRating.get(key_value)).get("user")).getInt("credit"));
							    		
							    	LocalDate insertDate = LocalDate.parse(((JSONObject) listRating.get(key_value)).getString("insertDate"));
							    	int vote = ((JSONObject) listRating.get(key_value)).getInt("vote");
							    	ratingList.add(new Rating(u, insertDate, vote));
							    }
							}
							break;
					}
				}
				if(parts[0].contentEquals("Film") && index == 6) {
					idFilm = Integer.parseInt(parts[1]);
					Film f = new Film(idFilm, title, genre, plot, releaseYear, weeklyPrice, director, ratingList);
					filmList.add(f);
					index = -1;	
				}
				if(parts[0].contentEquals("Rental")) {
					index = -1;				
				}
			}
		} finally {
			iterator.close();
		}
		return filmList;
	}
	
	//function to refresh the rating list of a film
	public static Film refreshFilm(Film f) throws IOException {
		Film film = null;
		Set<Rating> ratingList = new HashSet<Rating>();
		int index = 0;
		
		DBIterator iterator = db.iterator();
		try {
			
			for (iterator.seekToFirst(); iterator.hasNext(); iterator.next(), index++) {
				String key = asString(iterator.peekNext().getKey());
				String[] parts = key.split(":");
				if(parts[0].contentEquals("Film") && Integer.parseInt(parts[1]) == f.getIdFilm() && parts[2].contentEquals("ratingList")) {
					String value = asString(iterator.peekNext().getValue());
					listRating = new JSONObject(value);
					Iterator<String> keys = listRating.keys();
					while(keys.hasNext()) {
					    String key_value = keys.next();
					    if (listRating.get(key_value) instanceof JSONObject) {
					    	u = new User(((JSONObject) ((JSONObject) listRating.get(key_value)).get("user")).getInt("idUser"),
					    			((JSONObject) ((JSONObject) listRating.get(key_value)).get("user")).getString("username"),
									((JSONObject) ((JSONObject) listRating.get(key_value)).get("user")).getString("password"),
									((JSONObject) ((JSONObject) listRating.get(key_value)).get("user")).getString("name"),
									((JSONObject) ((JSONObject) listRating.get(key_value)).get("user")).getString("surname"),
									((JSONObject) ((JSONObject) listRating.get(key_value)).get("user")).getString("email"),
									((JSONObject) ((JSONObject) listRating.get(key_value)).get("user")).getInt("credit"));
					    		
					    	LocalDate insertDate = LocalDate.parse(((JSONObject) listRating.get(key_value)).getString("insertDate"));
					    	int vote = ((JSONObject) listRating.get(key_value)).getInt("vote");
					    	ratingList.add(new Rating(u, insertDate, vote));						    	
						}
					    film = new Film(f.getIdFilm(), f.getTitle(), f.getGenre(), f.getPlot(), f.getReleaseYear(), f.getWeeklyPrice(), f.getDirector(), ratingList);
					    break;
					}
				}
				else {
					index = -1;				
				}
			}
		} finally {
			iterator.close();
		}
		return film;
	}
	
	public static List<Rating> getRatingByFilm(Film f) throws IOException {
		List<Rating> ratingList = new ArrayList<>();
		DBIterator iterator = db.iterator();
		try {
			
			for (iterator.seekToFirst(); iterator.hasNext(); iterator.next()) {
				String key = asString(iterator.peekNext().getKey());
				String[] parts = key.split(":");
				if(parts[0].contentEquals("Film") && Integer.parseInt(parts[1]) == f.getIdFilm() && parts[2].contentEquals("ratingList")) {
					String value = asString(iterator.peekNext().getValue());
					listRating = new JSONObject(value);
					Iterator<String> keys = listRating.keys();
					while(keys.hasNext()) {
					    String key_value = keys.next();
					    if (listRating.get(key_value) instanceof JSONObject) {
					    	u = new User(((JSONObject) ((JSONObject) listRating.get(key_value)).get("user")).getInt("idUser"),
					    			((JSONObject) ((JSONObject) listRating.get(key_value)).get("user")).getString("username"),
									((JSONObject) ((JSONObject) listRating.get(key_value)).get("user")).getString("password"),
									((JSONObject) ((JSONObject) listRating.get(key_value)).get("user")).getString("name"),
									((JSONObject) ((JSONObject) listRating.get(key_value)).get("user")).getString("surname"),
									((JSONObject) ((JSONObject) listRating.get(key_value)).get("user")).getString("email"),
									((JSONObject) ((JSONObject) listRating.get(key_value)).get("user")).getInt("credit"));
					    		
					    	LocalDate insertDate = LocalDate.parse(((JSONObject) listRating.get(key_value)).getString("insertDate"));
					    	int vote = ((JSONObject) listRating.get(key_value)).getInt("vote");
					    	ratingList.add(new Rating(u, f, insertDate, vote));
					    }
					}
				}
			}
		} finally {
			iterator.close();
		}
		return ratingList;
	}
	
	public static int getAverageRatingByFilm(Film f) throws IOException {
		int sum = 0;
		int avg = 0;
		int index = 0;
		DBIterator iterator = db.iterator();
		try {
			
			for (iterator.seekToFirst(); iterator.hasNext(); iterator.next()) {
				String key = asString(iterator.peekNext().getKey());
				String[] parts = key.split(":");
				String value = asString(iterator.peekNext().getValue());
				if(parts[0].contentEquals("Film") && Integer.parseInt(parts[1]) == f.getIdFilm() && parts[2].contentEquals("ratingList")) {
					listRating = new JSONObject(value);
					if(listRating.isEmpty()) {
						avg = 0;
					}
					else {
						Iterator<String> keys = listRating.keys();
						while(keys.hasNext()) {
							index++;
						    String key_value = keys.next();
						    if (listRating.get(key_value) instanceof JSONObject) {						    	
						    	sum = sum + ((JSONObject) listRating.get(key_value)).getInt("vote");
						    }
						}
						avg = sum/index;
					}
				}
			}
		} finally {
			iterator.close();
		}
		return avg;
	}
	
	public static void insertRating(Rating r, Film f) throws IOException {
		String key_value = null;
		rating = new JSONObject();
		DBIterator iterator = db.iterator();
		try {
			for (iterator.seekToFirst(); iterator.hasNext(); iterator.next()) {
				String key = asString(iterator.peekNext().getKey());
				String[] parts = key.split(":");
				if(parts[0].contentEquals("Film") && Integer.parseInt(parts[1]) == f.getIdFilm() && parts[2].contentEquals("ratingList")) {
					String value = asString(iterator.peekNext().getValue());
					listRating = new JSONObject(value);
					if(listRating.isEmpty()) {
						key_value = "0";
					}
					else {
						Iterator<String> keys = listRating.keys();
						while(keys.hasNext()) {
							key_value = keys.next();
						}
						
						int index = Integer.parseInt(key_value);
						index += 1;
			        	key_value = String.valueOf(index);
					}
					
					User u = r.getUser();
					userRental.put("idUser", u.getIdUser());
					userRental.put("name", u.getName());
					userRental.put("surname", u.getSurname());
					userRental.put("email", u.getEmail());
					userRental.put("username", u.getUsername());
					userRental.put("password", u.getPassword());
					userRental.put("credit", u.getCredit());
		        	//create a json to save a rating
		        	rating.put("insertDate", r.getDate());
		        	rating.put("vote", r.getVote());
		        	rating.put("user", userRental);
		        	//put each rating in another json
		        	listRating.put(key_value, rating);
				}
			}
		} finally {
			iterator.close();
		}
		db.put(bytes("Film:"+f.getIdFilm()+":ratingList"),bytes(listRating.toString()));
	}
	
	public static void deleteRatingByUser(User u) throws IOException {
		int idUser = u.getIdUser();
		int index = 0;
		int idFilm = 0;
		ratings = new JSONObject();
		DBIterator iterator = db.iterator();

		try {
			
			for (iterator.seekToFirst(); iterator.hasNext(); iterator.next()) {
				String key = asString(iterator.peekNext().getKey());
				String[] parts = key.split(":");
				if(parts[0].contentEquals("Film") && parts[2].contentEquals("ratingList")) {
					String value = asString(iterator.peekNext().getValue());
					idFilm = Integer.parseInt(parts[1]);
					listRating = new JSONObject(value);
					if(listRating.isEmpty()) {}
					else {
						Iterator<String> keys = listRating.keys();
						while(keys.hasNext()) {
							String key_value = keys.next();
						    if (listRating.get(key_value) instanceof JSONObject) {
						    	if(((JSONObject) ((JSONObject) listRating.get(key_value)).get("user")).getInt("idUser") != idUser) {
						    		ratings.put(String.valueOf(index), listRating.get(key_value));
						    		index++;
						    	}
						    }
						}
						db.put(bytes("Film:"+idFilm+":ratingList"), bytes(ratings.toString()));
						ratings = new JSONObject();
					}
				}
			}
		} finally {
			iterator.close();
		}
	}

}
