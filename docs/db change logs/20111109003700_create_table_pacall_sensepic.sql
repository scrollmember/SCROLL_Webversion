--@author houbin

--create table pacall_sensepic
CREATE TABLE `pacall_sensepic` (
  `id` varchar(32) NOT NULL,
  `folder_id` varchar(32) NOT NULL,
  `cam` varchar(128) NOT NULL,
  `index` int(11) default NULL,
  `reason` varchar(32) default NULL,
  `date` timestamp NOT NULL default CURRENT_TIMESTAMP on update CURRENT_TIMESTAMP,
  `clr` double default NULL,
  `tmp` double default NULL,
  `accx` double default NULL,
  `accy` double default NULL,
  `accz` double default NULL,
  `magx` double default NULL,
  `magy` double default NULL,
  `magz` double default NULL,
  `samepicid` varchar(32) default NULL,
  PRIMARY KEY  (`id`),
  KEY `fk_sensepic_folder` (`folder_id`),
  KEY `fk_same_pic_id` (`samepicid`),
  CONSTRAINT `fk_same_pic_id` FOREIGN KEY (`samepicid`) REFERENCES `pacall_sensepic` (`id`) ON DELETE SET NULL ON UPDATE CASCADE,
  CONSTRAINT `fk_sensepic_folder` FOREIGN KEY (`folder_id`) REFERENCES `pacall_folder` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--//@UNDO
DROP TABLE IF EXISTS `pacall_sensepic`