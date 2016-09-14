/*
Navicat MySQL Data Transfer

Source Server         : localhost
Source Server Version : 50709
Source Host           : localhost:3306
Source Database       : reportdb

Target Server Type    : MYSQL
Target Server Version : 50709
File Encoding         : 65001

Date: 2016-09-11 17:45:56
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for file_name_element
-- ----------------------------
DROP TABLE IF EXISTS `file_name_element`;
CREATE TABLE `file_name_element` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `REPORT_ID` int(11) NOT NULL,
  `TYPE` int(11) NOT NULL,
  `INDEX_NO` int(11) NOT NULL,
  `DESCRIPTION` varchar(30) NOT NULL,
  PRIMARY KEY (`ID`),
  KEY `REPORT_ID` (`REPORT_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='SR_NOMFICHIER_EXPORT';

-- ----------------------------
-- Table structure for report
-- ----------------------------
DROP TABLE IF EXISTS `report`;
CREATE TABLE `report` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `NAME` varchar(100) NOT NULL,
  `TYPE` int(11) NOT NULL,
  `DESCRIPTION` varchar(255) DEFAULT NULL,
  `SQL_TEXT` text,
  `VERSION` varchar(20) DEFAULT NULL,
  `RESOLUTION` int(11) DEFAULT NULL,
  `HEIGHT` int(11) DEFAULT NULL,
  `WIDTH` int(11) DEFAULT NULL,
  `EXCEL_TEMPLATE` varchar(255) DEFAULT NULL,
  `PROCEDURE_START` varchar(255) DEFAULT NULL COMMENT '?',
  `PROCEDURE_END` varchar(255) DEFAULT NULL COMMENT '?',
  `COMMENTS` varchar(255) DEFAULT NULL,
  `FILE` blob,
  PRIMARY KEY (`ID`),
  UNIQUE KEY `NAME` (`NAME`)
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=utf8 COMMENT='SR_REQUETES';

-- ----------------------------
-- Table structure for report_column
-- ----------------------------
DROP TABLE IF EXISTS `report_column`;
CREATE TABLE `report_column` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `REPORT_ID` int(11) NOT NULL,
  `NAME` varchar(100) NOT NULL,
  `DESCRIPTION` varchar(255) DEFAULT NULL,
  `INDEX_NO` int(11) NOT NULL,
  `TYPE` int(11) NOT NULL,
  `FLOAT_PRECISION` int(11) DEFAULT NULL,
  `WIDTH` int(11) DEFAULT NULL,
  `ALIGNEMENT` int(11) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `REPORT_ID` (`REPORT_ID`)
) ENGINE=InnoDB AUTO_INCREMENT=364 DEFAULT CHARSET=utf8 COMMENT='SR_COLONNES_REQUETES';

-- ----------------------------
-- Table structure for report_export
-- ----------------------------
DROP TABLE IF EXISTS `report_export`;
CREATE TABLE `report_export` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `REPORT_ID` int(11) NOT NULL,
  `DIRECTORY` varchar(255) DEFAULT NULL,
  `SEPARATOR` varchar(10) DEFAULT NULL,
  `COLOR_HEADERS` varchar(50) DEFAULT NULL,
  `COLOR_LINE1` varchar(50) DEFAULT NULL,
  `COLOR_LINE2` varchar(50) DEFAULT NULL,
  `COLOR_TOTAL` varchar(50) DEFAULT NULL,
  `EXPORT_TITLE` int(11) NOT NULL,
  `EXPORT_PARAMS` int(11) NOT NULL,
  `EXPORT_HEADERS` int(11) NOT NULL,
  PRIMARY KEY (`ID`),
  KEY `REPORT_ID` (`REPORT_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='SR_PARAMETRES_EXPORT';

-- ----------------------------
-- Table structure for report_parameter
-- ----------------------------
DROP TABLE IF EXISTS `report_parameter`;
CREATE TABLE `report_parameter` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `REPORT_ID` int(11) NOT NULL,
  `NAME` varchar(100) NOT NULL,
  `DESCRIPTION` varchar(255) DEFAULT NULL,
  `INDEX_NO` int(11) NOT NULL,
  `TYPE` int(11) NOT NULL,
  `DEFAULT_VALUE` varchar(100) DEFAULT NULL,
  `LIST_SQL_TEXT` text,
  `LIST_SQL_DEFAULT` text,
  `MULTI_SELECTION` int(11) DEFAULT '0',
  PRIMARY KEY (`ID`),
  KEY `REPORT_ID` (`REPORT_ID`)
) ENGINE=InnoDB AUTO_INCREMENT=170 DEFAULT CHARSET=utf8 COMMENT='SR_PARAMETRES_REQUETES';

-- ----------------------------
-- Table structure for report_send
-- ----------------------------
DROP TABLE IF EXISTS `report_send`;
CREATE TABLE `report_send` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `REPORT_ID` int(11) NOT NULL,
  `FTP_ACTIVE` int(11) NOT NULL DEFAULT '0',
  `FTP_HOST` varchar(100) DEFAULT NULL,
  `FTP_PORT` int(11) DEFAULT NULL,
  `FTP_USER` varchar(100) DEFAULT NULL,
  `FTP_PASSWORD` varchar(100) DEFAULT NULL,
  `EMAIL_ACTIVE` int(11) NOT NULL DEFAULT '0',
  `EMAIL_HOST` varchar(100) DEFAULT NULL,
  `EMAIL_PORT` int(11) DEFAULT NULL,
  `EMAIL_CHARSET` int(11) DEFAULT NULL,
  `EMAIL_USER` varchar(100) DEFAULT NULL,
  `EMAIL_PASSWORD` varchar(100) DEFAULT NULL,
  `EMAIL_PROTOCOL` varchar(100) DEFAULT NULL,
  `EMAIL_USE_SSL` int(11) NOT NULL DEFAULT '0',
  PRIMARY KEY (`ID`),
  KEY `REPORT_ID` (`REPORT_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='SR_PARAMETRES_COMMUNICATION';

-- ----------------------------
-- Table structure for report_treatment
-- ----------------------------
DROP TABLE IF EXISTS `report_treatment`;
CREATE TABLE `report_treatment` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `REPORT_ID` int(11) NOT NULL,
  `PROCEDURE_NAME` varchar(100) NOT NULL,
  `PROCEDURE_TYPE` int(11) NOT NULL,
  `QUESTION` varchar(100) NOT NULL,
  PRIMARY KEY (`ID`),
  KEY `REPORT_ID` (`REPORT_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='SR_TRAITEMENTS';

-- ----------------------------
-- Table structure for EMAIL_ADDRESS
-- ----------------------------
DROP TABLE IF EXISTS `EMAIL_ADDRESS`;
CREATE TABLE `EMAIL_ADDRESS` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `REPORT_ID` int(11) NOT NULL,
  `EMAIL_ADDRESS` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY (`REPORT_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT 'SR_ADRESSES_EMAIL';

-- ----------------------------
-- Table structure for treatment_parameter
-- ----------------------------
DROP TABLE IF EXISTS `treatment_parameter`;
CREATE TABLE `treatment_parameter` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `REPORT_ID` int(11) NOT NULL,
  `PROCEDURE_NAME` varchar(100) NOT NULL,
  `PROCEDURE_TYPE` int(11) NOT NULL,
  `PARAMETER_NAME` varchar(30) NOT NULL,
  `PARAMETER_DESC` varchar(30) DEFAULT NULL,
  `PARAMETER_TYPE` int(11) NOT NULL,
  `DEFAULT_VALUE` varchar(100) DEFAULT NULL,
  `LIST_SQL_TEXT` text,
  `LIST_SQL_DEFAULT` text,
  PRIMARY KEY (`ID`),
  KEY `REPORT_ID` (`REPORT_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='SR_PARAMETRES_PROCEDURES';
