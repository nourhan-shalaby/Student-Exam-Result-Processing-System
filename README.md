
# Student Exam Result Processing System

![Java](https://img.shields.io/badge/Java-17-blue)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.x-green)
![Spring Batch](https://img.shields.io/badge/Spring%20Batch-5.x-orange)
![H2 Database](https://img.shields.io/badge/H2-Database-lightgrey)

A Spring Batch application to process student exam results from a CSV file, calculate grades, and store the results in a database. The project demonstrates batch processing with multi-threading for parallel record processing and includes basic error handling for invalid data.

------

## Features

- **CSV File Processing**: Reads student data (roll number, name, marks) from a CSV file.
- **Grade Calculation**: Calculates grades based on marks (A, B, C, D).
- **Multi-threading**: Processes student records in parallel using Spring Batch's `TaskExecutor`.
- **Database Storage**: Saves processed results (including grades) in a database.
- **Error Handling**: Logs invalid records (e.g., missing marks or invalid roll numbers).

---

## Technologies Used

- **Java 17**
- **Spring Boot 3.x**
- **Spring Batch 5.x**
- **H2 Database** (or MySQL/PostgreSQL)
- **Mockaroo**: Generate Fake Student Data
- **Maven** (for dependency management)

---

## Prerequisites

Before running the project, ensure you have the following installed:

- Java 17 or higher
- Maven 3.x
- An IDE (e.g., IntelliJ IDEA, Eclipse,Netbeans)

---

## Getting Started

### 1. Clone the Repository

```bash
git clone https://github.com/nourhan-shalaby/Student-Exam-Result-Processing-System.git
cd Student-Exam-Result-Processing-System
