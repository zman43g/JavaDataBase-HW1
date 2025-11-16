-- liquibase formatted sql

-- changeset sfilimonov:1
CREATE INDEX student_name_index ON student (name);

-- changeset sfilimonov:2
CREATE INDEX faculty_name_and_color ON faculty (name,color);