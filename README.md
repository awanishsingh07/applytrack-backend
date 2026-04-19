# ApplyTrack — Backend

A RESTful backend API built with Spring Boot for tracking job applications.
Handles user authentication, resume management, and application tracking.

## 🔗 Links
- **Live API**: https://applytrack-backend.onrender.com
- **Frontend Repo**: https://github.com/awanishsingh07/applytrack-frontend

---

## 🛠️ Tech Stack

| Technology | Purpose |
|------------|---------|
| Java 21 | Programming language |
| Spring Boot 4 | Backend framework |
| Spring Security | Authentication & authorization |
| JWT (jjwt 0.11.5) | Stateless token-based auth |
| BCrypt | Password hashing |
| PostgreSQL | Relational database |
| Neon | Serverless PostgreSQL hosting |
| Hibernate / JPA | ORM for database interaction |
| Lombok | Reduce boilerplate code |
| Docker | Containerization for deployment |
| Render | Cloud deployment platform |

---

## ✨ Features

- **JWT Authentication** — Secure register/login with token-based auth
- **BCrypt Password Hashing** — Passwords never stored in plain text
- **Resume Management** — Add, edit, delete resumes with URL or PDF link
- **Application Tracking** — Full CRUD for job applications
- **Ownership Checks** — Users can only access their own data
- **Global Exception Handler** — Clean JSON error responses
- **CORS Configuration** — Configured for frontend cross-origin requests
- **Stateless Sessions** — No server-side sessions, JWT only

---

## 📁 Project Structure

```
src/main/java/com/awanish/applytrack_backend/
├── config/
│   ├── SecurityConfig.java       ← Spring Security + CORS config
│   └── JwtFilter.java            ← JWT request filter
├── controller/
│   ├── AuthController.java       ← /api/auth/register, /api/auth/login
│   ├── ApplicationController.java← /api/applications
│   ├── ResumeController.java     ← /api/resumes
│   └── UserController.java       ← /api/users
├── entity/
│   ├── User.java
│   ├── Application.java
│   └── Resume.java
├── repository/
│   ├── UserRepository.java
│   ├── ApplicationRepository.java
│   └── ResumeRepository.java
├── service/
│   └── ApplicationService.java
├── exception/
│   ├── GlobalExceptionHandler.java
│   └── ErrorResponse.java
└── util/
    └── JwtUtil.java
```

---

## 🚀 API Endpoints

### Auth
| Method | Endpoint | Description | Auth Required |
|--------|----------|-------------|---------------|
| POST | `/api/auth/register` | Register new user | No |
| POST | `/api/auth/login` | Login, returns JWT | No |

### Applications
| Method | Endpoint | Description | Auth Required |
|--------|----------|-------------|---------------|
| POST | `/api/applications/{resumeId}` | Add application | Yes |
| GET | `/api/applications` | Get all applications | Yes |
| PUT | `/api/applications/{id}` | Update application | Yes |
| DELETE | `/api/applications/{id}` | Delete application | Yes |

### Resumes
| Method | Endpoint | Description | Auth Required |
|--------|----------|-------------|---------------|
| POST | `/api/resumes` | Add resume | Yes |
| GET | `/api/resumes` | Get all resumes | Yes |
| PUT | `/api/resumes/{id}` | Update resume | Yes |
| DELETE | `/api/resumes/{id}` | Delete resume | Yes |

---

## ⚙️ Local Setup

### Prerequisites
- Java 21
- Maven
- PostgreSQL or Neon account

### Step 1 — Clone the repo
```bash
git clone https://github.com/awanishsingh07/applytrack-backend.git
cd applytrack-backend
```

### Step 2 — Create Neon database
1. Go to [neon.tech](https://neon.tech) → create a project
2. Copy the JDBC connection string

### Step 3 — Create `application-local.properties`
Create this file in `src/main/resources/`:
```properties
spring.datasource.url=jdbc:postgresql://your-neon-host/your-db?sslmode=require
spring.datasource.username=your-username
spring.datasource.password=your-password
spring.datasource.driver-class-name=org.postgresql.Driver
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.open-in-view=false
jwt.secret=your_local_secret_key_minimum_32_characters
```

### Step 4 — Run the application
```bash
./mvnw spring-boot:run -Dspring-boot.run.profiles=local
```

App starts at `http://localhost:8080`

### Step 5 — Test the API
```bash
# Register
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{"name":"Test User","email":"test@gmail.com","password":"123456"}'

# Login
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"email":"test@gmail.com","password":"123456"}'
```

---

## 🐳 Docker

### Build image
```bash
docker build -t applytrack-backend .
```

### Run container
```bash
docker run -p 8080:8080 \
  -e DB_URL="jdbc:postgresql://your-neon-host/your-db?sslmode=require" \
  -e DB_USERNAME="your-username" \
  -e DB_PASSWORD="your-password" \
  -e JWT_SECRET="your_secret_key_minimum_32_characters" \
  applytrack-backend
```

---

## 🌍 Deployment

### Environment Variables (Render)
| Key | Description |
|-----|-------------|
| `DB_URL` | Neon JDBC connection URL |
| `DB_USERNAME` | Neon database username |
| `DB_PASSWORD` | Neon database password |
| `JWT_SECRET` | Secret key for JWT signing (min 32 chars) |

### Deploy steps
1. Push code to GitHub
2. Connect repo to Render
3. Select **Docker** runtime
4. Add environment variables
5. Deploy — Render builds and runs the Docker container

---

## 🔐 Security Approach

- All passwords hashed with **BCrypt** before storing
- JWT tokens expire after **7 days**
- Every protected endpoint validates JWT in the `Authorization: Bearer <token>` header
- Users can only read/update/delete **their own** applications and resumes
- CORS restricted to known frontend origins only
- Stateless session management — no cookies or server-side sessions

---