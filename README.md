# E-Commerce API — Blandi & Arruti

API REST para una plataforma de comercio electrónico desarrollada como Trabajo Práctico Final de Programación III — UTN Mar del Plata.

## Descripción del proyecto

El sistema resuelve la problemática de una tienda online que necesita gestionar su catálogo de productos, procesar pedidos, emitir facturas y coordinar el despacho de envíos. Integra pagos en línea mediante **MercadoPago Checkout Pro** y notifica a los clientes por email en los eventos clave del ciclo de vida del pedido.

### Reglas de negocio principales

- Un pedido se crea en estado `PENDIENTE_PAGO`. El stock **no** se descuenta hasta que el pago es confirmado.
- El pago puede realizarse de forma directa (endpoint propio) o a través de MercadoPago (flujo de dos pasos con webhook de confirmación).
- Al confirmar el pago se genera automáticamente una **Factura** (tipo A, B o C) y el pedido pasa a `PAGADO`.
- El envío solo puede crearse para pedidos en estado `PAGADO`. Al crear el envío el pedido pasa a `DESPACHADO`.
- Las transiciones de estado de envío siguen la secuencia `DESPACHADO → EN_CAMINO → ENTREGADO`.
- Las bajas de clientes y productos son **lógicas** (soft-delete mediante campo `activo`).
- Los clientes solo pueden acceder a sus propios recursos; los administradores tienen acceso irrestricto.

---

## Stack tecnológico

| Tecnología | Versión |
|---|---|
| Java | 21 |
| Spring Boot | 4.0.5 |
| Spring Security + JWT | jjwt 0.12.6 |
| Spring Data JPA | Hibernate 7.x |
| PostgreSQL | 17+ |
| MapStruct | 1.6.3 |
| MercadoPago SDK | 3.2.1 |
| SpringDoc OpenAPI | 3.0.2 |
| Spring AOP | (transversal) |
| Maven | 3.9+ |

---

## Arquitectura

El proyecto aplica una arquitectura en capas desacoplada mediante interfaces:

```
Controller → IService (interface) → ServiceImpl → Repository
```

- **Controllers** (`/controller`): reciben requests HTTP, delegan en interfaces de servicio.
- **Services** (`/service`): contienen la lógica de negocio; cada uno implementa su interfaz (`IXxxService`).
- **Repositories** (`/repository`): acceso a datos con Spring Data JPA.
- **DTOs** (`/dto`): Java records para entrada (`*Request`) y salida (`*Response`).
- **Mappers** (`/mapper`): conversión entre entidades y DTOs mediante MapStruct.
- **AOP** (`/aop`): `LoggingAspect` intercepta todos los métodos de servicio para registrar duración y errores.

### Patrones de diseño aplicados

| Patrón | Dónde |
|---|---|
| **Builder** | Todas las entidades JPA (Lombok `@Builder`) |
| **Singleton** | Todos los `@Service`, `@Component`, `@Repository` (Spring IoC) |
| **Factory Method** | `UsuarioDetails.fromCliente()` / `UsuarioDetails.fromAdmin()` |
| **Strategy** | Lógica de validación de stock (`StockRequest.operacion`) |
| **Template Method** | `GlobalExceptionHandler` — flujo uniforme de respuesta de error |

---

## Estructura de paquetes

```
src/main/java/com/BlandiArruti/E_commerce/
├── aop/                  ← LoggingAspect (AOP transversal)
├── auth/                 ← JWT, OAuth2 GitHub, AuthController
├── administrador/        ← CRUD de administradores
├── carrito/              ← Carrito de compras
├── categoria/            ← Categorías de productos
├── cliente/              ← Clientes, direcciones
├── config/               ← DataLoader, SecurityConfig
├── cotizacion/           ← Cotización de envío (Correo Argentino)
├── envio/                ← Gestión de envíos
├── enums/                ← EstadoPedido, EstadoEnvio, TipoFactura, OperacionStock
├── exception/            ← Excepciones custom + GlobalExceptionHandler
├── factura/              ← Generación y consulta de facturas
├── geo/                  ← País, Provincia, Ciudad (datos geográficos AR)
├── mercadopago/          ← Checkout Pro + Webhook
├── notificacion/         ← Envío de emails (JavaMailSender)
├── pedido/               ← Pedidos e ítems
├── producto/             ← Productos y variantes con JSONB
└── shared/               ← PageResponse<T> genérico
```

---

## Endpoints principales

Base URL: `http://localhost:8080/api/v1`

| Método | Endpoint | Descripción | Auth |
|---|---|---|---|
| `POST` | `/auth/login` | Autenticación — devuelve JWT | Público |
| `POST` | `/auth/register` | Registro de cliente | Público |
| `GET` | `/productos` | Listar productos (filtros: categoría, precio, búsqueda) | Público |
| `GET` | `/productos/{id}` | Detalle de producto | Público |
| `POST` | `/productos` | Crear producto | ADMIN |
| `PATCH` | `/productos/{id}/variantes/{vid}/stock` | Ajustar stock | ADMIN |
| `GET` | `/categorias` | Listar categorías | Público |
| `POST` | `/pedidos` | Crear pedido | CLIENTE / ADMIN |
| `PATCH` | `/pedidos/{id}/pagar` | Pagar directamente (genera factura) | CLIENTE / ADMIN |
| `POST` | `/pedidos/{id}/iniciar-pago` | Iniciar pago con MercadoPago | CLIENTE / ADMIN |
| `POST` | `/webhook/mercadopago` | Confirmación de pago MP | Público |
| `PATCH` | `/pedidos/{id}/cancelar` | Cancelar pedido | CLIENTE / ADMIN |
| `PATCH` | `/pedidos/{id}/estado` | Cambiar estado (admin) | ADMIN |
| `POST` | `/pedidos/{id}/envio` | Crear envío para pedido PAGADO | ADMIN |
| `PATCH` | `/envio/{id}/estado` | Avanzar estado del envío | ADMIN |
| `GET` | `/clientes/{id}/pedidos` | Historial de pedidos del cliente | CLIENTE / ADMIN |
| `GET` | `/facturas` | Listar facturas (filtros: tipo, pedido, cliente) | ADMIN |
| `GET` | `/cotizacion` | Cotizar envío (Correo Argentino) | CLIENTE / ADMIN |

Documentación interactiva completa disponible en: `http://localhost:8080/swagger-ui.html`

---

## Prerrequisitos

- **Java 21**
- **Maven 3.9+**
- **Docker** y **Docker Compose** (opción recomendada)
- O bien: **PostgreSQL 17+** instalado localmente

---

## Ejecución con Docker Compose (recomendado)

```bash
# 1. Clonar el repositorio
git clone https://github.com/AlanArruti/E-commerce_Blandi_Arruti.git
cd E-commerce_Blandi_Arruti

# 2. Crear el archivo de variables de entorno
cp .env.example .env
# Editar .env con los valores reales (ver sección Variables de entorno)

# 3. Levantar base de datos + aplicación
docker compose up --build
```

La API quedará disponible en `http://localhost:8080`.

---

## Ejecución local (sin Docker)

```bash
# 1. Crear la base de datos en PostgreSQL
createdb ecommerce

# 2. Configurar variables de entorno o crear src/main/resources/application-local.yaml
#    con los valores correspondientes (ver Variables de entorno)

# 3. Compilar y ejecutar
./mvnw spring-boot:run -Dspring-boot.run.profiles=local
```

---

## Variables de entorno

| Variable | Descripción | Ejemplo |
|---|---|---|
| `DB_URL` | URL JDBC de la base de datos | `jdbc:postgresql://localhost:5432/ecommerce` |
| `DB_USERNAME` | Usuario de PostgreSQL | `postgres` |
| `DB_PASSWORD` | Contraseña de PostgreSQL | `secreto` |
| `JWT_SECRET` | Clave secreta para firmar tokens (mín. 32 chars) | `clave-super-secreta-de-64-caracteres...` |
| `MP_ACCESS_TOKEN` | Access token de MercadoPago | `APP_USR-...` |
| `MP_NOTIFICATION_URL` | URL pública para webhook de MP | `https://tu-dominio.com/api/v1/webhook/mercadopago` |
| `MP_BACK_URL` | URL de retorno tras el pago | `http://localhost:8080` |
| `MAIL_HOST` | Host SMTP | `smtp.gmail.com` |
| `MAIL_PORT` | Puerto SMTP | `587` |
| `MAIL_USERNAME` | Cuenta de correo | `tu@gmail.com` |
| `MAIL_PASSWORD` | Contraseña / app password | `xxxx xxxx xxxx xxxx` |
| `CORREO_ARGENTINO_USER_TOKEN` | Token API Correo Argentino | `...` |
| `GITHUB_CLIENT_ID` | Client ID de GitHub OAuth2 | `Ov23li...` |
| `GITHUB_CLIENT_SECRET` | Client Secret de GitHub OAuth2 | `...` |

Copiar `.env.example` como `.env` y completar con los valores reales.

---

## Tests

El proyecto incluye **112 tests unitarios** distribuidos en 12 clases de test, usando JUnit 5, Mockito y AssertJ.

```bash
# Ejecutar todos los tests
./mvnw test

# El test de integración (contextLoads) requiere Docker para levantar PostgreSQL
# Si Docker no está disponible, se saltea automáticamente
```

Cobertura de tests unitarios:
- `AuthServiceTest` — 5 tests (login OK, credenciales inválidas, cuenta bloqueada, cuenta desactivada, registro)
- `LoginAttemptServiceTest` — 8 tests (contador de intentos, bloqueo a los 5 fallos, reset tras login exitoso)
- `CarritoServiceTest` — 13 tests (obtener/crear carrito, agregar/fusionar ítems, stock, actualizar, eliminar, checkout)
- `CategoriaServiceTest` — 12 tests (listar con/sin filtro, crear, duplicado, actualizar, eliminar con confirmación)
- `WebhookControllerTest` — 7 tests (tipos inválidos, pago aprobado/rechazado, reference malformada)
- `PedidoServiceTest` — 14 tests (cancelación, pago, envío, estado, confirmación MP)
- `ProductoServiceTest` — 11 tests (variantes, stock, ownership)
- `ClienteServiceTest` — 12 tests (CRUD, owner-check, acceso)
- `EnvioServiceTest` — 10 tests (ciclo de vida del envío)
- `AdministradorServiceTest` — 11 tests (CRUD, duplicados)
- `CotizacionEnvioServiceTest` — 5 tests (cotización Correo Argentino)
- `NotificacionServiceTest` — 4 tests (emails, fallo de conexión)

---

## Colección Postman

Importar el archivo `postman/E-Commerce_Blandi_Arruti.postman_collection.json` en Postman para acceder a todas las solicitudes preconfiguradas.

La colección incluye:
- Variables de entorno (`baseUrl`, `token`)
- Script de login que guarda el JWT automáticamente en `{{token}}`
- Requests organizados por módulo (Auth, Productos, Pedidos, etc.)

---

## Integrantes

| Nombre | Rol en el proyecto |
|---|-------------|
| Alan Arruti | Iara Blandi |

**Carrera:** Tecnicatura Universitaria en Programación — UTN Mar del Plata  
**Materia:** Programación III  
**Docente:** Lic. Mango Eduardo
