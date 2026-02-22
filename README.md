# Shop App - Ứng dụng E-commerce

Ứng dụng web e-commerce được xây dựng bằng Spring Boot, cung cấp các chức năng quản lý sản phẩm, đơn hàng, người dùng và xác thực JWT.

## 📋 Mục lục

- [Tính năng](#tính-năng)
- [Công nghệ sử dụng](#công-nghệ-sử-dụng)
- [Yêu cầu hệ thống](#yêu-cầu-hệ-thống)
- [Cài đặt](#cài-đặt)
- [Cấu hình](#cấu-hình)
- [Chạy ứng dụng](#chạy-ứng-dụng)
- [API Endpoints](#api-endpoints)
- [Cấu trúc dự án](#cấu-trúc-dự-án)
- [Tác giả](#tác-giả)

## ✨ Tính năng

- **Quản lý người dùng**: Đăng ký, đăng nhập với JWT authentication
- **Quản lý sản phẩm**: CRUD sản phẩm, tìm kiếm, phân trang
- **Quản lý danh mục**: Quản lý các danh mục sản phẩm
- **Quản lý đơn hàng**: Tạo và quản lý đơn hàng
- **Upload hình ảnh**: Upload và quản lý hình ảnh sản phẩm (tối đa 5 ảnh/sản phẩm)
- **Bảo mật**: Spring Security với JWT token
- **Validation**: Kiểm tra dữ liệu đầu vào với Bean Validation

## 🛠 Công nghệ sử dụng

- **Java 17**
- **Spring Boot 3.5.10**
- **Spring Data JPA** - ORM và quản lý database
- **Spring Security** - Bảo mật ứng dụng
- **MySQL** - Database
- **JWT (JSON Web Token)** - Xác thực người dùng
- **Lombok** - Giảm boilerplate code
- **ModelMapper** - Mapping giữa DTO và Entity
- **JavaFaker** - Tạo dữ liệu giả cho testing
- **Maven** - Quản lý dependencies

## 📦 Yêu cầu hệ thống

- JDK 17 hoặc cao hơn
- Maven 3.6+
- MySQL 8.0+
- IDE (IntelliJ IDEA, Eclipse, VS Code)

## 🔧 Cài đặt

### 1. Clone repository

```bash
git clone <repository-url>
cd shopapp
```

### 2. Tạo database MySQL

Tạo database mới trong MySQL:

```sql
CREATE DATABASE shopapp;
```

### 3. Cấu hình database

Mở file `src/main/resources/application.yaml` và cập nhật thông tin kết nối database:

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/shopapp?useSSL=false&serverTimezone=Asia/Ho_Chi_Minh
    username: root
    password: your_password
```

### 4. Build project

```bash
mvn clean install
```

## ⚙️ Cấu hình

### JWT Configuration

Cấu hình JWT trong `application.yaml`:

```yaml
jwt:
  expiration: 2592000  # Thời gian hết hạn token (giây) - 30 ngày
  secretKey: your-secret-key-here
```

### File Upload Configuration

Giới hạn upload file được cấu hình trong `application.yaml`:

```yaml
spring:
  servlet:
    multipart:
      max-file-size: 10MB
      max-request-size: 10MB
```

## 🚀 Chạy ứng dụng

### Cách 1: Sử dụng Maven

```bash
mvn spring-boot:run
```

### Cách 2: Chạy từ IDE

1. Mở project trong IDE của bạn
2. Tìm file `ShopappApplication.java`
3. Chạy class `ShopappApplication`

### Cách 3: Chạy JAR file

```bash
mvn clean package
java -jar target/shopapp-0.0.1-SNAPSHOT.jar
```

Ứng dụng sẽ chạy tại: `http://localhost:8080`

## 📡 API Endpoints

### Authentication

- `POST /users/register` - Đăng ký người dùng mới
- `POST /users/login` - Đăng nhập và nhận JWT token

### Products

- `GET /products?page={page}&limit={limit}` - Lấy danh sách sản phẩm (phân trang)
- `GET /products/{id}` - Lấy thông tin sản phẩm theo ID
- `POST /products` - Tạo sản phẩm mới
- `PATCH /products/{id}` - Cập nhật sản phẩm
- `DELETE /products/{id}` - Xóa sản phẩm
- `POST /products/uploads/{id}` - Upload hình ảnh cho sản phẩm
- `POST /products/generateFakeProducts` - Tạo dữ liệu sản phẩm giả (testing)

### Categories

- `GET /categories` - Lấy danh sách danh mục
- `POST /categories` - Tạo danh mục mới
- `PUT /categories/{id}` - Cập nhật danh mục
- `DELETE /categories/{id}` - Xóa danh mục

### Orders

- `GET /orders` - Lấy danh sách đơn hàng
- `GET /orders/{id}` - Lấy thông tin đơn hàng theo ID
- `POST /orders` - Tạo đơn hàng mới
- `PUT /orders/{id}` - Cập nhật đơn hàng
- `DELETE /orders/{id}` - Xóa đơn hàng

### Order Details

- `GET /order_details` - Lấy danh sách chi tiết đơn hàng
- `POST /order_details` - Tạo chi tiết đơn hàng mới

## 📁 Cấu trúc dự án

```
shopapp/
├── src/
│   ├── main/
│   │   ├── java/com/project/shopapp/
│   │   │   ├── Components/          # Các component (JWT util)
│   │   │   ├── Configurations/      # Cấu hình (Security, Mapper)
│   │   │   ├── Controller/          # REST Controllers
│   │   │   ├── DTO/                 # Data Transfer Objects
│   │   │   │   ├── request/         # Request DTOs
│   │   │   │   └── response/        # Response DTOs
│   │   │   ├── Exception/           # Custom exceptions
│   │   │   ├── filter/              # JWT Filter
│   │   │   ├── Model/               # Entity models
│   │   │   ├── Repository/          # JPA Repositories
│   │   │   ├── Service/             # Business logic
│   │   │   │   ├── Interface/       # Service interfaces
│   │   │   │   └── impl/            # Service implementations
│   │   │   └── ShopappApplication.java
│   │   └── resources/
│   │       ├── application.yaml     # Application configuration
│   │       └── static/              # Static files
│   └── test/                        # Test files
├── uploads/                         # Thư mục lưu hình ảnh upload
├── pom.xml                          # Maven dependencies
└── README.md                        # File này
```

## 🔐 Bảo mật

- JWT token được sử dụng để xác thực người dùng
- Spring Security được cấu hình để bảo vệ các endpoints
- Password được mã hóa trước khi lưu vào database
- Validation đầu vào để ngăn chặn dữ liệu không hợp lệ

## 📝 Ghi chú

- Đảm bảo MySQL đã được cài đặt và chạy trước khi khởi động ứng dụng
- Thư mục `uploads/` sẽ được tạo tự động khi upload hình ảnh lần đầu
- JWT token cần được gửi trong header `Authorization: Bearer {token}` cho các API yêu cầu xác thực

## 👤 Tác giả

Dự án được phát triển bởi [Tên tác giả]

## 📄 License

Dự án này được phát hành dưới license [Tên license]
