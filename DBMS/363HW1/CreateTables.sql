drop table if exists minor;
drop table if exists register;
drop table if exists major;
drop table if exists courses;
drop table if exists degrees;
drop table if exists departments;
drop table if exists students; 

create table students(
snum integer unique,
ssn integer,  
name varchar(10),
gender varchar(1),
dob datetime,
c_addr varchar(20),
c_phone varchar(20),
p_addr varchar(20),
p_phone varchar(10),  
primary key(ssn));

create table departments(
code integer,
name varchar(50) unique,
phone varchar(10),
college varchar(20),
primary key(code));

create table degrees(
name varchar(50), 
level varchar(5),
department_code integer,
primary key(name, level),
foreign key(department_code) references departments(code) ON DELETE CASCADE);

create table courses(
number integer,
name varchar(50) unique,
description varchar(50),
credithours integer,
level varchar(20),
department_code integer,
primary key (number),
foreign key(department_code) references departments(code) ON DELETE CASCADE);

create table register(
snum integer,
course_number integer,
regtime varchar(20),
grade integer,
primary key(snum, course_number),
foreign key(snum) references students(snum) ON DELETE CASCADE,
foreign key(course_number) references courses(number) ON DELETE CASCADE);

create table major(
snum integer,
name varchar(50),
level varchar(5),
primary key(snum, name, level),
foreign key(snum) references students(snum) ON DELETE CASCADE,
foreign key(name, level) references degrees(name, level) ON DELETE CASCADE);

create table minor(
snum integer,
name varchar(50),
level varchar(5),
primary key(snum, name, level),
foreign key(snum) references students(snum) ON DELETE CASCADE,
foreign key(name, level) references degrees(name, level) ON DELETE CASCADE);
