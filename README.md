# Project Tasks Manager - Full-Stack Application

A full-stack task management application built with Spring Boot (Backend) and Angular (Frontend), implementing clean architecture principles.

## 📋 Overview

This application allows users to:
- Authenticate with email/password (JWT-based)
- Create and manage projects
- Add tasks to projects with title, description, and due date
- Mark tasks as completed/uncompleted
- View project progress with visual progress bars
- Delete projects and tasks

---

## 🛠️ Technologies Used

### Backend
- **Java 17**
- **Spring Boot 3.4.12**
  - Spring Data JPA
  - Spring Security
  - Spring Web
- **MySQL** (Database)
- **JWT (JSON Web Tokens)** - Authentication (jjwt 0.12.5)
- **BCrypt** - Password hashing
- **Maven** - Build tool

### Frontend
- **Angular 20** (Standalone Components)
- **TypeScript**
- **RxJS** - Reactive programming
- **HttpClient** - API communication
- **CSS3** - Custom styling with gradients

### Database
- **MySQL 8.0+** (via XAMPP or standalone)

---

## 🏗️ Architecture

### Backend: Clean Architecture

The backend follows **Clean Architecture** principles with clear separation of concerns:

```
miniproject/
└── src/main/java/com/whyboutrasseyt/miniproject/
    ├── domain/                          # Core business logic
    │   ├── model/                       # Domain entities (Project, Task, User)
    │   ├── repository/                  # Repository interfaces (ports)
    │   └── exception/                   # Domain exceptions
    │
    ├── application/                     # Use cases / Application services
    │   ├── service/                     # Service interfaces
    │   │   └── impl/                    # Service implementations
    │   ├── command/                     # Command objects (input)
    │   └── dto/                         # Data Transfer Objects (output)
    │
    ├── infrastructure/                  # External concerns
    │   ├── persistence/jpa/             # Database implementation
    │   │   ├── entity/                  # JPA entities
    │   │   ├── repository/              # Spring Data JPA repositories
    │   │   ├── mapper/                  # Entity <-> Domain mappers
    │   │   └── adapter/                 # Repository implementations
    │   ├── security/                    # JWT, Security config
    │   └── bootstrap/                   # Data initialization
    │
    ├── presentation/                    # API Layer (Controllers)
    │   ├── request/                     # Request DTOs
    │   ├── response/                    # Response DTOs
    │   └── exception/                   # Global exception handler
    │
    └── config/                          # Configuration (CORS, etc.)
```

#### Layer Responsibilities

1. **Domain Layer**: 
   - Pure business logic
   - No dependencies on frameworks
   - Entities with invariants
   - Repository interfaces (ports)

2. **Application Layer**:
   - Orchestrates use cases
   - Calls domain services
   - Handles business validation
   - Independent of delivery mechanism

3. **Infrastructure Layer**:
   - Implements repository interfaces
   - Database access (JPA)
   - Security (JWT, BCrypt)
   - External service integrations

4. **Presentation Layer**:
   - REST API controllers
   - Request/Response mapping
   - Input validation
   - HTTP concerns only

### Frontend: Component-Based Architecture

```
webApp/src/app/
├── components/
│   ├── login/                   # Login page
│   ├── projects/                # Projects list
│   └── project-detail/          # Project detail with tasks
├── services/
│   ├── auth.service.ts          # Authentication
│   ├── project.service.ts       # Project CRUD
│   └── task.service.ts          # Task CRUD
├── interceptors/
│   └── auth.interceptor.ts      # JWT token injection
├── guards/
│   └── auth.guard.ts            # Route protection
├── models/
│   └── index.ts                 # TypeScript interfaces
└── app.routes.ts                # Routing configuration
```

---

## 🗄️ Database Setup

### 1. Install MySQL

Option A: **XAMPP** (Recommended for Windows)
- Download from [https://www.apachefriends.org](https://www.apachefriends.org)
- Install and start MySQL service

Option B: **Standalone MySQL**
- Download from [https://dev.mysql.com/downloads/mysql/](https://dev.mysql.com/downloads/mysql/)
- Install and configure

### 2. Create Database

```sql
CREATE DATABASE project_tasks;
```

### 3. Configure Database Connection

Edit `miniproject/src/main/resources/application.yml`:

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/project_tasks?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC
    username: root
    password: your_password_here  # Leave empty if using XAMPP default
    driver-class-name: com.mysql.cj.jdbc.Driver
```

### 4. Database Schema

The application uses Hibernate with `ddl-auto: update` to automatically create/update tables:
- `users` - User accounts
- `projects` - Project information
- `tasks` - Task details

**Default User** (seeded automatically):
- Email: `user@example.com`
- Password: `password`

---

## 🚀 Running the Application

### Prerequisites

- **Java 17** or higher
- **Node.js 18+** and npm
- **MySQL 8.0+**
- **Maven** (included via wrapper)

### Backend Setup

1. **Navigate to backend directory:**
   ```bash
   cd miniproject
   ```

2. **Configure database** (see Database Setup above)

3. **Run the application:**
   
   Windows:
   ```bash
   .\mvnw.cmd spring-boot:run
   ```
   
   Linux/Mac:
   ```bash
   ./mvnw spring-boot:run
   ```

4. **Verify backend is running:**
   - Server starts on `http://localhost:8081`
   - Check console for: `Started MiniprojectApplication`

### Frontend Setup

1. **Navigate to frontend directory:**
   ```bash
   cd webApp/webApp
   ```

2. **Install dependencies (first time only):**
   ```bash
   npm install
   ```

3. **Run the development server:**
   ```bash
   npm start
   ```
   or
   ```bash
   ng serve
   ```

4. **Access the application:**
   - Open browser to `http://localhost:4200`
   - Login with: `user@example.com` / `password`

---

## 🔐 Authentication Flow

1. User submits credentials to `/auth/login`
2. Backend validates credentials with BCrypt
3. Backend generates JWT token with user info
4. Frontend stores token in localStorage
5. Frontend includes token in all API requests via interceptor
6. Backend validates token on protected endpoints

---

## 📡 API Endpoints

### Authentication
- `POST /auth/login` - Login (public)

### Projects
- `GET /api/projects` - List user's projects
- `POST /api/projects` - Create new project
- `GET /api/projects/{id}` - Get project details
- `DELETE /api/projects/{id}` - Delete project

### Tasks
- `GET /api/tasks?projectId={id}` - List project tasks
- `POST /api/tasks` - Create new task
- `PATCH /api/tasks/{id}/toggle` - Toggle task completion
- `DELETE /api/tasks/{id}` - Delete task

All endpoints (except login) require JWT authentication via `Authorization: Bearer <token>` header.

---

## 🎨 Features

### Progress Calculation
- Backend calculates: `completedTasks / totalTasks * 100`
- Displayed in real-time on project cards and detail view
- Visual progress bar with gradient styling

### Security
- JWT-based authentication with 60-minute expiration
- BCrypt password hashing (10 rounds)
- CORS enabled for Angular frontend
- Protected routes with authentication guard

### Validation
- Backend: Jakarta Validation annotations + custom service validation
- Frontend: Form validation and disabled buttons
- Global exception handler for consistent error responses

---

## 🧪 Testing the Application

1. **Login** with default credentials
2. **Create a project** with title and description
3. **View project** progress (starts at 0%)
4. **Add tasks** with titles, descriptions, and due dates
5. **Toggle task completion** by clicking checkboxes
6. **Watch progress bar update** automatically
7. **Delete tasks/projects** as needed

---

## 📝 Code Logic Highlights

### Domain-Driven Design
- Rich domain models with validation in constructors
- Repository pattern for data access abstraction
- Service layer orchestrates business logic

### Task Progress Calculation
```java
// Backend: ProjectServiceImpl
long totalTasks = taskRepository.countByProjectId(project.id());
long completedTasks = taskRepository.countCompletedByProjectId(project.id());
double progress = totalTasks > 0 ? (completedTasks * 100.0) / totalTasks : 0.0;
```

### JWT Token Generation
```java
// Infrastructure: JwtService
Jwts.builder()
    .subject(user.email())
    .claim("role", user.role())
    .issuedAt(Date.from(now))
    .expiration(Date.from(now.plusSeconds(expirationMinutes * 60)))
    .signWith(signingKey())
    .compact();
```

### Ownership Validation
```java
// Application: TaskServiceImpl
Project project = projectRepository.findById(projectId)
    .orElseThrow(() -> new NotFoundException("Project", projectId));
if (!project.ownerId().equals(ownerId)) {
    throw new ValidationException("You do not own this project");
}
```

---

## 🐛 Troubleshooting

### Backend Issues

**Port already in use:**
```bash
# Change port in application.yml
server:
  port: 8082
```

**Database connection failed:**
- Verify MySQL is running
- Check credentials in application.yml
- Ensure database `project_tasks` exists

### Frontend Issues

**CORS errors:**
- Ensure backend is running on port 8081
- Check CORS configuration in SecurityConfig.java

**Module not found:**
```bash
cd webApp/webApp
rm -rf node_modules package-lock.json
npm install
```

---

## 👥 Authors

Wassim Boutrasseyt

---

## 📄 License

This project was created as part of an internship technical test.
