-- --------------------------------------------------------
-- Host:                         127.0.0.1
-- Server version:               5.7.10-log - MySQL Community Server (GPL)
-- Server OS:                    Win64
-- HeidiSQL Version:             9.3.0.4984
-- --------------------------------------------------------

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET NAMES utf8mb4 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;

-- Dumping database structure for flinkhashdb
CREATE DATABASE IF NOT EXISTS `flinkhashdb` /*!40100 DEFAULT CHARACTER SET utf8 */;
USE `flinkhashdb`;


-- Dumping structure for table flinkhashdb.datahash
CREATE TABLE IF NOT EXISTS `datahash` (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `hash` blob NOT NULL,
  `datehash` blob,
  `placehash` blob,
  `name` varchar(50) DEFAULT NULL,
  `flinkdbid` int(10) DEFAULT NULL,
  `flinkdbuuid` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=496 DEFAULT CHARSET=latin1;

-- Dumping data for table flinkhashdb.datahash: ~0 rows (approximately)
/*!40000 ALTER TABLE `datahash` DISABLE KEYS */;
/*!40000 ALTER TABLE `datahash` ENABLE KEYS */;
/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IF(@OLD_FOREIGN_KEY_CHECKS IS NULL, 1, @OLD_FOREIGN_KEY_CHECKS) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
