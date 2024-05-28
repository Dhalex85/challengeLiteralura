package com.alura.challengeLiteralura.model;
import jakarta.persistence.*;
import org.springframework.boot.autoconfigure.domain.EntityScan;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Table(name="libros")
public class Libro {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long Id;
    @Column(unique = true)
    private String titulo;
    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> idiomas;
    private Integer descargas;
    @ManyToMany(cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    private List<Autor> autores = new ArrayList<>();

    public Libro(){}

    public Libro(DatosLibro datosL){
        this.titulo=datosL.titulo();
        this.idiomas=datosL.idiomas();
        this.descargas= datosL.descargas();
    }

    public List<Autor> getAutores() {
        return autores;
    }

    public void setAutores(List<Autor> autores) {
        this.autores = autores;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public List<String> getIdiomas() {
        return idiomas;
    }

    public void setIdiomas(List<String> idiomas) {
        this.idiomas = idiomas;
    }

    public Integer getDescargas() {
        return descargas;
    }

    public void setDescargas(Integer descargas) {
        this.descargas = descargas;
    }

    public void agregarAutor(Autor autor){
        autores.add(autor);
        //autor.addLibro(this);
    }
    public void removeAutor(Autor autor) {
        autores.remove(autor);
        autor.getLibros().remove(this);
    }

    @Override
    public String toString() {
        return titulo;
    }
}
