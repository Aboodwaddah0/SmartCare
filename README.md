# SmartCare

A comprehensive healthcare management system built with Spring Boot.

## Table of Contents
- [Features](#features)
- [Technology Stack](#technology-stack)
- [Project Structure](#project-structure)
- [Prerequisites](#prerequisites)
- [Installation & Setup](#installation--setup)
- [Configuration](#configuration)
- [API Documentation](#api-documentation)
- [Testing](#testing)
- [Contributing](#contributing)
- [License](#license)

## Features

### Authentication & Authorization
- JWT-based stateless authentication
- Role-based access control (ADMIN, DOCTOR, PATIENT)

### Patient Management
- Create, read, update, delete patient profiles
- Search patients by username
- Patient-specific appointment booking

### Doctor Management
- Doctor registration and profile management
- Search doctors by specialty
- Schedule management

### Appointment System
- Book appointments with slot validation
- Prevent double-booking (unique constraint: doctorId + date + time)
- Cancel appointments (with business rules)
- Mark appointments as completed
- View available time slots

### Medical Records (MongoDB)
- Prescription management (CRUD)
- Medical history tracking
- Lab results storage

### AI Integration
- OpenAI (GPT-4o-mini) support
- Anthropic (Claude) support
- AI-powered chatbot for patient queries

## Technology Stack

| Component | Technology |
|-----------|-------------|
| Language | Java 17 |
| Framework | Spring Boot 3.5.13 |
| Database (Relational) | MySQL 8.x (JPA/Hibernate) |
| Database (NoSQL) | MongoDB |
| Security | Spring Security + JWT (jjwt 0.11.5) |
| AI Integration | Spring AI 1.1.4 |
| Build Tool | Maven |
| Testing | JUnit 5 + Mockito |
| Code Generator | Lombok |
| Password Encoding | BCrypt |

## Project Structure

```
SmartCare/
├── src/main/java/com/example/SmartCare/
│   ├── config/              # Security & app configuration
│   ├── controller/          # REST API controllers
│   │   ├── AuthController.java
│   │   ├── DoctorController.java
│   │   ├── PatientController.java
│   │   ├── AppointmentController.java
│   │   ├── PrescriptionController.java
│   │   └── MedicalHistoryController.java
│   ├── service/             # Business logic
│   ├── repository/          # Data access layer
│   ├── entity/              # JPA entities & MongoDB documents
│   ├── dto/                 # Data Transfer Objects
│   ├── exception/           # Custom exceptions & global handler
│   └── security/            # JWT filter & services
├── src/main/resources/
│   └── application.properties
├── src/test/java/           # Unit & integration tests
└── pom.xml
```

## Prerequisites

- Java 17 or higher
- Maven 3.6+
- MySQL 8.x running on port 3306
- MongoDB running on port 27017

## Installation & Setup

1. Clone the repository:
   ```bash
   git clone https://github.com/yourusername/SmartCare.git
   cd SmartCare
   ```

2. Configure MySQL:
   - Create database `smartcare`
   - Update credentials in `application.properties`

3. Configure MongoDB:
   - Create database `smartcare_db`
   - Default connection: `mongodb://localhost:27017`

4. Configure AI (Optional):
   - Add OpenAI API key in `application.properties`
   - Add Anthropic API key in `application.properties`

5. Build and run:
   ```bash
   cd SmartCare
   mvn clean install
   mvn spring-boot:run
   ```

## Configuration

Update `src/main/resources/application.properties`:

```properties
# Server
server.port=8090

# MySQL
spring.datasource.url=jdbc:mysql://localhost:3306/smartcare
spring.datasource.username=root
spring.datasource.password=your_password

# JPA
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true

# MongoDB
spring.data.mongodb.uri=mongodb://localhost:27017/smartcare_db

# JWT
jwt.secret=your_base64_encoded_secret
jwt.expiration=604800000

# AI (Optional)
spring.ai.openai.api-key=your_openai_key
spring.ai.openai.chat.options.model=gpt-4o-mini
spring.ai.anthropic.api-key=your_anthropic_key
```

## API Documentation

### Base URL
```
http://localhost:8090/api
```

### Authentication Endpoints

| Method | Endpoint | Description | Access |
|--------|-----------|-------------|--------|
| POST | `/auth/login` | Login and get JWT token | Public |

**Login Request:**
```json
{
    "username": "string",
    "password": "string"
}
```

**Login Response:**
```
"jwt_token_here"
```

### Doctor Endpoints

| Method | Endpoint | Description | Access |
|--------|-----------|-------------|--------|
| POST | `/doctors` | Create doctor | ADMIN |
| GET | `/doctors/{id}` | Get doctor by ID | ADMIN, DOCTOR, PATIENT |
| GET | `/doctors` | Get all doctors | Public |
| PUT | `/doctors/{id}` | Update doctor | ADMIN, DOCTOR |
| DELETE | `/doctors/{id}` | Delete doctor | ADMIN |
| GET | `/doctors/specialty?specialty=` | Get doctors by specialty | ADMIN, PATIENT |

### Patient Endpoints

| Method | Endpoint | Description | Access |
|--------|-----------|-------------|--------|
| POST | `/patients` | Create patient | ADMIN |
| GET | `/patients/{id}` | Get patient by ID | ADMIN, DOCTOR, PATIENT |
| GET | `/patients` | Get all patients | ADMIN, DOCTOR |
| PUT | `/patients/{id}` | Update patient | ADMIN, PATIENT |
| DELETE | `/patients/{id}` | Delete patient | ADMIN |
| GET | `/patients/search/{username}` | Search patient by username | ADMIN, DOCTOR, PATIENT |

**Update Patient Request:**
```json
{
    "user": {
        "fullName": "John Doe Updated",
        "email": "john.updated@example.com",
        "phone": "0987654321"
    },
    "gender": "Male",
    "dateOfBirth": "1990-05-15",
    "address": "456 New Street",
    "bloodType": "A+"
}
```

### Appointment Endpoints

| Method | Endpoint | Description | Access |
|--------|-----------|-------------|--------|
| POST | `/appointments` | Book appointment | PATIENT |
| PATCH | `/appointments/{id}` | Cancel appointment | PATIENT |
| PATCH | `/appointments/{id}/complete` | Mark as completed | DOCTOR, ADMIN |
| GET | `/appointments/doctor/{doctorId}?date=` | Get doctor's appointments | DOCTOR, ADMIN |
| GET | `/appointments/available-slots/{doctorId}?date=` | Get available slots | Authenticated |

**Book Appointment Request:**
```json
{
    "doctorId": 1,
    "patientId": 1,
    "date": "2026-04-29",
    "time": "10:00"
}
```

### Prescription Endpoints (MongoDB)

| Method | Endpoint | Description | Access |
|--------|-----------|-------------|--------|
| POST | `/prescriptions` | Create prescription | DOCTOR |
| PUT | `/prescriptions/{id}` | Update prescription | DOCTOR |
| DELETE | `/prescriptions/{id}` | Delete prescription | DOCTOR |
| GET | `/prescriptions/patient/{patientId}` | View patient prescriptions | DOCTOR, PATIENT |

## Testing

Run all tests:
```bash
mvn test
```

### Test Coverage (55 tests)
- **Service Layer**: AppointmentService (14), DoctorService (11), PatientService (11)
- **Controller Layer**: AppointmentController (4), DoctorController (7), PatientController (7)
- **Integration**: SmartCareApplicationTests (1)

### Test Scenarios Covered
-  Prevent double booking (appointment)
-  Appointment cancellation rules
-  CRUD operations for doctors & patients
-  Duplicate username/email handling
-  Role-based access control

## Contributing

1. Fork the repository
2. Create your feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.
