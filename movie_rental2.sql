-- MySQL dump 10.13  Distrib 8.0.18, for Win64 (x86_64)
--
-- Host: 127.0.0.1    Database: movie_rental2
-- ------------------------------------------------------
-- Server version	8.0.18

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `film`
--

DROP TABLE IF EXISTS `film`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `film` (
  `id_film` int(11) NOT NULL AUTO_INCREMENT,
  `director` varchar(255) DEFAULT NULL,
  `genre` varchar(255) DEFAULT NULL,
  `plot` varchar(255) DEFAULT NULL,
  `pubblication_year` int(11) DEFAULT NULL,
  `title` varchar(255) DEFAULT NULL,
  `weekly_price` int(11) DEFAULT NULL,
  PRIMARY KEY (`id_film`)
) ENGINE=InnoDB AUTO_INCREMENT=35 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `film`
--

LOCK TABLES `film` WRITE;
/*!40000 ALTER TABLE `film` DISABLE KEYS */;
INSERT INTO `film` VALUES (1,'Anthony e Joe Russo','Action','Friction arises between superheroes when one group supports the government\'s decision to implement a law to control their powers while the other opposes it',2016,'Captain America: Civil War',4),(2,'Todd Phillips','Thriller','Forever alone in a crowd, failed comedian Arthur Fleck seeks connection as he walks the streets of Gotham City. Arthur wears two masks -- the one he paints for his day job as a clown.',2019,'Joker',7),(3,'Quentin Tarantino','Drama','Actor Rick Dalton gained fame and fortune by starring in a 1950s television Western, but is now struggling to find meaningful work in a Hollywood that he doesn\'t recognize anymore.',2019,'Once Upon a Time In Hollywood',7),(4,'Andrés Muschietti','Horror','Seven helpless and bullied children are forced to face their worst nightmares when Pennywise, a shape-shifting clown, reappears. The clown lives in the sewers and targets small innocent children',2017,'It',5),(5,'Quentin Tarantino','Drama','In the realm of underworld, a series of incidents intertwines the lives of two Los Angeles mobsters, a gangster\'s wife, a boxer and two small-time criminals.',1994,'Pulp Fiction',2),(6,'James Cameron','Drama','Seventeen-year-old Rose hails from an aristocratic family and is set to be married. When she boards the Titanic, she meets Jack Dawson, an artist, and falls in love with him.',1998,'Titanic',2),(7,'Stanley Kubrick','Horror','Jack and his family move into an isolated hotel with a violent past. Living in isolation, Jack begins to lose his sanity, which affects his family members',1980,'The Shining',2),(8,'Chris Columbus','Fantasy','Harry Potter, an eleven-year-old orphan, discovers that he is a wizard and is invited to study at Hogwarts. Even as he escapes a dreary life and enters a world of magic, he finds trouble awaiting him',2001,'Harry Potter and the Philosopher\'s Stone',2),(9,'Tim Burton','Fantasy','Edward, a synthetic man with scissor hands, is taken in by Peg, a kindly Avon lady, after the passing of his inventor. Things take a turn for the worse when he is blamed for a crime he did not commit',1991,'Edward Scissorhands',2),(10,'Bryan Singer','Drama','Freddie Mercury -- the lead singer of Queen -- defies stereotypes and convention to become one of history\'s most beloved entertainers. The band\'s revolutionary sound and popular songs lead to Queen\'s meteoric rise in the 1970s.',2018,'Bohemian Rhapsody',6),(11,'Damien Chazelle','Romance','Sebastian and Mia are drawn together by their common desire to do what they love. But as success mounts they are faced with decisions that begin to fray the fragile fabric of their love affair.',2016,'La La Land',5),(12,'James Gunn','Fantasy','After an outrageous mission, Star-Lord and his team of galactic defenders meet Ego, a man claiming to be his father. However, they set out to investigate the secret of his parentage',2017,'Guardians of the Galaxy Vol. 2',5);
/*!40000 ALTER TABLE `film` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `film_rental`
--

DROP TABLE IF EXISTS `film_rental`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `film_rental` (
  `Film_id_film` int(11) NOT NULL,
  `rentalList_id_rental` int(11) NOT NULL,
  PRIMARY KEY (`Film_id_film`,`rentalList_id_rental`),
  KEY `FKonrmyxwd74rracbd0yndp9hom` (`rentalList_id_rental`),
  CONSTRAINT `FKf3ro9xwbliomatd3gba0n3i8j` FOREIGN KEY (`Film_id_film`) REFERENCES `film` (`id_film`),
  CONSTRAINT `FKonrmyxwd74rracbd0yndp9hom` FOREIGN KEY (`rentalList_id_rental`) REFERENCES `rental` (`id_rental`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `film_rental`
--

LOCK TABLES `film_rental` WRITE;
/*!40000 ALTER TABLE `film_rental` DISABLE KEYS */;
/*!40000 ALTER TABLE `film_rental` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `hibernate_sequence`
--

DROP TABLE IF EXISTS `hibernate_sequence`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `hibernate_sequence` (
  `next_val` bigint(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `hibernate_sequence`
--

LOCK TABLES `hibernate_sequence` WRITE;
/*!40000 ALTER TABLE `hibernate_sequence` DISABLE KEYS */;
INSERT INTO `hibernate_sequence` VALUES (1),(1);
/*!40000 ALTER TABLE `hibernate_sequence` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `rating`
--

DROP TABLE IF EXISTS `rating`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `rating` (
  `id_rating` int(11) NOT NULL AUTO_INCREMENT,
  `insert_date` date DEFAULT NULL,
  `vote` int(11) DEFAULT NULL,
  `id_film` int(11) DEFAULT NULL,
  `id_user` int(11) DEFAULT NULL,
  PRIMARY KEY (`id_rating`),
  KEY `FKtlbswr0ymoq0ufccm0yyl0a8v` (`id_film`),
  KEY `FK19w7ioj0t787iykijq6yb6gx0` (`id_user`),
  CONSTRAINT `FK19w7ioj0t787iykijq6yb6gx0` FOREIGN KEY (`id_user`) REFERENCES `user` (`id_user`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `FKtlbswr0ymoq0ufccm0yyl0a8v` FOREIGN KEY (`id_film`) REFERENCES `film` (`id_film`)
) ENGINE=InnoDB AUTO_INCREMENT=47 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `rating`
--

LOCK TABLES `rating` WRITE;
/*!40000 ALTER TABLE `rating` DISABLE KEYS */;
/*!40000 ALTER TABLE `rating` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `rental`
--

DROP TABLE IF EXISTS `rental`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `rental` (
  `id_rental` int(11) NOT NULL AUTO_INCREMENT,
  `end_date` date DEFAULT NULL,
  `start_date` date DEFAULT NULL,
  `total_price` int(11) DEFAULT NULL,
  `id_user` int(11) DEFAULT NULL,
  PRIMARY KEY (`id_rental`),
  KEY `FKgk5kyawvswecuob5svobmyfey` (`id_user`),
  CONSTRAINT `FKgk5kyawvswecuob5svobmyfey` FOREIGN KEY (`id_user`) REFERENCES `user` (`id_user`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=22 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `rental`
--

LOCK TABLES `rental` WRITE;
/*!40000 ALTER TABLE `rental` DISABLE KEYS */;
/*!40000 ALTER TABLE `rental` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `rental_film`
--

DROP TABLE IF EXISTS `rental_film`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `rental_film` (
  `Rental_id_rental` int(11) NOT NULL,
  `listFilm_id_film` int(11) NOT NULL,
  PRIMARY KEY (`Rental_id_rental`,`listFilm_id_film`),
  KEY `FK7af485547j4lbslq6wyctcuh4` (`listFilm_id_film`),
  CONSTRAINT `FK7af485547j4lbslq6wyctcuh4` FOREIGN KEY (`listFilm_id_film`) REFERENCES `film` (`id_film`),
  CONSTRAINT `FKe8b86nacbfdky2q652jpdyy11` FOREIGN KEY (`Rental_id_rental`) REFERENCES `rental` (`id_rental`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `rental_film`
--

LOCK TABLES `rental_film` WRITE;
/*!40000 ALTER TABLE `rental_film` DISABLE KEYS */;
/*!40000 ALTER TABLE `rental_film` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user` (
  `id_user` int(11) NOT NULL AUTO_INCREMENT,
  `credit` int(11) DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  `surname` varchar(255) DEFAULT NULL,
  `username` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id_user`)
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user`
--

LOCK TABLES `user` WRITE;
/*!40000 ALTER TABLE `user` DISABLE KEYS */;
INSERT INTO `user` VALUES (1,0,'admin@gmail.it','Admin','d033e22ae348aeb5660fc2140aec35850c4da997','Admin','admin');
/*!40000 ALTER TABLE `user` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2019-11-09 20:18:36
