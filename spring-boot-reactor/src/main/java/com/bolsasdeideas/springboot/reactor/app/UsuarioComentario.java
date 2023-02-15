package com.bolsasdeideas.springboot.reactor.app;

import lombok.AllArgsConstructor;
import lombok.ToString;

@AllArgsConstructor
@ToString
public class UsuarioComentario {

    private Usuario usuario;
    private Comentarios comentarios;
}
