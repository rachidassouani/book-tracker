package io.rachidassouani.booktracker.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping("login")
    public String login() {
        return "login";
    }

    @GetMapping("/users/new")
    public String saveUserForm(Model model, @ModelAttribute("user") User user) {
        user.setEnabled(true);
        user.setRoleName("User");
        return "user-form";
    }

    @PostMapping("/users/save")
    public String saveUser(User user, RedirectAttributes redirectAttributes) {
        User savedUser = userRepository.save(user);
        if (savedUser != null) {
            redirectAttributes.addFlashAttribute("successMessage", "The user has been saved successfully");
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "The user has NOT been saved");
        }
        return "redirect:/users";
    }
}
