package batch_2.capstone.controller;

import batch_2.capstone.model.PendingUser;
import batch_2.capstone.model.User;
import batch_2.capstone.repository.PendingUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import java.util.UUID;

@Controller
public class AuthController {

    @Autowired
    private PendingUserRepository pendingUserRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // REMOVED @GetMapping("/login") - Already in ViewController
    // REMOVED @GetMapping("/signup") - Already in ViewController

    @PostMapping("/signup")
    public String processSignup(User user) {
        PendingUser pending = new PendingUser();
        pending.setFullName(user.getFullName());
        pending.setEmail(user.getEmail());

        // Encrypt password before saving to pending table
        pending.setPassword(passwordEncoder.encode(user.getPassword()));
        pending.setInitialDeposit(user.getWalletBalance());
        pending.setSessionId("REG_" + UUID.randomUUID().toString());
        pending.setStatus("PENDING");

        pendingUserRepository.save(pending);

        // Redirect to StrivePay
        return "redirect:https://strivepay.com/checkout?session=" + pending.getSessionId();
    }
}