package com.alura.challengeLiteralura.repository;


import com.alura.challengeLiteralura.model.Autor;
import com.alura.challengeLiteralura.model.Libro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface LibroRepository extends JpaRepository<Libro,Long> {

    @Query("SELECT a FROM Libro l JOIN l.autores a")
    List<Autor> getAutores();
    @Query("SELECT a FROM Libro l JOIN l.autores a WHERE a.nombre = :name")
    Autor findByName(String name);

    @Query("SELECT l FROM Libro l JOIN l.idiomas i WHERE i = :idioma")
    List<Libro> findLibrosByIdioma(@Param("idioma") String idioma);
}
