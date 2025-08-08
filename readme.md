# API de Productos - Quarkus

Una API REST simple para gestión de productos construida con Quarkus, que incluye validaciones, persistencia en memoria y manejo uniforme de errores.

## 🚀 Características

- **Framework**: Quarkus 3.9+
- **Persistencia**: Panache + H2 en memoria
- **Validaciones**: Bean Validation
- **Tests**: JUnit con @QuarkusTest
- **Manejo de errores**: Respuestas uniformes con JAX-RS ExceptionMapper

## 📋 Requisitos

- Java 17+
- Maven 3.8+

## 🏗️ Estructura del Proyecto

```
repo/
├── app/                 # Código principal Quarkus
│   ├── pom.xml
│   └── src/
│       ├── main/java/   # Recursos y clases
│       └── test/java/   # Tests @QuarkusTest
├── tests/               # Módulo Tests @QuarkusTest
│   └── pom.xml
|   └── src/
│       └── test/java/
├── logs/
│   └── diagnostico.log
└── pom.xml              # POM padre
```

## 🚦 Ejecución

### Desarrollo
```bash
mvn -pl app quarkus:dev
```

### Tests
**Nota:** No correrlo mientras el servicio esta corriendo.
```bash
mvn -U clean test
```

## 📚 API Endpoints

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| `GET` | `/productos` | Lista todos los productos |
| `POST` | `/productos` | Crea un nuevo producto |
| `GET` | `/productos/{id}` | Obtiene producto por ID |
| `DELETE` | `/productos/{id}` | Elimina producto por ID |

### Ejemplos con cURL

```bash
# Crear producto
curl -s -X POST http://localhost:8080/productos \
  -H "Content-Type: application/json" \
  -d '{ "nombre": "Escoba", "precio": 12000 }'

# Listar productos
curl -s http://localhost:8080/productos

# Obtener por ID
curl -s http://localhost:8080/productos/1

# Eliminar producto
curl -i -X DELETE http://localhost:8080/productos/1
```

## 🧩 Modelo de Datos

### Entidad Producto
```java
{
  "id": Long,
  "nombre": String,
  "precio": double
}
```

### Validaciones
- **nombre**: `@NotBlank` - No puede estar vacío
- **precio**: `@Min(0)` - Debe ser mayor o igual a 0

### Reglas de Negocio
- `aplicarImpuesto(%)`: Solo acepta valores entre 0-50%

## 🛡️ Manejo de Errores

La API retorna respuestas de error uniformes:

```json
// Error 404 - No encontrado
{
  "code": 404,
  "message": "No encontrado"
}

// Error 400 - Validación
{
  "code": 400,
  "message": "<detalle del error de validación>"
}
```

## 🧱 Configuración de Base de Datos

La aplicación usa H2 en memoria con la siguiente configuración:

```properties
# JDBC URL
jdbc:h2:mem:productos;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE

# Hibernate
quarkus.hibernate-orm.database.generation=drop-and-create
```

No requiere instalación de base de datos externa.

## 🧪 Testing

### Tests de Dominio
- Ubicación: `tests/.../producto/model/ProductoTest.java`
- Cubre reglas de negocio y validaciones

### Tests de API REST
- Ubicación: `app/src/test/.../producto/controller/ProductoControllerTest.java`
- Usa `@QuarkusTest` + RestAssured
- Incluye casos felices y manejo de errores (404, 400)

## 📋 Checklist de Requisitos Cumplidos

- ✅ Persistencia en memoria (Panache + H2)
- ✅ Bean Validation (nombre no vacío, precio >= 0)
- ✅ Respuesta de error uniforme
- ✅ Tests JUnit (casos felices y manejo de errores)
- ✅ Reglas de dominio implementadas

## 🔎 Archivo de logs
**Archivo:** `logs/diagnostico.log`

**Causa**  
`NumberFormatException: For input string: "abc"` al intentar convertir un valor no numérico como entero.

**Prevención**
- Validar el formato antes de convertir, si es númerio o no.
- En las APIs: Bean Validation y retornar **400** si no cumple.
- En procesos/colas/variables de entorno: normalizar entradas, `try/catch` con *defaults* seguros y registrar el contexto del dato.

**Nivel de log adecuado**
- **ERROR** cuando el proceso falla y no puede continuar, lo que impacta la ejecución.
- Si el sistema puede continuar, registrar el dato inválido como **WARN** y marcar para reintento, pero conservar **ERROR** cuando aborta.



## ☁️ AWS

### EC2 vs Lambda

Uso EC2 cuando mi app debe estar prendida 24/7 y quiero disponibilidad.
En cambio Lambda para cuestiones específicas como operaciones simples o que no requieran de memoria,
como realizar un cálculo, autorizar una conexión o devolver un dato.

### Herramientas de Observabilidad

| Servicio | Propósito                                                         |
|------|-------------------------------------------------------------------|
| **CloudWatch** | Métricas, logs centralizados y alarmas                            |
| **OpenSearch** | Búsqueda y análisis en tiempo real sobre logs y métricas.            |
| **CloudTrail** | Auditoría de llamadas a APIs, quién hizo qué, cuándo y desde dónde |

## 🗃️ Consultas SQL Útiles

### Top 5 Productos por Precio
```sql
SELECT id, nombre, precio
FROM productos
WHERE precio > 100000
ORDER BY precio DESC
FETCH FIRST 5 ROWS ONLY;

SELECT id, nombre, precio
FROM productos
WHERE precio > 100000
ORDER BY precio DESC
LIMIT 5;
```

### Optimización con Índices

**Índice por nombre:**
Un `INDEX(nombre)` acelera búsquedas por igualdad y por prefijo 
`(WHERE nombre='x' o LIKE 'pre%')` en tablas medianas/grandes, 
evitando full scans a costa de más escritura y algo de espacio.
