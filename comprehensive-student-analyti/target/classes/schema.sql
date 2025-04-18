/* 
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Other/SQLTemplate.sql to edit this template
 */
/**
 * Author:  Lenovo
 * Created: Apr 17, 2025
 */




-- Schema initialized for Comprehensive Student Analytics
DROP TABLE IF EXISTS student_results;
CREATE TABLE student_results (
    studentId VARCHAR(50),
    name VARCHAR(100),
    subject VARCHAR(50),
    marks INT,
    grade VARCHAR(2)
);

DROP TABLE IF EXISTS advanced_stats;
CREATE TABLE advanced_stats (
    subject VARCHAR(50),
    average_marks DOUBLE,
    passRate DOUBLE,
    gradeACount INT,
    gradeBCount INT,
    gradeCCount INT,
    gradeDCount INT,
    gradeFCount INT,
    topStudents VARCHAR(500),
    lowestStudents VARCHAR(500),
    outliers VARCHAR(500)
);

DROP TABLE IF EXISTS error_log;
CREATE TABLE error_log (
    id INT AUTO_INCREMENT PRIMARY KEY,
    studentId VARCHAR(50),
    error_message VARCHAR(500),
    timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);