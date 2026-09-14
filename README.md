# 🏥 ProCare Clinic

The Hospital Management System is a Java desktop application designed to help clinics and hospitals manage administrative and medical records from one central interface. Branded as PROCARE CLINIC, I built this desktop client to connect to a MySQL database to handle staff authentication, patient management, and hospital statistics.

---

### 📦 Technologies

*   Java 25 LTS
*   Java Swing
*   MySQL 8.0+ / JDBC
*   Apache Maven
*   iText 5.5.13.3
*   JCalendar 1.4

---

### 🦄 Features

*   **Patient Management:** Creates new patient records, updates medical conditions, and searches by name or ID using full CRUD functionality.
*   **Doctor and Staff Management:** Tracks physician availability, duty status, and specialties like Cardiology or Pediatrics, with role-based access for administrators.
*   **Hospital Statistics:** Displays live data on total patients, total doctors, and doctors currently on duty using background threads for responsive UI updates.
*   **PDF Reporting:** Generates timestamped PDF reports for patient and staff records using iText.
*   **Authentication & Roles:** Validates users via MySQL, restricting certain create/delete actions based on ADMIN or DOCTOR roles.

---

### 🧱 The Process

I started by designing the presentation layer with Java Swing, creating screens for the Login, Home Page, Patient System, and Doctor System. I integrated JCalendar to support date-related UI functionality for patient and doctor records.

Next, I focused on the data access layer by setting up a MySQL database named `hospitalmgmt`. I wrote DatabaseConnection and DatabaseOperations classes to manage JDBC connections and execute SQL statements for persistent storage.

To ensure the UI remained responsive, I implemented background SwingWorkers to handle tasks like user validation and live statistics loading. I also built role-check logic directly into the Swing interface to separate Administrator capabilities from general Doctor access.

Finally, I used Apache Maven to handle dependencies and packaging, and integrated iText to allow administrators to export timestamped PDF reports. The application even automatically creates the required tables at startup to ensure a smooth deployment.

---

### 📚 What I Learned

*   🖥️ **Desktop GUI Development:** Building the presentation layer taught me how to manage Swing screens, dialogs, and custom loading feedback.
*   🗄️ **Database Integration:** I learned how to connect Java to MySQL using JDBC Connector/J to execute CRUD operations.
*   ⚙️ **Background Tasks:** Using SwingWorker and SwingUtilities.invokeLater helped me understand how to load live statistics without freezing the user interface.
*   🔐 **Role-Aware Logic:** Implementing specific access rules taught me how to handle user states and enforce conditional application logic.

---

### 💭 How can it be improved?

*   Implement modern password-hashing algorithms such as BCrypt or Argon2.
*   Add a full automated JUnit and integration test suite.
*   Add appointments, prescriptions, billing, and audit logs.
*   Transition database credentials to use environment variables or secure configuration.

---

### 🚦 Running the Project

1. Install JDK 25 or newer, Maven 3.9 or newer, and MySQL 8.0 or newer.
2. Create the database in MySQL by running `CREATE DATABASE hospitalmgmt;`.
3. Clone the repository using `git clone https://github.com/kmassoud05/Hospital_Management_System.git` and enter the directory.
4. Run `mvn clean package` to build the project.
5. Launch the application by running `java -jar target/Hospital_Management_System-1.0-SNAPSHOT-jar-with-dependencies.jar`.

---

## 🍿 Video

https://github.com/user-attachments/assets/be0c4bcb-342e-4d5a-afb7-a2834909ddb0
