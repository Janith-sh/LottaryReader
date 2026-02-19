# Lottery Reader - Web Scraping Application

A Java-based web scraping application that extracts Jayoda lottery results from the Development Lotteries Board website and stores them in a MySQL database.

## 📋 Project Overview

This application scrapes lottery data from `https://www.dlb.lk/result/6/` and specifically filters **Jayoda lottery** results, storing them in a structured MySQL database table.

## 🛠️ Technologies Used

- **Java 21**
- **Maven** (Build Tool)
- **Jsoup 1.17.2** (Web Scraping Library)
- **MySQL Connector/J 8.3.0** (Database Driver)
- **MySQL Database**

## 📦 Prerequisites

Before running this project, ensure you have:

1. **Java Development Kit (JDK) 21** or higher installed
2. **Apache Maven 3.9.12** or higher installed
3. **Internet connection** (to scrape website data)
4. **MySQL database access** (credentials provided in configuration)

## 🗄️ Database Configuration

- **Host:** `13.234.242.165:3306`
- **Database Name:** `a1`
- **Table Name:** `newResult_71`
- **Username:** `iDevice`
- **Password:** `iDevice_123456`

### Database Schema

The application creates a table `newResult_71` with the following structure:

| Column Name | Data Type | Description |
|-------------|-----------|-------------|
| id | INT (Primary Key, Auto Increment) | Unique identifier |
| lottery_name | VARCHAR(100) | Name of the lottery (always "Jayoda") |
| draw_number | INT | Draw number |
| winning_numbers | VARCHAR(200) | Winning lottery numbers |
| draw_date | VARCHAR(50) | Date of the draw |
| scraped_timestamp | TIMESTAMP | Timestamp when data was scraped |

**Unique Constraint:** Combination of `(lottery_name, draw_number)` to prevent duplicate entries.

## 🚀 How to Run the Project

### Option 1: Using Maven (Recommended)

1. **Navigate to project directory:**
   ```bash
   cd C:\PERSONAL\Projects\LottryReader
   ```

2. **Compile and run the application:**
   ```bash
   mvn compile exec:java -Dexec.mainClass="com.example.App"
   ```

### Option 2: Build and Run JAR

1. **Build the project:**
   ```bash
   mvn clean package
   ```

2. **Run the JAR file:**
   ```bash
   java -jar target/LottryReader-1.0-SNAPSHOT.jar
   ```

## 📊 Output

When the application runs successfully, you will see output similar to:

```
Step 1: Scraping lottery data...
Page Title: Development Lotteries Board
================================

Step 2: Setting up database...
Connected to database 'a1'
Table 'newResult_71' created/verified

Step 3: Parsing and storing Jayoda lottery results...
Stored: Jayoda #2174 | Date: 2025-Oct-29 Wednesday | Numbers: V 15 30 54 55

================================
Jayoda lottery data successfully stored in database!
Total Jayoda records processed: X
```

## 📁 Project Structure

```
LottryReader/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/
│   │   │       └── example/
│   │   │           └── App.java          # Main application class
│   │   └── resources/                     # Resources folder
│   └── test/
│       └── java/
│           └── com/
│               └── example/
│                   └── AppTest.java       # Test class
├── pom.xml                                # Maven configuration
├── .gitignore                             # Git ignore file
└── README.md                              # This file
```

## 🔧 Configuration

To modify database credentials or scraping URL, edit the constants in `App.java`:

```java
private static final String DB_HOST = "jdbc:mysql://13.234.242.165:3306/";
private static final String DB_NAME = "a1";
private static final String TABLE_NAME = "newResult_71";
private static final String DB_USER = "iDevice";
private static final String DB_PASS = "iDevice_123456";
```

## 🌐 GitHub Repository

Repository URL: `https://github.com/Janith-sh/LottaryReader.git`

### Branches:
- `main` - Initial project setup
- `Q2` - Question 2 implementation
- `Q3` - Question 3 implementation (current work)

## ⚠️ Troubleshooting

### Maven not recognized
If you get "mvn is not recognized" error, Maven is not installed or not in PATH. The application will attempt to use the installed version at:
```
C:\Users\CHAMA COMPUTERS\.maven\maven-3.9.12\bin
```

### Timeout Errors
If you encounter `SocketTimeoutException`, the website may be slow. The application uses a 30-second timeout which should be sufficient in most cases.

### Database Connection Errors
Ensure you have network access to the remote MySQL server at `13.234.242.165:3306` and that the credentials are correct.

## 👨‍💻 Author

Project developed as part of a web scraping assignment for lottery data extraction and database storage.

## 📝 Assignment Information

This project demonstrates:
- Web scraping using Jsoup
- HTML parsing and data extraction
- MySQL database integration
- Structured data storage
- Error handling and connection management

## 📄 License

This project is for educational purposes.

---

**Last Updated:** February 19, 2026
