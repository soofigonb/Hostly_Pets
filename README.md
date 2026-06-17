# 🐾 Hostly Pets

> Plataforma de alojamientos **Pet Friendly** desarrollada con arquitectura de microservicios usando Spring Boot.

---

## ✨ Descripción

Hostly Pets es una plataforma enfocada exclusivamente en alojamientos que aceptan mascotas 🐶🐱.

El sistema permite:

- 👤 Registrar usuarios y anfitriones
- 🏠 Publicar propiedades
- 📅 Gestionar reservas
- 💳 Registrar pagos
- 🐾 Filtrar alojamientos según tipo y tamaño de mascota

---

## 🧩 Arquitectura de Microservicios

### 🌐 api-gateway (Puerto: 8080)
Punto de entrada único (API Gateway) que recibe y enruta todas las peticiones externas hacia los microservicios correspondientes.

### 🧭 eureka-server (Puerto: 8761)
Servidor de descubrimiento (Service Discovery) de Spring Cloud Netflix donde todos los microservicios se registran automáticamente.

### 👤 hostly-usuarios-service
Gestiona:
- Usuarios
- Roles
- Estados de usuario

### 🏠 hostly-propiedades-service
Gestiona:
- Propiedades
- Tipos de propiedad
- Tipo de mascota permitida
- Tamaño permitido
- Disponibilidad

### 📅 hostly-reservas-service
Gestiona:
- Reservas
- Estados de reserva
- Detalles de reserva

### 💳 hostly-pagos-service
Gestiona:
- Pagos
- Métodos de pago
- Estados de pago

---

## 🚀 Tecnologías Utilizadas

### Backend
- ☕ Java 17
- 🌱 Spring Boot
- 🔗 Spring Web
- 🗄️ Spring Data JPA
- ✅ Validation
- ☁️ OpenFeign
- 🌐 Spring Cloud Gateway
- 🧭 Spring Cloud Netflix Eureka
- 📖 SpringDoc OpenAPI (Swagger)

### Infraestructura y Despliegue
- 🐳 Docker & Docker Compose

### Base de Datos
- 🐘 PostgreSQL
- ☁️ Supabase

### Herramientas
- 🔄 Flyway
- 📦 Maven
- ✨ Lombok
- 🐙 Git & GitHub

---

## 📂 Estructura del Proyecto

Hostly_Pets/
│
├── api-gateway
├── eureka-server
├── hostly-usuarios-service
├── hostly-propiedades-service
├── hostly-reservas-service
├── hostly-pagos-service
│
├── docker-compose.yml
└── README.md
```

---

## 🧪 Testing y Cobertura

Pruebas unitarias desarrolladas con **JUnit 5** y **Mockito** (estructura `Given-When-Then`), validadas con **JaCoCo** para asegurar un mínimo de **80% de cobertura**.

### Ejecutar Tests
En la carpeta de cada microservicio, ejecuta:
```bash
mvn clean test
mvn clean verify
```
*(Debe superar el 80% de cobertura para obtener un `BUILD SUCCESS`)*

### Reporte de Cobertura
Para ver el reporte visual, abre en tu navegador:
```bash
./target/site/jacoco/index.html
```

---

## 🐳 Despliegue con Docker

El proyecto completo está contenedorizado. Puedes levantar toda la infraestructura (API Gateway, Eureka y los 4 microservicios) con un solo comando:

```bash
docker compose up --build -d
```

> [!NOTE]
> Al levantar Docker, los microservicios se conectan automáticamente a **Supabase** mediante el *Transaction Pooler* y se registran en **Eureka**.

---

## 📖 Documentación de APIs (Swagger)

Todas las APIs están documentadas dinámicamente con OpenAPI 3. Una vez levantados los contenedores, puedes explorar los endpoints, probar llamadas y ver los esquemas DTO en:

*   👤 **Usuarios:** [http://localhost:8081/doc/swagger-ui.html](http://localhost:8081/doc/swagger-ui.html)
*   🏠 **Propiedades:** [http://localhost:8082/doc/swagger-ui.html](http://localhost:8082/doc/swagger-ui.html)
*   📅 **Reservas:** [http://localhost:8083/doc/swagger-ui.html](http://localhost:8083/doc/swagger-ui.html)
*   💳 **Pagos:** [http://localhost:8084/doc/swagger-ui.html](http://localhost:8084/doc/swagger-ui.html)
