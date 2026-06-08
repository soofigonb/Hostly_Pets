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

```bash
Hostly_Pets/
│
├── hostly-usuarios-service
├── hostly-propiedades-service
├── hostly-reservas-service
├── hostly-pagos-service
│
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
