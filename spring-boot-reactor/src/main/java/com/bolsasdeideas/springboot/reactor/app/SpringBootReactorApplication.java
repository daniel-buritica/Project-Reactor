package com.bolsasdeideas.springboot.reactor.app;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import java.time.Duration;
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

        ejemploDelayElement();
    }


    public void mediaType(){



    }


    public void ejemploDelayElement(){

        Flux<Integer> rango = Flux.range(1 , 12)
                .delayElements(Duration.ofSeconds(1))
                .doOnNext(info -> log.info(info.toString()));

        rango.blockLast();

    }

    public void ejemploInterval(){

        Flux<Integer> rango = Flux.range(1,12);
        Flux<Long> retraso = Flux.interval(Duration.ofSeconds(1));

        rango.zipWith(retraso, (ra, re) -> ra)
                .doOnNext( info -> log.info(info.toString()))
                .blockLast();
    }

    public void ejemploZipWithRange(){

        Flux.just(1,2,3,4)
                .map( i -> (i*2))
                .zipWith(Flux.range(0,4), (uno, dos) -> String.format("primer Flux: %d, segundo Flux: %d", uno, dos))
                .subscribe( text -> log.info(text));
    }

    public void ejemploUsuarioComentarioZipWithForma2(){
        Mono<Usuario> usuarioMono = Mono.fromCallable(() -> new Usuario("daniel", "buritica"));

        Mono<Comentarios> comentariosMono = Mono.fromCallable(() -> {
            Comentarios comentarios= new Comentarios();
            comentarios.addComentario("prueba 1");
            comentarios.addComentario("prueba 2");
            comentarios.addComentario("prueba 4");
            return comentarios;
        });

        Mono<UsuarioComentario> usuariosConComentarios = usuarioMono
                .zipWith(comentariosMono)
                .map(tupla -> {
                    Usuario u = tupla.getT1();
                    Comentarios c = tupla.getT2();
                    return new UsuarioComentario(u, c);
                });

        usuariosConComentarios.subscribe(uc ->log.info(uc.toString()));
    }

    public void ejemploUsuarioComentarioZipWith(){
        Mono<Usuario> usuarioMono = Mono.fromCallable(() -> new Usuario("daniel", "buritica"));

        Mono<Comentarios> comentariosMono = Mono.fromCallable(() -> {
            Comentarios comentarios= new Comentarios();
            comentarios.addComentario("prueba 1");
            comentarios.addComentario("prueba 2");
            comentarios.addComentario("prueba 4");
            return comentarios;
        });

        Mono<UsuarioComentario> usuariosConComentarios = usuarioMono.zipWith(comentariosMono, (usuario, comentariosUsuario) -> new UsuarioComentario(usuario, comentariosUsuario));

        usuariosConComentarios.subscribe(uc ->log.info(uc.toString()));
    }



    public void ejemploUsuarioComentarioFlatMap(){
        Mono<Usuario> usuarioMono = Mono.fromCallable(() -> new Usuario("daniel", "buritica"));

        Mono<Comentarios> comentariosMono = Mono.fromCallable(() -> {
            Comentarios comentarios= new Comentarios();
            comentarios.addComentario("prueba 1");
            comentarios.addComentario("prueba 2");
            comentarios.addComentario("prueba 4");
            return comentarios;
        });

        usuarioMono.flatMap(u -> comentariosMono.map(c -> new UsuarioComentario(u,c)))
                .subscribe(uc ->log.info(uc.toString()));
    }
    public void ejemploCollectList() throws Exception {
        List<Usuario> usuariosList = new ArrayList<>();
        usuariosList.add(new Usuario("daniel","buritica"));
        usuariosList.add(new Usuario("sonia","junco"));
        usuariosList.add(new Usuario("blanca","triana"));
        usuariosList.add(new Usuario("steven","buritica"));
        usuariosList.add(new Usuario("steven","buritica"));

        Flux.fromIterable(usuariosList)
                .collectList()
                .subscribe(lista -> {
                    lista.forEach(item -> {
                        log.info(item.toString());
                    });
                });


    }
    public void ejemploMap() throws Exception {
        List<Usuario> usuariosList = new ArrayList<>();
        usuariosList.add(new Usuario("daniel","buritica"));
        usuariosList.add(new Usuario("sonia","junco"));
        usuariosList.add(new Usuario("blanca","triana"));
        usuariosList.add(new Usuario("steven","buritica"));
        usuariosList.add(new Usuario("steven","buritica"));

        Flux.fromIterable(usuariosList)
                .map(nombre -> nombre.getNombre().toUpperCase().concat(" ").concat(nombre.getApellido().toUpperCase()))
                .flatMap(usuario -> {
                    if (usuario.contains("buritica".toUpperCase())){
                        return Mono.just(usuario);
                    }else {
                     return Mono.empty();
                    }
                })
                .map(usuario -> {
                    return usuario;
                })
                .subscribe(e -> log.info(e.toString()));


    }

    public void ejemploIterable() throws Exception {
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
