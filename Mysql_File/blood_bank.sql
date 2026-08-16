-- ============================================================
-- BLOOD BANK AND DONOR MANAGEMENT SYSTEM
-- Database: blood_bank
-- ============================================================

CREATE DATABASE IF NOT EXISTS blood_bank;

USE blood_bank;

-- ============================================================
-- 1. TABLES
-- ============================================================

-- Admin Table
CREATE TABLE Admin
(
    Username VARCHAR(50) PRIMARY KEY,
    Password VARCHAR(100) NOT NULL
);

-- Hospital Table
CREATE TABLE Hospital
(
    HospitalID VARCHAR(10) PRIMARY KEY,
    HospitalName VARCHAR(100) NOT NULL,
    Email VARCHAR(100) NOT NULL UNIQUE,
    Password VARCHAR(100) NOT NULL,
    Phone VARCHAR(15) NOT NULL,
    City VARCHAR(50) NOT NULL
);

-- Blood Stock Table
CREATE TABLE Blood_Stock
(
    BloodGroup VARCHAR(50) PRIMARY KEY,
    UnitsAvailable INT NOT NULL,
    LastUpdated DATE NOT NULL
);

-- Donor Table
CREATE TABLE Donor
(
    DonorID VARCHAR(10) PRIMARY KEY,
    Name VARCHAR(100) NOT NULL,
    Age INT NOT NULL,
    Gender VARCHAR(20) NOT NULL,
    BloodGroup VARCHAR(50) NOT NULL,
    Mobile VARCHAR(15) NOT NULL,
    City VARCHAR(50) NOT NULL,
    LastDonationDate DATE NOT NULL,

    CONSTRAINT fk_bloodgroup1
    FOREIGN KEY (BloodGroup)
    REFERENCES Blood_Stock(BloodGroup)
    ON UPDATE CASCADE
    ON DELETE RESTRICT
);

-- Emergency Request Table
CREATE TABLE Emergency_Request
(
    RequestID VARCHAR(10) PRIMARY KEY,
    HospitalID VARCHAR(10) NOT NULL,
    BloodGroup VARCHAR(50) NOT NULL,
    UnitsRequired INT NOT NULL,
    RequestDate DATE NOT NULL,
    Priority VARCHAR(20) NOT NULL,
    Status VARCHAR(20) NOT NULL,

    CONSTRAINT fk_hospital1
    FOREIGN KEY (HospitalID)
    REFERENCES Hospital(HospitalID)
    ON UPDATE CASCADE
    ON DELETE RESTRICT,

    CONSTRAINT fk_bloodgroup2
    FOREIGN KEY (BloodGroup)
    REFERENCES Blood_Stock(BloodGroup)
    ON UPDATE CASCADE
    ON DELETE RESTRICT
);

-- ============================================================
-- 2. TRIGGERS
-- ============================================================

DELIMITER //

-- Check that blood stock never becomes negative
CREATE TRIGGER CheckBloodStock
BEFORE UPDATE ON Blood_Stock
FOR EACH ROW
BEGIN
    IF NEW.UnitsAvailable < 0 THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'Blood stock cannot be negative';
    END IF;
END //

-- Automatically update LastUpdated when stock changes
CREATE TRIGGER UpdateBloodDate
BEFORE UPDATE ON Blood_Stock
FOR EACH ROW
BEGIN
    IF NEW.UnitsAvailable <> OLD.UnitsAvailable THEN
        SET NEW.LastUpdated = CURDATE();
    END IF;
END //

DELIMITER ;

-- ============================================================
-- 3. STORED PROCEDURES
-- ============================================================

DELIMITER //

-- Issue blood from blood stock
CREATE PROCEDURE IssueBlood
(
    IN p_BloodGroup VARCHAR(50),
    IN p_Units INT
)
BEGIN
    UPDATE Blood_Stock
    SET UnitsAvailable = UnitsAvailable - p_Units
    WHERE BloodGroup = p_BloodGroup
    AND UnitsAvailable >= p_Units;
END //

-- Add blood units to blood stock
CREATE PROCEDURE AddBlood
(
    IN p_BloodGroup VARCHAR(50),
    IN p_Units INT
)
BEGIN
    UPDATE Blood_Stock
    SET UnitsAvailable = UnitsAvailable + p_Units
    WHERE BloodGroup = p_BloodGroup;
END //

-- Update donor city
CREATE PROCEDURE UpdateDonorCity
(
    IN p_DonorID VARCHAR(10),
    IN p_City VARCHAR(50)
)
BEGIN
    UPDATE Donor
    SET City = p_City
    WHERE DonorID = p_DonorID;
END //

DELIMITER ;

-- ============================================================
-- 4. FUNCTIONS
-- ============================================================

DELIMITER //

-- Return available units for a blood group
CREATE FUNCTION AvailableUnits
(
    p_BloodGroup VARCHAR(50)
)
RETURNS INT
DETERMINISTIC
BEGIN
    DECLARE units INT;

    SELECT UnitsAvailable
    INTO units
    FROM Blood_Stock
    WHERE BloodGroup = p_BloodGroup;

    RETURN IFNULL(units, 0);
END //

-- Return total number of donors
CREATE FUNCTION TotalDonors()
RETURNS INT
DETERMINISTIC
BEGIN
    DECLARE total INT;

    SELECT COUNT(*)
    INTO total
    FROM Donor;

    RETURN total;
END //

-- Return total number of hospitals
CREATE FUNCTION TotalHospitals()
RETURNS INT
DETERMINISTIC
BEGIN
    DECLARE total INT;

    SELECT COUNT(*)
    INTO total
    FROM Hospital;

    RETURN total;
END //

DELIMITER ;

-- ============================================================
-- 5. OPTIONAL SAMPLE DATA
-- ============================================================

-- Admin
INSERT INTO Admin (Username, Password)
VALUES
('admin', 'admin123'),
('manager', 'manager123');

-- Hospital
INSERT INTO Hospital
(HospitalID, HospitalName, Email, Password, Phone, City)
VALUES
('H101', 'City Care Hospital', 'citycare@gmail.com', 'city123', '9876501001', 'Ahmedabad'),
('H102', 'Life Line Hospital', 'lifeline@gmail.com', 'life123', '9876501002', 'Surat'),
('H103', 'Shree Hospital', 'shree@gmail.com', 'shree123', '9876501003', 'Vadodara'),
('H104', 'Civil Hospital', 'civil@gmail.com', 'civil123', '9876501004', 'Rajkot'),
('H105', 'Sunrise Hospital', 'sunrise@gmail.com', 'sunrise123', '9876501005', 'Gandhinagar'),
('H106', 'Care Plus Hospital', 'careplus@gmail.com', 'care123', '9876501006', 'Bhavnagar'),
('H107', 'Apex Hospital', 'apex@gmail.com', 'apex123', '9876501007', 'Anand'),
('H108', 'Unity Hospital', 'unity@gmail.com', 'unity123', '9876501008', 'Mehsana'),
('H109', 'Healing Hospital', 'healing@gmail.com', 'heal123', '9876501009', 'Nadiad'),
('H110', 'Hope Hospital', 'hope@gmail.com', 'hope123', '9876501010', 'Bharuch');

-- Blood Stock
INSERT INTO Blood_Stock
(BloodGroup, UnitsAvailable, LastUpdated)
VALUES
('A+', 25, '2026-08-01'),
('A-', 12, '2026-07-28'),
('B+', 20, '2026-08-05'),
('B-', 8, '2026-07-20'),
('AB+', 10, '2026-08-07'),
('AB-', 5, '2026-07-15'),
('O+', 30, '2026-08-03'),
('O-', 7, '2026-07-25');

-- Donors
INSERT INTO Donor
(DonorID, Name, Age, Gender, BloodGroup, Mobile, City, LastDonationDate)
VALUES
('D101', 'Rahul Patel', 24, 'Male', 'O+', '9876500001', 'Ahmedabad', '2026-06-15'),
('D102', 'Priya Shah', 27, 'Female', 'A+', '9876500002', 'Surat', '2026-05-20'),
('D103', 'Amit Mehta', 31, 'Male', 'B+', '9876500003', 'Vadodara', '2026-04-10'),
('D104', 'Neha Desai', 23, 'Female', 'AB+', '9876500004', 'Rajkot', '2026-06-05'),
('D105', 'Karan Joshi', 29, 'Male', 'O-', '9876500005', 'Ahmedabad', '2026-03-18'),
('D106', 'Riya Patel', 26, 'Female', 'A-', '9876500006', 'Gandhinagar', '2026-05-12'),
('D107', 'Harsh Trivedi', 34, 'Male', 'B-', '9876500007', 'Bhavnagar', '2026-02-25'),
('D108', 'Pooja Mehta', 28, 'Female', 'AB-', '9876500008', 'Surat', '2026-04-22'),
('D109', 'Dhruv Shah', 25, 'Male', 'O+', '9876500009', 'Anand', '2026-06-28'),
('D110', 'Simran Patel', 30, 'Female', 'A+', '9876500010', 'Mehsana', '2026-05-30');

-- Emergency Requests
INSERT INTO Emergency_Request
(RequestID, HospitalID, BloodGroup, UnitsRequired, RequestDate, Priority, Status)
VALUES
('R101', 'H101', 'O+', 5, '2026-08-08', 'Critical', 'Pending'),
('R102', 'H102', 'A+', 3, '2026-08-08', 'High', 'Pending'),
('R103', 'H103', 'B+', 4, '2026-08-09', 'Normal', 'Pending'),
('R104', 'H104', 'O-', 2, '2026-08-09', 'Critical', 'Pending'),
('R105', 'H105', 'AB+', 3, '2026-08-09', 'High', 'Pending'),
('R106', 'H106', 'A-', 4, '2026-08-10', 'Normal', 'Pending'),
('R107', 'H107', 'B-', 2, '2026-08-10', 'Critical', 'Pending'),
('R108', 'H108', 'AB-', 1, '2026-08-10', 'High', 'Pending'),
('R109', 'H109', 'A+', 5, '2026-08-11', 'Normal', 'Pending'),
('R110', 'H110', 'O+', 6, '2026-08-11', 'Critical', 'Pending');

-- ============================================================
-- 6. TEST QUERIES
-- ============================================================

SELECT * FROM Admin;

SELECT * FROM Hospital;

SELECT * FROM Blood_Stock;

SELECT * FROM Donor;

SELECT * FROM Emergency_Request;

-- Functions
SELECT AvailableUnits('A+') AS Available_A_Positive;

SELECT TotalDonors() AS Total_Donors;

SELECT TotalHospitals() AS Total_Hospitals;

-- Procedures
-- CALL AddBlood('A+', 5);
-- CALL IssueBlood('A+', 5);
-- CALL UpdateDonorCity('D101', 'Surat');

-- Triggers can be tested using:
-- UPDATE Blood_Stock
-- SET UnitsAvailable = 50
-- WHERE BloodGroup = 'A+';

-- This should automatically update LastUpdated.
