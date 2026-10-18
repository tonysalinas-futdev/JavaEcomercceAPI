# E-commerce

Aplicación desarrollada con Spring Boot para proporcionar funcionalidades de una tienda en línea. El proyecto sigue una arquitectura de Monolito Modular debido a su simplicidad de desarrollo y mantenimiento, sin sacrificar escalabilidad, testabilidad y capacidad de evolución.

## Características principales

* Gestión de productos y categorías: operaciones CRUD completas con validaciones y reglas de negocio.

* Autenticación y autorización: implementación basada en JWT con roles y permisos granulares.

* Carrito de compras: gestión de carritos e ítems, incluyendo cálculo automático de totales.

* Pagos: integración con Stripe y manejo de eventos para procesar pagos completados.

* Mensajería: envío de correos electrónicos para confirmaciones, recuperación de contraseñas y notificaciones.

* Logging y trazabilidad: logs técnicos, de auditoría y errores estructurados; propagación de `X-Correlation-ID` para seguimiento de solicitudes extremo a extremo.

* Eventos internos: publicación y consumo de eventos para desacoplar responsabilidades entre módulos.

* Versionado de API: versionado por URL para facilitar la evolución y mantenimiento de los endpoints.

* Testing: cobertura mediante tests unitarios y de integración para validar los flujos críticos de negocio.

* Caché: implementación del patrón Cache Aside utilizando Redis para reducir latencia y mejorar el rendimiento de las consultas.

* Idempotencia: soporte para claves de idempotencia en operaciones críticas como pagos y creación de órdenes.

* Separación de perfiles: configuraciones específicas para entornos de desarrollo, testing y producción.

* Monitorización y observabilidad: integración con Spring Boot Actuator y Spring Boot Admin para supervisión del estado de la aplicación, métricas y gestión operativa.

* Despliegue dockerizado: soporte completo para Docker y Docker Compose incluyendo PostgreSQL y Redis.

## Módulos principales

* Auth — autenticación, registro y refresco de tokens.

* Users — gestión de usuarios, roles y permisos.

* Categories — CRUD de categorías, búsquedas y filtros.

* Products — gestión de productos, búsquedas y filtrado.

* Cart — gestión de carritos e ítems por usuario.

* Payment — integración con Stripe, confirmación de pagos y manejo de idempotencia.

* Messaging — plantillas y envío de correos electrónicos.

* Logging — configuración centralizada de logs y propagación de trazabilidad.

* Tests — pruebas unitarias e integración documentadas.

## Requisitos

* Java 21

* Gradle

* PostgreSQL

* Redis

* Docker (opcional)

* Docker Compose (opcional)

* Configurar las variables de entorno siguiendo el archivo `.env.example`.

## Ejecutar la aplicación localmente

1. Abrir una terminal.

2. Compilar el proyecto:

```bash
./gradlew build
```

3. Aplicar formato de código (si es necesario):

```bash
./gradlew spotlessApply
```

4. Ejecutar la aplicación:

```bash
./gradlew bootRun
```

## Soporte Docker

La aplicación está completamente dockerizada mediante Docker Compose.

Los servicios incluidos son:

* Aplicación Spring Boot
* PostgreSQL
* Redis

Los contenedores utilizan health checks para garantizar el orden correcto de arranque y asegurar que la aplicación no se inicie hasta que las dependencias se encuentren disponibles.

## Ejecutar con Docker

### 1. Configurar variables de entorno

Crear y configurar el archivo `.env` tomando como referencia `.env.example`.

### 2. Construir e iniciar todos los servicios

```bash
docker compose up --build
```

### 3. Ejecutar los servicios en segundo plano

```bash
docker compose up -d --build
```

### 4. Detener los servicios

```bash
docker compose down
```

### 5. Eliminar servicios y volúmenes

```bash
docker compose down -v
```

## Documentación de la API

La aplicación incluye documentación interactiva mediante Swagger/OpenAPI para explorar y probar los endpoints disponibles.

Swagger UI:

```text
http://localhost:8080/swagger-ui.html
```

o

```text
http://localhost:8080/swagger-ui/index.html
```

Especificación OpenAPI:

```text
http://localhost:8080/v3/api-docs
```

## Monitorización y observabilidad

La aplicación expone endpoints de Spring Boot Actuator para supervisar el estado y comportamiento de los distintos componentes.

Endpoint de salud:

```text
http://localhost:8080/actuator/health
```

Información general:

```text
http://localhost:8080/actuator/info
```

Métricas:

```text
http://localhost:8080/actuator/metrics
```

Además, el proyecto integra Spring Boot Admin para proporcionar una interfaz centralizada para la monitorización de la aplicación, permitiendo visualizar métricas, estado de salud, información del sistema y otros endpoints de gestión.

## Tecnologías utilizadas

* Java 21
* Spring Boot
* Spring Security
* Spring Data JPA
* Spring Validation
* Spring Actuator
* Spring Boot Admin
* PostgreSQL
* Redis
* Stripe
* JWT
* Docker
* Docker Compose
* Gradle
* JUnit 5
* Mockito
* OpenAPI / Swagger