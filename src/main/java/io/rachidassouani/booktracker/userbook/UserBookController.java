package io.rachidassouani.booktracker.userbook;

import io.rachidassouani.booktracker.security.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;

import java.time.LocalDate;

@Controller
public class UserBookController {

    @Autowired
    private UserBookRepository userBookRepository;

    @PostMapping("addUserBook")
    public String addBookForUser(@AuthenticationPrincipal UserDetailsImpl loggedUser,
                                 UserBookRequest userBookRequest) {

        if (loggedUser == null || loggedUser.getUsername().isEmpty()) {
            return null;
        }

        System.out.println("userBookRequest.getUserId() " + userBookRequest.getUserId());
        System.out.println("userBookRequest.getBookId() " + userBookRequest.getBookId());
        UserBookPrimaryKey key = new UserBookPrimaryKey();
        key.setUserId(userBookRequest.getUserId());
        key.setBookId(userBookRequest.getBookId());
        UserBook userBook = new UserBook();
        userBook.setPrimaryKey(key);
        userBook.setReadingStatus(userBookRequest.getReadingStatus());
        userBook.setRating(userBookRequest.getRating());
        userBook.setStartedDate(LocalDate.parse(userBookRequest.getStartedDate()));
        userBook.setCompletedDate(userBookRequest.getCompletedDate());
        userBookRepository.save(userBook);
        return "redirect:/books/"+userBookRequest.getBookId();
    }
}
