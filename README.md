# Blood Bank and Donor Management System

A Java-based **Blood Bank and Donor Management System** designed to manage blood donors, hospitals, blood inventory, and emergency blood requests using **Java, MySQL, JDBC, OOP, Data Structures, and DBMS concepts**.

## 📌 Project Overview

The system provides separate access for **Hospitals** and **Blood Bank Admins**.

Hospitals can register and log in to create and manage emergency blood requests, while administrators manage blood stock and process requests based on their priority.

The project demonstrates how Java application logic, data structures, and relational database concepts can be combined to solve a real-world blood management problem.

## ✨ Features

### 🏥 Hospital Module

* New Hospital Sign Up
* Hospital Login
* Automatic Hospital ID generation
* View hospital details
* Create emergency blood requests
* View own emergency requests
* Update own requests
* View request status
* Logout

### 👨‍💼 Admin Module

* Admin Login
* View pending emergency requests
* Process next priority request
* Approve requests
* Reject requests
* View request details
* Update emergency requests
* Manage blood inventory
* Manage donor information
* View database information

### 🚨 Emergency Request Management

Emergency requests contain:

* Request ID
* Hospital ID
* Blood Group
* Units Required
* Request Date
* Priority
* Status

Requests are processed using a **Priority Queue**:

```text
Critical
   ↓
High
   ↓
Normal
```

When an approved request has sufficient blood stock, the system automatically issues the required blood and updates the inventory.

## 🧠 Data Structures

The project uses:

### Priority Queue

A custom priority queue implemented using a linked-list-based structure.

It ensures that emergency requests are processed according to:

```text
Critical → High → Normal
```

### LinkedList

Used for maintaining collections such as blood inventory and donor-related records.

### HashMap

Used for efficient blood-group-based searching.

## 🗄️ Database

Database:

```text
blood_bank
```

### Tables

```text
Admin
Hospital
Blood_Stock
Donor
Emergency_Request
```

### Database Relationships

```text
Hospital
   │
   └── Emergency_Request

Blood_Stock
   │
   ├── Donor
   │
   └── Emergency_Request
```

Primary keys and foreign keys are used to maintain data integrity.

## ⚙️ Stored Procedures

The project includes:

| Procedure           | Purpose                                   |
| ------------------- | ----------------------------------------- |
| `IssueBlood()`      | Issues blood and deducts units from stock |
| `AddBlood()`        | Adds blood units to existing stock        |
| `UpdateDonorCity()` | Updates a donor's city                    |

## 🔍 Functions

| Function           | Purpose                                   |
| ------------------ | ----------------------------------------- |
| `AvailableUnits()` | Returns available units for a blood group |
| `TotalDonors()`    | Returns total number of donors            |
| `TotalHospitals()` | Returns total number of hospitals         |

## 🔔 Triggers

| Trigger           | Purpose                                     |
| ----------------- | ------------------------------------------- |
| `CheckBloodStock` | Prevents blood stock from becoming negative |
| `UpdateBloodDate` | Automatically updates the stock update date |

## 🛠️ Technologies Used

* **Java**
* **MySQL**
* **JDBC**
* **XAMPP**
* **phpMyAdmin**
* **NetBeans / IntelliJ IDEA / Eclipse**
* **OOP**
* **Data Structures**
* **DBMS**

## 📂 Project Structure

A typical project structure is:

```text
Blood-Bank-and-Donor-Management-System/
│
├── src/
│   ├── Main.java
│   ├── DBConnection.java
│   ├── Admin.java
│   ├── Hospital.java
│   ├── Donor.java
│   ├── BloodInventory.java
│   ├── Blood_Entry.java
│   ├── Emergency.java
│   ├── Emergency_Entry.java
│   ├── PriorityQueueDS.java
│   └── Report.java
│
├── database/
│   └── blood_bank.sql
│
├── reports/
│   └── Emergency_Report.txt
│
└── README.md
```

> File names may vary depending on the final project structure.

## 🚀 Installation & Setup

### 1. Install Requirements

Install:

* JDK
* MySQL
* XAMPP
* Java IDE such as NetBeans, IntelliJ IDEA, or Eclipse

### 2. Start MySQL

Open XAMPP and start:

```text
Apache
MySQL
```

Apache is required if you want to use phpMyAdmin.

### 3. Create the Database

Open phpMyAdmin and create:

```sql
CREATE DATABASE blood_bank;
```

Select the database and execute the project's SQL file.

The SQL file should create:

```text
Admin
Hospital
Blood_Stock
Donor
Emergency_Request
```

along with the required:

```text
Procedures
Functions
Triggers
Foreign Keys
```

### 4. Configure JDBC

Make sure the MySQL JDBC driver is added to the Java project.

Update the database connection in:

```text
DBConnection.java
```

Example:

```java
String url = "jdbc:mysql://localhost:3306/blood_bank";
String username = "root";
String password = "";
```

Change the username/password according to your MySQL configuration.

### 5. Run the Application

Run:

```text
Main.java
```

The application will provide the main system menu.

## 🔐 Login Flow

### Hospital

```text
Hospital
   ↓
Sign Up / Login
   ↓
Hospital Menu
   ↓
Create / Manage Emergency Requests
```

### Admin

```text
Admin
   ↓
Login
   ↓
Admin Menu
   ↓
Manage Blood Bank
   ↓
Process Emergency Requests
```

## 🔄 Emergency Request Flow

```text
Hospital Creates Request
          ↓
       Pending
          ↓
 Priority Queue
          ↓
Admin Processes Request
       ↙       ↘
   Approve      Reject
      ↓
Check Blood Stock
      ↓
Enough Stock?
   ↙        ↘
 Yes         No
 ↓            ↓
Issue Blood   Waiting
 ↓
Update Stock
 ↓
Issued
```

## 🧪 DBMS Concepts Demonstrated

This project demonstrates:

* Database creation
* Tables
* Primary Keys
* Foreign Keys
* Constraints
* `SELECT`
* `INSERT`
* `UPDATE`
* `DELETE`
* JDBC
* Prepared Statements
* ResultSet
* ResultSetMetadata
* Stored Procedures
* Functions
* Triggers
* Referential Integrity

## 📊 Reports

The system can generate reports containing information such as emergency requests and their statuses.

Example:

```text
========== EMERGENCY REPORT ==========

Request ID
Hospital ID
Blood Group
Units Required
Request Date
Priority
Status
```

## 🎯 Project Objectives

* Digitize blood bank management.
* Maintain organized donor records.
* Maintain accurate blood-stock information.
* Allow hospitals to submit emergency requests.
* Prioritize critical blood requirements.
* Reduce manual processing.
* Demonstrate Java OOP and Data Structures.
* Demonstrate practical DBMS concepts.

## 🔮 Future Scope

Possible future improvements include:

* Web-based interface
* Android/mobile application
* Real-time notifications
* Donor appointment scheduling
* Location-based donor search
* Online donor registration
* Blood-demand analytics
* Blood-stock forecasting
* Integration with multiple blood banks
* Cloud-based database
* Role-based access control

## 👨‍💻 Academic Project

This project was developed as an academic project to demonstrate practical implementation of:

```text
Java
   +
Object-Oriented Programming
   +
Data Structures
   +
JDBC
   +
MySQL
   +
DBMS
```

## 📜 License

This project is intended primarily for **educational and academic purposes**.
