select  student.name, student.age, faculty.name from student
LEFT join faculty ON student.faculty_id = faculty.id
WHERE EXISTS (SELECT true FROM avatar WHERE avatar.student_id = student.id);
