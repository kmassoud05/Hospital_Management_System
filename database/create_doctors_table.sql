CREATE TABLE IF NOT EXISTS doctors (
    staff_id INT AUTO_INCREMENT PRIMARY KEY,
    first_name VARCHAR(50) NOT NULL,
    last_name VARCHAR(50) NOT NULL,
    phone_number VARCHAR(20),
    date_of_birth DATE,
    gender VARCHAR(1),
    age INT,
    height INT,
    specialty VARCHAR(50),
    status VARCHAR(20) DEFAULT 'Off Duty'
);
