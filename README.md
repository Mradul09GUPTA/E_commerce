# 🛒 E-Commerce Platform — Microservices Architecture

> A production-grade **E-Commerce backend** engineered with **Java 17 & Spring Boot**, decomposed into independently deployable microservices: User Authentication, Product Catalog, and Payment Processing — all wired through a **Netflix Eureka service registry**.

<p align="center">
  <img src="https://img.shields.io/badge/Java-17-orange?style=for-the-badge&logo=java" />
  <img src="https://img.shields.io/badge/Spring%20Boot-3.5-green?style=for-the-badge&logo=springboot" />
  <img src="https://img.shields.io/badge/MySQL-8.0-blue?style=for-the-badge&logo=mysql" />
  <img src="https://img.shields.io/badge/Razorpay-Payment-blueviolet?style=for-the-badge" />
  <img src="https://img.shields.io/badge/Eureka-Service%20Discovery-red?style=for-the-badge" />
  <img src="https://img.shields.io/badge/JWT-HS256-yellow?style=for-the-badge" />
</p>

---

## 📐 System Architecture

```
┌─────────────────────────────────────────────────────────────────┐
│                        CLIENT (HTTP)                            │
└───────────────────────┬─────────────────────────────────────────┘
                        │
          ┌─────────────▼──────────────┐
          │   Netflix Eureka Server    │  :8761
          │   (Service Discovery)      │
          └──┬──────────┬──────────────┘
             │          │          │
    ┌────────▼──┐  ┌────▼──────┐  ┌▼───────────┐
    │  User     │  │  Product  │  │  Payment   │
    │  Service  │  │  Service  │  │  Service   │
    │  :${PORT} │  │  :${PORT} │  │  :${PORT}  │
    └────────┬──┘  └────┬──────┘  └────────────┘
             │          │
    ┌────────▼──┐  ┌────▼──────┐        ┌──────────────┐
    │  MySQL    │  │  MySQL    │        │  Razorpay    │
    │userservice│  │productDB  │        │  Payment API │
    └───────────┘  └───────────┘        └──────────────┘
```

All microservices register with Eureka on startup. The Product Service uses `@LoadBalanced RestTemplate` to call `http://userservice/users/{id}` — letting Eureka resolve the User Service address dynamically, with built-in client-side load balancing.

---

## 🏛️ Microservices Breakdown

| Service | Port | Database | Key Responsibility |
|---|---|---|---|
| `Eureka` (Service Registry) | `8761` | — | Service discovery & health tracking |
| `userServices` | `${SERVER_PORT}` | `userservice` (MySQL) | Registration, JWT auth, session management |
| `product` | `${SERVER_PORT}` | `productServiceDB` (MySQL) | Product catalog, search, pagination, inter-service calls |
| `payment` | — | — | Razorpay payment link generation + webhook handling |

Each service is an **independent Spring Boot application** with its own `pom.xml`, `application.properties`, and entry point — deployable and scalable independently.

---

## ✨ Key Engineering Decisions & Features

### 🔐 Security — JWT + Stateful Sessions (Hybrid Model)
Rather than a purely stateless JWT approach, this system persists every token in a **`Session` table** — giving the server the ability to **invalidate tokens server-side** (logout, suspicious activity) without waiting for token expiry. On validation, the service:
1. Verifies the token exists in the `session` table
2. Parses and verifies the HMAC-SHA256 signature
3. Checks the `exp` claim against current system time
4. If expired → marks the session `INACTIVE` and throws `InvalidToken`

```java
// Stateful session — token stored in DB on login
private void addTokenInSession(String token, User user) {
    Session session = new Session();
    session.setToken(token);
    session.setUser(user);
    session.setState(State.ACTIVE);
    sessionRepo.save(session);
}
```

### 🔀 Strategy Pattern — Pluggable Product Sources
The Product Service implements the **Strategy Pattern** via a `ProductService` interface with two concrete implementations:

- `DBProductService` — `@Primary`, `@Qualifier("DBProductService")`: Reads from MySQL, used in production
- `FakeProductService` — wraps the public [FakeStoreAPI](https://fakestoreapi.com/), used for seeding / local dev without a database

Controllers inject the strategy via `@Qualifier`, making it trivially swappable without touching business logic. This mirrors how large systems isolate external data source contracts.

```java
@Autowired
@Qualifier("DBProductService")
ProductService productService;
```

### 📄 JPA Projections — Query Optimization
Instead of fetching full `Product` entities when only a subset of fields is needed, the repository uses **Spring Data Projections**:

```java
@Query("select p.id as id, p.title as title from Product p")
List<ProductWithTitleAndId> randomSearchMethodForProduct();
```

This avoids over-fetching and reduces deserialization overhead — a pattern used heavily in high-read systems.

### ⚖️ Client-Side Load Balancing via Eureka
The Product Service calls the User Service using a `@LoadBalanced RestTemplate` bean — Eureka resolves the logical service name `userservice` to actual instances, distributing load without a dedicated API gateway:

```java
@LoadBalanced
@Bean
public RestTemplate getRestTemplate() { ... }

// Usage in service
UserDto user = restTemplate
    .getForEntity("http://userservice/users/{userId}", UserDto.class, userId)
    .getBody();
```

### 📦 Pagination & Multi-Field Sorting
The Product Service exposes paginated endpoints with compound sorting — price ascending as primary sort, title descending as tiebreaker:

```java
Sort sort = Sort.by("price").ascending().and(Sort.by("title")).descending();
Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
Page<Product> products = productRepository.findAll(pageable);
```

The `SearchService` also supports **dynamic filter-driven sort direction** (Low-to-High / High-to-Low) via a `FilterProductDto`.

### 💳 Razorpay Payment Integration
The Payment Service follows an **Interface Segregation** design — a `PaymentGateway` interface allows plugging in any payment provider. The current `RazorpayPaymentGateway` implementation:
- Generates INR payment links with 10-minute expiry
- Attaches customer name, phone, and email
- Enables SMS + email payment reminders
- Provides webhook endpoint for payment confirmation callbacks

---

## 🛠️ Tech Stack

| Layer | Technology |
|---|---|
| Language | Java 17 |
| Framework | Spring Boot 3.5.x |
| Service Discovery | Spring Cloud Netflix Eureka |
| Security | Spring Security + JJWT 0.12.6 (HS256) |
| Password Hashing | BCrypt (Spring Security) |
| ORM | Spring Data JPA / Hibernate |
| Database | MySQL 8 |
| Inter-service Communication | RestTemplate (Load Balanced) |
| Payment Gateway | Razorpay Java SDK 1.4.8 |
| Build Tool | Maven |
| Boilerplate Reduction | Lombok |
| Testing | JUnit 5, Spring Boot Test, MockMvc, Mockito |

---

## 📁 Project Structure

```
E_commerce/
├── Eurka/                          # Service registry (Netflix Eureka Server)
│   └── src/main/java/com/example/eurka/
│       └── EurkaApplication.java  # @EnableEurekaServer
│
├── userServices/                   # Auth & User management microservice
│   └── src/main/java/com/example/userservices/
│       ├── controllers/
│       │   ├── AuthController.java     # POST /auth/signup, /auth/login, /auth
│       │   └── UserContoller.java      # GET /users/{id}
│       ├── services/
│       │   ├── AuthService.java        # JWT creation, BCrypt, session management
│       │   └── UserService.java        # User lookup by ID
│       ├── models/
│       │   ├── User.java               # @Entity: name, email, password, phone, roles
│       │   ├── Role.java               # @Entity: role value + state
│       │   ├── Session.java            # @Entity: token + user FK
│       │   ├── State.java              # Enum: ACTIVE / INACTIVE
│       │   └── BaseModel.java          # id, createdAt, updatedAt
│       ├── repos/
│       │   ├── UserRepo.java
│       │   ├── SessionRepo.java        # findByToken() for JWT validation
│       │   └── RoleRepo.java
│       ├── dtos/
│       │   ├── SignupUserDto.java       # firstName, lastName, email, password, phone
│       │   ├── LoginUserDto.java
│       │   └── UserDto.java            # email, role list, status
│       ├── exceptions/                 # UserNotFound, PasswordInvalid, InvalidToken, UserPresent
│       ├── controllerAdvices/
│       │   └── ExceptionAdvice.java    # @ControllerAdvice — global error handling
│       └── config/
│           └── AuthConfig.java         # BCryptPasswordEncoder, SecurityFilterChain, SecretKey beans
│
├── product/                        # Product catalog microservice
│   └── src/main/java/com/ecommerce/product/
│       ├── controller/
│       │   ├── ProductContoller.java   # CRUD + paginated listing + user-scoped search
│       │   └── SearchController.java   # Filter-based search with dynamic sorting
│       ├── services/
│       │   ├── ProductService.java     # Interface (Strategy contract)
│       │   ├── DBProductService.java   # @Primary — MySQL-backed implementation
│       │   ├── FakeProductService.java # FakeStoreAPI-backed (dev/seed)
│       │   └── SearchService.java      # Filter + sort + pagination logic
│       ├── model/
│       │   ├── Product.java            # title, price, @ManyToOne Category, @OneToMany ProductType
│       │   ├── Category.java           # name, description, @OneToMany products (LAZY)
│       │   ├── ProductType.java
│       │   └── Base.java               # Base entity with id
│       ├── repository/
│       │   └── ProductRepository.java  # JpaRepository + JPQL + Projections + Case-insensitive search
│       ├── projection/
│       │   └── ProductWithTitleAndId.java  # Interface-based projection
│       ├── dtos/                       # ProductDto, FakeProductDto, FilterProductDto, Filter, PriceFilter
│       ├── exception/                  # ProductNotFound, ProductsNotAvailable
│       ├── controllerAdvice/
│       │   └── GlobalException.java    # @RestControllerAdvice — centralized error response
│       └── configures/
│           └── ApplicationConfigure.java  # @LoadBalanced RestTemplate bean
│
└── payment/                        # Payment processing microservice
    └── src/main/java/com/ecomerce/payment/
        ├── contoller/
        │   └── PaymentContoller.java   # POST /payment, POST /payment/webhook
        ├── service/
        │   └── PaymentService.java     # Delegates to payment gateway
        ├── paymentGateway/
        │   ├── PaymentGateway.java     # Interface (extensible to Stripe, PayU, etc.)
        │   └── RazorpayPaymentGateway.java  # Razorpay implementation
        ├── dto/
        │   └── PaymentLinkReq.java     # orderId, amount, phoneNumber, name
        └── configs/
            └── RazorpayConfig.java     # RazorpayClient bean (key injected via @Value)
```

---

## 📡 API Reference

### 🔐 User Service — `http://localhost:{port}`

| Method | Endpoint | Request Body | Response | Description |
|---|---|---|---|---|
| `POST` | `/auth/signup` | `SignupUserDto` | `UserDto` | Register user — email normalized, password BCrypt hashed |
| `POST` | `/auth/login` | `LoginUserDto` | `UserDto` + `Set-Cookie: <JWT>` | Login — JWT returned in response header |
| `POST` | `/auth` | `String token` | `Boolean` | Verify token — checks DB session + expiry |
| `GET` | `/users/{id}` | — | `UserDto` | Fetch user by ID (called internally by Product Service) |

### 📦 Product Service — `http://localhost:{port}`

| Method | Endpoint | Params / Body | Response | Description |
|---|---|---|---|---|
| `GET` | `/product/{id}` | — | `ProductDto` | Fetch product by ID |
| `GET` | `/product` | `?pageNumber=0&pageSize=10` | `Page<Product>` | Paginated list, sorted by price ASC + title DESC |
| `POST` | `/product` | `ProductDto` | `ProductDto` | Create product with category |
| `PUT` | `/product/{id}` | `Product` | `ProductDto` | Replace product |
| `GET` | `/product/{name}/{userId}` | `?pageNumber&pageSize` | `Page<ProductDto>` | Search by title, access-gated by user role |
| `POST` | `/search` | `FilterProductDto` | `Page<Product>` | Filter & sort search |

### 💳 Payment Service — `http://localhost:{port}`

| Method | Endpoint | Request Body | Response | Description |
|---|---|---|---|---|
| `POST` | `/payment` | `PaymentLinkReq` | `String (short_url)` | Generate Razorpay payment link |
| `POST` | `/payment/webhook` | — | — | Razorpay payment status callback |

#### Payment Request Example
```json
{
  "orderID": 1001,
  "amount": 49900,
  "phoneNumber": "9876543210",
  "name": "Mradul Gupta"
}
```

---

## ⚙️ Local Setup & Running

### Prerequisites

- Java 17+
- Maven 3.8+
- MySQL 8 running locally
- Razorpay account ([sign up free](https://razorpay.com/))

### 1. Database Setup

```sql
CREATE DATABASE userservice;
CREATE DATABASE productServiceDB;

-- Create a dedicated user for product service
CREATE USER 'user_productServices'@'localhost' IDENTIFIED BY 'yourpassword';
GRANT ALL PRIVILEGES ON productServiceDB.* TO 'user_productServices'@'localhost';
```

### 2. Configure Application Properties

**`userServices/src/main/resources/application.properties`**
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/userservice
spring.datasource.username=root
spring.datasource.password=YOUR_PASSWORD
```

**`product/src/main/resources/application.properties`**
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/productServiceDB
spring.datasource.username=user_productServices
spring.datasource.password=YOUR_PASSWORD
SERVER_PORT=8082
```

**`payment/src/main/resources/application.properties`**
```properties
razorpy.key.id=YOUR_RAZORPAY_KEY_ID
razorpy.key.secret=YOUR_RAZORPAY_SECRET
```

> ⚠️ **Security Note:** Never commit real credentials. Move secrets to environment variables or use Spring Cloud Config / Vault in production.

### 3. Start Services (Order Matters)

```bash
# 1. Start Eureka first — all other services register to it
cd Eurka && mvn spring-boot:run
# Eureka dashboard: http://localhost:8761

# 2. Start User Service
cd ../userServices && SERVER_PORT=8081 mvn spring-boot:run

# 3. Start Product Service
cd ../product && SERVER_PORT=8082 mvn spring-boot:run

# 4. Start Payment Service
cd ../payment && mvn spring-boot:run
```

---

## 🔐 Security Architecture

```
User signs up  ──→  Password BCrypt-hashed ──→  Stored in MySQL
User logs in   ──→  BCrypt.matches() check ──→  JWT (HS256) generated
                                             ──→  Token saved to Session table (ACTIVE)
                                             ──→  JWT returned via Set-Cookie header

Subsequent request with token:
  ├── Session found in DB? ──→ No  → 401 InvalidToken
  ├── JWT signature valid? ──→ No  → 401 InvalidToken
  └── Token expired?       ──→ Yes → Session marked INACTIVE → 401 InvalidToken
                                     No  → ✅ Authorized
```

**Why stateful JWT sessions?** Pure stateless JWT cannot be revoked without a token blacklist or short expiry. Persisting sessions gives this system **server-side revocation** capability — critical for logout, account compromise response, and session management — while keeping the client-facing protocol JWT-based.

---

## 🧪 Testing

The project includes layered tests covering:

**Controller Layer** — `@WebMvcTest` with `MockMvc`:
- `GET /product/{id}` with valid ID → 200 + correct JSON body
- `GET /product/{id}` with missing product → 404 + error message
- `POST /product` with valid body → 200 + created product

**Service Layer** — `@SpringBootTest` with `@MockBean` repository:
- Positive ID → returns mocked product
- Negative ID → throws `ProductNotFound`
- ID not in DB → throws `ProductNotFound` with correct message

```bash
# Run all tests
mvn test

# Run tests for a specific module
cd product && mvn test
```

Test classes: `ProductContollerTest`, `ProductServiceTest`, `UserServicesApplicationTests`

---

## 📈 Design Patterns Used

| Pattern | Where | Why |
|---|---|---|
| **Strategy** | `ProductService` interface → `DBProductService` / `FakeProductService` | Swap data sources without changing consumers |
| **DTO (Data Transfer Object)** | `ProductDto`, `UserDto`, `PaymentLinkReq` | Decouple API contract from domain model |
| **Repository** | Spring Data JPA repositories | Abstracted persistence with zero boilerplate SQL |
| **Global Exception Handler** | `@RestControllerAdvice` in each service | Consistent error format across all endpoints |
| **Interface Segregation** | `PaymentGateway` interface | Future-proofs switching from Razorpay to Stripe/PayU |
| **Projection** | `ProductWithTitleAndId` | Partial entity fetch — avoids SELECT * anti-pattern |
| **Service Registry** | Netflix Eureka | Location transparency; no hardcoded service URLs |

---

## 🚀 Future Enhancements

- **API Gateway** (Spring Cloud Gateway) — centralized routing, rate limiting, and auth filter
- **Distributed Config** (Spring Cloud Config Server) — externalize all `application.properties`
- **Docker + Docker Compose** — containerize each service + MySQL for one-command startup
- **Kafka / RabbitMQ** — event-driven payment confirmation instead of polling webhook
- **Redis Caching** — cache product catalog with TTL to reduce DB load
- **CI/CD Pipeline** — GitHub Actions for build, test, and container image push
- **API Documentation** — Swagger/OpenAPI 3.0 annotations on all controllers

---

## 👤 Author

**Mradul Gupta**

[![GitHub](https://img.shields.io/badge/GitHub-Mradul09GUPTA-181717?style=flat&logo=github)](https://github.com/Mradul09GUPTA)

---

## 📄 License

This project is open source and available under the [MIT License](LICENSE).
