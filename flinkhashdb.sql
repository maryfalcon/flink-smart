# --------------------------------------------------------
# Host:                         127.0.0.1
# Database:                     flinkhashdb
# Server version:               5.5.29
# Server OS:                    Win32
# HeidiSQL version:             5.0.0.3272
# Date/time:                    2016-01-20 00:38:51
# --------------------------------------------------------

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET NAMES utf8 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
# Dumping database structure for flinkhashdb
DROP DATABASE IF EXISTS `flinkhashdb`;
CREATE DATABASE IF NOT EXISTS `flinkhashdb` /*!40100 DEFAULT CHARACTER SET latin1 */;
USE `flinkhashdb`;


# Dumping structure for table flinkhashdb.datahash
DROP TABLE IF EXISTS `datahash`;
CREATE TABLE IF NOT EXISTS `datahash` (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `hash` blob NOT NULL,
  `name` varchar(50) DEFAULT NULL,
  `flinkdbid` int(10) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=latin1;

# Dumping data for table flinkhashdb.datahash: 0 rows
/*!40000 ALTER TABLE `datahash` DISABLE KEYS */;
/*!40000 ALTER TABLE `datahash` ENABLE KEYS */;
/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
