CREATE DATABASE  IF NOT EXISTS `ICHAT` /*!40100 DEFAULT CHARACTER SET utf8 */;
USE `ICHAT`;
-- MySQL dump 10.13  Distrib 5.7.17, for Win64 (x86_64)
--
-- Host: 47.106.83.27    Database: ICHAT
-- ------------------------------------------------------
-- Server version	5.7.22

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `TFRIEND`
--

DROP TABLE IF EXISTS `TFRIEND`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `TFRIEND` (
  `FID` varchar(20) NOT NULL,
  `UID` varchar(20) NOT NULL,
  KEY `FTU_idx` (`UID`),
  CONSTRAINT `FTU` FOREIGN KEY (`UID`) REFERENCES `TUSER` (`ID`) ON DELETE CASCADE ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `THEADPORTRAIT`
--

DROP TABLE IF EXISTS `THEADPORTRAIT`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `THEADPORTRAIT` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `URL` varchar(80) NOT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=57 DEFAULT CHARSET=utf8 COMMENT='用户头像表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `TSTATUS`
--

DROP TABLE IF EXISTS `TSTATUS`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `TSTATUS` (
  `ID` varchar(20) NOT NULL,
  `STATUS` varchar(5) NOT NULL,
  PRIMARY KEY (`ID`),
  CONSTRAINT `-tuser` FOREIGN KEY (`ID`) REFERENCES `TUSER` (`ID`) ON DELETE CASCADE ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='离在线线状态表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `TUSER`
--

DROP TABLE IF EXISTS `TUSER`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `TUSER` (
  `ID` varchar(20) NOT NULL,
  `NICKNAME` varchar(20) DEFAULT NULL,
  `PASWD` varchar(18) DEFAULT NULL,
  `SEX` varchar(3) DEFAULT NULL,
  `EMAIL` varchar(25) DEFAULT NULL,
  `PLACE` varchar(25) DEFAULT NULL,
  `HOMETOWN` varchar(25) DEFAULT NULL,
  `SIGNATURE` varchar(35) DEFAULT NULL,
  `BIRTHDAY` bigint(30) DEFAULT NULL,
  `REG_DATE` bigint(30) DEFAULT NULL,
  `HEADPORTRAIT_ID` int(11) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE KEY `ID_UNIQUE` (`ID`),
  KEY `T-THEADPORTRAIT_idx` (`HEADPORTRAIT_ID`),
  CONSTRAINT `T-THEADPORTRAIT` FOREIGN KEY (`HEADPORTRAIT_ID`) REFERENCES `THEADPORTRAIT` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping events for database 'ICHAT'
--

--
-- Dumping routines for database 'ICHAT'
--
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2018-12-17 17:26:44

/*
USE ICHAT;

DELIMITER $$
CREATE TRIGGER NEW_STATUS_TRIGGER
	AFTER INSERT ON ICHAT.TUSER
	FOR EACH ROW
	BEGIN
    DECLARE STATUS_ID VARCHAR(30);
    SELECT LAST_INSERT_ID() INTO STATUS_ID;
    INSERT INTO TSTATUS
    VALUES(STATUS_ID, '离线');
    END $$
DELIMITER ;

SHOW TRIGGERS;


DELIMITER $$
CREATE TRIGGER tri_message_commentsInsert
	AFTER INSERT ON message_comments
	FOR EACH ROW
BEGIN
	declare current_message_comments_count int;
	declare message_comments_count int;
    declare comments_id int;
    declare message_id int;

    set comments_id = ( SELECT LAST_INSERT_ID() );

    set message_id = (
		select message_comments.mess_id
		from message_comments
		where message_comments.UID = comments_id );

	set current_message_comments_count = (
		select message.comments_count
        from message
        where message.UID = message_id );

	update message
		set message.comments_count = current_message_comments_count + 1
        where message.UID = message_id;

END $$
DELIMITER ;


#CREATE USER 'zjp'@'%' IDENTIFIED BY 'Zjp.123456';

#GRANT all ON shop.* TO 'zjp'@'%';

#select user, host from mysql.user

#CREATE USER 'lihuaixiang'@'%'
#IDENTIFIED BY 'Lihx.123456';

#GRANT all ON lihuaixiang.* TO 'lihuaixiang'@'%';

#revoke usage on *.* from lihuaixiang;

#SHOW GRANTS FOR 'lihuaixiang'@'%';
*/