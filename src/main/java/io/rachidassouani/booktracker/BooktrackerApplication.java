package io.rachidassouani.booktracker;

import io.rachidassouani.booktracker.author.Author;
import io.rachidassouani.booktracker.author.AuthorRepository;
import jakarta.annotation.PostConstruct;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

@SpringBootApplication
public class BooktrackerApplication {

	@Value("${datadump.location.author}")
	private String authorDumpLocation;

	@Value("${datadump.location.works}")
	private String worksDumpLocation;

	@Autowired
	private AuthorRepository authorRepository;

	public static void main(String[] args) {
		SpringApplication.run(BooktrackerApplication.class, args);
	}

	private void initAuthors() {
		Path path = Paths.get(authorDumpLocation);
		try (Stream<String> lines = Files.lines(path)) {
			lines.forEach(line -> {
				// read line
				String jsonString = line.substring(line.indexOf("{"));
				// parse it into json
				JSONObject jsonObject = new JSONObject(jsonString);

				// new author object
				Author author = new Author();
				author.setId(jsonObject.optString("key").replace("/authors/", ""));
				author.setName(jsonObject.optString("name"));

				// saving author
				authorRepository.save(author);

				System.out.println(author.getName() + " saved successfully");
			});
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@PostConstruct
	public void start() {
		initAuthors();
	}
}
