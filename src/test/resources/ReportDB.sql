DROP DATABASE IF EXISTS `ReportDB`;
CREATE DATABASE `ReportDB`;

USE `ReportDB`;

DROP TABLE IF EXISTS `Report`;
CREATE TABLE `Report` (
  `Id` int(11) NOT NULL AUTO_INCREMENT,
  `Name` varchar(100) NOT NULL,
  `Type` int(11) NOT NULL,
  `Description` varchar(255) DEFAULT NULL,
  `SqlText` TEXT,
  `Version` varchar(20) DEFAULT NULL,
  `Resolution` int(11) DEFAULT NULL,
  `Height` int(11) DEFAULT NULL,
  `Width` int(11) DEFAULT NULL,
  `ExcelTemplate` varchar(255) DEFAULT NULL,
  `ProcedureStart` varchar(255) DEFAULT NULL,
  `ProcedureEnd` varchar(255) DEFAULT NULL,
  `Comments` varchar(255) DEFAULT NULL,
  `File` blob,
  PRIMARY KEY (`Id`),
  UNIQUE KEY (`Name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT 'SR_REQUETES';

DROP TABLE IF EXISTS `ReportColumn`;
CREATE TABLE `ReportColumn` (
  `Id` int(11) NOT NULL AUTO_INCREMENT,
  `ReportId` int(11) NOT NULL,
  `Name` varchar(100) NOT NULL,
  `Description` varchar(255) DEFAULT NULL,
  `IndexNo` int(11) NOT NULL,
  `Type` int(11) NOT NULL,
  `FloatPrecision` int(11) DEFAULT NULL,
  `Width` int(11) DEFAULT NULL,
  `Alignement` int(11) DEFAULT NULL,
  PRIMARY KEY (`Id`),
  KEY (`ReportId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT 'SR_COLONNES_REQUETES';

DROP TABLE IF EXISTS `ReportParameter`;
CREATE TABLE `ReportParameter` (
  `Id` int(11) NOT NULL AUTO_INCREMENT,
  `ReportId` int(11) NOT NULL,
  `Name` varchar(100) NOT NULL,
  `Description` varchar(255) DEFAULT NULL,
  `IndexNo` int(11) NOT NULL,
  `Type` int(11) NOT NULL,
  `DefaultValue` varchar(100) NOT NULL,
  `ListSqlText` TEXT,
  `ListSqlDefault` varchar(255) NOT NULL,
  `MultiSelection` int(11) NOT NULL,
  PRIMARY KEY (`Id`),
  KEY (`ReportId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT 'SR_PARAMETRES_REQUETES';

DROP TABLE IF EXISTS `ReportExport`;
CREATE TABLE `ReportExport` (
  `Id` int(11) NOT NULL AUTO_INCREMENT,
  `ReportId` int(11) NOT NULL,
  `Directory` varchar(255) DEFAULT NULL,
  `Separator` varchar(10) DEFAULT NULL,
  `ColorENTETES` varchar(50) DEFAULT NULL COMMENT '?',
  `ColorLine1` varchar(50) DEFAULT NULL,
  `ColorLine2` varchar(50) DEFAULT NULL,
  `ColorTotal` varchar(50) DEFAULT NULL,
  `ExportTitle` int(11) NOT NULL,
  `ExportParams` int(11) NOT NULL,
  `ExportENTETES` int(11) NOT NULL COMMENT '?',
  PRIMARY KEY (`Id`),
  KEY (`ReportId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT 'SR_PARAMETRES_EXPORT';

DROP TABLE IF EXISTS `FileNameElement`;
CREATE TABLE `FileNameElement` (
  `Id` int(11) NOT NULL AUTO_INCREMENT,
  `ReportId` int(11) NOT NULL,
  `Type` int(11) NOT NULL,
  `IndexNo` int(11) NOT NULL,
  `Description` varchar(30) NOT NULL,
  PRIMARY KEY (`Id`),
  KEY (`ReportId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT 'SR_NOMFICHIER_EXPORT';

DROP TABLE IF EXISTS `REPORT_SEND`;
CREATE TABLE `REPORT_SEND` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `REPORT_ID` int(11) NOT NULL,
  `FTP_ACTIVE` int(11) NOT NULL DEFAULT 0,
  `FTP_HOST` varchar(100) DEFAULT NULL,
  `FTP_PORT` int(11) DEFAULT NULL,
  `FTP_USER` varchar(100) DEFAULT NULL,
  `FTP_PASSWORD` varchar(100) DEFAULT NULL,
  `EMAIL_ACTIVE` int(11) NOT NULL DEFAULT 0,
  `EMAIL_HOST` varchar(100) DEFAULT NULL,
  `EMAIL_PORT` int(11) DEFAULT NULL,
  `EMAIL_CHARSET` int(11) DEFAULT NULL,
  `EMAIL_USER` varchar(100) DEFAULT NULL,
  `EMAIL_PASSWORD` varchar(100) DEFAULT NULL,
  `EMAIL_PROTOCOL` varchar(100) DEFAULT NULL,
  `EMAIL_USE_SSL` int(11) NOT NULL DEFAULT 0,
  PRIMARY KEY (`ID`),
  KEY (`REPORT_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT 'SR_PARAMETRES_COMMUNICATION';

DROP TABLE IF EXISTS `SEND_EMAIL`;
CREATE TABLE `SEND_EMAIL` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `REPORT_ID` int(11) NOT NULL,
  `EMAIL_ADDRESS` varchar(255) DEFAULT NULL,
  `REQADRESSES` varchar(100) COMMENT '?',
  PRIMARY KEY (`ID`),
  KEY (`REPORT_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT 'SR_ADRESSES_EMAIL';

DROP TABLE IF EXISTS `ReportTreatment`;
CREATE TABLE `ReportTreatment` (
  `Id` int(11) NOT NULL AUTO_INCREMENT,
  `ReportId` int(11) NOT NULL,
  `ProcedureName` varchar(100) NOT NULL COMMENT '?',
  `ProcedureType` int(11) NOT NULL COMMENT '?',
  `Question` varchar(100) NOT NULL COMMENT '?',
  PRIMARY KEY (`Id`),
  KEY (`ReportId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT 'SR_TRAITEMENTS';


-- unknown table: SR_PARAMETRES_PROCEDURES










