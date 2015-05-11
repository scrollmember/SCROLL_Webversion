--@author houbin

--create table pacall_type
CREATE TABLE `pacall_ptype` (
  `id` varchar(32) NOT NULL,
  `name` varchar(32) default NULL,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
insert into pacall_ptype (id,name) values ('68cb01c04f80444a9c171f5755e72287', 'manual');
insert into pacall_ptype (id,name) values ('b66fa632977245099912b73ea53b90f0', 'normal');
insert into pacall_ptype (id,name) values ('336b45eb477c43a589cf69c8bdb99327', 'duplicate');
insert into pacall_ptype (id,name) values ('3c0e578112364a22a8c115611c86987e', 'shake');
insert into pacall_ptype (id,name) values ('937867d548fd481e8c8e8d15ebd484ec', 'dark');

--//@UNDO
DROP TABLE IF EXISTS `pacall_ptype`