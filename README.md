# 🛒 E-Commerce Platform — Microservices Architecture

A production-style **E-Commerce backend** built with **Java & Spring Boot**, designed as independent microservices for User Authentication, Product Management, and Payment Processing.

---

## 🏛️ Microservices Overview

| Service | Responsibility |
|---|---|
| `userServices` | Auth, JWT session management, role-based access |
| `product` | Product catalog, categories, pagination & sorting |
| `payment` | Razorpay payment link generation & webhook handling |

Each service is independently deployable with its own `pom.xml` and Spring Boot application entry point.

---

## ✨ Features

- **JWT Authentication** — Signup, login, and token verification with session tracking. Tokens are signed using HMAC-SHA256 and validated on every request.
- **BCrypt Password Encryption** — Passwords are never stored in plain text; encrypted via Spring Security's `BCryptPasswordEncoder`.
- **Role-Based Access Control** — Users are assigned roles stored via a many-to-many JPA relationship.
- **Product Catalog with Pagination & Sorting** — Fetch products with configurable `pageNumber` / `pageSize` and multi-field sorting (price ascending + title descending).
- **Dual Product Service Strategy** — Supports both a `DBProductService` (database-backed, marked `@Primary`) and a `FakeProductService` (third-party API via `RestTemplate`) — switchable via Spring's `@Qualifier`.
- **Razorpay Payment Integration** — Generates secure payment links with customer details, expiry, INR currency, SMS/email notifications, and webhook callback support.
- **Global Exception Handling** — Centralised `@ControllerAdvice` for clean, consistent error responses across all services.
- **JPA Projections** — Uses Spring Data projections (`ProductWithTitleAndId`) to fetch only required fields, optimising DB queries.

---

## 🛠️ Tech Stack

| Layer | Technology |
|---|---|
| Language | Java 17 |
| Framework | Spring Boot |
| Security | Spring Security + JJWT (HS256) |
| ORM | Spring Data JPA / Hibernate |
| Database | MySQL |
| Payment Gateway | Razorpay SDK |
| Build Tool | Maven |
| Utilities | Lombok, Spring DevTools |
| Testing | Spring Boot Test (JUnit) |

---

## 📁 Project Structure

```
E_commerce/
├── userServices/               # Auth microservice
│   └── src/main/java/
│       ├── controllers/        # AuthController (login, signup, token verify)
│       ├── services/           # AuthService (JWT creation & validation)
│       ├── models/             # User, Role, Session, State
│       ├── repos/              # UserRepo, SessionRepo, RoleRepo
│       ├── dtos/               # LoginUserDto, SignupUserDto, UserDto
│       ├── exceptions/         # UserNotFound, PasswordInvalid, InvalidToken, UserPresent
│       └── config/             # AuthConfig (BCrypt, SecurityFilterChain, SecretKey)
│
├── product/                    # Product microservice
│   └── src/main/java/
│       ├── controller/         # ProductController (CRUD + paginated listing)
│       ├── services/           # ProductService interface, DBProductService, FakeProductService
│       ├── model/              # Product, Category (1:M relationship)
│       ├── repository/         # ProductRepository + custom projections
│       ├── dtos/               # ProductDto, FakeProductDto
│       └── exception/          # ProductNotFound, GlobalException (ControllerAdvice)
│
└── payment/                    # Payment microservice
    └── src/main/java/
        ├── contoller/          # PaymentController (create link, webhook)
        ├── service/            # PaymentService
        ├── paymentGateway/     # PaymentGateway interface, RazorpayPaymentGateway
        ├── dto/                # PaymentLinkReq
        └── configs/            # RazorpayConfig (RazorpayClient bean)
```

---

## 📡 API Endpoints

### 🔐 User Service (`/auth`)

| Method | Endpoint | Description |
|---|---|---|
| `POST` | `/auth/signup` | Register a new user (BCrypt encrypted password) |
| `POST` | `/auth/login` | Login — returns `UserDto` + JWT token via `Set-Cookie` header |
| `POST` | `/auth` | Verify JWT token validity and session state |

### 📦 Product Service (`/product`)

| Method | Endpoint | Description |
|---|---|---|
| `GET` | `/product/{id}` | Fetch a product by ID |
| `GET` | `/product?pageNumber=0&pageSize=10` | Paginated & sorted product listing |
| `POST` | `/product` | Create a new product with category |
| `PUT` | `/product/{id}` | Replace/update an existing product |

### 💳 Payment Service (`/payment`)

| Method | Endpoint | Description |
|---|---|---|
| `POST` | `/payment` | Generate a Razorpay payment link (returns `short_url`) |
| `POST` | `/payment/webhook` | Webhook endpoint for Razorpay payment callbacks |

---

## ⚙️ Getting Started

### Prerequisites

- Java 17+
- Maven 3.8+
- MySQL running locally
- Razorpay account (Key ID & Secret)

### Setup & Run

```bash
# Clone the repository
git clone https://github.com/Mradul09GUPTA/E_commerce.git
cd E_commerce
```

Configure each service in its `src/main/resources/application.properties`:

```properties
# userServices & product
spring.datasource.url=jdbc:mysql://localhost:3306/ecommerce_db
spring.datasource.username=your_username
spring.datasource.password=your_password

# payment service
razorpay.key.id=your_razorpay_key_id
razorpay.key.secret=your_razorpay_key_secret
```

Run each service independently:

```bash
# Terminal 1 — User Service
cd userServices && mvn spring-boot:run

# Terminal 2 — Product Service
cd product && mvn spring-boot:run

# Terminal 3 — Payment Service
cd payment && mvn spring-boot:run
```

---

## 🔐 Security Design

- JWT tokens signed with **HMAC-SHA256 (HS256)** carrying user info + expiry timestamp
- Tokens are **persisted in a Session table** — enabling server-side invalidation and state tracking (`ACTIVE` / `INACTIVE`)
- On **token expiry**, the session is automatically marked `INACTIVE` in the database
- Passwords encoded with **BCrypt** (adaptive hashing, industry standard)

---

## 🧪 Testing

Unit and integration tests included for service and controller layers:

```bash
mvn test
```

Test classes: `ProductServiceTest`, `ProductControllerTest`, `UserServicesApplicationTests`

---

## 👤 Author

**Mradul Gupta**
- GitHub: [@Mradul09GUPTA](https://github.com/Mradul09GUPTA)
