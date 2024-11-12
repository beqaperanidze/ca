package prjnightsky;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import prjnightsky.entity.StarMap;
import prjnightsky.service.generator.StarMapGenerator;

import java.awt.*;
import java.io.File;
import java.io.FileWriter;
import java.time.LocalDate;
import java.time.LocalTime;

@SpringBootApplication
@EntityScan("prjnightsky/entity")
public class PrjNightSkyApplication {

    public static void main(String[] args) {
        SpringApplication.run(PrjNightSkyApplication.class, args);
    }
}


