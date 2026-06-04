package org.example.data;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.bson.Document;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.types.ObjectId;

/**
 * DataSeeder: Automatically initializes MongoDB collections and inserts test data.
 * Creates 20 categories, 100+ products, buyers, and sample sales.
 */
public class DataSeeder {

    public static void seedDatabaseIfEmpty(MongoDatabase database) {
        try {
            // Check if collections are empty
            MongoCollection<Document> categoriasCollection = database.getCollection("categorias");
            MongoCollection<Document> productosCollection = database.getCollection("productos");
            
        if (categoriasCollection.countDocuments() == 0) {
            System.out.println("Database is empty. Seeding initial data...");
            seedCategories(database);
            seedProducts(database);
            seedBuyers(database);
            seedSales(database);
            System.out.println("Database seeding completed successfully!");
        } else {
            System.out.println("Database already contains data. Dropping existing collections and reseeding.");
            // Drop existing collections
            categoriasCollection.drop();
            productosCollection.drop();
            // Reseed fresh data
            seedCategories(database);
            seedProducts(database);
            seedBuyers(database);
            seedSales(database);
            System.out.println("Database reseeded successfully!");
        }
        } catch (Exception e) {
            System.err.println("Error during database seeding: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Seeds 20 different product categories
     */
    private static void seedCategories(MongoDatabase database) {
        MongoCollection<Document> collection = database.getCollection("categorias");
        List<Document> categories = new ArrayList<>();

        String[][] categoryData = {
                {"Informática", "Computadoras, monitores y accesorios"},
                {"Smartphones", "Teléfonos móviles y tablets"},
                {"Televisores", "TVs de diferentes tamaños y tecnologías"},
                {"Electrodomésticos", "Electrodomésticos para el hogar"},
                {"Videojuegos", "Juegos para consolas y PC"},
                {"Consolas", "Consolas de videojuegos"},
                {"Libros", "Libros impresos y eBooks"},
                {"Deportes", "Equipo deportivo y fitness"},
                {"Jardinería", "Herramientas y accesorios de jardinería"},
                {"Herramientas", "Herramientas manuales y eléctricas"},
                {"Automoción", "Accesorios y repuestos para autos"},
                {"Oficina", "Muebles y equipos de oficina"},
                {"Hogar", "Decoración y accesorios para el hogar"},
                {"Cocina", "Utensilios y electrodomésticos de cocina"},
                {"Mascotas", "Alimentos y accesorios para mascotas"},
                {"Juguetes", "Juguetes para niños"},
                {"Moda", "Ropa, zapatos y accesorios"},
                {"Belleza", "Productos de cosmética y cuidado personal"},
                {"Música", "Instrumentos musicales y accesorios"},
                {"Cine", "DVDs, Blu-rays y proyectores"}
        };

        LocalDateTime now = LocalDateTime.now();
        for (String[] data : categoryData) {
            Document doc = new Document()
                    .append("nombre", data[0])
                    .append("descripcion", data[1])
                    .append("fechaAlta", Date.from(now.atZone(ZoneId.systemDefault()).toInstant()));
            categories.add(doc);
        }

        collection.insertMany(categories);
        System.out.println("✓ Inserted " + categories.size() + " categories");
    }

    /**
     * Seeds 100+ products with 5+ per category and specific attributes per category
     */
    private static void seedProducts(MongoDatabase database) {
        MongoDatabase db = database;
        MongoCollection<Document> categoriasCollection = db.getCollection("categorias");
        MongoCollection<Document> productosCollection = db.getCollection("productos");

        List<Document> categories = categoriasCollection.find().into(new ArrayList<>());
        List<Document> products = new ArrayList<>();
        LocalDateTime now = LocalDateTime.now();

        // Define products per category with specific attributes
        for (Document category : categories) {
            String categoryName = category.getString("nombre");
            ObjectId categoryId = category.getObjectId("_id");

            switch (categoryName) {
                case "Informática":
                    addProductsInformatica(products, categoryId, categoryName, now);
                    break;
                case "Smartphones":
                    addProductsSmartphones(products, categoryId, categoryName, now);
                    break;
                case "Televisores":
                    addProductsTelevisores(products, categoryId, categoryName, now);
                    break;
                case "Electrodomésticos":
                    addProductsElectrodomesticos(products, categoryId, categoryName, now);
                    break;
                case "Videojuegos":
                    addProductsVideojuegos(products, categoryId, categoryName, now);
                    break;
                case "Consolas":
                    addProductsConsolas(products, categoryId, categoryName, now);
                    break;
                case "Libros":
                    addProductsLibros(products, categoryId, categoryName, now);
                    break;
                case "Deportes":
                    addProductsDeportes(products, categoryId, categoryName, now);
                    break;
                case "Jardinería":
                    addProductsJardineria(products, categoryId, categoryName, now);
                    break;
                case "Herramientas":
                    addProductsHerramientas(products, categoryId, categoryName, now);
                    break;
                case "Automoción":
                    addProductsAutomocion(products, categoryId, categoryName, now);
                    break;
                case "Oficina":
                    addProductsOficina(products, categoryId, categoryName, now);
                    break;
                case "Hogar":
                    addProductsHogar(products, categoryId, categoryName, now);
                    break;
                case "Cocina":
                    addProductsCocina(products, categoryId, categoryName, now);
                    break;
                case "Mascotas":
                    addProductsMascotas(products, categoryId, categoryName, now);
                    break;
                case "Juguetes":
                    addProductsJuguetes(products, categoryId, categoryName, now);
                    break;
                case "Moda":
                    addProductsModa(products, categoryId, categoryName, now);
                    break;
                case "Belleza":
                    addProductsBelleza(products, categoryId, categoryName, now);
                    break;
                case "Música":
                    addProductsMusica(products, categoryId, categoryName, now);
                    break;
                case "Cine":
                    addProductsCine(products, categoryId, categoryName, now);
                    break;
            }
        }

        productosCollection.insertMany(products);
        System.out.println("✓ Inserted " + products.size() + " products");
    }

    // Product insertion methods for each category

    private static void addProductsInformatica(List<Document> products, ObjectId categoryId, String categoryName, LocalDateTime now) {
        String[][] data = {
                {"Laptop Dell XPS 13", "Laptop ultradelgada de alto rendimiento", "1299.99", "15", "Dell"},
                {"Monitor LG UltraWide", "Monitor 34 pulgadas ultrawide 3440x1440", "599.99", "8", "LG"},
                {"Teclado Mecánico Corsair K95", "Teclado gaming RGB premium", "249.99", "20", "Corsair"},
                {"Mouse Logitech MX Master 3", "Mouse inalámbrico de precisión", "99.99", "30", "Logitech"},
                {"SSD Samsung 970 EVO Plus 1TB", "Unidad SSD NVMe de 1TB", "149.99", "25", "Samsung"},
                {"Torre PC NZXT H710", "Caja para PC gamer negra", "149.99", "12", "NZXT"}
        };
        addProductBatch(products, data, categoryId, categoryName, now);
    }

    private static void addProductsSmartphones(List<Document> products, ObjectId categoryId, String categoryName, LocalDateTime now) {
        String[][] data = {
                {"Samsung Galaxy S23", "Pantalla 6.1 pulgadas AMOLED", "899.99", "20", "Samsung"},
                {"iPhone 15 Pro", "Pantalla 6.1 pulgadas Super Retina", "999.99", "15", "Apple"},
                {"Google Pixel 8", "Pantalla 6.2 pulgadas OLED", "799.99", "18", "Google"},
                {"Xiaomi 13", "Pantalla 6.36 pulgadas AMOLED", "599.99", "25", "Xiaomi"},
                {"OnePlus 11", "Pantalla 6.7 pulgadas AMOLED", "699.99", "22", "OnePlus"},
                {"Motorola Edge 40", "Pantalla 6.55 pulgadas AMOLED", "499.99", "30", "Motorola"}
        };
        addProductBatch(products, data, categoryId, categoryName, now);
    }

    private static void addProductsTelevisores(List<Document> products, ObjectId categoryId, String categoryName, LocalDateTime now) {
        String[][] data = {
                {"LG OLED55C3PUA", "Pantalla 55 pulgadas 4K OLED", "2499.99", "5", "LG"},
                {"Sony BRAVIA 65XR80", "Pantalla 65 pulgadas 4K Mini-LED", "2299.99", "7", "Sony"},
                {"Samsung QLED 75Q80B", "Pantalla 75 pulgadas 4K QLED", "1999.99", "8", "Samsung"},
                {"TCL 55S535", "Pantalla 55 pulgadas 4K Smart TV", "599.99", "20", "TCL"},
                {"Hisense 65E7HQ", "Pantalla 65 pulgadas 4K Mini-LED", "799.99", "15", "Hisense"},
                {"JBL Link Bar", "Pantalla 32 pulgadas Smart TV", "349.99", "25", "JBL"}
        };
        addProductBatch(products, data, categoryId, categoryName, now);
    }

    private static void addProductsElectrodomesticos(List<Document> products, ObjectId categoryId, String categoryName, LocalDateTime now) {
        String[][] data = {
                {"Refrigeradora LG French Door", "Refrigeradora de 25 pies cúbicos", "2499.99", "4", "LG"},
                {"Lavadora Samsung FlexWash", "Lavadora con 2 tambores", "1899.99", "6", "Samsung"},
                {"Horno Eléctrico GE 30 pulgadas", "Horno autolimpiable 30 pulgadas", "1299.99", "8", "GE"},
                {"Dishwasher Bosch 800 Series", "Lavavajillas de 24 pulgadas", "1199.99", "10", "Bosch"},
                {"Aire Acondicionado Daikin 24000 BTU", "AC inverter silencioso", "899.99", "12", "Daikin"},
                {"Microondas Panasonic 2.2 cu ft", "Microondas inverter 2200W", "349.99", "20", "Panasonic"}
        };
        addProductBatch(products, data, categoryId, categoryName, now);
    }

    private static void addProductsVideojuegos(List<Document> products, ObjectId categoryId, String categoryName, LocalDateTime now) {
        String[][] data = {
                {"Baldur's Gate 3", "RPG de fantasía épico", "59.99", "30", "Larian Studios"},
                {"The Legend of Zelda: Tears of the Kingdom", "Aventura open-world", "69.99", "25", "Nintendo"},
                {"Elden Ring", "RPG de acción desafiante", "59.99", "35", "FromSoftware"},
                {"Starfield", "RPG de ciencia ficción espacial", "69.99", "20", "Bethesda"},
                {"Final Fantasy XVI", "RPG de fantasía J-RPG", "69.99", "22", "Square Enix"},
                {"Metaphor: ReFantazio", "JRPG con temas sociales", "69.99", "18", "Atlus"}
        };
        addProductBatch(products, data, categoryId, categoryName, now);
    }

    private static void addProductsConsolas(List<Document> products, ObjectId categoryId, String categoryName, LocalDateTime now) {
        String[][] data = {
                {"PlayStation 5", "Consola Sony de nueva generación", "499.99", "10", "Sony"},
                {"Xbox Series X", "Consola Microsoft de última generación", "499.99", "12", "Microsoft"},
                {"Nintendo Switch OLED", "Consola híbrida portátil", "349.99", "18", "Nintendo"},
                {"Nintendo Switch Lite", "Consola portátil compacta", "199.99", "25", "Nintendo"},
                {"Steam Deck 512GB", "Computadora portátil gaming", "649.99", "15", "Valve"},
                {"Retro Game Console", "Consola retro con 400 juegos", "39.99", "40", "Genérica"}
        };
        addProductBatch(products, data, categoryId, categoryName, now);
    }

    private static void addProductsLibros(List<Document> products, ObjectId categoryId, String categoryName, LocalDateTime now) {
        String[][] data = {
                {"Java Effective", "Guía para escribir Java profesional", "39.99", "50", "Pearson"},
                {"Clean Code", "Código limpio y buenas prácticas", "35.99", "45", "Prentice Hall"},
                {"Design Patterns", "Patrones de diseño GoF", "49.99", "40", "Addison-Wesley"},
                {"The Pragmatic Programmer", "Consejos prácticos de programación", "44.99", "38", "Pragmatic"},
                {"Refactoring", "Mejora del código existente", "37.99", "42", "Pearson"},
                {"1984", "Novela distópica clásica", "14.99", "100", "Siglo XXI"}
        };
        addProductBatch(products, data, categoryId, categoryName, now);
    }

    private static void addProductsDeportes(List<Document> products, ObjectId categoryId, String categoryName, LocalDateTime now) {
        String[][] data = {
                {"Bicicleta Mountain Bike Trek", "Bicicleta con suspensión 21 velocidades", "599.99", "10", "Trek"},
                {"Zapatillas Nike Running", "Zapatillas para correr ultraligeras", "129.99", "40", "Nike"},
                {"Mancuernas Ajustables Dumbbells", "Set de mancuernas 5-50 lbs", "249.99", "15", "PowerBlocks"},
                {"Cinta de Correr Treadmill", "Cinta motorizada plegable", "499.99", "8", "NordicTrack"},
                {"Bicicleta Estática Indoor", "Bicicleta para spinning", "399.99", "12", "Peloton"},
                {"Balón de Fútbol Profesional", "Balón oficial FIFA 2024", "149.99", "60", "Adidas"}
        };
        addProductBatch(products, data, categoryId, categoryName, now);
    }

    private static void addProductsJardineria(List<Document> products, ObjectId categoryId, String categoryName, LocalDateTime now) {
        String[][] data = {
                {"Cortadora de Césped Eléctrica", "Cortadora sin cable 40V", "399.99", "12", "Greenworks"},
                {"Bordeadora de Hilo", "Bordeadora inalámbrica 20V", "149.99", "20", "Black+Decker"},
                {"Podadora de Setos", "Podadora de setos a batería", "179.99", "18", "Makita"},
                {"Mochila Pulverizadora", "Pulverizador a presión 2 galones", "89.99", "30", "Chapin"},
                {"Rastrillo de Jardín", "Rastrillo de acero inoxidable", "34.99", "50", "Fiskars"},
                {"Macetas de Terracota", "Set de 10 macetas de terracota", "49.99", "40", "Terra"}
        };
        addProductBatch(products, data, categoryId, categoryName, now);
    }

    private static void addProductsHerramientas(List<Document> products, ObjectId categoryId, String categoryName, LocalDateTime now) {
        String[][] data = {
                {"Taladro-Destornillador DeWalt", "Taladro-destornillador 20V sin cable", "149.99", "25", "DeWalt"},
                {"Lijadora Orbital Makita", "Lijadora orbital de 5 pulgadas", "99.99", "20", "Makita"},
                {"Sierra Circular Bosch", "Sierra circular 7.25 pulgadas", "129.99", "18", "Bosch"},
                {"Caja de Herramientas 100 piezas", "Set completo de herramientas", "79.99", "35", "Stanley"},
                {"Nivel Láser Dewalt", "Nivel láser rojo de cruz", "199.99", "15", "DeWalt"},
                {"Medidor Digital Bosch", "Medidor de distancia 120m", "89.99", "22", "Bosch"}
        };
        addProductBatch(products, data, categoryId, categoryName, now);
    }

    private static void addProductsAutomocion(List<Document> products, ObjectId categoryId, String categoryName, LocalDateTime now) {
        String[][] data = {
                {"Llantas Michelin LTX 265/65R17", "Llantas all-terrain premium", "189.99", "24", "Michelin"},
                {"Batería Optima 800A", "Batería de carro 800A", "189.99", "16", "Optima"},
                {"Aceite Mobil Synthetic 5W-30", "Aceite sintético 5 litros", "49.99", "100", "Mobil"},
                {"Filtro de Aire K&N", "Filtro de aire reutilizable", "59.99", "40", "K&N"},
                {"Amortiguadores KYB Excel-G", "Par de amortiguadores", "299.99", "12", "KYB"},
                {"Frenos Brembo Cerámicos", "Pastillas de freno cerámicas", "129.99", "30", "Brembo"}
        };
        addProductBatch(products, data, categoryId, categoryName, now);
    }

    private static void addProductsOficina(List<Document> products, ObjectId categoryId, String categoryName, LocalDateTime now) {
        String[][] data = {
                {"Escritorio Gamer RGB", "Escritorio gaming con LED 150cm", "349.99", "10", "Green Soul"},
                {"Silla Ergonómica HM", "Silla ergonómica reclinable", "499.99", "8", "Herman Miller"},
                {"Lámpara LED Escritorio", "Lámpara LED recargable USB", "49.99", "50", "TaoTronics"},
                {"Organizador Escritorio Bambú", "Organizador multi-compartimentos", "39.99", "60", "Bamboo"},
                {"Pizarra Blanca Magnética", "Pizarra 90x60cm", "29.99", "80", "Nobo"},
                {"Archivador de Metal 4 gavetas", "Archivador con cerradura", "199.99", "20", "Steelcase"}
        };
        addProductBatch(products, data, categoryId, categoryName, now);
    }

    private static void addProductsHogar(List<Document> products, ObjectId categoryId, String categoryName, LocalDateTime now) {
        String[][] data = {
                {"Espejo de Pared Decorativo", "Espejo redondo 80cm diámetro", "99.99", "25", "Home Decor"},
                {"Lámpara de Techo Colgante", "Lámpara moderna minimalista", "79.99", "30", "Ikea"},
                {"Cortinas Blackout 2 paneles", "Cortinas oscurecedoras 100x200cm", "59.99", "40", "AmazonBasics"},
                {"Alfombra Persa 3x5", "Alfombra artesanal persa", "349.99", "8", "Handmade"},
                {"Cojín Decorativo Set 4", "Set de 4 cojines de terciopelo", "79.99", "35", "Cushion Craft"},
                {"Tapestría Mandala", "Tapestría de tela 150x130cm", "29.99", "50", "Mandala"}
        };
        addProductBatch(products, data, categoryId, categoryName, now);
    }

    private static void addProductsCocina(List<Document> products, ObjectId categoryId, String categoryName, LocalDateTime now) {
        String[][] data = {
                {"Juego Ollas 12 piezas", "Juego de ollas antiadherente", "149.99", "20", "T-fal"},
                {"Cuchillo Chef 8 pulgadas", "Cuchillo chef alemán de acero", "89.99", "40", "Wüsthof"},
                {"Licuadora Vitamix Pro", "Licuadora de alta potencia 2HP", "499.99", "10", "Vitamix"},
                {"Horno Tostador Breville", "Horno tostador digital", "199.99", "15", "Breville"},
                {"Batidora KitchenAid 5 qt", "Batidora stand profesional", "349.99", "12", "KitchenAid"},
                {"Juego Cuchillos 6 piezas", "Juego cuchillos con bloque madera", "79.99", "35", "Chicago Cutlery"}
        };
        addProductBatch(products, data, categoryId, categoryName, now);
    }

    private static void addProductsMascotas(List<Document> products, ObjectId categoryId, String categoryName, LocalDateTime now) {
        String[][] data = {
                {"Alimento Perro Premium 15kg", "Alimento balanceado premium", "49.99", "50", "Purina Pro"},
                {"Cama Perro Memory Foam", "Cama ortopédica de memory foam", "89.99", "20", "PetFusion"},
                {"Jaula Gato 3 niveles", "Jaula gato multi-nivel", "199.99", "12", "PAWFY"},
                {"Collar Inteligente GPS", "Collar con GPS para mascotas", "79.99", "35", "AirTag"},
                {"Juguete Pelota Interactiva", "Pelota robot interactiva", "49.99", "40", "WobbleWag"},
                {"Arenero Gato Auto-limpiante", "Arenero automático de IA", "599.99", "5", "Litter-Robot"}
        };
        addProductBatch(products, data, categoryId, categoryName, now);
    }

    private static void addProductsJuguetes(List<Document> products, ObjectId categoryId, String categoryName, LocalDateTime now) {
        String[][] data = {
                {"LEGO Harry Potter 4-16", "Set LEGO Castillo Hogwarts", "399.99", "25", "LEGO"},
                {"Drone DJI Mini 3 Pro", "Dron 4K con estabilizador", "349.99", "12", "DJI"},
                {"Robot Programable LEGO Mindstorms", "Robot educativo con IA", "299.99", "15", "LEGO"},
                {"Patineta Eléctrica Niños", "Patineta eléctrica 250W", "199.99", "18", "Razor"},
                {"Scooter Eléctrico Niños", "Scooter 3-ruedas con LED", "99.99", "40", "Micro"},
                {"Cubo de Rubik Profesional", "Cubo Rubik 3x3 Speedcubing", "19.99", "100", "Rubik's"}
        };
        addProductBatch(products, data, categoryId, categoryName, now);
    }

    private static void addProductsModa(List<Document> products, ObjectId categoryId, String categoryName, LocalDateTime now) {
        String[][] data = {
                {"Jeans Levi's 501", "Jeans clásico azul oscuro", "69.99", "100", "Levi's"},
                {"Zapatos Nike Air Max 90", "Zapatos de running Air Max", "129.99", "80", "Nike"},
                {"Camiseta Uniqlo Cotton", "Camiseta 100% algodón", "19.99", "200", "Uniqlo"},
                {"Chaqueta Leather Biker", "Chaqueta de piel auténtica", "299.99", "25", "Harley-Davidson"},
                {"Vestido Zara Evening", "Vestido de noche elegante", "149.99", "40", "Zara"},
                {"Gorro Beanie The North Face", "Gorro de lana invierno", "34.99", "60", "The North Face"}
        };
        addProductBatch(products, data, categoryId, categoryName, now);
    }

    private static void addProductsBelleza(List<Document> products, ObjectId categoryId, String categoryName, LocalDateTime now) {
        String[][] data = {
                {"Crema Facial Retinol Neutrogena", "Crema anti-envejecimiento", "24.99", "80", "Neutrogena"},
                {"Perfume Dior Miss Dior", "Perfume 100ml", "99.99", "35", "Dior"},
                {"Champú Kerastase Elixir", "Champú reparador lujo", "39.99", "50", "Kerastase"},
                {"Maquillaje MAC Studio Fix", "Base maquillaje profesional", "44.99", "60", "MAC"},
                {"Mascarilla Facial Peel-off", "Mascarilla carbón purificante", "14.99", "120", "Freeman"},
                {"Sérum Vitamina C Skinceuticals", "Sérum antioxidante 30ml", "119.99", "25", "Skinceuticals"}
        };
        addProductBatch(products, data, categoryId, categoryName, now);
    }

    private static void addProductsMusica(List<Document> products, ObjectId categoryId, String categoryName, LocalDateTime now) {
        String[][] data = {
                {"Guitarra Acústica Yamaha FG800", "Guitarra acústica 6 cuerdas", "199.99", "20", "Yamaha"},
                {"Teclado Sintetizador Casio WK-240", "Teclado 76 teclas", "299.99", "15", "Casio"},
                {"Micrófono Condenser Audio-Technica", "Micrófono para estudio", "149.99", "30", "Audio-Technica"},
                {"Audífonos Sony WH-1000XM5", "Audífonos inalámbricos noise-cancel", "349.99", "25", "Sony"},
                {"Batería Electrónica Roland TD-07", "Batería electrónica compacta", "399.99", "10", "Roland"},
                {"Violín 4/4 Cremona", "Violín de estudio", "249.99", "12", "Cremona"}
        };
        addProductBatch(products, data, categoryId, categoryName, now);
    }

    private static void addProductsCine(List<Document> products, ObjectId categoryId, String categoryName, LocalDateTime now) {
        String[][] data = {
                {"Blu-ray Avatar El Camino del Agua", "Película Blu-ray 4K", "24.99", "50", "20th Century"},
                {"DVD Set Breaking Bad Temporada 1", "Serie TV completa 4 DVD", "39.99", "40", "Sony Pictures"},
                {"Proyector 4K BenQ LU9715", "Proyector laser 4K 5000 lumens", "1999.99", "5", "BenQ"},
                {"Pantalla Proyector 100 pulgadas", "Pantalla motorizada 100 inch", "299.99", "12", "Elite Screens"},
                {"Dolby Atmos Soundbar Denon", "Soundbar 7.1 Dolby Atmos", "799.99", "8", "Denon"},
                {"Reproductor Steaming Apple TV 4K", "Streaming 4K HDR", "199.99", "35", "Apple"}
        };
        addProductBatch(products, data, categoryId, categoryName, now);
    }

    private static void addProductBatch(List<Document> products, String[][] data, ObjectId categoryId, String categoryName, LocalDateTime now) {
        for (String[] item : data) {
            Document doc = new Document()
                    .append("nombre", item[0])
                    .append("descripcion", item[1])
                    .append("precio", Double.parseDouble(item[2]))
                    .append("stock", Integer.parseInt(item[3]))
                    .append("marca", item[4])
                    .append("categoriaId", categoryId)
                    .append("categoriaNombre", categoryName)
                    .append("fechaAlta", Date.from(now.atZone(ZoneId.systemDefault()).toInstant()));
            
            // Add category-specific attributes for MongoDB flexibility
            addCategorySpecificAttributes(doc, categoryName);
            
            products.add(doc);
        }
    }

    private static void addCategorySpecificAttributes(Document doc, String categoryName) {
        switch (categoryName) {
            case "Smartphones":
                doc.append("pulgadasPantalla", 6.1)
                   .append("memoriaRAM", "8GB")
                   .append("almacenamiento", "256GB");
                break;
            case "Televisores":
                doc.append("resolucion", "4K")
                   .append("pulgadas", 55)
                   .append("tecnologia", "OLED");
                break;
            case "Libros":
                doc.append("autor", "Autor")
                   .append("editorial", "Editorial")
                   .append("ISBN", "978-0-0000000-0");
                break;
            case "Videojuegos":
                doc.append("plataforma", "PC")
                   .append("genero", "RPG");
                break;
            case "Herramientas":
                doc.append("material", "Acero Inoxidable")
                   .append("potencia", "1200W");
                break;
            case "Informática":
                doc.append("procesador", "Intel/AMD")
                   .append("RAM", "16GB")
                   .append("almacenamiento", "512GB SSD");
                break;
        }
    }

    /**
     * Seeds sample buyers
     */
    private static void seedBuyers(MongoDatabase database) {
        MongoCollection<Document> collection = database.getCollection("compradores");
        List<Document> buyers = new ArrayList<>();
        LocalDateTime now = LocalDateTime.now();

        String[][] buyerData = {
                {"Juan", "Pérez García", "juan.perez@email.com", "+34-915-123456", "Calle Principal 123, Madrid"},
                {"María", "González López", "maria.gonzalez@email.com", "+34-916-234567", "Avenida España 456, Barcelona"},
                {"Carlos", "Martínez Ruiz", "carlos.martinez@email.com", "+34-917-345678", "Plaza Mayor 789, Valencia"},
                {"Ana", "Rodríguez Fernández", "ana.rodriguez@email.com", "+34-918-456789", "Paseo Recoletos 321, Bilbao"},
                {"Pedro", "López Jiménez", "pedro.lopez@email.com", "+34-919-567890", "Ronda Sant Antoni 654, Sevilla"},
                {"Laura", "García Sánchez", "laura.garcia@email.com", "+34-920-678901", "Calle Toledo 987, Zaragoza"},
                {"Miguel", "Fernández Torres", "miguel.fernandez@email.com", "+34-921-789012", "Avenida Diagonal 147, Málaga"},
                {"Sofia", "Díaz Moreno", "sofia.diaz@email.com", "+34-922-890123", "Paseo Marítimo 258, Palma"},
                {"Diego", "Sánchez Cabrera", "diego.sanchez@email.com", "+34-923-901234", "Calle Mayor 369, Murcia"},
                {"Isabel", "Morales Vega", "isabel.morales@email.com", "+34-924-012345", "Ronda Pinos 741, Córdoba"}
        };

        for (String[] data : buyerData) {
            Document doc = new Document()
                    .append("nombre", data[0])
                    .append("apellidos", data[1])
                    .append("email", data[2])
                    .append("telefono", data[3])
                    .append("direccion", data[4])
                    .append("fechaRegistro", Date.from(now.atZone(ZoneId.systemDefault()).toInstant()));
            buyers.add(doc);
        }

        collection.insertMany(buyers);
        System.out.println("✓ Inserted " + buyers.size() + " buyers");
    }

    /**
     * Seeds sample sales with multiple products
     */
    private static void seedSales(MongoDatabase database) {
        MongoCollection<Document> compradorCollection = database.getCollection("compradores");
        MongoCollection<Document> productosCollection = database.getCollection("productos");
        MongoCollection<Document> ventasCollection = database.getCollection("ventas");

        List<Document> compradores = compradorCollection.find().into(new ArrayList<>());
        List<Document> allProducts = productosCollection.find().into(new ArrayList<>());

        List<Document> sales = new ArrayList<>();
        LocalDateTime now = LocalDateTime.now();

        // Create 10 sample sales
        for (int i = 0; i < Math.min(10, compradores.size()); i++) {
            Document buyer = compradores.get(i);
            List<Document> items = new ArrayList<>();
            double total = 0;

            // Each sale has 2-4 random products from different categories
            for (int j = 0; j < 3; j++) {
                Document product = allProducts.get((i + j) % allProducts.size());
                int quantity = 1 + (i * j % 3);
                double subtotal = product.getDouble("precio") * quantity;
                
                Document item = new Document()
                        .append("productoId", product.getObjectId("_id"))
                        .append("productoNombre", product.getString("nombre"))
                        .append("categoriaNombre", product.getString("categoriaNombre"))
                        .append("cantidad", quantity)
                        .append("precioUnitario", product.getDouble("precio"))
                        .append("subtotal", subtotal);
                items.add(item);
                total += subtotal;
            }

            Document sale = new Document()
                    .append("compradorId", buyer.getObjectId("_id"))
                    .append("compradorNombre", buyer.getString("nombre"))
                    .append("productosComprados", items)
                    .append("cantidadProductos", items.size())
                    .append("subtotal", total)
                    .append("impuesto", total * 0.15)
                    .append("totalVenta", total * 1.15)
                    .append("fecha", Date.from(now.minusDays(i).atZone(ZoneId.systemDefault()).toInstant()))
                    .append("estado", "Completada");
            
            sales.add(sale);
        }

        ventasCollection.insertMany(sales);
        System.out.println("✓ Inserted " + sales.size() + " sales");
    }
}
