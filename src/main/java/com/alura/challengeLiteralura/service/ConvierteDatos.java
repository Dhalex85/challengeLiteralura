package com.alura.challengeLiteralura.service;

import com.alura.challengeLiteralura.model.DatosLibro;
import com.alura.challengeLiteralura.model.Root;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

public class ConvierteDatos {

    private ObjectMapper objectMapper = new ObjectMapper();

    public DatosLibro obtenerLibro(String json){
        try {
            Root root = objectMapper.readValue(json, Root.class);
            if (root.listaLibros().isEmpty()){
                return null;
            }else
            return root.listaLibros().getFirst();
        }catch (IOException e){
            e.printStackTrace();
        }
        return null;
    }
}
