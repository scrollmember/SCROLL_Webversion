--@author houbin

--create table r_users_interest
CREATE TABLE `r_users_interest` (
`user_id`  varchar(32) NULL ,
`interest_id`  varchar(32) NULL ,
PRIMARY KEY (`user_id`, `interest_id`),
CONSTRAINT `fk_user_interest_user` FOREIGN KEY (`user_id`) REFERENCES `t_users` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
CONSTRAINT `fk_user_interest_interest` FOREIGN KEY (`interest_id`) REFERENCES `t_interest` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
)ENGINE=InnoDB DEFAULT CHARSET=utf8;

--//@UNDO
DROP TABLE IF EXISTS `r_users_interest`