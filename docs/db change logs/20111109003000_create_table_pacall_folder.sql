--@author houbin

--create table pacall_folder
CREATE TABLE `pacall_folder` (
  `id` varchar(32) NOT NULL,
  `name` varchar(128) NOT NULL,
  `hash` varchar(40) default NULL,
  `last_modified` timestamp NULL default NULL,
  `createtime` timestamp NULL default NULL,
  `user_id` varchar(32) NOT NULL default '',
  PRIMARY KEY  (`id`),
  KEY `fk_folder_user` (`user_id`),
  CONSTRAINT `fk_folder_user` FOREIGN KEY (`user_id`) REFERENCES `t_users` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
--//@UNDO
DROP TABLE IF EXISTS `pacall_folder`