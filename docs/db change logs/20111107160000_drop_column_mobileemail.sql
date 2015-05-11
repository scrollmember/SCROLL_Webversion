--@type Database Change Log
--@author houbin
--@Date 2011/11/07
--drop column mobile_email
alter table t_users drop column mobile_email

--//@UNDO
alter table t_users add column mobile_email