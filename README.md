# Movie Time
The project consists in an online movie rental application. A certain user who 
wants to rent a movie, first must sign up to the system submitting his info into 
the registration form. Once registered, the user can sign in using his credentials
and visualize the list of available movies, among these he can pick the ones he is 
interested in and put them in the cart for rental. A customer can even check
for the availability of a specific movie searching it through the system by title.
Each rented film remains available to the user for a week, at the end of this
period the film is automatically returned. A film has a certain cost per week
which is detracted from the customer's credit when the movie is rented: if the
user credit isn't enough the system denies the operation, thus the user needs to
top up his credit providing his credit card first. Moreover, the system offers the
rating feature to its users: a film can be voted with a score varying from 1 to 5,
each of these votes equally contributes to the average score of the film which is
automatically calculated by the application.

## Brief descripion of the application
The project consists in an online movie rental application.

### Ordinary User
A certain user who wants to rent a movie, first must sign up to the system
submitting his info into the registration form. Once registered, the user can sign
in using his credentials and visualize the list of available movies, among these
he can pick the ones he is interested in and put them in the cart for rental.
A customer can even check for the availability of a specific movie searching it
through the system by title.
Each rented film remains available to the user for a week, at the end of this
period the film is automatically returned. A film has a certain cost per week
which is detracted from the customer's credit when the movie is rented: if the
user credit isn't enough the system denies the operation, thus the user needs to
top up his credit providing his credit card first.
Moreover, the system offers the rating feature to its users: a film can be
voted with a score varying from 1 to 5, each of these votes equally contributes
to the average score of the film which is automatically calculated by the appli-
cation.

### Administrator user
The movie rental system is managed by an administrator user. The admin has
the possibility to perform the necessary operations in order to manage the film
database. In order to access to the system, the admin must provide its privileged
credentials.
In particular, the administrator can visualize all the available movies and
for each of these he can visualize the details. Furthermore, he can search for a
certain movie by title. The admin can even add new movies to the database.
As far as the rentals handling is concerned, the administrator can visualize
the rentals history checking for all rentals occurred in the past. Moreover,
the system provides the admin with the possibility to visualize all the rentals
occurred within a certain time interval: he can specify a starting date and an
ending date. The system computes and displays the number of rentals occurred
in the requested period, and the total expenditure for these.
Regarding the user management, the system provides the administrator with
the features which follow. The system admin can visualize the list of all the user
currently registered to the application, on top of this, he can search for a specific
user by username. Once the admin has found a user, he can visualize his account
details and eventually delete the user.

## Use case
![Use case image](https://github.com/elenaveltroni/Task1/blob/master/UseCase.png?raw=true)

## Software architecture
The application data is organized in a MySQL relational database.
The application interacting with the database is implemented in JAVA language
and it exploits the Hibernate implementation of the Java Persistence API for
handling relational data.
The use of the key-value database supports the SQL database in order to speed up 
and minimize the use of the most expensive queries made on the SQL database.
![Software architecture image](https://github.com/elenaveltroni/Task1/blob/master/ArchitectureSchema.png?raw=true)
