# Splitwise Web Application

## Overview
The Splitwise Web Application is a full-stack Java-based web application built using:
- **Backend**: Spring Boot (Java)
- **Database**: PostgreSQL
- **Frontend**: HTML, CSS, JavaScript 
The application follows a **layered architecture** to ensure modularity, scalability, and ease of maintenance.

---

## 📁 Project Structure

### Root Directory
```
splitwise-backend/
├── .idea/                         # IntelliJ project settings
├── splitwise-app-frontend/       # Frontend source code (if applicable)
├── splitwise-backend/            # Spring Boot backend project
│   ├── src/
│   │   └── main/
│   │       ├── java/
│   │       │   └── com/
│   │       │       └── splitwise/
│   │       │           ├── config/         # Spring Security and CORS configs
│   │       │           ├── controller/     # REST controllers for endpoints
│   │       │           ├── dto/            # Data Transfer Objects
│   │       │           ├── model/          # JPA entities representing DB tables
│   │       │           ├── repository/     # Spring Data JPA repository interfaces
│   │       │           ├── service/        # Business logic services
│   │       │           └── SplitwiseApplication.java  # Main Spring Boot class
│   │       └── resources/
│   │           └── application.properties  # Application config (DB URL, port, etc.)
│   ├── target/                 # Compiled build output (auto-generated)
│   └── pom.xml                # Maven configuration and dependency file
├── .gitignore
```

---

## 🔧 Key Components

### 🔹 `pom.xml`
- Defines dependencies (Spring Boot, PostgreSQL, etc.) and build plugins.

### 🔹 `SplitwiseApplication.java`
- Main class annotated with `@SpringBootApplication`.
- Starts the embedded Spring Boot server.

### 🔹 `controller/`
- Handles HTTP requests and defines REST API endpoints.

### 🔹 `service/`
- Implements business logic and communicates with repositories.

### 🔹 `model/`
- Contains JPA entity classes (`@Entity`) mapped to database tables.

### 🔹 `repository/`
- JPA repository interfaces for CRUD operations on entities.

### 🔹 `dto/`
- Safe data transfer between backend and frontend.
- Hides sensitive/internal fields from external clients.

### 🔹 `config/`
- Security configurations (authentication/authorization).
- Cross-Origin Resource Sharing (CORS) rules.

### 🔹 `application.properties`
- Contains environment-specific configs (DB URL, username/password, port, etc.).

---

## 🔁 Application Flow

1. **Application Bootstraps** via `SplitwiseApplication.java`
2. **Frontend** sends HTTP request → mapped to controller method
3. **Controller** processes request → delegates to service layer
4. **Service** validates input → calls repository method
5. **Repository** performs DB operations using JPA
6. **Result** bubbles back → service → controller → frontend response

---

## 🔐 Authentication: Spring Security (Session-Based)

### Login Flow:
- User logs in → session cookie created
- Spring Security maintains session on server
- Protected endpoints are guarded based on session state

### Why Session-Based?
- Simpler for traditional web apps
- JWT adds statelessness but more complexity

---

## ✅ Summary Table
| Component | Description |
|----------|-------------|
| `pom.xml` | Build and dependency config |
| `SplitwiseApplication` | Main entry point |
| `controller` | REST API layer |
| `service` | Business logic |
| `model` | Database entities |
| `repository` | Data access with Spring Data JPA |
| `dto` | Safe data transfer objects |
| `config` | Security and app configurations |
| `application.properties` | App-level settings |
| `static/templates` | (If used) for frontend files |

