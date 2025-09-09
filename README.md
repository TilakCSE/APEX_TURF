# APEX\_TURF: Smart Turf Booking Web App

## Project Description

APEX\_TURF is a smart turf booking system for sports enthusiasts. It allows users to book and manage reservations through a modern web interface and provides a comprehensive admin panel for site management.

## Inspiration

This project is inspired by [hudle.in](https://hudle.in), aiming for intuitive sport and turf booking online.

## Technology Stack

  - Java (JDK 17+)
  - Servlets & JSPs (MVC design)
  - MySQL Database
  - Maven for build management (WAR packaging)
  - Jakarta EE 10 (Servlet 6.0, JSP 3.1, JSTL 3.0)
  - HikariCP for connection pooling
  - **jBCrypt** for secure password hashing
  - **Jakarta Mail** for sending email notifications

## Features

#### User-Facing Features

  - **Secure User Authentication:** Full user registration and login system.
  - **Dynamic Booking Form:** Select a turf and the sports dropdown dynamically updates to show only available sports for that location.
  - **My Bookings Page:** View a complete history of past and upcoming bookings.
  - **Booking Cancellation:** Users can cancel their upcoming bookings up to 12 hours before the start time.

#### Admin Panel Features

  - **Secure Admin Login:** Separate, role-based login for administrators at `/admin/login`.
  - **Role-Based Access Control:** All `/admin/*` routes are protected, accessible only by authenticated admins.
  - **Bookings Dashboard:**
      - View all bookings from every user.
      - Filter bookings by turf, sport, or a specific date.
      - Perform admin actions: Cancel or Re-confirm any booking.
      - Export the filtered list of bookings to a CSV file.
  - **Turf Management (CRUD):**
      - Add, edit, and deactivate turfs.
      - Assign available sports to each turf (many-to-many relationship).
  - **Sport Management (CRUD):**
      - Add, edit, and delete sports available on the platform.

#### System Features

  - **Automated Email Notifications:**
      - Users receive emails for booking confirmations, cancellations (initiated by user or admin), and reconfirmations.
      - Admins are notified of new bookings and cancellations.

## Project Status

**September 2025:**

The core booking engine, user authentication system, and a comprehensive admin panel are now complete. The application is fully functional, with features like dynamic forms, role-based security, CRUD management for core entities, and an automated email notification system.

## How to Run

1.  Clone the repository.
2.  Install JDK 17+ and Apache Tomcat 10.1+ (Jakarta EE 10 compatible).
3.  Create a MySQL database named `apex_turf`.
4.  Configure database credentials in `src/main/java/org/example/config/DatabaseConfig.java`.
5.  Configure SMTP credentials for email. Copy `src/main/resources/email.properties.example` to `src/main/resources/email.properties` and fill in your Gmail App Password and email addresses.
6.  Build the project:
    ```bash
    mvn clean package
    ```
7.  Deploy `target/apex-turf.war` to your Tomcat `webapps/` directory.
8.  Access the application at `http://localhost:8080/apex-turf/`.

## Database Schema

Create the following tables in your `apex_turf` database.

```sql
CREATE TABLE users (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  name VARCHAR(100) NOT NULL,
  email VARCHAR(150) NOT NULL UNIQUE,
  phone VARCHAR(32) NOT NULL,
  password VARCHAR(255) NOT NULL,
  role VARCHAR(10) NOT NULL DEFAULT 'USER'
);

CREATE TABLE turfs (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  name VARCHAR(150) NOT NULL,
  location VARCHAR(200) NOT NULL,
  active TINYINT(1) NOT NULL DEFAULT 1
);

CREATE TABLE sports (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  name VARCHAR(100) NOT NULL,
  active TINYINT(1) NOT NULL DEFAULT 1
);

CREATE TABLE turf_sports (
  turf_id BIGINT NOT NULL,
  sport_id BIGINT NOT NULL,
  PRIMARY KEY (turf_id, sport_id),
  FOREIGN KEY (turf_id) REFERENCES turfs(id) ON DELETE CASCADE,
  FOREIGN KEY (sport_id) REFERENCES sports(id) ON DELETE CASCADE
);

CREATE TABLE bookings (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  user_id BIGINT NOT NULL,
  turf_id BIGINT NOT NULL,
  sport_id BIGINT NOT NULL,
  start_time DATETIME NOT NULL,
  end_time DATETIME NOT NULL,
  status VARCHAR(32) NOT NULL,
  created_at DATETIME NOT NULL,
  CONSTRAINT fk_user FOREIGN KEY (user_id) REFERENCES users(id),
  CONSTRAINT fk_turf FOREIGN KEY (turf_id) REFERENCES turfs(id),
  CONSTRAINT fk_sport FOREIGN KEY (sport_id) REFERENCES sports(id),
  INDEX idx_turf_time (turf_id, start_time, end_time)
);
```

## Contributing

Pull requests are welcome\! Issues, feature suggestions, and feedback are appreciated.

## License

MIT License
