# Bookstore Microservices

Hệ thống Bookstore được xây dựng dựa trên kiến trúc microservices, bao gồm các service sau:

## Các Service

- **auth-service**: Xác thực và phân quyền người dùng
- **order-service**: Quản lý đơn hàng
- **payment-service**: Xử lý thanh toán qua Payos
- **user-service**: Quản lý thông tin người dùng
- **service-discovery**: Eureka Service Registry

## Yêu cầu hệ thống

- Java 17
- Maven
- MariaDB/MySQL
- Docker (tuỳ chọn)

## Cài đặt và chạy

### Sử dụng Maven

```bash
# Compile và chạy từng service
cd service-discovery
mvn clean install
mvn spring-boot:run

# Compile và chạy các service khác tương tự
```

### Sử dụng Docker

```bash
# Build và chạy toàn bộ hệ thống
docker-compose up --build
```

## Cấu trúc API

- Auth Service: http://localhost:8002
- User Service: http://localhost:8001
- Order Service: http://localhost:8005
- Payment Service: http://localhost:8989
- Service Discovery: http://localhost:8761
