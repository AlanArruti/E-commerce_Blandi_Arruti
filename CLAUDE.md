# CLAUDE.md — TP Final Progra 3 (E-commerce)

> Archivo de contexto para Claude Code. Lee esto antes de hacer cualquier cosa en el proyecto.

---

## 🏗️ Stack técnico

- **Java:** 21
- **Spring Boot:** 4.0.5
- **Build:** Maven
- **BD:** PostgreSQL 18 (local en `localhost:5432`, BD `ecommerce`)
- **ORM:** JPA / Hibernate 7.2.7
- **Mapper:** MapStruct 1.6.3 (con `lombok-mapstruct-binding`)
- **Boilerplate:** Lombok
- **Doc API:** SpringDoc OpenAPI (Swagger UI) 3.0.2
- **Validación:** spring-boot-starter-validation
- **Auth:** JWT (pendiente — última etapa)
- **AOP:** Spring AOP para logging y auditoría transversal

---

## 📁 Estructura de paquetes (estándar UTN Progra 3)

```
src/main/java/com/BlandiArruti/E_commerce/
├── ECommerceApplication.java
├── config/              ← config y DataLoader (seed)
├── entity/              ← entidades JPA (clases en SINGULAR)
├── enums/               ← EstadoPedido, EstadoEnvio, TipoFactura
├── repository/          ← interfaces JpaRepository
├── service/             ← lógica de negocio
├── controller/          ← endpoints REST
├── dto/                 ← Request/Response
├── mapper/              ← interfaces MapStruct
└── exception/           ← excepciones custom + GlobalHandler

src/main/resources/
└── application.yaml     ← config de BD y JPA
```

**Reglas de nomenclatura:**
- Clases de entidad: **SINGULAR** (`Producto`, `Cliente`, `Pedido`).
- Tablas en BD: **plural snake_case** (`productos`, `clientes`, `items_pedido`).
- Columnas: **snake_case** (`id_producto`, `nombre_calle`, `precio_total`).
- Atributos Java: **camelCase** (`idProducto`, `nombreCalle`).
- DTOs: `XxxRequest.java` (entrada) y `XxxResponse.java` (salida).
- Repos: `XxxRepository.java`.
- Servicios: `XxxService.java`.
- Controllers: `XxxController.java`.

---

## 🧠 Reglas y convenciones JPA aplicadas

### Mapeo de relaciones

- `@ManyToOne` siempre con `fetch = FetchType.LAZY` y `optional = false` cuando aplica.
- `@OneToMany` siempre con `mappedBy` apuntando al atributo del lado dueño.
- `@OneToMany` con `cascade = CascadeType.ALL, orphanRemoval = true` cuando la hija no existe sin la padre (`Pedido → ItemPedido`, `Producto → Variante`).
- `@OneToMany` SIN cascade cuando la hija vive independiente (`Categoria → Producto`, `Ciudad → Direccion`).
- `@OneToOne` lado dueño tiene `@JoinColumn(unique = true)`. Lado inverso tiene `mappedBy`.
- Listas inicializadas con `new ArrayList<>()` y marcadas con `@Builder.Default` (Lombok).

### Otros

- IDs: siempre `Long` con `GenerationType.IDENTITY`.
- Enums: siempre `@Enumerated(EnumType.STRING)`. NUNCA `ORDINAL`.
- `@Column` con `nullable`, `unique`, `length` explícitos en atributos críticos (`email`, `dni`).
- `@ToString(exclude = {...})` excluyendo TODAS las relaciones para evitar StackOverflow.
- `Map<String, String> atributos` de `Variante` mapeado con `@ElementCollection` (futuro: migrar a JSONB con `@JdbcTypeCode(SqlTypes.JSON)`).

---

## 🌐 API REST — endpoints (según OpenAPI YAML)

Servidor: `http://localhost:8080/api/v1`

### Recursos principales

| Recurso | Endpoints destacados |
|---|---|
| `/auth` | `POST /login`, `POST /register` |
| `/productos` | CRUD completo + `/{id}/variantes`, `/{id}/variantes/{varianteId}/stock` (PATCH) |
| `/categorias` | CRUD |
| `/clientes` | CRUD + `/{id}/pedidos`, `/{id}/direcciones` |
| `/administradores` | CRUD |
| `/pedidos` | CRUD + `/{id}/estado`, `/{id}/pagar`, `/{id}/factura`, `/{id}/envio` |
| `/facturas` | GET listar, GET por id |
| `/paises` | GET listar, `/{id}/provincias` |
| `/provincias` | `/{id}/ciudades` |

### Procesos críticos de negocio

1. **Crear pedido** (`POST /pedidos`)
   - Validar stock de cada variante antes de crear
   - Estado inicial: `EN_PREPARACION`
   - Calcular `precioTotalProducto` por item

2. **Pagar pedido** (`POST /pedidos/{id}/pagar`)
   - Cambia estado a `PAGADO`
   - Descuenta stock de cada variante
   - Genera `Factura` asociada con `tipoFactura` (A/B/C)

3. **Crear envío** (`POST /pedidos/{id}/envio`)
   - Solo si pedido está en `PAGADO`
   - Pedido pasa a `DESPACHADO`

4. **Cambiar estado de envío** (`PATCH /pedidos/{id}/envio/estado`)
   - Transiciones válidas: `DESPACHADO → EN_CAMINO → ENTREGADO`
   - Al llegar a `ENTREGADO`, el pedido también pasa a `ENTREGADO`

5. **Actualizar stock variante** (`PATCH /productos/{id}/variantes/{varianteId}/stock`)
   - Operaciones: `AGREGAR` o `REDUCIR`
   - Validar que no quede negativo

6. **Transiciones de estado de pedido** (`PATCH /pedidos/{id}/estado`)
   - `EN_PREPARACION → PAGADO → DESPACHADO → ENTREGADO`
   - `EN_PREPARACION → CANCELADO`

7. **Historial de pedidos por cliente** (`GET /clientes/{id}/pedidos`)
   - Filtrable por estado

---

## 🔐 Seguridad (a implementar)

- JWT con `Bearer` token.
- Roles: `CLIENTE` y `ADMINISTRADOR`.
- Endpoints públicos: `/auth/**`, `GET /productos/**`, `GET /categorias/**`, `GET /paises/**`, `GET /provincias/**`.
- Endpoints solo `ADMINISTRADOR`: gestión de productos (POST/PUT/DELETE), administradores, listado de clientes.
- Endpoints `CLIENTE` propio: ver sus pedidos, sus direcciones.

---

## 🧪 Validaciones esperadas

- `@NotBlank` en strings obligatorios.
- `@Email` en email.
- `@Positive` o `@Min(0)` en precios y stock.
- `@Size(min, max)` en strings con longitud.
- `@Valid` en `@RequestBody` de los controllers.

---

## 🚨 Errores y excepciones

Devolver siempre `ErrorResponse` con este formato:

```json
{
  "timestamp": "...",
  "status": 400,
  "error": "Bad Request",
  "message": "descripción clara",
  "path": "/api/v1/..."
}
```

| HTTP | Cuándo |
|---|---|
| `400` | Datos inválidos, stock insuficiente, transición de estado inválida |
| `401` | No autenticado |
| `403` | Sin permisos (rol incorrecto) |
| `404` | Recurso no encontrado |
| `409` | Conflicto (email duplicado, categoría con productos) |

---

## 🗺️ Roadmap de trabajo

1. ✅ Setup proyecto + entidades + repositorios
2. 🟦 DataLoader completo
3. ⏭️ DTOs + MapStruct mappers
4. ⏭️ Services (CRUD + procesos críticos)
5. ⏭️ Controllers (endpoints REST según YAML)
6. ⏭️ Exception handling
7. ⏭️ Spring Security + JWT
8. ⏭️ Tests
9. ⏭️ Doc Swagger

---

## 🔄 Mantenimiento de este archivo

Este archivo es la fuente de verdad del proyecto. Claude Code debe mantenerlo actualizado automáticamente:

- Si se completa una tarea del roadmap → actualizá el emoji correspondiente (⏭️ → 🟦 → ✅).
- Si se agrega una nueva dependencia al `pom.xml` → agregala en la sección de stack técnico.
- Si se crea un nuevo paquete o se reorganiza la estructura → actualizá la sección de estructura.
- Si cambia alguna convención o regla de negocio → reflejalo en la sección correspondiente.
- Si se implementa seguridad JWT → mover esa sección de "a implementar" a "hecho".
- **Siempre** que hagas un cambio relevante en el proyecto, preguntate si el CLAUDE.md refleja ese cambio. Si no, actualizalo antes de terminar la tarea.

---

## ⚠️ Notas para Claude

- Antes de generar cualquier código, leer los archivos existentes del proyecto para no duplicar ni contradecir lo ya hecho.
- Si encontrás clases mal ubicadas o paquetes que no respetan el estándar de la UTN, reorganizalas antes de continuar. Mové cada clase al paquete que le corresponde según su responsabilidad y actualizá los imports afectados.
- El estudiante prefiere `application.yaml` sobre `.properties`.
- Comandos en **Windows / IntelliJ** (NO Linux).
- Hablar en español rioplatense (vos, querés, dale).
- Cuando expliques, usar tablas y listas. Evitar bloques de código gigantes.

---

## 📂 Documentos de referencia (fuera del repo)

- `F:\E-commerce_ProyectoFinal\TpFinal Blandi - Arruti (13)\E-commerce-openapi-grupo_13.yaml` — OpenAPI completo
- `F:\Descargas\Consigna TP Final Progr3.pdf` — Consigna oficial
- `F:\Descargas\Guia estandares utn prog3.pdf` — Estándares de código UTN
- `F:\Descargas\Requisitos Funcionales E-commerce.docx.pdf` — Requisitos funcionales
