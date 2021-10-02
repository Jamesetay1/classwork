-- 1) The student number and ssn of the student whose name is "Becky"
select snum,ssn from students where name = 'Becky';

-- 2)	The major name and major level of the student whose ssn is 123097834
select name,level from major where snum in (select snum FROM students where ssn = 123097834);

-- 3)	The names of all courses offered by the department of Computer Science
select name from courses where department_code in (select code from departments where name = 'Computer Science');

-- 4)	All degree names and levels offered by the department Computer Science
select name,level from degrees where department_code in (select code from departments where name = 'Computer Science');

-- 5)	The names of all students who have a minor
select name from students where snum in (select snum from minor);

-- 6)	The number of students who have a minor
select count(name) from students where snum in (select snum from minor);

-- 7)	The names and numbers of all students enrolled in course “Algorithm”
select name,snum from students where snum in (select snum from register where course_number in (select number from courses where name = 'Algorithm'));

-- 8)	The name and snum of the oldest student
select name,snum from students ORDER BY dob ASC LIMIT 1;

-- 9)	The name and snum of the youngest student
select name,snum from students ORDER BY dob DESC LIMIT 1;

-- 10)	The name, snum and SSN of the students whose name contains letter “n” or “N”
select name,S.snum,S.ssn from students S WHERE name LIKE '%n%' or '%N%';

-- 11)	The name, snum and SSN of the students whose name does not contain letter “n” or “N”
select name,snum,ssn from students WHERE name NOT LIKE '%n%' or '%N%';

-- 12)	The course number, name and the number of students registered for each course
-- Information needs to be together
SELECT name,course_number,count(*) as c FROM register R,courses C WHERE R.course_number = C.number GROUP BY course_number;

SELECT name from courses where number in (select course_number from register);

-- 13)	The name of the students enrolled in Fall2015 semester.
select name from students where snum in (select snum from register where regtime = 'Fall2015');

-- 14)	The course numbers and names of all courses offered by Department of Computer Science
select number,name from courses where department_code in (select code from departments where name = 'Computer Science');

-- 15)	The course numbers and names of all courses offered by either Department of Computer Science or Department of Landscape Architect.
select number,name from courses where department_code in (select code from departments where name = 'Computer Science') or department_code in (select code from departments where name = 'Landscape Architect');

