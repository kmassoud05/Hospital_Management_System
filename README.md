# ProCare Clinic

Desktop hospital management software built with Java Swing, MySQL, and Maven. ProCare Clinic provides a focused workspace for clinic staff to manage authentication, doctors, patients, and generated PDF records.

![Java](https://img.shields.io/badge/Java-25-orange?logo=openjdk&logoColor=white)
![Maven](https://img.shields.io/badge/Maven-3.9%2B-C71A36?logo=apachemaven&logoColor=white)
![MySQL](https://img.shields.io/badge/MySQL-8.0%2B-4479A1?logo=mysql&logoColor=white)
![License](https://img.shields.io/badge/license-MIT-green)

## Overview

ProCare Clinic is a desktop application for small healthcare teams. It opens with a login screen and provides role-aware workflows for clinic operations through a Java Swing interface.

### Highlights

- Securely structured login and user validation flow
- Doctor records with specialty, status, and contact details
- Patient management workflows
- Database initialization for users, doctors, and patients
- PDF generation for clinic and doctor records
- Date selection with JCalendar
- Maven packaging with dependencies included in the executable JAR

## Technology

| Area | Technology |
| --- | --- |
| Language and runtime | Java 25 LTS |
| User interface | Java Swing |
| Build | Apache Maven |
| Database | MySQL 8.0+ |
| Database driver | MySQL Connector/J 8.0.33 |
| PDF generation | iText 5.5.13.3 |
| Date picker | JCalendar 1.4 |

## Requirements

Install the following before running the application:

- JDK 25 or newer
- Maven 3.9 or newer
- MySQL Server 8.0 or newer
- Git, if cloning the repository
```bash
git clone https://github.com/kmassoud05/Hospital_Management_System.git
```sql
CREATE DATABASE hospitalmgmt;
```java
private static final String URL = "jdbc:mysql://localhost:3306/hospitalmgmt?useSSL=false";
private static final String USERNAME = "your_mysql_user";
private static final String PASSWORD = "your_mysql_password";
```

Do not commit real passwords, production connection strings, or other secrets to GitHub. A future improvement should move these values to environment variables or a local configuration file.

### 4. Build the application

```bash
```bash
java -jar target/Hospital_Management_System-1.0-SNAPSHOT-jar-with-dependencies.jar

Open the repository root, the folder containing `pom.xml`, in your IDE.

Use this main class:

```text
hospital_management_system.Hospital_Management_System
```

In VS Code, select **Run Hospital Management System** from `.vscode/launch.json`. In NetBeans, run the project or run `Hospital_Management_System.java` as the main class.

## Project Structure

```text
Hospital_Management_System/
├── database/                 # Optional database SQL scripts
├── src/main/java/            # Application source code
│   └── hospital_management_system/
│       ├── Login.java        # Login screen and startup flow
│       ├── Home_Page.java    # Main application dashboard
│       ├── DatabaseConnection.java
│       ├── DatabaseSetup.java
│       ├── DatabaseOperations.java
│       ├── Patient_System.java
│       ├── Doctor_System.java
│       └── PDFGenerator.java
├── .vscode/                  # VS Code launch configuration
├── pom.xml                   # Maven build and dependencies
└── README.md
```

## Useful Commands

```bash
# Compile production and test sources
mvn clean test-compile

# Run the test suite
mvn test

# Create the executable JAR
mvn clean package
```

## Troubleshooting

### The application does not open

Confirm that you are running the assembled JAR or the configured main class, and verify that JDK 25 is selected by your IDE.

### Database connection errors

Check that MySQL is running, the `hospitalmgmt` database exists, and the credentials in `DatabaseConnection.java` are correct. Also verify that port `3306` is available.

### Maven cannot be found

Install Maven and confirm that `mvn --version` works. The project uses Maven for dependency resolution and packaging.

## Security Notice

Before making this repository public, rotate any database password that may previously have been committed and replace hard-coded credentials with environment variables or local configuration. Never publish credentials in source code, documentation, screenshots, or SQL scripts.

## Status

The project builds and tests successfully with Java 25. The application still requires a configured MySQL instance for login and database-backed workflows.

## License

This project is licensed under the [MIT License](LICENSE).

Third-party dependencies retain their respective licenses. See the dependency projects for their licensing terms.

