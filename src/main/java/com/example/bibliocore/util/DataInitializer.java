package com.example.bibliocore.util;

import com.example.bibliocore.modelo.*;
import com.example.bibliocore.repositorio.*;
import com.example.bibliocore.servicio.PrestamoServicio;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Component
public class DataInitializer implements CommandLineRunner {

        @Value("${app.base-url}")
        private String baseUrl;

        private final BCryptPasswordEncoder passwordEncoder;

        private final GeneroRepositorio generoRepo;
        private final LibroRepositorio libroRepo;
        private final AutorRepositorio autorRepo;
        private final EditorialRepositorio editorialRepo;
        private final UsuarioRepositorio usuarioRepo;
        private final PrestamoRepositorio prestamoRepo;
        private final ListaDeseadosRepositorio listaDeseadosRepo;
        private final PrestamoServicio prestamoServicio;

        public DataInitializer(GeneroRepositorio generoRepo, LibroRepositorio libroRepo,
                        AutorRepositorio autorRepo, EditorialRepositorio editorialRepo, UsuarioRepositorio usuarioRepo,
                        PrestamoRepositorio prestamoRepo, ListaDeseadosRepositorio listaDeseadosRepo,
                        PrestamoServicio prestamoServicio,
                        BCryptPasswordEncoder passwordEncoder) {
                this.generoRepo = generoRepo;
                this.libroRepo = libroRepo;
                this.autorRepo = autorRepo;
                this.editorialRepo = editorialRepo;
                this.usuarioRepo = usuarioRepo;
                this.prestamoRepo = prestamoRepo;
                this.listaDeseadosRepo = listaDeseadosRepo;
                this.prestamoServicio = prestamoServicio;
                this.passwordEncoder = passwordEncoder;
        }

        @Override
        @Transactional
        public void run(String... args) throws Exception {
                // Solo si la base de datos está vacía.
                if (libroRepo.count() == 0) {

                        // Géneros
                        Genero f = new Genero();
                        f.setNombre("Fantasía");

                        Genero e = new Genero();
                        e.setNombre("Épica");

                        Genero a = new Genero();
                        a.setNombre("Aventura");

                        generoRepo.saveAll(List.of(f, e, a));
                        Genero clasico = new Genero();
                        clasico.setNombre("Clásico");
                        generoRepo.save(clasico);

                        Genero mito = new Genero();
                        mito.setNombre("Mitología");
                        generoRepo.save(mito);

                        // Autor
                        Autor sanderson = new Autor();
                        sanderson.setNombre("Brandon Sanderson");
                        sanderson.setNacionalidad("Estadounidense");
                        autorRepo.save(sanderson);

                        Autor tolkien = new Autor();
                        tolkien.setNombre("J.R.R. Tolkien");
                        autorRepo.save(tolkien);

                        // Editorial
                        Editorial nova = new Editorial();
                        nova.setNombre("Nova");
                        editorialRepo.save(nova);

                        Editorial minotauro = new Editorial();
                        minotauro.setNombre("Minotauro");
                        editorialRepo.save(minotauro);

                        // Libros de prueba

                        // Libro 1 Mistborn: El Imperio Final
                        Libro l1 = new Libro(
                                        "Mistborn: El Imperio Final",
                                        "En un mundo donde las cenizas caen del cielo, un grupo de valientes intenta derrocar a un dios.",
                                        "978-8466658898",
                                        "Primera Edición",
                                        "Español",
                                        2006,
                                        null,
                                        2,
                                        baseUrl + "/portadas/MistBorn1.jpg",
                                        nova,
                                        List.of(sanderson),
                                        List.of(f, e));

                        libroRepo.save(l1);

                        // Libro 1 El Camino de los Reyes
                        Libro archivo1 = new Libro(
                                        "El Camino de los Reyes",
                                        "La epopeya de Roshar comienza aquí, con la Guerra de la Venganza y los Caballeros Radiantes.",
                                        "978-8466657648",
                                        "Primera Edición",
                                        "Español",
                                        2010,
                                        null,
                                        1,
                                        baseUrl + "/portadas/Archivo1.jpg",
                                        nova,
                                        List.of(sanderson),
                                        List.of(f, e, a));
                        libroRepo.save(archivo1);

                        // Libro 2 Palabras Radiantes
                        Libro archivo2 = new Libro(
                                        "Palabras Radiantes",
                                        "Dalinar Kholin lidera a los ejércitos de los Alezi contra los Parshendi mientras Kaladin lucha por protegerlo.",
                                        "978-8466657501",
                                        "Primera Edición",
                                        "Español",
                                        2014,
                                        null,
                                        5,
                                        baseUrl + "/portadas/Archivo2.jpg",
                                        nova,
                                        List.of(sanderson),
                                        List.of(f, e, a));
                        libroRepo.save(archivo2);

                        // Libro 3 Juramentada
                        Libro archivo3 = new Libro(
                                        "Juramentada",
                                        "La humanidad se enfrenta a una nueva Desolación mientras los secretos de la ciudad de Urithiru salen a la luz.",
                                        "978-8417347000",
                                        "Primera Edición",
                                        "Español",
                                        2017,
                                        null,
                                        5,
                                        baseUrl + "/portadas/Archivo3.jpg",
                                        nova,
                                        List.of(sanderson),
                                        List.of(f, e, a));
                        libroRepo.save(archivo3);

                        // Libro 4 El Ritmo de la Guerra
                        Libro archivo4 = new Libro(
                                        "El Ritmo de la Guerra",
                                        "Una carrera tecnológica y mística comienza mientras la ocupación de Urithiru pone a prueba a los Radiantes.",
                                        "978-8417347918",
                                        "Primera Edición",
                                        "Español",
                                        2020,
                                        null,
                                        5,
                                        baseUrl + "/portadas/Archivo4.jpg",
                                        nova,
                                        List.of(sanderson),
                                        List.of(f, e, a));
                        libroRepo.save(archivo4);

                        // Libro 5 Viento y Verdad
                        Libro archivo5 = new Libro(
                                        "Viento y Verdad",
                                        "El final de la primera pentalogía del Archivo de las Tormentas. El duelo de campeones contra Odium es inminente.",
                                        "978-8418037000",
                                        "Primera Edición",
                                        "Español",
                                        2024,
                                        null,
                                        5,
                                        baseUrl + "/portadas/Archivo5.jpg",
                                        nova,
                                        List.of(sanderson),
                                        List.of(f, e, a));
                        libroRepo.save(archivo5);

                        // --- LA COMUNIDAD DEL ANILLO ---
                        Libro esda1 = new Libro(
                                        "El Señor de los Anillos: La Comunidad del Anillo",
                                        "En la adormecida Comarca, un joven hobbit recibe un encargo: custodiar el Anillo Único y emprender un viaje hacia su destrucción.",
                                        "978-8445073728",
                                        "Edición Ilustrada",
                                        "Español",
                                        1954,
                                        null,
                                        1,
                                        baseUrl + "/portadas/ESDA1.jpg",
                                        minotauro,
                                        List.of(tolkien),
                                        List.of(f, e, a, clasico));
                        libroRepo.save(esda1);

                        // --- LAS DOS TORRES ---
                        Libro esda2 = new Libro(
                                        "El Señor de los Anillos: Las Dos Torres",
                                        "La Comunidad se ha disuelto. Mientras Frodo y Sam se acercan a Mordor, sus compañeros luchan por defender el mundo libre.",
                                        "978-8445073735",
                                        "Edición Ilustrada",
                                        "Español",
                                        1954,
                                        null,
                                        1,
                                        baseUrl + "/portadas/ESDA2.jpg",
                                        minotauro,
                                        List.of(tolkien),
                                        List.of(f, e, a, mito));
                        libroRepo.save(esda2);

                        // --- EL RETORNO DEL REY ---
                        Libro esda3 = new Libro(
                                        "El Señor de los Anillos: El Retorno del Rey",
                                        "Los ejércitos de Sauron se concentran ante Minas Tirith. El destino de la Tierra Media depende de la destrucción del Anillo.",
                                        "978-8445073742",
                                        "Edición Ilustrada",
                                        "Español",
                                        1955,
                                        null,
                                        1,
                                        baseUrl + "/portadas/ESDA3.jpg",
                                        minotauro,
                                        List.of(tolkien),
                                        List.of(f, e, a, clasico, mito));
                        libroRepo.save(esda3);

                        // Usuarios
                        Usuario root = new Usuario("root", passwordEncoder.encode("root"), "Administrador",
                                        "admin@bibliocore.com");
                        root.setAdmin(true);

                        ListaDeseados listaDese1 = new ListaDeseados();
                        listaDese1.setUsuario(root);
                        listaDeseadosRepo.save(listaDese1);
                        usuarioRepo.save(root);

                        // Crear un usuario de prueba para Kaladin
                        Usuario usuario = new Usuario("kaladin", passwordEncoder.encode("1234"),
                                        "Kaladin Bendito por la Tormenta",
                                        "kaladin@benditotormenta.com");

                        ListaDeseados listaDese2 = new ListaDeseados();
                        listaDese2.setUsuario(usuario);
                        listaDeseadosRepo.save(listaDese2);
                        usuarioRepo.save(usuario);

                        // Crear un usuario de prueba para Shallan
                        Usuario usuario2 = new Usuario("lightweaver", passwordEncoder.encode("dibujo123"),
                                        "Shallan Davar de Jah Keved",
                                        "shallan.davar@archivo.com");
                        // Ella empieza sin sanciones, una ciudadana ejemplar (por ahora...)

                        ListaDeseados listaDese3 = new ListaDeseados();
                        listaDese3.setUsuario(usuario2);
                        listaDeseadosRepo.save(listaDese3);

                        usuarioRepo.save(usuario2);

                        // Prestamo.
                        Prestamo prestamo1 = new Prestamo(usuario, l1);
                        prestamoRepo.save(prestamo1);

                        Prestamo prestamo2 = new Prestamo(usuario2, archivo1);
                        prestamoRepo.save(prestamo2);

                        // Prueba sancion
                        prestamoServicio.devolverLibro(prestamo1.getId());

                        Prestamo pAtrasado = new Prestamo(usuario, archivo1);
                        pAtrasado.setFechaInicioPrestamo(LocalDate.now().minusDays(10));
                        pAtrasado.setFechaDevolucionPrevista(LocalDate.now().minusDays(1));
                        pAtrasado.devolucion();
                        usuarioRepo.save(usuario);
                        prestamoRepo.save(pAtrasado);

                        // Prueba lista deseados
                        listaDese1.agregarOQuitarLibro(archivo2);
                        listaDeseadosRepo.save(listaDese1);

                        listaDese2.agregarOQuitarLibro(archivo3);
                        listaDese2.agregarOQuitarLibro(archivo4);
                        listaDeseadosRepo.save(listaDese2);

                        listaDese3.agregarOQuitarLibro(archivo2);
                        listaDese3.agregarOQuitarLibro(archivo3);
                        listaDese3.agregarOQuitarLibro(archivo4);
                        listaDese3.agregarOQuitarLibro(archivo5);
                        listaDeseadosRepo.save(listaDese3);

                        System.out.println("¡Base de datos de Bibliocore inicializada con el Cosmere!");
                }
        }
}