-- MySQL dump 10.13  Distrib 8.0.28, for Win64 (x86_64)
--
-- Host: localhost    Database: silkroad
-- ------------------------------------------------------
-- Server version	8.0.28

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `address`
--

DROP TABLE IF EXISTS `address`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `address` (
  `latitude` double NOT NULL,
  `longitude` double NOT NULL,
  `country` varchar(100) NOT NULL,
  `location` varchar(100) NOT NULL,
  `street_name` varchar(100) NOT NULL,
  `street_number` varchar(45) NOT NULL,
  `zip_code` varchar(45) NOT NULL,
  PRIMARY KEY (`latitude`,`longitude`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `address`
--

LOCK TABLES `address` WRITE;
/*!40000 ALTER TABLE `address` DISABLE KEYS */;
INSERT INTO `address` VALUES (-88.9751,31.7505,'Malaysia','Lessiebury','Botsford Tunnel','936','379'),(-68.0417,4.1718,'Norfolk Island','Lake Alyson','Murphy Circles','262','153'),(-66.8345,80.5658,'Panama','Port Jeremie','Hoppe Mission','175','756'),(-9.8841,-94.543,'Mozambique','Pollichville','Konopelski Drive','992','374'),(-6.324,133.8062,'Togo','East Bransonchester','D\'Amore Common','808','417'),(-3.8729,58.0915,'Ireland','Kalamazoo','Maurice Expressway','443','36'),(-1.4937,-21.5241,'Zimbabwe','Maggioberg','Garrett Turnpike','864','404'),(1,1,'Lithuania','Keelyshire','Linwood Estates','743','822'),(3.3731,-46.7249,'French Guiana','Cynthiahaven','Clarabelle Fork','369','824'),(8.909,-26.9957,'South Africa','Hartmannfurt','Hector Spring','490','718'),(16.3328,29.0118,'Austria','South Melyssa','Claudine Villages','41','133'),(26.7393,-164.7311,'Nicaragua','New Ransomchester','Icie Rapid','78','442'),(37.101,-109.834,'Angola','South Jessikastad','Carroll Meadows','199','583'),(41.3656,121.5107,'Japan','New Uriahfort','Leilani Light','935','157'),(49.7109,-4.0654,'Brazil','Asheville','Ozella Falls','46','141'),(50.1627,-158.2997,'Eritrea','East Damon','Larkin Junction','876','371'),(72.5833,-26.153,'Jamaica','Roswell','Gabriella Vista','721','322'),(74.3382,57.6977,'American Samoa','Lake Duncanland','Bernhard Loaf','622','576'),(84.4083,172.4377,'Nepal','Deronchester','Price Stravenue','819','203');
/*!40000 ALTER TABLE `address` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `auction`
--

DROP TABLE IF EXISTS `auction`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `auction` (
  `id` bigint unsigned NOT NULL AUTO_INCREMENT,
  `name` text NOT NULL,
  `description` text NOT NULL,
  `start_date` datetime NOT NULL,
  `end_date` datetime NOT NULL,
  `buy_price` double unsigned DEFAULT NULL,
  `first_bid` double unsigned NOT NULL,
  `highest_bid` double unsigned NOT NULL,
  `total_bids` bigint unsigned NOT NULL DEFAULT '0',
  `latitude` double NOT NULL,
  `longitude` double NOT NULL,
  `seller_id` varchar(45) NOT NULL,
  `version` bigint unsigned NOT NULL DEFAULT '0',
  `bid_id` bigint unsigned DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_Auction_Address1_idx` (`latitude`,`longitude`),
  KEY `fk_Auction_User1_idx` (`seller_id`),
  KEY `fk_Auction_Bid1_idx` (`bid_id`),
  CONSTRAINT `fk_Auction_Address1` FOREIGN KEY (`latitude`, `longitude`) REFERENCES `address` (`latitude`, `longitude`),
  CONSTRAINT `fk_Auction_Bid1` FOREIGN KEY (`bid_id`) REFERENCES `bid` (`id`),
  CONSTRAINT `fk_Auction_User1` FOREIGN KEY (`seller_id`) REFERENCES `user` (`username`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `auction`
--

LOCK TABLES `auction` WRITE;
/*!40000 ALTER TABLE `auction` DISABLE KEYS */;
INSERT INTO `auction` VALUES (1,'Keyboard Manat EXE','Clothing Card Legacy Associate Automotive Brazilian Regional Metrics Buckinghamshire','2022-05-18 04:08:14','2022-05-22 19:49:18',262,58,2000,2,74.3382,57.6977,'Alek',0,1),(3,'Locks Alaska parse','Berkshire Chips Shoes Books Arizona intermediate hardware Avon','2022-05-18 04:09:51','2022-05-20 19:00:48',NULL,80,2000,1,16.3328,29.0118,'Alek',0,3),(4,'Tennessee grid-enabled collaborative','analyzer Savings Prairie Trail RAM Dynamic Unbranded Nevada Industrial','2022-05-18 04:13:23','2022-05-22 18:45:03',NULL,69,0,0,26.7393,-164.7311,'Boris',0,NULL),(5,'SMS Chair green','Salad New morph Brand AI','2022-05-18 04:13:34','2022-05-22 15:38:17',317,94,0,0,-68.0417,4.1718,'Boris',0,NULL),(6,'streamline pink customized','Bedfordshire multi-byte redundant HTTP architecture front-end Berkshire','2022-05-18 04:13:47','2022-05-22 15:32:26',759,85,0,0,-1.4937,-21.5241,'Boris',0,NULL);
/*!40000 ALTER TABLE `auction` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `bid`
--

DROP TABLE IF EXISTS `bid`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `bid` (
  `id` bigint unsigned NOT NULL AUTO_INCREMENT,
  `user_id` varchar(45) NOT NULL,
  `auction_id` bigint unsigned NOT NULL,
  `amount` double unsigned NOT NULL,
  `submission_date` datetime NOT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_User_has_Auction_Auction1_idx` (`auction_id`),
  KEY `fk_Bid_User1_idx` (`user_id`),
  CONSTRAINT `fk_Bid_User1` FOREIGN KEY (`user_id`) REFERENCES `user` (`username`),
  CONSTRAINT `fk_User_has_Auction_Auction1` FOREIGN KEY (`auction_id`) REFERENCES `auction` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `bid`
--

LOCK TABLES `bid` WRITE;
/*!40000 ALTER TABLE `bid` DISABLE KEYS */;
INSERT INTO `bid` VALUES (1,'Boris',1,2000,'2022-05-18 18:35:00'),(2,'Christine',1,202,'2022-05-18 18:04:13'),(3,'Boris',3,2000,'2022-05-18 19:14:13');
/*!40000 ALTER TABLE `bid` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `category`
--

DROP TABLE IF EXISTS `category`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `category` (
  `id` varchar(45) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `category`
--

LOCK TABLES `category` WRITE;
/*!40000 ALTER TABLE `category` DISABLE KEYS */;
INSERT INTO `category` VALUES ('Appliances'),('Books'),('Clothing'),('Collectibles'),('Electronics'),('Movies'),('Music'),('Sports'),('Toys & Games'),('Transportation');
/*!40000 ALTER TABLE `category` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `classification`
--

DROP TABLE IF EXISTS `classification`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `classification` (
  `auction_id` bigint unsigned NOT NULL,
  `category_id` varchar(45) NOT NULL,
  PRIMARY KEY (`auction_id`,`category_id`),
  KEY `fk_Category_has_Auction_Auction1_idx` (`auction_id`),
  KEY `fk_Category_has_Auction_Category_idx` (`category_id`),
  CONSTRAINT `fk_Category_has_Auction_Auction1` FOREIGN KEY (`auction_id`) REFERENCES `auction` (`id`),
  CONSTRAINT `fk_Category_has_Auction_Category` FOREIGN KEY (`category_id`) REFERENCES `category` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `classification`
--

LOCK TABLES `classification` WRITE;
/*!40000 ALTER TABLE `classification` DISABLE KEYS */;
INSERT INTO `classification` VALUES (1,'Appliances'),(1,'Books'),(1,'Collectibles'),(1,'Music'),(3,'Appliances'),(3,'Collectibles'),(3,'Electronics'),(3,'Music'),(4,'Appliances'),(4,'Books'),(5,'Appliances'),(5,'Books'),(5,'Music'),(5,'Toys & Games'),(6,'Appliances'),(6,'Collectibles'),(6,'Electronics'),(6,'Music');
/*!40000 ALTER TABLE `classification` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `image`
--

DROP TABLE IF EXISTS `image`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `image` (
  `path` varchar(256) NOT NULL,
  `auction_id` bigint unsigned NOT NULL,
  PRIMARY KEY (`path`),
  KEY `fk_Image_Auction1_idx` (`auction_id`),
  CONSTRAINT `fk_Image_Auction1` FOREIGN KEY (`auction_id`) REFERENCES `auction` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `image`
--

LOCK TABLES `image` WRITE;
/*!40000 ALTER TABLE `image` DISABLE KEYS */;
INSERT INTO `image` VALUES ('Uploads/1/Alek1.png',1),('Uploads/1/Alek11.png',1),('Uploads/1/Alek111.png',1),('Uploads/3/Alek3.png',3),('Uploads/3/Alek33.png',3),('Uploads/3/Alek333.png',3),('Uploads/4/Boris1.png',4),('Uploads/4/Boris11.png',4),('Uploads/4/Boris111.png',4),('Uploads/5/Boris2.png',5),('Uploads/5/Boris22.png',5),('Uploads/6/Boris3.png',6),('Uploads/6/Boris33.png',6),('Uploads/6/Boris333.png',6);
/*!40000 ALTER TABLE `image` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `message`
--

DROP TABLE IF EXISTS `message`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `message` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `title` text NOT NULL,
  `body` longtext NOT NULL,
  `creation_date` datetime NOT NULL,
  `sender` varchar(45) NOT NULL,
  `recipient` varchar(45) NOT NULL,
  `is_read` tinyint NOT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_Message_User1_idx` (`sender`),
  KEY `fk_Message_User2_idx` (`recipient`),
  CONSTRAINT `fk_Message_User1` FOREIGN KEY (`sender`) REFERENCES `user` (`username`),
  CONSTRAINT `fk_Message_User2` FOREIGN KEY (`recipient`) REFERENCES `user` (`username`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `message`
--

LOCK TABLES `message` WRITE;
/*!40000 ALTER TABLE `message` DISABLE KEYS */;
INSERT INTO `message` VALUES (1,'Hello','Hi from Alek','2022-05-18 18:38:03','Alek','Boris',1);
/*!40000 ALTER TABLE `message` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `messageaccess`
--

DROP TABLE IF EXISTS `messageaccess`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `messageaccess` (
  `user_id` varchar(45) NOT NULL,
  `message_id` bigint NOT NULL,
  PRIMARY KEY (`user_id`,`message_id`),
  KEY `fk_User_has_Message_Message1_idx` (`message_id`),
  KEY `fk_User_has_Message_User1_idx` (`user_id`),
  CONSTRAINT `fk_User_has_Message_Message1` FOREIGN KEY (`message_id`) REFERENCES `message` (`id`),
  CONSTRAINT `fk_User_has_Message_User1` FOREIGN KEY (`user_id`) REFERENCES `user` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `messageaccess`
--

LOCK TABLES `messageaccess` WRITE;
/*!40000 ALTER TABLE `messageaccess` DISABLE KEYS */;
INSERT INTO `messageaccess` VALUES ('Alek',1);
/*!40000 ALTER TABLE `messageaccess` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `role`
--

DROP TABLE IF EXISTS `role`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `role` (
  `id` varchar(45) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `role`
--

LOCK TABLES `role` WRITE;
/*!40000 ALTER TABLE `role` DISABLE KEYS */;
INSERT INTO `role` VALUES ('ADMIN'),('USER');
/*!40000 ALTER TABLE `role` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `searchhistory`
--

DROP TABLE IF EXISTS `searchhistory`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `searchhistory` (
  `auction_id` bigint unsigned NOT NULL,
  `user_id` varchar(45) NOT NULL,
  `interactions` bigint unsigned NOT NULL,
  PRIMARY KEY (`auction_id`,`user_id`),
  KEY `fk_User_has_Auction_Auction2_idx` (`auction_id`),
  KEY `fk_SearchHistory_User1_idx` (`user_id`),
  CONSTRAINT `fk_SearchHistory_User1` FOREIGN KEY (`user_id`) REFERENCES `user` (`username`),
  CONSTRAINT `fk_User_has_Auction_Auction2` FOREIGN KEY (`auction_id`) REFERENCES `auction` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `searchhistory`
--

LOCK TABLES `searchhistory` WRITE;
/*!40000 ALTER TABLE `searchhistory` DISABLE KEYS */;
INSERT INTO `searchhistory` VALUES (1,'Boris',6),(1,'Christine',1),(3,'Boris',1),(4,'Alek',10);
/*!40000 ALTER TABLE `searchhistory` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user` (
  `username` varchar(45) NOT NULL,
  `password` varchar(256) NOT NULL,
  `email` varchar(45) NOT NULL,
  `first_name` varchar(45) NOT NULL,
  `last_name` varchar(45) NOT NULL,
  `phone` varchar(45) NOT NULL,
  `tin` varchar(45) NOT NULL,
  `buyer_rating` double unsigned NOT NULL,
  `seller_rating` double unsigned NOT NULL,
  `approved` tinyint NOT NULL,
  `role_id` varchar(45) NOT NULL,
  `latitude` double NOT NULL,
  `longitude` double NOT NULL,
  `join_date` datetime NOT NULL,
  PRIMARY KEY (`username`),
  UNIQUE KEY `user_name_UNIQUE` (`username`),
  UNIQUE KEY `email_UNIQUE` (`email`),
  KEY `fk_User_Address1_idx` (`latitude`,`longitude`),
  KEY `fk_User_Role1_idx` (`role_id`),
  CONSTRAINT `fk_User_Address1` FOREIGN KEY (`latitude`, `longitude`) REFERENCES `address` (`latitude`, `longitude`),
  CONSTRAINT `fk_User_Role1` FOREIGN KEY (`role_id`) REFERENCES `role` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user`
--

LOCK TABLES `user` WRITE;
/*!40000 ALTER TABLE `user` DISABLE KEYS */;
INSERT INTO `user` VALUES ('Alek','$2a$04$30iugFQu0.g10cBFKJcyoObXnyz06YrcS/XUBywjvqLcAwg3acdg2','Katlyn.Turcotte92@hotmail.com','Khalil','Kuvalis','207-922-4602','XXXXXX',0,0,1,'USER',1,1,'2022-05-18 03:03:55'),('Boris','$2a$04$xpntmL/h8ixlpaL/ul0pmum6zye74ds3GLHA7EeU2g8QDULKTHQ3O','Felicita.Kassulke@yahoo.com','Matilde','Volkman','209-219-9348','XXXXXX',0,0,1,'USER',1,1,'2022-05-18 03:04:04'),('Christine','$2a$04$loMNTz86DEzPk9XgFD/6M.Zic9QMD4OmUcMHMIbsNu..UPYfekF4a','Bobbie_Powlowski@yahoo.com','Aliya','Pollich','524-770-6515','XXXXXX',0,0,1,'USER',72.5833,-26.153,'2022-05-18 03:16:42'),('Dan','$2a$04$xUYrHcTVu7NTb304Pz97tOFyq9HaVSjsM4QZetZumuAVahzFL2yvu','Velma.Gaylord@hotmail.com','Mabelle','Kassulke','256-349-8573','XXXXXX',0,0,0,'USER',8.909,-26.9957,'2022-05-18 03:16:33'),('George','$2a$04$ynGp7vdKMqAN0.wcbQxo0eRPQeyKjoxyCceM5oz6DjMpEzJMm4Ng2','Makayla_Ritchie@gmail.com','Justina','Kilback','315-973-8538','XXXXXX',0,0,1,'ADMIN',37.101,-109.834,'2022-05-18 03:03:33');
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

-- Dump completed on 2022-05-18 19:30:22
