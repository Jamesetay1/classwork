-- delete before submission (or ignore if I forgot)
select * FROM students order by snum; 
select * FROM departments order by code; 
select * FROM degrees order by department_code;
select * FROM major order by snum; 
select * FROM minor order by snum DESC; 
select * FROM courses order by department_code;
select * FROM register order by snum;  
-- end delete area

-- 1)	Change the name of the student with ssn = 746897816 to Scott
UPDATE students SET name = 'Scott' WHERE ssn = 746897816;

-- 2)	Change the major of the student with ssn = 746897816 to Computer Science, Master. 
UPDATE major SET name = 'Computer Science',level = 'MS' WHERE snum in(select snum from students where ssn = 746897816) AND snum!=0;

-- 3)	Delete all registration records that were in “Spring2015”,
DELETE FROM register WHERE regtime = 'Fall2015' AND snum!=0;