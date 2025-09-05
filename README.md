# APEX_TURF: Smart Turf Booking Web App

## Project Description
APEX_TURF is a smart turf booking system for sports enthusiasts. It allows users to:

- Select from multiple turfs
- Choose their sport and preferred time slot
- Book and manage reservations through a modern web interface

## Inspiration
This project is inspired by [hudle.in](https://hudle.in), aiming for intuitive sport and turf booking online.

## Technology Stack
- Java (JDK 24)
- Servlets & JSPs (MVC design)
- MySQL database
- Maven build system (WAR packaging)
- Jakarta Servlet 6 + JSP + JSTL
- HikariCP connection pool

## Features
- Browse and filter available sports turfs
- Choose sport type and time slot
- User account management (booking history, profile)
- Admin module for managing turfs, time slots, and bookings (planned)
- Clean separation of backend (`java` models, servlets) and frontend (JSP views)

## Project Status
April 2025:

This is a fresh web-based refactor of APEX_TURF. The previous version used Java Swing and direct DB connections; this repo focuses on scalable backend and web UI. Daily updates planned.

## How to Run
1. Clone the repository.
2. Install JDK 24 and Apache Tomcat 10.1+ (Jakarta EE 10 compatible).
3. Create a MySQL database `apex_turf` and grant a user access.
4. Configure database credentials in `src/main/resources/application.properties`:

```properties
db.url=jdbc:mysql://localhost:3306/apex_turf?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC
db.username=YOUR_USER
db.password=YOUR_PASS
```

5. Build the project:

```bash
mvn clean package
```

6. Deploy `target/apex-turf.war` to Tomcat `webapps/`.
7. Open http://localhost:8080/apex-turf/ to access the booking page.

## Minimal Database Schema (to test quickly)
Create the following tables to get started:

```sql
CREATE TABLE users (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  name VARCHAR(100) NOT NULL,
  email VARCHAR(150) NOT NULL UNIQUE,
  phone VARCHAR(32) NOT NULL
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
  INDEX idx_turf_sport_time (turf_id, sport_id, start_time, end_time, status)
);
```

## Contributing
Pull requests are welcome! Issues, feature suggestions, and feedback are appreciated.

## License
MIT License

