--@author houbin

--create table pacall_sensepic
CREATE TABLE `pacall_sensedata` (
  `id` varchar(32) NOT NULL,
  `folder_id` varchar(32) NOT NULL,
  `index` int(11) NOT NULL,
  `key` varchar(32) NOT NULL,
  `date` timestamp NOT NULL default CURRENT_TIMESTAMP on update CURRENT_TIMESTAMP,
  `v1` varchar(128) default NULL,
  `v2` varchar(128) default NULL,
  `v3` varchar(128) default NULL,
  `v4` varchar(128) default NULL,
  PRIMARY KEY  (`id`),
  KEY `uk_sensedata` (`folder_id`,`index`),
  CONSTRAINT `fk_sensedata_folder` FOREIGN KEY (`folder_id`) REFERENCES `pacall_folder` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--//@UNDO
DROP TABLE IF EXISTS `pacall_sensedata`