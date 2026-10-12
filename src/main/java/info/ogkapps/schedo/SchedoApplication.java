package info.ogkapps.schedo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync
@SpringBootApplication
public class SchedoApplication {

	public static void main(String[] args) {
		SpringApplication.run(SchedoApplication.class, args);
	}

}
