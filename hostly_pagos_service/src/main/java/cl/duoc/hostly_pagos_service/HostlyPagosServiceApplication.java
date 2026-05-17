package cl.duoc.hostly_pagos_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients; 

@SpringBootApplication
@EnableFeignClients 
public class HostlyPagosServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(HostlyPagosServiceApplication.class, args);
    }

}
