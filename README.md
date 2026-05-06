# SmartCare Microservices

A comprehensive healthcare management system split into microservices built with Spring Boot and Spring Cloud Gateway.

## Table of Contents
- [Architecture](#architecture)
- [Services](#services)
- [Technology Stack](#technology-stack)
- [Project Structure](#project-structure)
- [Prerequisites](#prerequisites)
- [Installation & Setup](#installation--setup)
- [Configuration](#configuration)
- [API Documentation](#api-documentation)
- [Testing](#testing)
- [Contributing](#contributing)

## Architecture

SmartCare has been refactored from a monolith into a microservices architecture. All external traffic flows through the **API Gateway**, which handles authentication, JWT issuance/validation, and routes requests to internal services.

```
Client -> API Gateway (8080)
              |
              +-> Main Service (8091) - Users, Doctors, Patients
              +-> Appointment Service (8092) - Booking, Slots, Schedules
              +-> Medical Record Service (8093) - Prescriptions, Medical History (MongoDB)
              +-> Chatbot Service (8094) - AI-powered chat
```

### Authentication Flow
1. Client sends credentials to `POST /api/v2/auth/login` on the Gateway
2. Gateway validates against MySQL, issues JWT containing user ID, username, and role
3. On subsequent requests, Gateway validates JWT and forwards user claims via headers (`X-User-Id`, `X-User-Username`, `X-User-Roles`)
4. Downstream services trust gateway headers and enforce role-based access

## Services

### API Gateway (Port 8080)
- Single entry point for all `/api/v2/*` traffic
- JWT authentication and token issuance
- Static routing to internal services
- Uses Spring Cloud Gateway (WebFlux)

### Main Service (Port 8091)
- Doctor and patient CRUD operations
- User master data management
- Caching with Ehcache and Redis

### Appointment Service (Port 8092)
- Appointment booking, cancellation, and completion
- Double-booking prevention (unique constraint on doctorId + date + time)
- Doctor schedule management
- Available slot computation
- Auto-completes past appointments (scheduled task)

### Medical Record Service (Port 8093)
- Prescription CRUD (MongoDB)
- Medical history management (MongoDB)
- Lab results storage

### Chatbot Service (Port 8094)
- Role-based AI chat (OpenAI for doctors, Anthropic for patients)
- Dynamic system prompts based on user role

## Technology Stack

| Component | Technology |
|-----------|-------------|
| Language | Java 17 |
| Framework | Spring Boot 3.5.13 + Spring Cloud 2025.0.0 |
| API Gateway | Spring Cloud Gateway (WebFlux) |
| Database (Relational) | MySQL 8.x (JPA/Hibernate) |
| Database (NoSQL) | MongoDB |
| Security | Spring Security + JWT (jjwt 0.11.5) |
| AI Integration | Spring AI 1.1.4 (OpenAI + Anthropic) |
| Build Tool | Maven (multi-module) |
| Testing | JUnit 5 + Mockito |
| Code Generator | Lombok |
| Caching | Ehcache + Redis |
| API Docs | SpringDoc OpenAPI |

## Project Structure

```
SmartCare/
├── pom.xml                          # Parent aggregator POM
├── api-gateway/                     # API Gateway service
│   ├── pom.xml
│   └── src/main/java/com/example/gateway/
│       ├── GatewayApplication.java
│       ├── config/
│       ├── controller/
│       ├── dto/
│       ├── entity/
│       ├── filter/
│       ├── repository/
│       └── service/
├── main-service/                    # Main business service
│   ├── pom.xml
│   └── src/main/java/com/example/mainservice/
│       ├── MainServiceApplication.java
│       ├── config/
│       ├── controller/
│       ├── dto/
│       ├── entity/
│       ├── exception/
│       ├── repository/
│       └── service/
├── appointment-service/             # Appointment management
│   ├── pom.xml
│   └── src/main/java/com/example/appointment/
│       ├── AppointmentServiceApplication.java
│       ├── config/
│       ├── controller/
│       ├── dto/
│       ├── entity/
│       ├── exception/
│       ├── repository/
│       └── service/
├── medical-record-service/          # Medical records (MongoDB)
│   ├── pom.xml
│   └── src/main/java/com/example/medicalrecord/
│       ├── MedicalRecordServiceApplication.java
│       ├── config/
│       ├── controller/
│       ├── document/
│       ├── dto/
│       ├── exception/
│       ├── repository/
│       └── service/
└── chatbot-service/                 # AI chatbot
    ├── pom.xml
    └── src/main/java/com/example/chatbot/
        ├── ChatbotServiceApplication.java
        ├── config/
        ├── controller/
        ├── dto/
        └── service/
```

## Prerequisites

- Java 17 or higher
- Maven 3.6+
- MySQL 8.x running on port 3306
- MongoDB running on port 27017
- Redis (optional, for caching in main-service)

## Installation & Setup

1. Clone the repository:
   ```bash
   git clone https://github.com/yourusername/SmartCare.git
   cd SmartCare
   ```

2. Create MySQL databases:
   - `smartcare` (used by api-gateway and main-service)
   - `smartcare_appointments` (used by appointment-service)

3. Configure MongoDB:
   - Database `smartcare_db` (used by medical-record-service)
   - Default connection: `mongodb://localhost:27017`

4. Configure AI keys in `chatbot-service/src/main/resources/application.yml`

5. Build all services:
   ```bash
   mvn clean install
   ```

6. Run each service (from project root):
   ```bash
   mvn spring-boot:run -pl api-gateway
   mvn spring-boot:run -pl main-service
   mvn spring-boot:run -pl appointment-service
   mvn spring-boot:run -pl medical-record-service
   mvn spring-boot:run -pl chatbot-service
   ```

## Configuration

Each service has its own `application.yml` with dedicated ports and database connections.

### Gateway Routes

| Path Pattern | Target Service |
|---|---|
| `/api/v2/auth/**` | API Gateway (auth handled locally) |
| `/api/v2/users/**`, `/api/v2/doctors/**`, `/api/v2/patients/**` | main-service (8091) |
| `/api/v2/appointments/**`, `/api/v2/slots/**`, `/api/v2/schedule/**` | appointment-service (8092) |
| `/api/v2/prescriptions/**`, `/api/v2/medical-history/**` | medical-record-service (8093) |
| `/api/v2/chat/**` | chatbot-service (8094) |

## API Documentation

### Base URL
```
http://localhost:8080/api/v2
```

### Authentication

| Method | Endpoint | Description | Access |
|--------|-----------|-------------|--------|
| POST | `/api/v2/auth/login` | Login and get JWT token | Public |

**Login Request:**
```json
{
    "username": "admin",
    "password": "1234"
}
```

**Login Response:**
```json
{
    "status": 200,
    "message": "success",
    "data": {
        "token": "jwt_token_here",
        "username": "admin",
        "role": "ADMIN",
        "userId": "1"
    }
}
```

Use the returned token in subsequent requests:
```
Authorization: Bearer <token>
```

### Doctor Endpoints

| Method | Endpoint | Description | Access |
|--------|-----------|-------------|--------|
| POST | `/api/v2/doctors` | Create doctor | ADMIN |
| GET | `/api/v2/doctors/{id}` | Get doctor by ID | All authenticated |
| GET | `/api/v2/doctors` | Get all doctors | ADMIN, DOCTOR |
| PUT | `/api/v2/doctors/{id}` | Update doctor | ADMIN, DOCTOR |
| DELETE | `/api/v2/doctors/{id}` | Delete doctor | ADMIN |
| GET | `/api/v2/doctors/specialty?specialty=` | Search by specialty | ADMIN, PATIENT |

### Patient Endpoints

| Method | Endpoint | Description | Access |
|--------|-----------|-------------|--------|
| POST | `/api/v2/patients` | Create patient | ADMIN |
| GET | `/api/v2/patients/{id}` | Get patient by ID | All authenticated |
| GET | `/api/v2/patients` | Get all patients | ADMIN, DOCTOR |
| PUT | `/api/v2/patients/{id}` | Update patient | ADMIN, PATIENT |
| DELETE | `/api/v2/patients/{id}` | Delete patient | ADMIN |
| GET | `/api/v2/patients/search/{username}` | Search by username | All authenticated |

### Appointment Endpoints

| Method | Endpoint | Description | Access |
|--------|-----------|-------------|--------|
| POST | `/api/v2/appointments` | Book appointment | PATIENT |
| PATCH | `/api/v2/appointments/{id}` | Cancel appointment | PATIENT |
| PATCH | `/api/v2/appointments/{id}/complete` | Mark as completed | DOCTOR |
| GET | `/api/v2/appointments/doctor/{doctorId}?date=` | Get doctor appointments | DOCTOR, ADMIN |
| GET | `/api/v2/appointments/patient/{patientId}` | Get patient appointments | PATIENT, ADMIN |
| GET | `/api/v2/appointments/available-slots/{doctorId}?date=` | Get available slots | All authenticated |

**Book Appointment Request:**
```json
{
    "doctorId": 1,
    "patientId": 1,
    "date": "2026-05-06",
    "time": "10:00"
}
```

### Schedule Endpoints

| Method | Endpoint | Description | Access |
|--------|-----------|-------------|--------|
| POST | `/api/v2/schedule/{doctorId}` | Create doctor schedule | DOCTOR, ADMIN |
| GET | `/api/v2/schedule/{doctorId}?date=` | Get doctor schedule | DOCTOR, ADMIN |

### Prescription Endpoints (MongoDB)

| Method | Endpoint | Description | Access |
|--------|-----------|-------------|--------|
| POST | `/api/v2/prescriptions` | Create prescription | DOCTOR |
| PUT | `/api/v2/prescriptions/{id}` | Update prescription | DOCTOR |
| DELETE | `/api/v2/prescriptions/{id}` | Delete prescription | DOCTOR |
| GET | `/api/v2/prescriptions/patient/{patientId}` | View patient prescriptions | DOCTOR, PATIENT |

### Medical History Endpoints (MongoDB)

| Method | Endpoint | Description | Access |
|--------|-----------|-------------|--------|
| POST | `/api/v2/medical-history` | Create medical record | DOCTOR |
| PUT | `/api/v2/medical-history/{id}` | Update medical record | DOCTOR |
| DELETE | `/api/v2/medical-history/{id}` | Delete medical record | DOCTOR |
| GET | `/api/v2/medical-history/patient/{patientId}` | View patient history | DOCTOR, PATIENT, ADMIN |

### Chatbot Endpoint

| Method | Endpoint | Description | Access |
|--------|-----------|-------------|--------|
| POST | `/api/v2/chat` | Send chat message | All authenticated |

The chatbot automatically selects the AI model based on the user's role (OpenAI for doctors, Anthropic for patients).

## Testing

Run all tests across modules:
```bash
mvn test
```

Run tests for a specific module:
```bash
mvn test -pl appointment-service
```

## Contributing

1. Fork the repository
2. Create your feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request
