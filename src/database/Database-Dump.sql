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


CREATE DATABASE SILKROAD;
USE SILKROAD;

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
INSERT INTO `address` VALUES (14.415778249999999,121.0149235115982,'Philippines','TS Cruz Subdivision','Everlasting','27','1751');
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
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `auction`
--

LOCK TABLES `auction` WRITE;
/*!40000 ALTER TABLE `auction` DISABLE KEYS */;
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
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `bid`
--

LOCK TABLES `bid` WRITE;
/*!40000 ALTER TABLE `bid` DISABLE KEYS */;
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
) ENGINE=InnoDB AUTO_INCREMENT=37 DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `message`
--

LOCK TABLES `message` WRITE;
/*!40000 ALTER TABLE `message` DISABLE KEYS */;
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
INSERT INTO `user` VALUES ('admin','$2a$04$zt6XS7SVz9KjUosLl9Tcp.6sGse7c6HzurgHWTaIVPWlUySnAqcZm','admin@gmail.com','Giorgos','Patelis','0123456789','ABC1234',0,0,1,'ADMIN',14.415778249999999,121.0149235115982,'2022-07-03 22:11:06');
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

