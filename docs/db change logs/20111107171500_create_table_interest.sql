--@author houbin

--create table t_interest
CREATE TABLE `t_interest` (
`id`  varchar(32) NOT NULL ,
`name`  varchar(1024) NULL ,
PRIMARY KEY (`id`)
)ENGINE=InnoDB DEFAULT CHARSET=utf8;


--//@UNDO
DROP TABLE IF EXISTS `t_interest`