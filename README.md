# Clinic Management System

A Java-based desktop application for managing clinic operations, including doctor consultations, patient appointments, and medical billing. Built with **Java Swing**, **Hibernate ORM**, and an **H2 Database**.

## Features

* **Patient Portal:** Book, reschedule, and cancel appointments. View doctor availability based on specialization.
* **Doctor Dashboard:** View daily appointments, access patient medical history, prescribe medications (from catalog or manual entry), issue sick notes, and schedule follow-ups.
* **Billing & Invoicing:** Automatic invoice generation for scheduled appointments including consultation fees and VAT.
* **Medical Records:** Keep track of diagnoses, treatments, and medical notes.
* **Staff Management:** Manage Doctors, Nurses, and Departments with specialization assignments.

## Tech Stack

* **Language:** Java (JDK 24)
* **UI Framework:** Java Swing
* **ORM:** Hibernate 5.6
* **Database:** H2 Database (File-based)
* **Build Tool:** Maven

## Getting Started

### Prerequisites

* Java Development Kit (JDK)
* Maven

### Installation & Setup

1.  **Clone the repository** (or download the source code).
2.  **Build the project** using Maven:
    ```bash
    mvn clean install
    ```
3.  **Initialize the Database:**
    Before running the application, run the `util.EntitySeeder` class to populate the local H2 database with mock data (Doctors, Patients, Medications, Departments, etc.).

### Running the Scenarios

The project includes two main entry points for testing different user roles:

* **Patient Flow:** Run `PatientScenario.java`. This opens the Patient Appointment Dashboard (logged in as "Walter White" by default if the database was seeded).
* **Doctor Flow:** Run `DoctorScenario.java`. This opens the Doctor Dashboard (logged in as "Dr. Stephen Strange" by default if the database was seeded).

## Database

The project uses an H2 file-based database. The database file `clinical_management.mv.db` will be automatically generated in the root directory upon running the seeder or the application.
