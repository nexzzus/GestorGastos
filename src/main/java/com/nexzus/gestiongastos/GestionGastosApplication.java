package com.nexzus.gestiongastos;

import jakarta.annotation.PostConstruct;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.TimeZone;

@SpringBootApplication
public class GestionGastosApplication {
    @PostConstruct
    public void init() {
        // Define la zona horaria predeterminada de la JVM al arrancar
        TimeZone.setDefault(TimeZone.getTimeZone("America/Bogota"));
    }

    public static void main(String[] args) {
        SpringApplication.run(GestionGastosApplication.class, args);
    }

}
