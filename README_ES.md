# E-commerce

Esta es una aplicación desarrollada con spring para proporcionar funcionalidades para una tienda en línea. Se utiliza como patrón arquitectónico un monolito modular debido a su "simplicidad" pero facilidad para escalar , testear y depurar.El proyecto está en fase final de desarrollo.

Características principales

* Gestión de productos y categorías: CRUD completo con validaciones y más

* Autenticación y autorización: JWT; roles y permisos granulares.

* Carrito de compras: añadir, actualizar y eliminar ítems; cálculo de totales.

* Pagos: integración con Stripe y uso de eventos para manejar pagos completados.

* Mensajería: envío de emails para confirmaciones, notificaciones y recuperación de contraseña.

* Logging y trazabilidad: logs técnicos, de auditoria y de error estructurados; X-Correlation-ID propagado en peticiones y logs para trazabilidad.

* Eventos internos: publicación/consumo de eventos para desacoplar responsabilidades.

* Patrones de diseño: uso de patrones como Facade para encapsular subsistemas complejos.

* Versionado por URL: rutas versionadas para facilitar evolución de la API.

* Testing: tests unitarios e integración para validar flujos críticos.

* Caché: implementación de una estrategia de cache aside utilizando redis para reducir latencia en consultas. 

* Idempotencia: soporte de claves de idempotencia en endpoints críticos (pagos, órdenes).

## Módulos principales

* Auth — login, signup, refresh tokens.

* Users — gestión de usuarios, permisos y roles.

* Categories —  crud para categorías, búsquedas y filtros.

* Product — endpoints para productos y categorías; búsquedas y filtros.

* Cart — gestión del carrito así como sus items por usuario .

* Payment — integración con Stripe; manejo de idempotency keys; confirmación de pagos.

* Messaging — plantillas y envío de emails (órdenes, confirmaciones, alertas).

* Logging — configuración centralizada de logs; propagación de X-Correlation-ID.

* Tests — test unitarios y de integración bien documentados.

## Requisitos

* Java 21

* Gradle

* PostgreSQL

* Setear las variables de entorno en un .env, siguiendo el ejemplo de .env.example

## Arrancar la aplicación

1. Abrir la consola
2. /gradlew build
3. /gradlew spotlessApply (si es necesario)
4. /gradlew bootRun
