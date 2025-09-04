# Event-Registration-System
Event Registration System for organizing and managing events.

---

# Spring Boot Application Setup & Run Guide

## 1. Prerequisites

Before you start, ensure you have the following installed on your system:

- **Java Development Kit (JDK) 17 or above**  
  Check installation:
  ```bash
  java -version
  ```

* **Maven 3.8+**
  Check installation:

  ```bash
  mvn -v
  ```
* **PostgreSQL 13+** (or your configured database)
  Make sure a database is created for the application.

---

## 2. Clone the Project

```bash
git clone https://github.com/usershivhare/Event-Registration-System.git
cd <project-folder>
```

---

## 3. Configure Application Properties

Edit `src/main/resources/application.properties` (or `application.yml`) to set your database and JWT configurations:

```properties
# Server
server.port=8081

# Database
spring.datasource.url=jdbc:postgresql://localhost:5432/eventdb
spring.datasource.username=postgres
spring.datasource.password=your_password
spring.jpa.hibernate.ddl-auto=update

# JWT configuration (example)
jwt.secret=your_secret_key
jwt.expiration=3600000
```

> ⚠️ Make sure your PostgreSQL server is running and the database exists.

---

## 4. Build the Project

Use Maven to build the project:

```bash
mvn clean install
```

This will:

* Download all dependencies.
* Compile the code.
* Run unit tests (optional).

---

## 5. Run the Application

### Option 1: Using Maven

```bash
mvn spring-boot:run
```

### Option 2: Using JAR

```bash
java -jar target/event-registration-0.0.1-SNAPSHOT.jar
```

Once started, the application will run on:

```
http://localhost:8081
```

---

## 6. Verify Application is Running

Open a browser or use **Postman/cURL** to check the base URL:

```bash
curl http://localhost:8081/actuator/health
```

Expected response:

```json
{"status":"UP"}
```

---

## 7. Additional Notes

* Ensure **ports are free** (default 8081). Change `server.port` in `application.properties` if needed.
* If changed then make sure to update that new port to every curl command/request that you hit.
* All APIs are **secured using JWT**, except authentication endpoints (`/auth/register`, `/auth/login`).
* Use **Postman or Swagger** for testing APIs.

##

# User Registration, Authentication, and Accessing Secured APIs

The Event Registration System uses JWT-based authentication. Below is the step-by-step guide:

## 1. User Registration

**Endpoint:** `POST /auth/register`
**Description:** Create a new user account.

**Request Body Example:**

```json
{
  "username": "user",
  "password": "password123"
}
```

**Response Example:**

```json
"User registered successfully"
```

> Note: User accounts are created with default `ROLE_USER`.

---

## 2. User Authentication (Login)

**Endpoint:** `POST /auth/login`
**Description:** Authenticate a registered user and obtain a JWT token.

**Request Body Example:**

```json
{
  "username": "user",
  "password": "password123"
}
```

**Response Example:**

```json
"eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
```

> Save this token, it is required to access secured APIs.

---

## 3. Accessing Secured APIs

All endpoints under `/events` and `/registrations` require a valid JWT in the **Authorization header**:

```
Authorization: Bearer <JWT_TOKEN>
```

### Example API Usage

#### a) Get All Events

**Endpoint:** `GET /events`
**Response:**

```json
[
  {
    "id": 1,
    "name": "Hackathon",
    "date": "2025-09-04",
    "location": "Berlin",
    "description": "Tech event"
  }
]
```

## 4. Notes

* Always include the JWT token in `Authorization` header for secured APIs.
* Event creation (`POST /events`) and updates are restricted to users with administrative privileges if implemented.
* You can use tools like **Postman** or **Swagger UI** to explore all endpoints.

##

Base URL: `http://localhost:8081`

---

# API Endpoints & cURL Commands

All APIs use JSON. Secured APIs require JWT in **Authorization header**.

---

## 1. User Registration

**Endpoint:** `POST /auth/register`

```bash
curl -X POST http://localhost:8081/auth/register \
-H "Content-Type: application/json" \
-d '{"username":"user","password":"password123"}'
```

**Response:** `"User registered successfully"`

---

## 2. User Login

**Endpoint:** `POST /auth/login`

```bash
curl -X POST http://localhost:8081/auth/login \
-H "Content-Type: application/json" \
-d '{"username":"user","password":"password123"}'
```

**Response:** JWT Token string, e.g.:

```
"eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
```

> Save this token to use for all secured API calls below.

---

## 3. Get All Events

**Endpoint:** `GET /events`
**JWT Secured**

```bash
curl -X GET http://localhost:8081/events \
-H "Authorization: Bearer <JWT_TOKEN>"
```

**Response Example:**

```json
[
  {"id":1,"name":"Hackathon","date":"2025-09-04","location":"Berlin","description":"Tech event"}
]
```

---

## 4. Get Event by ID

**Endpoint:** `GET /events/{id}`

```bash
curl -X GET http://localhost:8081/events/1 \
-H "Authorization: Bearer <JWT_TOKEN>"
```

**Response Example:**

```json
{"id":1,"name":"Hackathon","date":"2025-09-04","location":"Berlin","description":"Tech event"}
```

---

## 5. Create Event

**Endpoint:** `POST /events` (Admin only if implemented)

```bash
curl -X POST http://localhost:8081/events \
-H "Authorization: Bearer <JWT_TOKEN>" \
-H "Content-Type: application/json" \
-d '{"name":"Tech Meetup","date":"2025-10-01","location":"London","description":"Networking event"}'
```

**Response Example:**

```json
{"id":2,"name":"Tech Meetup","date":"2025-10-01","location":"London","description":"Networking event"}
```

---

## 6. Update Event

**Endpoint:** `PUT /events/{id}`

```bash
curl -X PUT http://localhost:8081/events/2 \
-H "Authorization: Bearer <JWT_TOKEN>" \
-H "Content-Type: application/json" \
-d '{"id":2,"name":"Tech Meetup Updated","date":"2025-10-01","location":"London","description":"Updated description"}'
```

**Response Example:**

```json
{"id":2,"name":"Tech Meetup Updated","date":"2025-10-01","location":"London","description":"Updated description"}
```

---

## 7. Delete Event

**Endpoint:** `DELETE /events/{id}`

```bash
curl -X DELETE http://localhost:8081/events/2 \
-H "Authorization: Bearer <JWT_TOKEN>"
```

**Response Example:** `"OK"`

---

## 8. Register for Event

**Endpoint:** `POST /registrations?eventId={id}`

```bash
curl -X POST "http://localhost:8081/registrations?eventId=1" \
-H "Authorization: Bearer <JWT_TOKEN>"
```

**Response Example:**

```json
{"id":1,"user":{"id":1,"username":"user"},"event":{"id":1,"name":"Hackathon"},"registrationDate":"2025-09-04T14:16:44"}
```

---

## 9. Get My Registrations

**Endpoint:** `GET /registrations`

```bash
curl -X GET http://localhost:8081/registrations \
-H "Authorization: Bearer <JWT_TOKEN>"
```

**Response Example:**

```json
[
  {"id":1,"event":{"id":1,"name":"Hackathon"},"user":{"id":1,"username":"user"},"registrationDate":"2025-09-04T14:16:44"}
]
```

---

## 10. Cancel Registration

**Endpoint:** `DELETE /registrations/{id}`

```bash
curl -X DELETE http://localhost:8081/registrations/1 \
-H "Authorization: Bearer <JWT_TOKEN>"
```

**Response Example:** `"Registration cancelled"`

> ⚠️ Users can only cancel their own registrations.

---

# Predefined data in the database
There are 2 predefined users as you can see in import.sql file.

Regular user 1: alice.
Regular user 2: bob.
All passwords are 123.

---

# Conclusion

This Event Registration System provides a fully functional backend for managing users, events, and registrations. With JWT-based security, only authenticated users can register for events and manage their own registrations. Admin users (if implemented) can create, update, and delete events.

By following the steps and using the provided cURL commands, you can:

- Set up and run the application locally.
- Register users and obtain JWT tokens.
- Create, view, update, and delete events.
- Register for events, view your registrations, and cancel them if needed.

This documentation serves as a foundation for both development and testing. You can extend it further by integrating a frontend or automating API tests for continuous validation.

Enjoy exploring and extending the Event Registration System! 🎉
