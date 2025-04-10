package intbyte4.learnsmate;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class LearnsMateApplication {

    public static void main(String[] args) {
        SpringApplication.run(LearnsMateApplication.class, args);
    }

}