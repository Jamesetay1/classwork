-- SQL Script for Generating Kitchen Data
-- Randy PatanasakPinyo and W. Tavanapong
-- drop database if exists `testuser`;

create database if not exists `testuser`;
use `testuser`;

-- SET FOREIGN_KEY_CHECKS=0;
drop table if exists recipe; 
drop table if exists food;
drop table if exists ingredient;

-- foodid, food name cannot be null, unique ID is food id
create table food (
fid int,  
fname varchar(45) not null, 
primary key(fid));

-- iid, ingredient name must be unique, primary key is iid
create table ingredient (
iid int,  
iname varchar(45) unique, 
primary key(iid));

-- foodid, food name cannot be null, -----m-----  -----m-----
create table recipe (
amount varchar(10),  
fid int,  
iid int,  
primary key(fid,iid),  
foreign key(fid) references food(fid) /*on delete cascade*/,  
foreign key(iid) references ingredient(iid));

INSERT INTO `food` VALUES (18,'Fried Rice'),(22,'Orange Chicken'),(25,'BBQ Pork'),
(26,'BBQ Chicken'),(27,'Fried Noodle'),(30,'Mongolian Beef'),(32,'Pad Thai');

INSERT INTO `ingredient` VALUES (19,'Jasmine Rice'),(20,'Egg'),(21,'Pork'),(23,'Chicken'),
(24,'Orange'),(28,'Green Onion'),(29,'Egg Noodle'),(31,'Beef'),(33,'Rice Noodle'),(34,'Tofu'),(35,'Shrimp');

INSERT INTO `recipe` VALUES ('200g',18,19),('50g',18,20),('100g',18,21),('250g',22,23),
('100g',22,24),('300g',25,21),('250g',26,23),('200g',27,23),('60g',27,28),('250g',27,29),
('50g',30,28),('300g',30,31),('200g',32,33),('50g',32,34),('100g',32,35);

-- wrong insertions, fail for various reasons
Insert into food(fid,fname) values ('fid','fried Rice');
Insert into food(fid,fname) values (18,'brown rice');
Insert into food values ('Fried Rice');

-- select the records in recipe table order by amount attribute limit the output to 5
select * from recipe order by fid; -- desc limit 5;

-- select food information of BBQ Chicken
select fid,fname from food where fname = 'BBQ Chicken';

-- count the number of recipe contains fried rice
select count(r.fid) from recipe r,food f where f.fid = r.fid and f.fname = 'fried rice';
select count(r.fid) from recipe r where r.fid in (select fid from food where fname = 'fried rice');

-- aggregation function: count min max avg sum
-- output the quantity of recipe group by fid
select count(r.fid) as fidNum, f.fname from recipe r, food f where f.fid = r.fid group by r.fid;
-- delete the recipe with fid = 18 and iid = 19
select * from recipe where fid = 18 and iid = 19;
delete from recipe where fid = 18 and iid = 19;
-- delete the record of fried rice in food table 
select * from food where fname = 'fried rice';
delete from food where fname = 'fried rice';
-- update the food name to be brown rice where the fid is 22
select * from food where fid = 22;
update food set fname = 'brown rice' where fid = 22; 