CREATE DATABASE Campus_Management_System;
USE Campus_Management_System;


CREATE TABLE Student (
    student_id INT PRIMARY KEY,
    name VARCHAR(100),
    email VARCHAR(100),
    phone VARCHAR(15),
    dob DATE,
    gender VARCHAR(10),
    department VARCHAR(50),
    year_of_study INT
);

INSERT INTO Student VALUES
(1, 'Alice Johnson', 'alice.j@univ.edu', '9876543210', '2002-05-15', 'Female', 'CSE', 2),
(2, 'Bob Smith', 'bob.s@univ.edu', '9123456789', '2001-10-02', 'Male', 'ECE', 3),
(3, 'Charlie Lee', 'charlie.l@univ.edu', '9988776655', '2003-03-22', 'Male', 'ME', 1),
(4, 'Diana Rose', 'diana.r@univ.edu', '9090909090', '2002-12-30', 'Female', 'EEE', 2),
(5, 'Ethan Black', 'ethan.b@univ.edu', '9012345678', '2001-08-08', 'Male', 'CSE', 4),
(6, 'Fiona White', 'fiona.w@univ.edu', '9000112233', '2003-01-18', 'Female', 'CSE', 1);




CREATE TABLE Course (
    course_id INT PRIMARY KEY,
    course_name VARCHAR(100),
    credits INT,
    department VARCHAR(50),
    semester INT,
    faculty_id INT,
    FOREIGN KEY (faculty_id) REFERENCES Faculty(faculty_id)
);

INSERT INTO Course VALUES
(201, 'Data Structures', 4, 'CSE', 2, 101),
(202, 'Digital Circuits', 3, 'ECE', 3, 102),
(203, 'Thermodynamics', 4, 'ME', 1, 103),
(204, 'Power Systems', 3, 'EEE', 2, 104),
(205, 'Algorithms', 4, 'CSE', 4, 105),
(206, 'Embedded Systems', 3, 'ECE', 4, 106);




CREATE TABLE Faculty (
    faculty_id INT PRIMARY KEY,
    name VARCHAR(100),
    email VARCHAR(100),
    department VARCHAR(50),
    designation VARCHAR(50)
);

INSERT INTO Faculty VALUES
(101, 'Dr. Ramesh Kumar', 'ramesh.k@univ.edu', 'CSE', 'Professor'),
(102, 'Dr. Sita Mehra', 'sita.m@univ.edu', 'ECE', 'Associate Professor'),
(103, 'Dr. Arjun Dev', 'arjun.d@univ.edu', 'ME', 'Assistant Professor'),
(104, 'Dr. Kavita Rao', 'kavita.r@univ.edu', 'EEE', 'Professor'),
(105, 'Dr. Rohit Jain', 'rohit.j@univ.edu', 'CSE', 'Lecturer'),
(106, 'Dr. Sneha Singh', 'sneha.s@univ.edu', 'ECE', 'Associate Professor');




CREATE TABLE Enrollment (
    enrollment_id INT PRIMARY KEY,
    student_id INT,
    course_id INT,
    enrolled_on DATE,
    FOREIGN KEY (student_id) REFERENCES Student(student_id),
    FOREIGN KEY (course_id) REFERENCES Course(course_id)
);

INSERT INTO Enrollment VALUES
(301, 1, 201, '2024-07-01'),
(302, 2, 202, '2024-07-01'),
(303, 3, 203, '2024-07-01'),
(304, 4, 204, '2024-07-01'),
(305, 5, 205, '2024-07-01'),
(306, 1, 205, '2024-07-01'),
(307, 6, 201, '2024-07-01');



CREATE TABLE Notification (
    notification_id INT PRIMARY KEY,
    student_id INT,
    message TEXT,
    type VARCHAR(20),  
    generated_by VARCHAR(20),  
    timestamp DATETIME,
    FOREIGN KEY (student_id) REFERENCES Student(student_id)
);

INSERT INTO Notification VALUES
(401, 1, 'Your class for Algorithms is rescheduled.', 'info', 'manual', '2025-06-15 10:00:00'),
(402, 2, 'Reminder: Digital Circuits midterm tomorrow.', 'reminder', 'genAI', '2025-06-14 09:00:00'),
(403, 3, 'Thermodynamics lab starts next week.', 'alert', 'manual', '2025-06-13 08:30:00'),
(404, 4, 'Power Systems assignment is due.', 'info', 'manual', '2025-06-12 11:15:00'),
(405, 5, 'Algorithms viva scheduled.', 'reminder', 'genAI', '2025-06-11 13:45:00'),
(406, 1, 'Welcome to Semester 4!', 'info', 'manual', '2025-06-10 08:00:00');




CREATE TABLE Timetable (
    timetable_id INT PRIMARY KEY,
    course_id INT,
    faculty_id INT,
    day_of_week VARCHAR(15),
    start_time TIME,
    end_time TIME,
    room_no VARCHAR(20),
    FOREIGN KEY (course_id) REFERENCES Course(course_id),
    FOREIGN KEY (faculty_id) REFERENCES Faculty(faculty_id)
);

INSERT INTO Timetable VALUES
(501, 201, 101, 'Monday', '09:00:00', '10:30:00', 'CSE101'),
(502, 202, 102, 'Tuesday', '11:00:00', '12:30:00', 'ECE202'),
(503, 203, 103, 'Wednesday', '10:00:00', '11:30:00', 'ME303'),
(504, 204, 104, 'Thursday', '13:00:00', '14:30:00', 'EEE404'),
(505, 205, 105, 'Friday', '09:00:00', '10:30:00', 'CSE505'),
(506, 206, 106, 'Monday', '14:00:00', '15:30:00', 'ECE606');






-- Index Use Cases

-- Use Case 1: Index on Enrollment(student_id)
-- Use Case: As a student, I want to quickly retrieve all courses I am enrolled in to view my current academic plan.
-- Why Index Helps: Adding an index on Enrollment(student_id) speeds up SELECT queries filtering by student ID, which are common when displaying student-specific course details.

-- SQL:
CREATE INDEX idx_enrollment_student_id ON Enrollment(student_id);


-- Use Case 2: Index on Timetable(faculty_id)
-- Use Case: As a faculty member, I want to load my weekly teaching schedule promptly when I log in to the system.
-- Why Index Helps: Faculty dashboards often fetch timetable data using faculty_id. Indexing this column helps return results faster for timetable-related queries.

-- SQL:
CREATE INDEX idx_timetable_faculty_id ON Timetable(faculty_id);



--View Use Cases

--Use Case 1: View - StudentCourseView
-- Use Case: As a student, I want to see all my enrolled courses along with course name, faculty, and department in a single screen.
-- Benefit: Encapsulates a multi-table join in a single logical view, so application developers or reporting tools can use SELECT * FROM StudentCourseView instead of writing complex joins.

-- SQL:
CREATE VIEW StudentCourseView AS
SELECT
s.student_id,
s.name AS student_name,
c.course_id,
c.course_name,
c.department,
f.name AS faculty_name
FROM Enrollment e
JOIN Student s ON s.student_id = e.student_id
JOIN Course c ON c.course_id = e.course_id
JOIN Faculty f ON f.faculty_id = c.faculty_id;


-- Use Case 2: View - FacultyTimetableView
-- Use Case: As a faculty member, I want to view my weekly timetable with course, time, and room in an organized way.
-- Benefit: Simplifies access to multiple timetable attributes without the need to perform joins in every faculty portal page or backend logic.

-- SQL:
CREATE VIEW FacultyTimetableView AS
SELECT
f.faculty_id,
f.name AS faculty_name,
c.course_name,
t.day_of_week,
t.start_time,
t.end_time,
t.room_no
FROM Timetable t
JOIN Course c ON c.course_id = t.course_id
JOIN Faculty f ON f.faculty_id = t.faculty_id;
