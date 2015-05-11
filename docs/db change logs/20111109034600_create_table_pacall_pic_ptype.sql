--@author houbin

--create table pacall_pic_ptype

CREATE TABLE `pacall_pic_ptype` (
  `sensepic_id` varchar(32) NOT NULL default '',
  `ptype_id` varchar(32) NOT NULL default '',
  PRIMARY KEY  (`sensepic_id`,`ptype_id`),
  KEY `fk_pic_ptype_ptype` (`ptype_id`),
  CONSTRAINT `fk_pic_ptype_ptype` FOREIGN KEY (`ptype_id`) REFERENCES `pacall_ptype` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_pic_ptype_pic` FOREIGN KEY (`sensepic_id`) REFERENCES `pacall_sensepic` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


--//@UNDO
DROP TABLE IF EXISTS `pacall_pic_ptype`
