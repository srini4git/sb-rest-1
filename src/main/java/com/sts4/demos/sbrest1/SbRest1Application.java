package com.sts4.demos.sbrest1;

import java.time.LocalDateTime;
import java.util.concurrent.atomic.AtomicInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
public class SbRest1Application {

	public static void main(String[] args) {
		SpringApplication.run(SbRest1Application.class, args);
	}

}


@RestController
class HelloController{
	private static Logger log = LoggerFactory.getLogger(HelloController.class);
	private static AtomicInteger reqCount = new AtomicInteger(1);
	
	@GetMapping("/hello")
	public String sayGreeting() {
		log.info("Got request {}", reqCount);
		String ServerResponse = new StringBuilder().append(" Server response ")
				.append(LocalDateTime.now()).toString();
		log.info("Server response {}", ServerResponse);
		return ServerResponse;
	}
}