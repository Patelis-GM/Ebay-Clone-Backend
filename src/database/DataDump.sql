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
-- Current Database: `silkroad`
--

CREATE DATABASE /*!32312 IF NOT EXISTS*/ `silkroad` /*!40100 DEFAULT CHARACTER SET utf8 */ /*!80016 DEFAULT ENCRYPTION='N' */;

USE `silkroad`;

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
INSERT INTO `address` VALUES (-89.2122,89.9337,'Congo','South Paoloberg','Hoeger Ferry','992','373'),(-82.5954,28.9132,'Mongolia','Schimmelchester','Fletcher Crescent','960','198'),(-80.4218,-99.309,'Republic of Korea','South Mortimerhaven','Fahey Points','261','603'),(-29.5236,-126.3965,'Tunisia','Saraiville','Raul Squares','601','1'),(-28.3861,27.2883,'Pitcairn Islands','Brentwood','Pearline Junctions','919','156'),(-28.2671,145.3656,'Estonia','Trompside','Ebert Ville','676','447'),(-9.0898,149.8206,'Ethiopia','Eden Prairie','Goodwin Tunnel','722','428'),(-6.4766,108.8964,'Norfolk Island','Port Freda','Cecile Drive','93','127'),(-2.4969,-46.0937,'Iceland','Alhambra','Katelyn Dam','847','26'),(-1.3398,155.7,'Cyprus','Tarynside','Betsy Mill','504','30'),(0.0175,-102.5948,'Israel','Lake Osbaldoshire','Rachelle Throughway','852','174'),(4.4667,-20.3616,'United Arab Emirates','Lake Owen','Leuschke Route','231','279'),(19.6371,127.1541,'Mauritius','Rosariofort','Stroman Stream','769','524'),(31.8932,-139.4988,'Azerbaijan','Botsfordton','Smith Loop','957','927'),(38.6209,101.0354,'Andorra','New Freidamouth','Mayert Grove','196','744'),(48.5993,56.1957,'Thailand','Blue Springs','Aubree Pine','800','123'),(53.9423,-157.9495,'Slovenia','Annieville','Madilyn Place','954','324'),(54.0371,-165.714,'French Southern Territories','Dakotaberg','Lockman Fords','884','135'),(63.3937,-152.7668,'Lao People\'s Democratic Republic','South Yolanda','Wilford Trafficway','579','636'),(76.5681,-12.8845,'Bosnia and Herzegovina','Marshallbury','Steuber Parkway','624','221'),(88.8195,-31.9489,'Barbados','Paterson','Renner Way','62','323');
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
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `auction`
--

LOCK TABLES `auction` WRITE;
/*!40000 ALTER TABLE `auction` DISABLE KEYS */;
INSERT INTO `auction` VALUES (1,'connecting','Forward upward-trending','2022-05-12 19:11:25','2022-05-16 13:02:46',1000,76,1000,2,-28.3861,27.2883,'George',1,1),(2,'input','revolutionary array dot-com','2022-05-12 19:13:26','2022-05-12 19:45:22',NULL,71,101,1,48.5993,56.1957,'Alek',0,3),(3,'mesh','Managed Identity Mission','2022-05-12 20:23:02','2022-05-16 12:02:20',NULL,71,0,0,4.4667,-20.3616,'Alek',0,NULL),(4,'empower','Sahara dynamic RSS green Chips','2022-05-12 20:23:03','2022-05-16 06:58:30',NULL,76,0,0,88.8195,-31.9489,'Alek',0,NULL),(5,'Investment','Account niches Frozen Customizable','2022-05-12 20:23:04','2022-05-19 19:24:01',NULL,92,0,0,-82.5954,28.9132,'Alek',0,NULL),(6,'Specialist','XML reintermediate','2022-05-12 20:23:41','2022-05-17 18:07:11',1000,80,2005,1,-80.4218,-99.309,'Alek',0,4),(7,'virtual','Borders Delaware','2022-05-12 20:25:06','2022-05-15 19:33:24',NULL,87,0,0,63.3937,-152.7668,'Alek',0,NULL);
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
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `bid`
--

LOCK TABLES `bid` WRITE;
/*!40000 ALTER TABLE `bid` DISABLE KEYS */;
INSERT INTO `bid` VALUES (1,'Alek',1,1000,'2022-05-12 19:48:59'),(2,'Boris',1,101,'2022-05-12 19:45:58'),(3,'Boris',2,101,'2022-05-12 19:51:33'),(4,'Boris',6,2005,'2022-05-12 20:30:38');
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
INSERT INTO `category` VALUES ('Appliances'),('Clothing'),('Collectibles'),('Movies'),('Music'),('Toys & Games');
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
INSERT INTO `classification` VALUES (1,'Clothing'),(2,'Appliances'),(2,'Clothing'),(2,'Movies'),(2,'Music'),(3,'Appliances'),(3,'Clothing'),(3,'Movies'),(3,'Music'),(4,'Appliances'),(4,'Clothing'),(4,'Movies'),(4,'Music'),(5,'Appliances'),(5,'Clothing'),(5,'Movies'),(5,'Music'),(6,'Movies'),(7,'Movies'),(7,'Music');
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
INSERT INTO `image` VALUES ('C:/Users/PLAISIO/Desktop/Web-Applications/Uploads/1/Alek1.png',1),('C:/Users/PLAISIO/Desktop/Web-Applications/Uploads/2/Alek1.png',2),('C:/Users/PLAISIO/Desktop/Web-Applications/Uploads/2/Alek2.png',2),('C:/Users/PLAISIO/Desktop/Web-Applications/Uploads/2/Alek3.png',2),('C:/Users/PLAISIO/Desktop/Web-Applications/Uploads/3/Alek1.png',3),('C:/Users/PLAISIO/Desktop/Web-Applications/Uploads/3/Alek2.png',3),('C:/Users/PLAISIO/Desktop/Web-Applications/Uploads/3/Alek3.png',3),('C:/Users/PLAISIO/Desktop/Web-Applications/Uploads/4/Alek1.png',4),('C:/Users/PLAISIO/Desktop/Web-Applications/Uploads/4/Alek2.png',4),('C:/Users/PLAISIO/Desktop/Web-Applications/Uploads/4/Alek3.png',4),('C:/Users/PLAISIO/Desktop/Web-Applications/Uploads/5/Alek1.png',5),('C:/Users/PLAISIO/Desktop/Web-Applications/Uploads/5/Alek2.png',5),('C:/Users/PLAISIO/Desktop/Web-Applications/Uploads/5/Alek3.png',5),('C:/Users/PLAISIO/Desktop/Web-Applications/Uploads/6/Alek1.png',6),('C:/Users/PLAISIO/Desktop/Web-Applications/Uploads/6/Alek2.png',6),('C:/Users/PLAISIO/Desktop/Web-Applications/Uploads/6/Alek3.png',6),('C:/Users/PLAISIO/Desktop/Web-Applications/Uploads/7/Alek3.png',7);
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
  `is_read` tinyint NOT NULL DEFAULT '0',
  `sender` varchar(45) NOT NULL,
  `recipient` varchar(45) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_Message_User1_idx` (`sender`),
  KEY `fk_Message_User2_idx` (`recipient`),
  CONSTRAINT `fk_Message_User1` FOREIGN KEY (`sender`) REFERENCES `user` (`username`),
  CONSTRAINT `fk_Message_User2` FOREIGN KEY (`recipient`) REFERENCES `user` (`username`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `message`
--

LOCK TABLES `message` WRITE;
/*!40000 ALTER TABLE `message` DISABLE KEYS */;
INSERT INTO `message` VALUES (1,'Hello','Hi from Alek','2022-05-12 19:59:06',0,'Alek','Boris'),(2,'Hello2','Hi from Alek','2022-05-12 20:04:26',1,'Alek','Boris'),(3,'Hello3','Hi from Alek','2022-05-12 20:04:31',0,'Alek','Boris');
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
INSERT INTO `messageaccess` VALUES ('Boris',2),('Alek',3),('Boris',3);
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
INSERT INTO `searchhistory` VALUES (1,'Alek'),(1,'Boris'),(1,'George'),(2,'Boris'),(4,'George'),(6,'Boris'),(6,'George'),(7,'Alek'),(7,'George');
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
INSERT INTO `user` VALUES ('Alek','$2a$04$Ny6MtjDc.AkP3VZU71pjheYComIG2pL0Dhc5ks.IjVgVZ9rLrN3WC','Carleton64@yahoo.com','Meda','Mohr','568-773-4053','XXXXXX',0,0,1,'USER',-1.3398,155.7,'2022-05-12 16:15:20'),('Boris','$2a$04$F/fkTIn.Ja9uCnnm98E9Ee4rNGtHC8zXdOODzZHrpL.0Zt0tiQyvG','Carlo_Champlin@yahoo.com','Carol','Bernier','759-781-3552','XXXXXX',0,0,1,'USER',-9.0898,149.8206,'2022-05-12 16:15:26'),('Christine','$2a$04$wa3mu1n61KzYB37zXlapzeCkT5Nuq9Hqt8h42qw.z6nPTRqF/YsrG','Ebony.Gibson@yahoo.com','Mable','Hickle','486-373-7836','XXXXXX',0,0,1,'USER',19.6371,127.1541,'2022-05-12 16:15:32'),('Dan','$2a$04$QeZ1KWahBCLto1FDcPwFjuypfaCrC31EgOfUpkj5UmD.Yr5E25OZa','Gerhard_Considine12@hotmail.com','Martine','Ankunding','835-477-0505','XXXXXX',0,0,0,'USER',-89.2122,89.9337,'2022-05-12 16:15:38'),('George','$2a$04$sTAYVehKlpcPWS/mEsP2iuka2ALfiQBJOQHDX714mdlu0bHMfVaGu','Jamaal_Altenwerth14@gmail.com','Elmira','Dare','601-467-1199','XXXXXX',0,0,1,'ADMIN',-28.2671,145.3656,'2022-05-12 16:15:53'),('Xin','$2a$04$lPNQq66Ai4NSNOZwpc73f.drLBBnZnX0tgNN5rFvIRj36KK.SCMwa','Cordelia_Ortiz11@hotmail.com','Geovanny','Kling','808-684-0793','XXXXXX',0,0,1,'USER',76.5681,-12.8845,'2022-05-12 19:21:04');
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

-- Dump completed on 2022-05-12 21:02:32
