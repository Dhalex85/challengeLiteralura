package com.alura.challengeLiteralura.principal;

import com.alura.challengeLiteralura.model.*;
import com.alura.challengeLiteralura.repository.LibroRepository;
import com.alura.challengeLiteralura.service.ConsumoApi;
import com.alura.challengeLiteralura.service.ConvierteDatos;


import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;


public class Principal {
    //private LibrosRepository repository;
    private Scanner entrada = new Scanner(System.in);
    private ConsumoApi consumoApi = new ConsumoApi();
    private final String URL_BASE = "http://gutendex.com/books/?search=";
    private ConvierteDatos conversor = new ConvierteDatos();
    private LibroRepository repository;
    private List<Libro> libroList;
    private List<Autor> autorList;

    public Principal(LibroRepository repository) {
        this.repository = repository;
    }

    public void showMenu() {
        var opc = -1;
        System.out.println("***LITERALURA***\n");

        while (opc != 0) {
            try {
                var menu = """
                        1. Buscar libro
                        2. Listar libros registrados
                        3. Listar autores registrados
                        4. Listar autores vivos segun una fecha
                        5. Listar libros por idioma
                        0. Salir
                        """;
                System.out.println(menu);
                opc = entrada.nextInt();
                entrada.nextLine();
                switch (opc) {
                    case 1:
                        buscarLibro();
                        break;
                    case 2:
                        listarLibros();
                        break;
                    case 3:
                        listarAutores();
                        break;
                    case 4:
                        listarAutoresVivos();
                        break;
                    case 5:
                        listarLibrosIdioma();
                        break;
                    case 0:
                        System.out.println("Saliendo.....");
                        break;
                    default:
                        System.out.println("Opcion invalida");
                }
            } catch (InputMismatchException e) {
                System.out.println("Opcion invalida (Ingrese un numero)");
                entrada.next();
            }
        }
    }


    private DatosLibro getLibro() {
        System.out.println("Ingresa el titulo del libro que quieres buscar: ");
        var nombreSer = entrada.nextLine();
        var json = consumoApi.obtenerDatos(URL_BASE + nombreSer.replace(" ", "%20"));
        return conversor.obtenerLibro(json);
    }

    private void buscarLibro() {
        DatosLibro datos = getLibro();
        if (datos == null) {
            System.out.println("\n***Libro no encontrado***\n");
        } else {
            boolean banderaLibro = libroExistente(datos.titulo());
            if (banderaLibro) {
                System.out.println("\n***El libro ya existe en la base de datos***\n");
            } else {
                List<Autor> autores = datos.autores().stream()
                        .map(d -> new Autor(d))
                        .collect(Collectors.toList());
                Libro libro = new Libro(datos);
                Autor autorExistente = repository.findByName(autores.getFirst().getNombre());
                if (autorExistente != null) {
                    repository.saveAndFlush(libro);
                    libro.agregarAutor(autorExistente);
                    repository.save(libro);
                    System.out.println("---Libro agregado a un autor existente---");
                } else {
                    libro.setAutores(autores);
                    repository.save(libro);
                    System.out.println("---Libro agregado correctamente---");
                }
            }
        }
    }

    private boolean libroExistente(String title) {
        List<Libro> librosExistente = repository.findAll();
        boolean existe = librosExistente.stream()
                .anyMatch(l -> l.getTitulo().equals(title));
        if (existe) {
            return true;
        } else {
            return false;
        }
    }

    private void listarLibros() {
        imprimirLibros(repository.findAll());
    }

    private void imprimirLibros(List<Libro> libroList) {
        libroList.stream()
                .forEach(l -> System.out.printf(
                        "*************-Libro-***************" +
                                "\ntitulo: " + l.getTitulo() +
                                "\nidiomas: " + l.getIdiomas() +
                                "\ndescargas: " + l.getDescargas() +
                                "\nautores: " + l.getAutores() + "\n**************************\n"));
    }

    private void imprimirAutores(List<Autor> autoresImprimir) {
        autoresImprimir.stream()
                .forEach(a -> System.out.printf("************-Autor-****************\n" +
                        "Nombre: " + a.getNombre() +
                        "\nAño de Nacimiento: " + a.getAnioNacimiento() +
                        "\nAño de muerte: " + a.getAnioMuerte() +
                        "\nLibros: " + a.getLibros() +
                        "\n***************************\n"));
    }

    private void listarAutores() {
        imprimirAutores(obtenerAutores());
    }

    private List<Autor> obtenerAutores() {
        autorList = repository.getAutores();
        return autorList;
    }

    private void listarAutoresVivos() {
        System.out.println("Por favor, introduce el año a validar: ");
        String respuesta = entrada.nextLine();

        try {
            int anio = Integer.parseInt(respuesta);
            List<Autor> autors = obtenerAutores();
            List<Autor> autoresVivos = autors.stream()
                    .filter(autor -> autor.getAnioNacimiento() <= anio && (autor.getAnioMuerte() == null || autor.getAnioMuerte() >= anio))
                    .collect(Collectors.toList());
            imprimirAutores(autoresVivos);
        } catch (NumberFormatException e) {
            System.out.println("Por favor, introduce un número valido");
        }
    }

    private void listarLibrosIdioma() {
        System.out.println("***Busqueda de libros por idioma***");
        System.out.println("1.Inglés");
        System.out.println("2.Español");
        System.out.println("Elige una opcion: ");
        String opcion = entrada.nextLine();
        try {
            int idioma = Integer.parseInt(opcion);
            List<Libro> librosIdioma;
            if (idioma==1){
                librosIdioma = repository.findLibrosByIdioma("en");
                imprimirLibros(librosIdioma);
            } else if (idioma == 2) {
                librosIdioma = repository.findLibrosByIdioma("es");
                imprimirLibros(librosIdioma);
            }else {
                System.out.println("Opción invalida");
            }
        } catch (NumberFormatException e) {
            System.out.println("Por favor, introduce un número valido");
        }
    }
}
