package batch_2.capstone.controller;

import batch_2.capstone.model.User;
import batch_2.capstone.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ViewController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/index")
    public String index(Model model, @AuthenticationPrincipal UserDetails userDetails) {
        // SAFETY CHECK: If the session timed out or user logged out, userDetails will be null
        if (userDetails == null) {
            return "redirect:/login";
        }

        // Fetch the user details using the email (username) stored in the session
        User user = userRepository.findByEmail(userDetails.getUsername())
                .orElse(null);

        // --- ADDED DEBUG SECTION: LOOK AT YOUR INTELLIJ CONSOLE ---
        if (user != null) {
            System.out.println(">>> SUCCESS: User found in DB!");
            System.out.println(">>> ID from DB: " + user.getUserId());
            System.out.println(">>> Email from DB: " + user.getEmail());
            System.out.println(">>> Balance from DB: " + user.getWalletBalance());
        } else {
            System.out.println(">>> ERROR: Logged in as " + userDetails.getUsername() + " but not found in Database!");
        }
        // -----------------------------------------------------------

        if (user == null) {
            return "redirect:/login?error=usernotfound";
        }

        model.addAttribute("user", user);
        return "index";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/signup")
    public String signup() {
        return "signup";
    }
}