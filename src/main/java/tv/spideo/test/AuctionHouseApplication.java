package tv.spideo.test;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@SpringBootApplication
@EnableConfigurationProperties
public class AuctionHouseApplication {

	public static void main(String[] args) {
		SpringApplication.run(AuctionHouseApplication.class, args);
	}

}
