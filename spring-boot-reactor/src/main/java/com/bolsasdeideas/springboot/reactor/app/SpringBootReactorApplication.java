package com.bolsasdeideas.springboot.reactor.app;

import org.apache.logging.slf4j.SLF4JLogger;
import org.slf4j.ILoggerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import reactor.core.publisher.Flux;

import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
public class SpringBootReactorApplication implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(SpringBootReactorApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(SpringBootReactorApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {

        ejmploEterable();
    }

    public void ejmploEterable() throws Exception {
        List<String> usuariosList = new ArrayList<>();
        usuariosList.add("daniel buritica");
        usuariosList.add("sonia junco");
        usuariosList.add("blanca triana");
        usuariosList.add("steven buritica");
        usuariosList.add("steven buritica");

        Flux<String> nombres = Flux.fromIterable(usuariosList);/*Flux.just("daniel buritica", "sonia junco", "blanca triana", "steven buritica", "steven buritica");*/

        Flux<Usuario> usuarios = nombres
                .map(nombre -> new Usuario(nombre.split(" ")[0], nombre.split(" ")[1]))
                .filter(usuario -> usuario.getApellido().equalsIgnoreCase("triana"))
                .doOnNext(usuario -> {
                    if (usuario == null) {
                        throw new RuntimeException("Nombres no pueden ir vacios");
                    }
                    System.out.println(usuario.getNombre());
                })
                .map(usuario -> {
                    usuario.setNombre(usuario.getNombre().toUpperCase());
                    return usuario;
                });

        usuarios.subscribe(e -> log.info(e.toString()),
                error -> log.error(error.getMessage()));


    }
}
