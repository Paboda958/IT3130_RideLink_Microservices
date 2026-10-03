# RideLink - Backend Microservices for a Ride-Sharing Platform

**Course/Module:** IT3130 – Application Development  
**Assessment:** Group Assignment (30 Marks Total - 15 Group Marks + 15 Individual Marks)  
**Technology Stack:** Java 17, Spring Boot 3.2.5, Spring Security (JWT), Spring Data MongoDB, MongoDB, Springdoc OpenAPI (Swagger UI), Maven.

---

## 📌 Executive Summary & Mandatory Compliance

As per assignment rules and mandatory technical requirements:
1. **100% Java and Spring Boot**: All 4 microservices are implemented strictly in Java using Spring Boot 3.x. The MERN stack (Node.js/Express) is not used.
2. **Database Separation**: Each microservice strictly owns its independent database/persistence boundary (`accountdb`, `driverdb`, `ridedb`, `paymentdb`). Direct cross-service database access is strictly prohibited.
3. **API First & Swagger Documentation**: Each service exposes OpenAPI/Swagger documentation at `/swagger-ui.html`. No frontend application is required; testing and demonstration are carried out via Swagger UI and Postman.

---

## 🏛️ Architecture & Service Boundaries

RideLink is decomposed into **four independent core microservices**:

```
                              ┌──────────────────────────────────┐
                              │     Client / Postman / Swagger   │
                              └────────────────┬─────────────────┘
                                               │
              ┌────────────────────────┬───────┴────────┬────────────────────────┐
              │ REST (Port 8081)       │ REST (Port 8082)│ REST (Port 8083)       │ REST (Port 8084)
              ▼                        ▼                ▼                        ▼
    ┌──────────────────┐     ┌──────────────────┐    ┌──────────────────┐     ┌──────────────────┐
    │ Account Service  │     │ Driver Service   │    │ Ride Service     │     │ Payment Service  │
    │   (Member 1)     │     │   (Member 2)     │    │   (Member 3)     │     │   (Member 4)     │
    └─────────┬────────┘     └─────────┬────────┘    └────────┬─────────┘     └─────────┬────────┘
              │                        │                      │                         │
              ▼                        ▼                      ▼                         ▼
 [ MongoDB: accountdb ]  [ MongoDB: driverdb ]  [ MongoDB: ridedb ]  [ MongoDB: paymentdb ]
```

### Member Ownership & Responsibilities

| # | Microservice | Primary Owner | Key Responsibilities & Capabilities |
|---|---|---|---|
| 1 | **Account Service** | Member 1 | User registration (Passenger/Driver), JWT authentication, Role-based authorization (`ROLE_PASSENGER`, `ROLE_DRIVER`, `ROLE_ADMIN`), User profile viewing/updating, Account status management (`ACTIVE`, `SUSPENDED`, `INACTIVE`). |
| 2 | **Driver & Vehicle Service** | Member 2 | Driver operational profiles, Vehicle details (make, model, license plate, vehicle type), Availability status (`AVAILABLE`, `OFF_DUTY`, `BUSY`), Simulated location tracking, Retrieval of eligible available drivers. |
| 3 | **Ride Management Service** | Member 3 | Ride request creation, Driver assignment (manual & auto-matching via Driver Service), Ride lifecycle state machine (`REQUESTED` -> `ASSIGNED` -> `ACCEPTED` -> `IN_PROGRESS` -> `COMPLETED` / `CANCELLED`), Ride lookup. |
| 4 | **Fare & Payment Service** | Member 4 | Fare estimation rule calculation, Final fare calculation, Simulated payment recording (`PENDING`, `SUCCESS`, `FAILED`), Payment method selection (`CREDIT_CARD`, `DEBIT_CARD`, `CASH`, `WALLET`), Receipt generation. |

---

## 🔄 Interservice Communication Design

RideLink utilizes context-appropriate **Synchronous REST communication** via Spring `RestTemplate` with DTO contracts, structured error responses, and fallback mechanics:
1. **Ride Service ➔ Driver Service**: When a ride is requested with auto-assignment, `Ride Service` queries `Driver Service` (`GET /api/v1/drivers/available`) to find eligible drivers and updates driver status to `BUSY` (`PATCH /api/v1/drivers/{id}/availability`). Upon ride completion or cancellation, `Ride Service` frees up the driver (`AVAILABLE`).
2. **Ride Service ➔ Payment Service**: When creating a ride request, `Ride Service` calls `Payment Service` (`POST /api/v1/payments/fare-estimate`) to calculate estimated fares based on pickup, destination, and distance.

---

## 🚀 Port Allocation & Swagger UI Endpoints

| Microservice | Port | Base URL | Swagger UI Documentation | MongoDB database |
|---|---|---|---|---|
| **Account Service** | 8081 | `http://localhost:8081` | `http://localhost:8081/swagger-ui.html` | `accountdb` |
| **Driver Service** | 8082 | `http://localhost:8082` | `http://localhost:8082/swagger-ui.html` | `driverdb` |
| **Ride Service** | 8083 | `http://localhost:8083` | `http://localhost:8083/swagger-ui.html` | `ridedb` |
| **Payment Service** | 8084 | `http://localhost:8084` | `http://localhost:8084/swagger-ui.html` | `paymentdb` |

---

## 🛠️ Build and Execution Instructions

### Prerequisites
- **Java JDK 17** or higher (`java -version`)
- **Apache Maven 3.8+** (`mvn -version`)
- **MongoDB 6+** running on `localhost:27017`; optional service-specific overrides are `ACCOUNT_MONGODB_URI`, `DRIVER_MONGODB_URI`, `RIDE_MONGODB_URI`, and `PAYMENT_MONGODB_URI`.

### 1. Build All Microservices & Run Unit Tests
To build the complete parent multi-module solution and execute unit tests across all 4 microservices:
```bash
mvn clean test
```

To package all services into executable JARs:
```bash
mvn clean package -DskipTests=true
```

### 2. Running the Microservices
Start each service in a separate terminal window or run them in the background:

**Terminal 1: Account Service (Port 8081)**
```bash
cd account-service
mvn spring-boot:run
```

**Terminal 2: Driver & Vehicle Service (Port 8082)**
```bash
cd driver-service
mvn spring-boot:run
```

**Terminal 3: Ride Management Service (Port 8083)**
```bash
cd ride-service
mvn spring-boot:run
```

**Terminal 4: Fare & Payment Service (Port 8084)**
```bash
cd payment-service
mvn spring-boot:run
```

---

## 🧪 Postman Collection & Automated Testing

A complete Postman Collection and Environment are provided in the `postman/` directory:
- `postman/RideLink.postman_collection.json`
- `postman/RideLink.postman_environment.json`

### Key Test Scenarios Covered

#### 🟢 Positive End-to-End Workflow
1. **Account Registration & Token Issuance**: Register Passenger (`alice@example.com`) and Driver (`bob@example.com`); login to receive JWT tokens.
2. **Driver Setup**: Register driver profile & vehicle, set status to `AVAILABLE`, update location coordinates.
3. **Fare Estimation**: Request distance fare estimate (`Base 250 LKR + 120 LKR/km`).
4. **Ride Request & Auto-Driver Assignment**: Passenger requests ride; Ride Service assigns available driver `bob`, changing status to `ASSIGNED` and driver status to `BUSY`.
5. **Ride Lifecycle Transitions**: Driver accepts ride (`ACCEPTED`), starts ride (`IN_PROGRESS`), and completes ride (`COMPLETED`).
6. **Payment & Receipt**: Process simulated credit card payment for completed ride; generate itemized receipt.

#### 🔴 Negative & Validation Scenarios
1. **Invalid Authentication**: Login attempt with invalid credentials returns `400 Bad Request`.
2. **No Driver Available**: Ride request with auto-assign when no drivers are `AVAILABLE` returns `404 Not Found` with `NoDriverAvailableException`.
3. **Invalid Lifecycle Transition**: Attempting to move a `COMPLETED` ride back to `IN_PROGRESS` returns `400 Bad Request` with `InvalidStatusTransitionException`.
4. **Simulated Payment Failure**: Processing payment with `simulateFailure: true` returns `402 Payment Required` with `PaymentFailedException`.

---

## ⚙️ Continuous Integration (CI)

The repository includes a pre-configured GitHub Actions CI workflow in `.github/workflows/ci.yml`.  
On every `push` or `pull_request` to `main`, `develop`, or `feature/*` branches, the CI pipeline automatically:
1. Checks out the code and provisions Java JDK 17 environment.
2. Runs unit tests and verifies compilation for `account-service`, `driver-service`, `ride-service`, and `payment-service`.
3. Ensures zero breaking changes before merging pull requests.

---

## 📄 License & Academic Integrity

This project is submitted for the **IT3130 - Application Development Group Assignment**. All code, tests, and documentation have been implemented by group members in full accordance with academic integrity guidelines.
