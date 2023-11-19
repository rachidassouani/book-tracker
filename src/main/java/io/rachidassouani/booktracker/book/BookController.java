package io.rachidassouani.booktracker.book;

import io.rachidassouani.booktracker.security.UserDetailsImpl;
import io.rachidassouani.booktracker.userbook.UserBook;
import io.rachidassouani.booktracker.userbook.UserBookPrimaryKey;
import io.rachidassouani.booktracker.userbook.UserBookRepository;
import io.rachidassouani.booktracker.userbook.UserBookRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Optional;

@Controller
public class BookController {
    private final String COVER_IMAGE_ROOT = "http://covers.openlibrary.org/b/id/";

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private UserBookRepository userBookRepository;

    @GetMapping(value = "/books/{bookId}")
    public String findBookById(@PathVariable String bookId,
                               Model model,
                               @AuthenticationPrincipal UserDetailsImpl loggedUser) {
        Optional<Book> optionalBook = bookRepository.findById(bookId);
        if (!optionalBook.isPresent())
            return "books/book-not-found";
        Book book = optionalBook.get();
        String coverImageUrl = "/images/no-image.png";
        if (book.getCoverIds() != null && book.getCoverIds().size() > 0) {
            coverImageUrl = COVER_IMAGE_ROOT + book.getCoverIds().get(0) + "-L.jpg";
        }
        model.addAttribute("coverImage", coverImageUrl);
        model.addAttribute("book", book);

        if (loggedUser != null && loggedUser.getUsername() != null && !loggedUser.getUsername().isEmpty()) {
            model.addAttribute("loggedUser", loggedUser.getUsername());
            UserBookPrimaryKey key = new UserBookPrimaryKey();
            key.setUserId(loggedUser.getUsername());
            key.setBookId(bookId);

            Optional<UserBook> optionalUserBook = userBookRepository.findById(key);
            if (optionalUserBook.isPresent()) {
                UserBookRequest userBookRequest = getUserBookRequest(bookId, loggedUser, optionalUserBook);
                model.addAttribute("userBookRequest", userBookRequest);
            } else {
                UserBookRequest userBookRequest = new UserBookRequest(loggedUser.getUsername(), bookId);
                model.addAttribute("userBookRequest", userBookRequest);
            }
        }
        return "books/book";
    }

    private static UserBookRequest getUserBookRequest(String bookId, UserDetailsImpl loggedUser, Optional<UserBook> optionalUserBook) {
        UserBookRequest userBookRequest = new UserBookRequest();
        UserBook userBook = optionalUserBook.get();
        userBookRequest.setBookId(bookId);
        userBookRequest.setUserId(loggedUser.getUsername());
        userBookRequest.setStartedDate(userBook.getStartedDate());
        userBookRequest.setCompletedDate(userBook.getCompletedDate());
        userBookRequest.setReadingStatus(userBook.getReadingStatus());
        userBookRequest.setRating(userBook.getRating());
        return userBookRequest;
    }
}
