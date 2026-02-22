package com.kitchen.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories;

@SpringBootApplication
@ComponentScan(basePackages = "com.kitchen") 
@EnableR2dbcRepositories(basePackages = "com.kitchen.order") // TELL SPRING TO LOOK HERE
public class KitchenApiApplication {
    public static void main(String[] args) {
        SpringApplication.run(KitchenApiApplication.class, args);
    }
}