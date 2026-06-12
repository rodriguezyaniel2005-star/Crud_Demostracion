#TIENDA MONGODB - DOCUMENTACIÓN TÉCNICA

## 1. RESUMEN EJECUTIVO

Este proyecto es una demostración de una tienda online generalista usando MongoDB como base de datos NoSQL. El proyecto demuestra arquitectura profesional de capas, patrones de diseño modernos y capacidades avanzadas de MongoDB.

**Base de Datos**: `tienda` (MongoDB)  
**Colecciones**: 4 (categorias, productos, compradores, ventas)  
**Productos**: 100+ (6 por categoría)  
**Categorías**: 20  
**Datos de Prueba**: Seeding automático  

---

## 0. EXPLICACIÓN DEL PROYECTO

Este proyecto es una demostración de una tienda online genérica basada en MongoDB, que muestra una arquitectura en capas (presentación, controlador, lógica de negocio, acceso a datos y persistencia) con patrones de diseño modernos como Singleton, DAO y Observer. La aplicación incluye una interfaz Swing para gestionar productos, categorías, compradores y ventas, soportando operaciones CRUD completas y actualizaciones en tiempo real mediante un sistema de eventos.

## 2. ARQUITECTURA DEL PROYECTO

### 2.1 Estructura de Capas

```
┌─────────────────────────────────┐
│   PRESENTATION LAYER (VIEW)     │
│  MainFrame, Panels (Swing GUI)  │
└──────────────┬──────────────────┘
               │
┌──────────────▼──────────────────┐
│  CONTROLLER LAYER               │
│  Controllers (Request Handlers)  │
└──────────────┬──────────────────┘
               │
┌──────────────▼──────────────────┐
│  BUSINESS LOGIC LAYER           │
│  BusinessLogic, Validator       │
└──────────────┬──────────────────┘
               │
┌──────────────▼──────────────────┐
│  DATA ACCESS LAYER (DAOs)       │
│  ProductoDAO, CategoriaDAO, etc │
└──────────────┬──────────────────┘
               │
┌──────────────▼──────────────────┐
│  PERSISTENCE LAYER              │
│  MongoDB Atlas / Local Instance  │
└─────────────────────────────────┘
```

### 2.2 Componentes Principales

#### **Modelo de Datos**
- `Buyer` (Comprador): Cliente de la tienda
- `Categoria`: Categoría de productos
- `Disco` (Producto): Artículo vendible
- `Sale` (Venta): Transacción
- `Sale.ProductoVendido`: Producto anidado en venta

#### **Capa de Datos (DAO)**
- `BuyerDAO`: CRUD para compradores
- `CategoriaDAO`: CRUD para categorías
- `DiscoDAO`: CRUD para productos
- `SaleDAO`: CRUD para ventas
- `MongoConnection`: Singleton para conexión MongoDB
- `DataSeeder`: Inicialización automática de datos
- `ConversionUtils`: Conversión de tipos (LocalDateTime ↔ Date)
- `AggregationQueries`: Consultas complejas con Aggregation Framework

#### **Capa de Negocio**
- `BusinessLogic`: Orquestación de operaciones CRUD
- `Validator`: Validación de datos

#### **Sistema de Eventos**
- `EntityChangeEvent`: Evento de cambio de entidad
- `EntityChangeListener`: Interfaz para escuchadores
- `EntityChangeManager`: Gestor centralizado de eventos (Singleton)

#### **Presentación (Views)**
- `MainFrame`: Contenedor principal con 4 pestañas
- `ProductoPanel`: Gestión de productos
- `CategoriaPanel`: Gestión de categorías
- `CompradorPanel`: Gestión de compradores
- `VentaPanel`: Gestión de ventas

---

## 3. MODELO DE DATOS MONGODB

### 3.1 Colección: `categorias`

```json
{
  "_id": ObjectId,
  "nombre": "Smartphones",
  "descripcion": "Teléfonos móviles y tablets",
  "fechaAlta": Date
}
```

**Índices**: `_id` (primario)  
**Cardinalidad**: ~20 documentos

---

### 3.2 Colección: `productos`

```json
{
  "_id": ObjectId,
  "nombre": "Samsung Galaxy S23",
  "descripcion": "Pantalla 6.1 pulgadas AMOLED",
  "categoriaId": ObjectId,
  "categoriaNombre": "Smartphones",
  "precio": 899.99,
  "stock": 20,
  "marca": "Samsung",
  "fechaAlta": Date,
  
  // Atributos dinámicos según categoría
  "pulgadasPantalla": 6.1,
  "memoriaRAM": "8GB",
  "almacenamiento": "256GB"
}
```

**Índices**: `_id`, `categoriaId`  
**Cardinalidad**: 100+  
**Atributos Flexibles por Categoría**:
- **Smartphones**: `pulgadasPantalla`, `memoriaRAM`, `almacenamiento`
- **Televisores**: `resolucion`, `pulgadas`, `tecnologia`
- **Libros**: `autor`, `editorial`, `ISBN`
- **Videojuegos**: `plataforma`, `genero`
- **Herramientas**: `material`, `potencia`

---

### 3.3 Colección: `compradores`

```json
{
  "_id": ObjectId,
  "nombre": "Juan",
  "apellidos": "Pérez García",
  "email": "juan.perez@email.com",
  "telefono": "+34-915-123456",
  "direccion": "Calle Principal 123, Madrid",
  "fechaRegistro": Date
}
```

**Índices**: `_id`  
**Cardinalidad**: ~10 documentos

---

### 3.4 Colección: `ventas`

```json
{
  "_id": ObjectId,
  "compradorId": ObjectId,
  "compradorNombre": "Juan Pérez García",
  "productosComprados": [
    {
      "productoId": ObjectId,
      "productoNombre": "Samsung Galaxy S23",
      "categoriaNombre": "Smartphones",
      "cantidad": 1,
      "precioUnitario": 899.99,
      "subtotal": 899.99
    }
  ],
  "cantidadProductos": 1,
  "subtotal": 899.99,
  "impuesto": 135.00,
  "totalVenta": 1034.99,
  "fecha": Date,
  "estado": "Completada"
}
```

**Índices**: `_id`, `compradorId`  
**Características**: Documentos anidados (ProductoVendido)  
**Cardinalidad**: Variable

---

## 4. OPERACIONES CRUD

### 4.1 Producto (Disco)

```java
// Crear
Disco producto = new Disco("Samsung Galaxy S23", "Descripción...", 
                           categoriaId, 899.99, 20, "Samsung");
businessLogic.createProducto(producto);

// Leer
Disco p = businessLogic.getProducto(productId);
List<Disco> todos = businessLogic.getAllProductos();

// Actualizar
producto.setPrecio(799.99);
businessLogic.updateProducto(producto);

// Eliminar
businessLogic.deleteProducto(productId);
```

### 4.2 Categoría

```java
Categoria cat = new Categoria("Smartphones", "Teléfonos móviles");
businessLogic.createCategoria(cat);
```

### 4.3 Comprador

```java
Buyer buyer = new Buyer("Juan", "Pérez García", 
                        "juan@email.com", "+34-915-123456", 
                        "Calle Principal 123");
businessLogic.createComprador(buyer);
```

### 4.4 Venta

```java
List<Sale.ProductoVendido> productos = new ArrayList<>();
productos.add(new Sale.ProductoVendido(
    productoId, "Samsung Galaxy S23", "Smartphones", 1, 899.99));

Sale venta = new Sale(compradorId, "Juan Pérez García", 
                      productos, 899.99, 135.00, 1034.99);
businessLogic.createVenta(venta);
```

---

## 5. CONSULTA COMPLEJA: AGGREGATION FRAMEWORK

### 5.1 Consulta Principal: Análisis de Ventas por Comprador

**Demostración de**: `$lookup`, `$group`, `$unwind`, `$project`, `$sort`

```
Stages:
1. $match: Filtrar ventas (opcional)
2. $lookup: Unir con colección compradores
3. $unwind: Desmontar arrays
4. $unwind: Desmontar productos
5. $lookup: Detalles de productos
6. $group: Agregar estadísticas
7. $sort: Ordenar por total gastado
```

**Resultado obtenido**:
```json
{
  "_id": ObjectId,
  "nombre": "Juan",
  "apellidos": "Pérez García",
  "email": "juan.perez@email.com",
  "totalCompras": 5,
  "totalGastado": 5234.50,
  "totalItems": 8,
  "categoriasDiferentes": ["Smartphones", "Libros", "Televisores"],
  "productoMasCaro": 2499.99,
  "ultimaCompra": Date
}
```

### 5.2 Otras Consultas Implementadas

**getSalesByCategory()**: Ventas por categoría  
**getTopProducts()**: Productos más vendidos  
**getBuyerSpendingRanges()**: Segmentación de clientes (Premium, VIP, Regular, Ocasional)  
**getSalesSummary()**: Estadísticas globales  

---

## 6. SISTEMA DE EVENTOS (OBSERVER PATTERN)

### 6.1 Flujo de Eventos

```
1. Controller llama: businessLogic.createProducto(producto)
2. BusinessLogic valida datos
3. BusinessLogic guarda en DB
4. BusinessLogic publica: 
   EntityChangeEvent(ENTITY_PRODUCTO, OPERATION_CREATE, id, producto)
5. EntityChangeManager distribuye evento
6. Views registradas reciben evento
7. Views actualizan UI automáticamente
```

### 6.2 Tipos de Eventos

```
Entity Types:
- ENTITY_CATEGORIA
- ENTITY_PRODUCTO (antes: ENTITY_DISCO)
- ENTITY_COMPRADOR (antes: ENTITY_BUYER)
- ENTITY_VENTA (antes: ENTITY_SALE)

Operations:
- OPERATION_CREATE
- OPERATION_UPDATE
- OPERATION_DELETE
```

### 6.3 Registro de Listeners

```java
EntityChangeManager manager = EntityChangeManager.getInstance();
manager.addListener(EntityChangeEvent.ENTITY_CATEGORIA, panel);
manager.addListener(EntityChangeEvent.ENTITY_PRODUCTO, panel);
```

---

## 7. VALIDACIÓN DE DATOS

### 7.1 Validadores Implementados

```java
// Strings
isValidString(String) → no null, no vacío

// Email
isValidEmail(String) → regex: ^[A-Za-z0-9+_.-]+@(.+)$

// Teléfono
isValidPhone(String) → regex: ^[0-9\\-\\+\\s()]+$

// Precios
isValidPrice(double) → > 0

// Stock
isValidStock(int) → >= 0

// Datos de producto
isValidProductData(nombre, descripcion, marca, precio, stock)

// Datos de comprador
isValidBuyerData(nombre, apellidos, email, telefono, direccion)

// Datos de venta
isValidSaleData(productosComprados, totalVenta)
```

---

## 8. SEEDING AUTOMÁTICO

### 8.1 Inicialización de Base de Datos

Al iniciar la aplicación:
```
1. MongoConnection.getInstance() verifica si DB existe
2. Si colecciones están vacías:
   a. Crea 20 categorías
   b. Crea 100+ productos (6 por categoría)
   c. Crea 10 compradores
   d. Crea 10 ventas de ejemplo
3. Si ya existen datos: salta seeding
```

### 8.2 Datos de Prueba

**20 Categorías**: Informática, Smartphones, Televisores, Electrodomésticos, Videojuegos, Consolas, Libros, Deportes, Jardinería, Herramientas, Automoción, Oficina, Hogar, Cocina, Mascotas, Juguetes, Moda, Belleza, Música, Cine

**100+ Productos**: 6 por categoría con atributos específicos

**10 Compradores**: Nombres, emails, teléfonos, direcciones españolas

**10 Ventas**: Con múltiples productos de diferentes categorías

---

## 9. VENTAJAS DE MONGODB PARA ESTE CASO

### 9.1 vs Base de Datos Relacional

| Característica | MongoDB | SQL |
|---|---|---|
| **Documentos Dinámicos** | ✓ Diferentes campos por tipo | ✗ Requiere columnas fijas |
| **Atributos Variable** | ✓ Smartphones con RAM, Libros con ISBN | ✗ NULL wasteful |
| **Nesting Anidado** | ✓ ProductoVendido dentro de Sale | ✗ Requiere joins múltiples |
| **Escalabilidad Horizontal** | ✓ Sharding nativo | ⚠ Complejo |
| **Flexibilidad Schema** | ✓ Cambios sin downtime | ✗ ALTER TABLE lento |
| **Consultas Complejas** | ✓ Aggregation Framework poderoso | ~ SQL avanzado necesario |
| **Performance Lectura** | ✓ Especialmente con índices | ~ Depende optimización |

### 9.2 Casos Óptimos para MongoDB

- **Productos con atributos variables**: Smartphones vs Libros vs Herramientas
- **Datos semi-estructurados**: Diferentes campos por categoría
- **Escalabilidad**: Millones de documentos distribuidos
- **Desarrollo ágil**: Cambios frecuentes sin migración DB
- **Análisis complejos**: Aggregation Framework

---

## 10. JUSTIFICACIÓN DE ATRIBUTOS DINÁMICOS

### 10.1 Ejemplo Real

**Smartphone necesita**: RAM, Pantalla, Almacenamiento  
**Libro necesita**: Autor, Editorial, ISBN  
**Televisor necesita**: Resolución, Pulgadas, Tecnología  
**Herramienta necesita**: Material, Potencia  

### 10.2 Solución SQL (Ineficiente)

```sql
CREATE TABLE productos (
  id INT,
  nombre VARCHAR,
  -- Smartphone columns
  pulgadas DECIMAL,
  ram VARCHAR,
  almacenamiento VARCHAR,
  -- Libro columns
  autor VARCHAR,
  editorial VARCHAR,
  isbn VARCHAR,
  -- Televisor columns
  resolucion VARCHAR,
  tecnologia VARCHAR,
  -- Herramienta columns
  material VARCHAR,
  potencia INT,
  -- Muchas columnas NULL wasteful
);
```

### 10.3 Solución MongoDB (Elegante)

```javascript
// Documento flexible
db.productos.insertOne({
  nombre: "Samsung Galaxy S23",
  categoriaId: ObjectId("..."),
  precio: 899.99,
  // Atributos específicos
  pulgadasPantalla: 6.1,
  memoriaRAM: "8GB",
  almacenamiento: "256GB"
});

db.productos.insertOne({
  nombre: "Clean Code",
  categoriaId: ObjectId("..."),
  precio: 39.99,
  // Diferentes atributos
  autor: "Robert C. Martin",
  editorial: "Prentice Hall",
  ISBN: "978-0132350884"
});
```

**Ventajas**: Sin columnas NULL, flexible, escalable

---

## 11. PATRONES DE DISEÑO IMPLEMENTADOS

### 11.1 Singleton
- `MongoConnection`: Única instancia de conexión
- `EntityChangeManager`: Único gestor de eventos

### 11.2 Observer Pattern
- `EntityChangeListener`: Interfaz observadora
- `EntityChangeEvent`: Evento publicado
- `EntityChangeManager`: Observable central

### 11.3 Data Access Object (DAO)
- `BuyerDAO`, `CategoriaDAO`, `DiscoDAO`, `SaleDAO`
- Abstracción de acceso a datos
- Encapsulación de lógica MongoDB

### 11.4 Factory Pattern
- `DataSeeder`: Crea datos iniciales

### 11.5 MVC Pattern
- **Model**: Clases entidad (Buyer, Categoria, Disco, Sale)
- **View**: Panels (BuyerPanel, CategoriaPanel, DiscoPanel, SalePanel)
- **Controller**: Controllers (BuyerController, etc.)

---

## 12. CONVENCIONES Y ESTÁNDARES

### 12.1 Nomenclatura

- **Classes**: PascalCase (Buyer, BusinessLogic)
- **Methods**: camelCase (createBuyer, getAllProductos)
- **Constants**: UPPER_SNAKE_CASE (ENTITY_CATEGORIA)
- **Collections**: Lowercase plural (categorias, productos)

### 12.2 Estructura de Archivos

```
src/main/java/org/example/
├── Main.java
├── business/
│   ├── BusinessLogic.java
│   └── Validator.java
├── controller/
│   ├── BaseController.java
│   ├── BuyerController.java
│   ├── CategoriaController.java
│   ├── DiscoController.java
│   └── SaleController.java
├── data/
│   ├── AggregationQueries.java
│   ├── BuyerDAO.java
│   ├── CategoriaDAO.java
│   ├── ConversionUtils.java
│   ├── DataSeeder.java
│   ├── DiscoDAO.java
│   ├── MongoConnection.java
│   └── SaleDAO.java
├── event/
│   ├── EntityChangeEvent.java
│   ├── EntityChangeListener.java
│   └── EntityChangeManager.java
├── model/
│   ├── Buyer.java
│   ├── Categoria.java
│   ├── Disco.java
│   └── Sale.java
└── view/
    ├── BuyerPanel.java
    ├── CategoriaPanel.java
    ├── DiscoPanel.java
    ├── MainFrame.java
    └── SalePanel.java
```

---

## 13. CONFIGURACIÓN Y EJECUCIÓN

### 13.1 Requisitos

- **Java**: 11+
- **MongoDB**: Local o Atlas
- **Gradle**: 7.0+
- **Swing**: Incluido en JDK

### 13.2 Conexión MongoDB

```java
// MongoConnection.java
String connectionString = "mongodb://localhost:27017";
String dbName = "tienda";
```

### 13.3 Compilación y Ejecución

```bash
./gradlew compileJava
./gradlew run
```

### 13.4 Parámetros MongoDB

- **Connection Timeout**: Default
- **Database**: `tienda`
- **Colecciones**: Auto-creadas al insertar

---

## 14. EXTENSIBILIDAD FUTURA

### 14.1 Mejoras Posibles

- **Autenticación**: Agregar login de usuarios
- **Pagos**: Integración con pasarelas de pago
- **Carrito Persistente**: Guardado de sesión
- **Notificaciones**: Email/SMS después de venta
- **Analytics**: Dashboard con gráficos
- **API REST**: Interfaz web moderna
- **Reportes**: Generación de PDF/Excel
- **Inventario**: Alertas de stock bajo

### 14.2 Escalabilidad

- **Replicasets**: Alta disponibilidad
- **Sharding**: Escalar horizontalmente
- **Índices Compuestos**: Optimización queries
- **Caché**: Redis para sesiones

---

## 15. GLOSARIO

- **DAO**: Data Access Object - Patrón para acceso a datos
- **CRUD**: Create, Read, Update, Delete
- **ObjectId**: Identificador único MongoDB
- **Aggregation**: Pipeline de transformación MongoDB
- **Seeding**: Inserción de datos iniciales
- **Sink**: Desmontar documentos en pipelines MongoDB
- **Lookup**: Join de colecciones en MongoDB

---

# 16. CONCLUSIÓN

Este proyecto demuestra una refactorización profesional de tienda digital usando MongoDB. Implementa:

- ✓ Arquitectura de capas limpia
- ✓ 100+ datos de prueba
- ✓ 20 categorías con atributos dinámicos
- ✓ CRUD completo funcionando
- ✓ Actualización UI en tiempo real
- ✓ Consultas avanzadas con Aggregation Framework
- ✓ Patrones de diseño modernos
- ✓ Código mantenible y escalable

MongoDB demuestra ser una excelente opción para tiendas con productos heterogéneos, permitiendo flexibilidad, escalabilidad y análisis complejos sin comprometer performance.

---

## 17. POSIBLES CASOS DE USO

1. **Gestión de inventario para una cadena de tiendas de electrónica** – La empresa puede definir categorías como "Smartphones", "Televisores" y "Accesorios" con atributos específicos (RAM, resolución, puertos). La flexibilidad del esquema permite añadir rápidamente nuevos atributos cuando se lanzan productos innovadores sin migraciones de base de datos.

2. **Plataforma de venta de libros y material educativo** – Cada categoría ("Libros", "Cursos en línea", "Material didáctico") requiere campos diferentes (ISBN, autor, nivel educativo). Con MongoDB, la compañía puede almacenar cada tipo de producto con sus metadatos únicos, facilitando búsquedas por atributos y generación de catálogos personalizados.

3. **Sistema interno de gestión de pedidos para una empresa de fabricación** – Los productos pueden ser componentes con especificaciones técnicas (material, potencia, tolerancia). El modelo flexible permite a los ingenieros registrar componentes nuevos y sus características sin alterar la base de datos existente, y usar el Aggregation Framework para generar informes de consumo y planificación de producción.

---

**Fecha de Documentación**: Junio 2026  
**Versión**: 1.0  
**Autor**: 

Este proyecto demuestra una refactorización profesional de tienda digital using MongoDB. Implementa:

✓ Arquitectura de capas limpia  
✓ 100+ datos de prueba  
✓ 20 categorías con atributos dinámicos  
✓ CRUD completo funcionando  
✓ Actualización UI en tiempo real  
✓ Consultas avanzadas con Aggregation Framework  
✓ Patrones de diseño modernos  
✓ Código mantenible y escalable  

MongoDB demuestra ser una excelente opción para tiendas con productos heterogéneos, permitiendo flexibilidad, escalabilidad y análisis complejos sin comprometer performance.

---

**Fecha de Documentación**: Junio 2026  
**Versión**: 1.0  
**Autor**: Refactorización Automática
