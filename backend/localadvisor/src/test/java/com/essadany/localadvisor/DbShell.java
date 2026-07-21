package com.essadany.localadvisor;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Profile;

@SpringBootApplication
@Profile("dbshell")
public class DbShell implements CommandLineRunner {
    public static void main(String[] args) {
        SpringApplication.run(DbShell.class, args);
    }
    @Override
    public void run(String... args) {
        // java.util.Scanner-based interactive loop here
    }
}