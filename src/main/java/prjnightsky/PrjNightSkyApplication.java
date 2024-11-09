package prjnightsky;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication
@EntityScan("entity")
public class PrjNightSkyApplication {

    public static void main(String[] args) {
        SpringApplication.run(PrjNightSkyApplication.class, args);
    }

}
