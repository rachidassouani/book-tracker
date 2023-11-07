package io.rachidassouani.booktracker;

import io.rachidassouani.booktracker.author.Author;
import io.rachidassouani.booktracker.author.AuthorRepository;
import io.rachidassouani.booktracker.book.Book;
import io.rachidassouani.booktracker.book.BookRepository;
import jakarta.annotation.PostConstruct;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@SpringBootApplication
public class BooktrackerApplication {

	@Value("${datadump.location.author}")
	private String authorDumpLocation;

	@Value("${datadump.location.works}")
	private String worksDumpLocation;

	@Autowired
	private AuthorRepository authorRepository;

	@Autowired
	private BookRepository bookRepository;

	public static void main(String[] args) {
		SpringApplication.run(BooktrackerApplication.class, args);
	}

	private void initAuthors() {
		Path path = Paths.get(authorDumpLocation);
		try (Stream<String> lines = Files.lines(path)) {
			lines.forEach(line -> {
				// read line
				String jsonString = line.substring(line.indexOf("{"));
				try {
					// parse it into json
					JSONObject jsonObject = new JSONObject(jsonString);

					// new author object
					Author author = new Author();
					author.setId(jsonObject.optString("key").replace("/authors/", ""));
					author.setName(jsonObject.optString("name"));

					// saving author
					authorRepository.save(author);
					System.out.println(author.getName() + " saved successfully");
				} catch(Exception ex) {
					ex.printStackTrace();
				}
			});
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void initWorks() {
		Path path = Paths.get(worksDumpLocation);
		DateTimeFormatter dateTimeFormatter = DateTimeFormatter
				.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSSSS");
		try (Stream<String> lines = Files.lines(path)) {
			lines.forEach(line -> {
				// read line
				String jsonString = line.substring(line.indexOf("{"));
				try {
					// parse it into json
					JSONObject jsonObject = new JSONObject(jsonString);

					// new book object
					Book book = new Book();
					book.setId(jsonObject.optString("key").replace("/works/", ""));
					book.setName(jsonObject.optString("title"));

					JSONObject descriptionJsonObject = jsonObject.optJSONObject("description");
					if (descriptionJsonObject != null) {
						book.setDescription(descriptionJsonObject.optString("value"));
					}

					JSONObject createdDateJsonObject = jsonObject.optJSONObject("created");
					if (createdDateJsonObject != null) {
						String createdDateString = createdDateJsonObject.optString("value");
						book.setPublishedDate(LocalDate.parse(createdDateString, dateTimeFormatter));
					}

					JSONArray coversIdsArray = jsonObject.optJSONArray("covers");
					if (coversIdsArray != null) {
						List<String> coverIds = new ArrayList<>();
						for (int i = 0; i < coversIdsArray.length(); i++) {
							coverIds.add(coversIdsArray.getInt(i)+"");
						}
						book.setCoverIds(coverIds);
					}

					JSONArray authorJsonArray = jsonObject.optJSONArray("authors");

					if (authorJsonArray != null) {
						List<String> authorIds = new ArrayList<>();
						for (int i = 0; i < authorJsonArray.length(); i++) {
							JSONObject authorJsonObject = authorJsonArray.getJSONObject(i);
							String authorId = authorJsonObject
									.optJSONObject("author")
									.optString("key")
									.replace("/authors/", "");
							authorIds.add(authorId);
						}
						book.setAuthorIds(authorIds);

						List<String> authorNames = authorIds.stream()
								.map(id -> authorRepository.findById(id))
								.map(optionalAuthor -> {
									if (!optionalAuthor.isPresent()) return "Unknown Author";
									return optionalAuthor.get().getName();
								}).collect(Collectors.toList());
						book.setAuthorNames(authorNames);

					}

					// saving the book
					bookRepository.save(book);
					System.out.println(book.getName() + " saved successfully");
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			});
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@PostConstruct
	public void start() {
		/**/
		 	initAuthors();
			initWorks();

	}
}
