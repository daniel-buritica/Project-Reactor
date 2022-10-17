package com.bolsasdeideas.springboot.reactor.app;

import org.apache.logging.slf4j.SLF4JLogger;
import org.slf4j.ILoggerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import reactor.core.publisher.Flux;

@SpringBootApplication
public class SpringBootReactorApplication implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(SpringBootReactorApplication.class);
    public static void main(String[] args) {
        SpringApplication.run(SpringBootReactorApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {

        Flux<String> nombres =  Flux.just("nombre1","","nombre2","nombre3")
                .doOnNext(e -> {
                    if (e.isEmpty()){
                        new RuntimeException("Nombres no vacios");
                    }
                    System.out.println(e);
                });
        nombres.subscribe(e -> log.info(e)
                , error -> log.error(error.getMessage())
                , new Runnable() {
                    @Override
                    public void run() {
                        log.info("Ha terminado el flujo");
                    }
                });

    }
}
