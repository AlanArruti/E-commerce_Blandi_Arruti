package com.BlandiArruti.E_commerce.config;

import com.BlandiArruti.E_commerce.administrador.entity.Administrador;
import com.BlandiArruti.E_commerce.administrador.repository.AdministradorRepository;
import com.BlandiArruti.E_commerce.categoria.entity.Categoria;
import com.BlandiArruti.E_commerce.categoria.repository.CategoriaRepository;
import com.BlandiArruti.E_commerce.cliente.entity.Cliente;
import com.BlandiArruti.E_commerce.cliente.entity.Direccion;
import com.BlandiArruti.E_commerce.cliente.repository.ClienteRepository;
import com.BlandiArruti.E_commerce.cliente.repository.DireccionRepository;
import com.BlandiArruti.E_commerce.envio.entity.Envio;
import com.BlandiArruti.E_commerce.envio.repository.EnvioRepository;
import com.BlandiArruti.E_commerce.enums.EstadoEnvio;
import com.BlandiArruti.E_commerce.enums.EstadoPedido;
import com.BlandiArruti.E_commerce.enums.TipoFactura;
import com.BlandiArruti.E_commerce.factura.entity.Factura;
import com.BlandiArruti.E_commerce.factura.repository.FacturaRepository;
import com.BlandiArruti.E_commerce.geo.entity.Ciudad;
import com.BlandiArruti.E_commerce.geo.entity.Pais;
import com.BlandiArruti.E_commerce.geo.entity.Provincia;
import com.BlandiArruti.E_commerce.geo.repository.CiudadRepository;
import com.BlandiArruti.E_commerce.geo.repository.PaisRepository;
import com.BlandiArruti.E_commerce.geo.repository.ProvinciaRepository;
import com.BlandiArruti.E_commerce.pedido.entity.ItemPedido;
import com.BlandiArruti.E_commerce.pedido.entity.Pedido;
import com.BlandiArruti.E_commerce.pedido.repository.ItemPedidoRepository;
import com.BlandiArruti.E_commerce.pedido.repository.PedidoRepository;
import com.BlandiArruti.E_commerce.producto.entity.Producto;
import com.BlandiArruti.E_commerce.producto.entity.Variante;
import com.BlandiArruti.E_commerce.producto.repository.ProductoRepository;
import com.BlandiArruti.E_commerce.producto.repository.VarianteRepository;
import com.BlandiArruti.E_commerce.exception.EntidadNoEncontradaException;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class DataLoader implements CommandLineRunner {

    private final AdministradorRepository administradorRepository;
    private final ClienteRepository clienteRepository;
    private final PasswordEncoder passwordEncoder;
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

        if (paisRepository.count() > 0) {
            return;
        }

        //------------GEO------------//
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

        //------------VARIANTES------------//
        Variante laptopPlata = varianteRepository.save(Variante.builder()
                .producto(laptop)
                .atributos(Map.of("color", "Plata", "ram", "16GB"))
                .stock(10)
                .build());
        varianteRepository.save(Variante.builder()
                .producto(laptop)
                .atributos(Map.of("color", "Negro", "ram", "32GB"))
                .stock(5)
                .build());
        Variante remeraM = varianteRepository.save(Variante.builder()
                .producto(remera)
                .atributos(Map.of("talla", "M", "color", "Blanco"))
                .stock(50)
                .build());
        varianteRepository.save(Variante.builder()
                .producto(remera)
                .atributos(Map.of("talla", "M", "color", "Negro"))
                .stock(0)
                .build());
        varianteRepository.save(Variante.builder()
                .producto(remera)
                .atributos(Map.of("talla", "L", "color", "Blanco"))
                .stock(2)
                .build());
        Variante sillaRoja = varianteRepository.save(Variante.builder()
                .producto(silla)
                .atributos(Map.of("color", "Rojo"))
                .stock(15)
                .build());

        //------------ADMIN------------//
        administradorRepository.save(Administrador.builder()
                .username("admin")
                .contrasenia(passwordEncoder.encode("admin123"))
                .build());

        //------------CLIENTES------------//
        String hashPassword = passwordEncoder.encode("password123");

        Cliente juan = clienteRepository.save(Cliente.builder()
                .nombre("Juan")
                .apellido("Pérez")
                .dni("30111222")
                .email("juan@mail.com")
                .contrasenia(hashPassword)
                .build());
        Cliente maria = clienteRepository.save(Cliente.builder()
                .nombre("María")
                .apellido("González")
                .dni("28999888")
                .email("maria@mail.com")
                .contrasenia(hashPassword)
                .build());
        Cliente pedro = clienteRepository.save(Cliente.builder()
                .nombre("Pedro")
                .apellido("López")
                .dni("35444555")
                .email("pedro@mail.com")
                .contrasenia(hashPassword)
                .build());

        //------------DIRECCIONES------------//
        Ciudad marDelPlata = ciudadRepository.findByNombre("Mar del Plata")
                .orElseThrow(() -> new EntidadNoEncontradaException("Ciudad 'Mar del Plata' no encontrada."));
        Ciudad laPlata = ciudadRepository.findByNombre("La Plata")
                .orElseThrow(() -> new EntidadNoEncontradaException("Ciudad 'La Plata' no encontrada."));
        Ciudad cordobaCapital = ciudadRepository.findByNombre("Córdoba Capital")
                .orElseThrow(() -> new EntidadNoEncontradaException("Ciudad 'Córdoba Capital' no encontrada."));

        direccionRepository.save(Direccion.builder()
                .nombreCalle("Av. Colón")
                .numeroCalle(1234)
                .ciudad(marDelPlata)
                .cliente(juan)
                .build());
        direccionRepository.save(Direccion.builder()
                .nombreCalle("Calle 13")
                .numeroCalle(567)
                .ciudad(laPlata)
                .cliente(maria)
                .build());
        Direccion dirPedro = direccionRepository.save(Direccion.builder()
                .nombreCalle("Bv. San Juan")
                .numeroCalle(890)
                .ciudad(cordobaCapital)
                .cliente(pedro)
                .build());

        //------------PEDIDOS------------//
        Pedido pedido1 = pedidoRepository.save(Pedido.builder()
                .cliente(juan)
                .estadoPedido(EstadoPedido.PENDIENTE_PAGO)
                .build());
        Pedido pedido2 = pedidoRepository.save(Pedido.builder()
                .cliente(maria)
                .estadoPedido(EstadoPedido.PAGADO)
                .build());
        Pedido pedido3 = pedidoRepository.save(Pedido.builder()
                .cliente(pedro)
                .estadoPedido(EstadoPedido.ENTREGADO)
                .build());

        //------------ITEMS PEDIDO------------//
        itemPedidoRepository.save(ItemPedido.builder()
                .pedido(pedido1)
                .variante(laptopPlata)
                .producto(laptop)
                .cantidad(1)
                .precioProducto(laptop.getPrecio())
                .build());
        itemPedidoRepository.save(ItemPedido.builder()
                .pedido(pedido2)
                .variante(remeraM)
                .producto(remera)
                .cantidad(3)
                .precioProducto(remera.getPrecio())
                .build());
        itemPedidoRepository.save(ItemPedido.builder()
                .pedido(pedido3)
                .variante(sillaRoja)
                .producto(silla)
                .cantidad(2)
                .precioProducto(silla.getPrecio())
                .build());

        //------------ENVIOS------------//
        // pedido1 (PENDIENTE_PAGO): sin envio
        // pedido2 (PAGADO): sin envio aun
        // pedido3 (ENTREGADO): tiene envio entregado
        envioRepository.save(Envio.builder()
                .pedido(pedido3)
                .direccion(dirPedro)
                .estado(EstadoEnvio.ENTREGADO)
                .fechaSalida(LocalDate.now().minusDays(5))
                .fechaLlegada(LocalDate.now().minusDays(2))
                .build());

        //------------FACTURAS------------//
        // pedido1 (PENDIENTE_PAGO): sin factura
        // pedido2 (PAGADO): factura generada al pagar
        // pedido3 (ENTREGADO): factura generada al pagar
        facturaRepository.save(Factura.builder()
                .pedido(pedido2)
                .tipoFactura(TipoFactura.B)
                .precioTotal(remera.getPrecio() * 3)
                .build());
        facturaRepository.save(Factura.builder()
                .pedido(pedido3)
                .tipoFactura(TipoFactura.A)
                .precioTotal(silla.getPrecio() * 2)
                .build());
    }
}
