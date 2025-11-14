ALTER TABLE student
ADD CONSTRAINT student_age
CHECK (age >= 16);

ALTER TABLE student
ADD CONSTRAINT name_unique UNIQUE (name);

ALTER TABLE student
ALTER COLUMN name SET NOT NULL;

ALTER TABLE faculty
ADD CONSTRAINT faculty_name_color UNIQUE (name, color);

ALTER TABLE student
ALTER COLUMN age SET DEFAULT 20;