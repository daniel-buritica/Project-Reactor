package com.bolsasdeideas.springboot.reactor.app;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@ToString
public class Comentarios {

    private List<String> comentarios;

    public Comentarios() {
        this.comentarios = new ArrayList<>();
    }

    public void addComentario(String lo){
        this.comentarios.add(lo);
    }
}
