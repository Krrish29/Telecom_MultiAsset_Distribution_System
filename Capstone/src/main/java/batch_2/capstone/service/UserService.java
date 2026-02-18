package batch_2.capstone.service;

import batch_2.capstone.model.User;
import batch_2.capstone.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public void saveUser(User user) {
        // IMPORTANT: Encrypt the password before saving!
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
    }
}