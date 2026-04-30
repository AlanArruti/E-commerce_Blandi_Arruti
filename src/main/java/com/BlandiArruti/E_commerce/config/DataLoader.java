package com.BlandiArruti.E_commerce.config;

import com.BlandiArruti.E_commerce.entity.*;
import com.BlandiArruti.E_commerce.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class DataLoader implements CommandLineRunner {

    private final AdministradorRepository administradorRepository;
    private final ClienteRepository clienteRepository;
    private final PaisRepository paisRepository;
    private final ProvinciaRepository provinciaRepository;
    private final CiudadRepository ciudadRepository;
    private final DireccionRepository direccionRepository;
    private final ProductoRepository productoRepository;
    private final VarianteRepository varianteRepository;
    private final PedidoRepository pedidoRepository;
    private final ItemPedidoRepository itemPedidoRepository;
    private final FacturaRepository facturaRepository;
    private final CategoriaRepository categoriaRepository;
    private final EnvioRepository envioRepository;



    @Override
    public void run(String... args) {

        //------------GEO------------//
        if (paisRepository.count() > 0) {
            return;
        }
        Pais argentina = paisRepository.save(
                Pais.builder().nombre("Argentina").build()
        );

        Pais uruguay = paisRepository.save(
                Pais.builder().nombre("Uruguay").build()
        );
        Provincia bsAs = provinciaRepository.save(
                Provincia.builder().nombre("Buenos Aires").pais(argentina).build()
        );

        Provincia cordoba = provinciaRepository.save(
                Provincia.builder().nombre("Córdoba").pais(argentina).build()
        );

        Provincia montevideo = provinciaRepository.save(
                Provincia.builder().nombre("Montevideo").pais(uruguay).build()
        );

        Provincia canelones = provinciaRepository.save(
                Provincia.builder().nombre("Canelones").pais(uruguay).build()
        );
        ciudadRepository.save(Ciudad.builder().nombre("Mar del Plata").provincia(bsAs).build());
        ciudadRepository.save(Ciudad.builder().nombre("La Plata").provincia(bsAs).build());

        ciudadRepository.save(Ciudad.builder().nombre("Córdoba Capital").provincia(cordoba).build());
        ciudadRepository.save(Ciudad.builder().nombre("Villa Carlos Paz").provincia(cordoba).build());

        ciudadRepository.save(Ciudad.builder().nombre("Montevideo").provincia(montevideo).build());
        ciudadRepository.save(Ciudad.builder().nombre("Pocitos").provincia(montevideo).build());

        ciudadRepository.save(Ciudad.builder().nombre("Las Piedras").provincia(canelones).build());
        ciudadRepository.save(Ciudad.builder().nombre("Pando").provincia(canelones).build());

        //------------GEO------------//

        //------------CATEGORIA------------//
        Categoria electronica = categoriaRepository.save(
                Categoria.builder().nombre("Electrónica").build()
        );
        Categoria indumentaria = categoriaRepository.save(
                Categoria.builder().nombre("Indumentaria").build()
        );
        Categoria hogar = categoriaRepository.save(
                Categoria.builder().nombre("Hogar").build()
        );
        Categoria deportes = categoriaRepository.save(
                Categoria.builder().nombre("Deportes").build()
        );

        //------------CATEGORIA------------//

        //------------PRODUCTO------------//
        Producto laptop = productoRepository.save(Producto.builder()
                .nombre("Laptop")
                .descripcion("Laptop con procesador Intel i7 y 16GB de RAM.")
                .precio(999.99)
                .categoria(electronica)
                .build());

        Producto smartphone = productoRepository.save(Producto.builder()
                .nombre("Smartphone")
                .descripcion("Smartphone Android con pantalla OLED de 6.5 pulgadas.")
                .precio(699.99)
                .categoria(electronica)
                .build());

        Producto remera = productoRepository.save(Producto.builder()
                .nombre("Remera básica")
                .descripcion("Remera de algodón 100% color sólido.")
                .precio(4999.99)
                .categoria(indumentaria)
                .build());

        Producto silla = productoRepository.save(Producto.builder()
                .nombre("Silla gamer")
                .descripcion("Silla ergonómica con apoyabrazos ajustables.")
                .precio(199.99)
                .categoria(hogar)
                .build());

        Producto pelota = productoRepository.save(Producto.builder()
                .nombre("Pelota fútbol")
                .descripcion("Pelota oficial talle 5.")
                .precio(89.99)
                .categoria(deportes)
                .build());
        //------------PRODUCTO------------//

        //------------VARIANTES------------//
        // Laptop: 2 variantes (color)
        varianteRepository.save(Variante.builder()
                .producto(laptop)
                .atributos(Map.of("color", "Plata", "ram", "16GB"))
                .stock(10)
                .build());
        varianteRepository.save(Variante.builder()
                .producto(laptop)
                .atributos(Map.of("color", "Negro", "ram", "32GB"))
                .stock(5)
                .build());

// Remera: 4 variantes (talla x color)
        varianteRepository.save(Variante.builder()
                .producto(remera)
                .atributos(Map.of("talla", "M", "color", "Blanco"))
                .stock(50)
                .build());
        varianteRepository.save(Variante.builder()
                .producto(remera)
                .atributos(Map.of("talla", "M", "color", "Negro"))
                .stock(0)  // ← stock 0 para probar "sin stock"
                .build());
        varianteRepository.save(Variante.builder()
                .producto(remera)
                .atributos(Map.of("talla", "L", "color", "Blanco"))
                .stock(2)  // ← stock bajo para probar "stock insuficiente"
                .build());

// Silla: 1 variante
        varianteRepository.save(Variante.builder()
                .producto(silla)
                .atributos(Map.of("color", "Rojo"))
                .stock(15)
                .build());

        //------------VARIANTES------------//

        //------------ADMIN------------//
        administradorRepository.save(Administrador.builder()
                .username("admin")
                .contrasenia("admin123")
                .build());

        //------------ADMIN------------//

        //------------CLIENTES------------//
        Cliente juan = clienteRepository.save(Cliente.builder()
                .nombre("Juan")
                .apellido("Pérez")
                .dni("30111222")
                .email("juan@mail.com")
                .contrasenia("password123")
                .build());

        Cliente maria = clienteRepository.save(Cliente.builder()
                .nombre("María")
                .apellido("González")
                .dni("28999888")
                .email("maria@mail.com")
                .contrasenia("password123")
                .build());

        Cliente pedro = clienteRepository.save(Cliente.builder()
                .nombre("Pedro")
                .apellido("López")
                .dni("35444555")
                .email("pedro@mail.com")
                .contrasenia("password123")
                .build());
        
        //------------CLIENTES------------//



    }
}