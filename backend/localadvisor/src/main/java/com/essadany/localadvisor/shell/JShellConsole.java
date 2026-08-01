package com.essadany.localadvisor.shell;

import jdk.jshell.tool.JavaShellToolBuilder;

import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

public class JShellConsole {

    public static ConfigurableApplicationContext ctx;

    public static void main(String[] args) throws Exception {
        SpringApplication app = new SpringApplication(
                com.essadany.localadvisor.LocaladvisorApplication.class
        );
        app.setDefaultProperties(Map.of("server.port", "0"));
        ctx = app.run(args);

        Path startup = Files.createTempFile("la-shell-", ".jsh");
        startup.toFile().deleteOnExit();
        Files.writeString(startup, String.format("""
                import com.essadany.localadvisor.model.*;
                import com.essadany.localadvisor.repository.*;
                import java.util.*;
                import java.util.stream.*;

                var ctx = %s.ctx;
                var userRepo = (UserRepository) ctx.getBean(UserRepository.class);
                var placeRepo = (PlaceRepository) ctx.getBean(PlaceRepository.class);
                var categoryRepo = (CategoryRepository) ctx.getBean(CategoryRepository.class);
                var reviewRepo = (ReviewRepository) ctx.getBean(ReviewRepository.class);
                var imageRepo = (ImageRepository) ctx.getBean(ImageRepository.class);
                var favoriteRepo = (FavoriteRepository) ctx.getBean(FavoriteRepository.class);
                """, JShellConsole.class.getName()
        ));

        System.out.println();
        System.out.println("  LocalAdvisor DB Shell  --  /exit to quit");
        System.out.println("  repos: userRepo  placeRepo  categoryRepo  reviewRepo  imageRepo  favoriteRepo");
        System.out.println("  examples:");
        System.out.println("    userRepo.findAll()");
        System.out.println("    placeRepo.findByCity(\"Paris\")");
        System.out.println("    userRepo.findByEmail(\"x@y.com\")");
        System.out.println("    favoriteRepo.findAll().stream().filter(f -> f.getUser().getEmail().contains(\"test\")).toList()");
        System.out.println();

        JavaShellToolBuilder.builder()
                .start(new String[]{"--startup", startup.toString(), "--execution", "local"});
    }
}
