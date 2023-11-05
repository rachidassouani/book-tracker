package io.rachidassouani.booktracker;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BooktrackerApplication {

	@Value("${datadump.location.author}")
	private String authorDumpLocation;

	@Value("${datadump.location.works}")
	private String worksDumpLocation;

	public static void main(String[] args) {
		SpringApplication.run(BooktrackerApplication.class, args);
	}

	@PostConstruct
	public void start() {
		System.out.println(authorDumpLocation);
		System.out.println(worksDumpLocation);
	}
}
