# 🔐 Secure Document Sharing System

A secure document-sharing platform built using **Java, Spring Boot, Spring Security, JWT, MySQL, and REST APIs**. The application allows users to upload documents, generate secure shareable links, and download files through time-limited public URLs.

---

## 🚀 Features

### Authentication & Security
- User Registration & Login
- BCrypt Password Encryption
- JWT-Based Authentication
- Stateless Security Configuration
- Protected REST APIs
- Role-ready Security Architecture

### Document Management
- Upload Documents
- Store File Metadata in MySQL
- Secure File Storage on Server
- View Uploaded Documents
- Retrieve Document Details

### Secure Sharing
- Generate UUID-Based Share Links
- Public File Download Without Login
- Automatic Link Expiry (5 Minutes)
- Share Link Validation
- Download Access Logging

### Exception Handling
- Centralized Global Exception Handling
- Structured JSON Error Responses
- Validation Error Handling

---

## 🛠️ Tech Stack

| Technology | Purpose |
|------------|----------|
| Java 17 | Programming Language |
| Spring Boot 3 | Backend Framework |
| Spring Security | Authentication & Authorization |
| JWT | Token-Based Security |
| Spring Data JPA | Database Operations |
| Hibernate | ORM Framework |
| MySQL | Relational Database |
| Maven | Build Tool |
| Lombok | Boilerplate Reduction |
| Jakarta Validation | Request Validation |
| Thymeleaf | UI Integration (Started) |
| HTML/CSS/JavaScript | Frontend Components |
| Postman | API Testing |

---

## 🏗️ Architecture

```text
Client
   │
   ▼
JWT Authentication Filter
   │
   ▼
Controller Layer
   │
   ▼
Service Layer
   │
   ▼
Repository Layer
   │
   ▼
MySQL Database

         │
         ▼
     File Storage
```

---

## 📂 Project Structure

```text
src/main/java/com/project/docshare

├── config
├── controller
├── service
├── repository
├── entity
├── dto
├── security
├── exception
└── SecureDocumentSharingApplication.java
```

---

## 🗄️ Database Design

### Users

| Column | Type |
|---------|------|
| id | BIGINT |
| username | VARCHAR |
| password | VARCHAR |
| enabled | BOOLEAN |

### Documents

| Column | Type |
|---------|------|
| id | BIGINT |
| original_filename | VARCHAR |
| content_type | VARCHAR |
| size_bytes | BIGINT |
| stored_path | VARCHAR |
| owner_id | BIGINT |

### Share Links

| Column | Type |
|---------|------|
| id | BIGINT |
| token | UUID |
| created_at | DATETIME |
| expires_at | DATETIME |
| document_id | BIGINT |

### Access Logs

| Column | Type |
|---------|------|
| id | BIGINT |
| token | VARCHAR |
| status | VARCHAR |
| accessed_at | DATETIME |

---

## 🔑 JWT Authentication Flow

```text
User Login
     │
     ▼
Spring Security Authentication
     │
     ▼
Generate JWT Token
     │
     ▼
Return Access Token
     │
     ▼
Client Sends Bearer Token
     │
     ▼
JWT Filter Validation
     │
     ▼
Access Protected APIs
```

---

## 📤 File Upload Workflow

```text
User Uploads File
        │
        ▼
DocumentController
        │
        ▼
DocumentService
        │
        ▼
Store File on Disk
        │
        ▼
Save Metadata in MySQL
        │
        ▼
Return Document Details
```

---

## 🔗 Share Link Workflow

```text
Authenticated User
         │
         ▼
Generate UUID Token
         │
         ▼
Save ShareLink Record
         │
         ▼
Set Expiry Time (5 Minutes)
         │
         ▼
Return Share URL
```

Example:

```text
http://localhost:8080/api/share/{uuid-token}
```

---

## 📡 REST API Endpoints

### Authentication

| Method | Endpoint | Description |
|----------|----------|----------|
| POST | /api/auth/register | Register User |
| POST | /api/auth/login | Login User |

### Documents

| Method | Endpoint | Description |
|----------|----------|----------|
| POST | /api/documents | Upload File |
| GET | /api/documents | Get All Documents |
| GET | /api/documents/{id} | Get Document By ID |

### Sharing

| Method | Endpoint | Description |
|----------|----------|----------|
| POST | /api/documents/{id}/share | Generate Share Link |
| GET | /api/share/{token} | Download Shared File |

---

## ⚙️ Installation & Setup

### Clone Repository

```bash
git clone https://github.com/your-username/secure-document-sharing-system.git
```

### Navigate to Project

```bash
cd secure-document-sharing-system
```

### Configure MySQL

```sql
CREATE DATABASE secure_document_sharing;
```

### Update application.properties

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/secure_document_sharing
spring.datasource.username=root
spring.datasource.password=root

spring.jpa.hibernate.ddl-auto=update

app.jwt.secret=your-secret-key
app.jwt.expiration-minutes=60

app.storage.root-directory=uploads
```

### Run Application

```bash
mvn spring-boot:run
```

---

## 🧪 API Testing

1. Register User
2. Login User
3. Copy JWT Token
4. Upload File
5. Generate Share Link
6. Open Share Link
7. Download File
8. Test Expired Link

---

## ✨ User Interface

The application provides a clean, responsive, and user-friendly interface built with **Thymeleaf, HTML, CSS, JavaScript, and Bootstrap**.

### UI Features

- 🔐 Professional Login Page
- 📝 Professional Registration Page
- 📂 Secure Dashboard
- 🛡️ Custom Application Header with Logo
- 🚪 Logout Functionality
- 📋 One-Click Copy Share Link
- 📱 Responsive Design
- 🎨 Modern Glassmorphism UI

---

## 🔮 Future Enhancements

- Refresh Token Support
- Role-Based Access Control (RBAC)
- Docker Deployment
- AWS S3 Storage
- Email-Based Sharing
- Virus Scanning
- Swagger/OpenAPI Documentation
- Admin Dashboard
- Download Analytics

---

## 👨‍💻 Author

**Pranav Kamble**  
B.E. Computer Science & Engineering  
Angadi Institute of Technology & Management (AITM)

### Connect with Me

- LinkedIn: https://www.linkedin.com/in/pranav-kamble-a29560255/

---

## ⭐ Key Concepts Demonstrated

- Core Java
- OOP Principles
- Collections Framework
- Exception Handling
- File Handling
- Spring Boot
- Spring Security
- JWT Authentication
- REST APIs
- JPA & Hibernate
- MySQL
- Maven
- Layered Architecture
- DTO Pattern
- Repository Pattern
- Secure File Sharing
