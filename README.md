# Library Management System

## Overview
The Library Management System (LMS) is a Java-based application 
it is designed to manage library operations, including adding books, borrowing books, returning books, and managing user accounts.
The system leverages a MySQL database to store information about books and users.

## Project Structure
The project is organized into the following packages:
- **DAOInterface**: Contains interfaces for Data Access Objects (DAOs).
- **DAOimplement**: Contains implementations of the DAOs.
- **database**: Contains database connection configuration.
- **enums**: Contains enumerations used in the project.
- **model**: Contains model classes representing data entities.

## Key Classes and Interfaces

### DAOInterface

- **BookDAO**
    - Methods: `addBook`, `borrowBook`, `returnBook`, `findBookById`, `findBookByTitle`, `listBooks`, `listBooksOfCategory`

- **UserDAO**
    - Methods: `addUser`, `getUserByUsername`, `login`

### DAOimplement

- **BookDAOImpl**
    - Implements `BookDAO` interface with methods for adding, borrowing, returning books, and querying the database for book details.

- **UserDaoImpl**
    - Implements `UserDAO` interface with methods for adding users, retrieving user details, and user authentication.

### database

- **Database**
    - Manages the connection to the MySQL database, ensuring a singleton connection instance is used throughout the application.

### enums

- **UserRole**
    - Enumeration for user roles, supporting `admin` and `user` roles.

### model

- **Book**
    - Represents a book entity with properties like `id`, `title`, `authorId`, `category`, and `userId`.

- **User**
    - Represents a user entity with properties like `id`, `username`, `password`, and `userRole`.

## Database Configuration

Ensure your MySQL server is running and create a database named `LibraryManagementSystem`. Update the `url`, `userName`, and `password` fields in the `Database` class to match your MySQL server configuration.

## How to Run

1. **Set Up Database**: Ensure you have a MySQL database named `LibraryManagementSystem`.
2. **Dependencies**: Make sure you have the necessary JDBC driver for MySQL in your classpath.
3. **Compile and Run**: Compile the Java files and run the application using your preferred method (e.g., IDE like IntelliJ IDEA or Eclipse, or via the command line).
