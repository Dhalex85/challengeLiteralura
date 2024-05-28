package com.alura.challengeLiteralura.model;
import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record Root (
    @JsonAlias("results") List<DatosLibro> listaLibros){}
