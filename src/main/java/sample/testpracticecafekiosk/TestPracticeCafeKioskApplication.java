package sample.testpracticecafekiosk;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
public class TestPracticeCafeKioskApplication {

    public static void main(String[] args) {
        SpringApplication.run(TestPracticeCafeKioskApplication.class, args);
    }

}
